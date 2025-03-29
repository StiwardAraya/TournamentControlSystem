package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroDeporte;
import cr.ac.una.tournamentcontrolsystem.service.RegistroEquipo;
import cr.ac.una.tournamentcontrolsystem.util.FlowController;
import cr.ac.una.tournamentcontrolsystem.util.Mensaje;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EquiposController extends Controller implements Initializable {

    @FXML
    private MFXTextField txfIdentificador;
    @FXML
    private MFXButton btnBuscar;
    @FXML
    private MFXButton btnVerEquipos;
    @FXML
    private MFXTextField txfNombre;
    @FXML
    private ImageView imvFoto;
    @FXML
    private MFXButton btnBuscarImagen;
    @FXML
    private MFXButton btnTomarFoto;
    @FXML
    private MFXComboBox<Deporte> cmbDeporte;
    @FXML
    private MFXButton btnGuardar;
    @FXML
    private MFXButton btnEliminar;
    @FXML
    private MFXButton btnNuevo;
  
    private Equipo equipo;
    private Deporte deporte;
    private File imagen;

    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEliminar.setDisable(true);
        loadDeportes();
    }

    @Override
    public void initialize() {
        // TODO
    }
    
    @FXML
    private void onActionBtnBuscar(ActionEvent event) {
          if (!txfIdentificador.getText().isBlank() || !txfIdentificador.getText().isEmpty()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Id", "Debe ingresar un id para buscar un equipo");
            return;
        }

        Respuesta respuestaBuscarEquipo = RegistroEquipo.getInstance().buscarEquipo(Integer.parseInt(txfIdentificador.getText()));
        if (!respuestaBuscarEquipo.getEstado()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Equipo", respuestaBuscarEquipo.getMensaje());
        } else {
            equipo = (Equipo) respuestaBuscarEquipo.getResultado("equipoEncontrado");
            String imagennEquipoURL = equipo.getFotoURL();
            txfIdentificador.setText(String.valueOf(equipo.getId()));
            txfIdentificador.setEditable(false);
            txfNombre.setText(equipo.getNombre());
            imvFoto.setImage(new Image(new File(imagennEquipoURL).toURI().toString()));
            btnEliminar.setDisable(false);
            btnGuardar.setText("Actualizar");
        }
    }
    
    @FXML
    private void onActionBtnVerEquipos(ActionEvent event) {
         //TODO: Desplegar una tabla con todos los equipos registrados
    }

  
    @FXML
    private void onDragDroppedStackPhoto(DragEvent event) {
          Dragboard db = event.getDragboard();
        boolean success = false;

        if (db.hasImage()) {
            Image image = db.getImage();
            imvFoto.setImage(image);
            success = true;
        }

        event.setDropCompleted(success);
        event.consume();
    }
    
    @FXML
    private void onDragOverStackPhoto(DragEvent event) {
        if (event.getGestureSource() != imvFoto && event.getDragboard().hasImage()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
    }

    @FXML
    private void onActionBtnBuscarImagen(ActionEvent event) {
        seleccionarImagen();
    }

    @FXML
    private void onActionBtnTomarFoto(ActionEvent event) {
        FlowController.getInstance().goViewInWindowModal("CamaraView", ((Stage) root.getScene().getWindow()), Boolean.FALSE);
    }

    @FXML
    private void onActionBtnGuardar(ActionEvent event) {
        if (txfNombre.getText().isBlank() || txfNombre.getText().isEmpty()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Guardar Equipo", "Debe llenar el espacio nombre");
            return;
        }
        if (!imagenCargada()) {
            new Mensaje().show(Alert.AlertType.ERROR, "No hay imagen", "Debe subir una imagen");
            return;
        }

        if (txfIdentificador.getText().isBlank() || txfIdentificador.getText().isBlank()) {
            equipo.setId(0);
        }
        equipo.setNombre(txfNombre.getText());
        Respuesta respuestaGuardarEquipo = RegistroEquipo.getInstance().guardarEquipo(equipo, imagen);
        if (!respuestaGuardarEquipo.getEstado()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Guardar equipo", respuestaGuardarEquipo.getMensaje());
        } else {
            new Mensaje().show(Alert.AlertType.CONFIRMATION, "Guardar equipo", respuestaGuardarEquipo.getMensaje());
        }
    }   

    @FXML
    private void onActionBtnEliminar(ActionEvent event) {
        if (equipo == null) {
            new Mensaje().show(Alert.AlertType.ERROR, "Eliminar equipo", "Debes buscar un equipo antes de eliminarlo");
            return;
        }

        Respuesta respuestaEliminarEquipo = RegistroEquipo.getInstance().eliminarEquipo(equipo.getId());

        if (respuestaEliminarEquipo.getEstado()) {
            new Mensaje().show(Alert.AlertType.CONFIRMATION, "Eliminar Equipo", respuestaEliminarEquipo.getMensaje());
        } else {
            new Mensaje().show(Alert.AlertType.ERROR, "Eliminar Equipo", respuestaEliminarEquipo.getMensaje());
        }
    }

    @FXML
    private void onActionBtnNuevo(ActionEvent event) {
      reiniciarVentana();
    }
    
     private void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione una imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de imagen", "*.png", "*.jpg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        imvFoto.setImage(new Image(selectedFile.toURI().toString()));
        imagen = selectedFile;
    }

    private boolean imagenCargada() {
        if (equipo != null) {
            String imagenURL = imvFoto.getImage().getUrl();
            return imagenURL.equals("../resources/img/camara_icon.png");
        }
        return false;
    }

    private void reiniciarVentana() {
        txfIdentificador.clear();
        txfIdentificador.setEditable(true);
        btnGuardar.setText("Guardar");
        txfNombre.clear();
        imvFoto.setImage(new Image(new File("../resources/img/camara_icon.png").toURI().toString()));
    }
    
     private void loadDeportes() { 
        List<Deporte> deportes = (List<Deporte>) RegistroDeporte.getInstance().getDeportes(); 
        System.out.println("Deportes disponibles: " + deportes); 

        cmbDeporte.getItems().clear();

        if (deportes != null && !deportes.isEmpty()) {
            cmbDeporte.getItems().addAll(deportes);
        } else {
            System.out.println("No hay deportes disponibles para mostrar."); 
        }
    }

}


   
  