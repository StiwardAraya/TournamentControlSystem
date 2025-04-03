package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import cr.ac.una.tournamentcontrolsystem.service.RegistroDeporte;
import cr.ac.una.tournamentcontrolsystem.util.Mensaje;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DeportesController extends Controller implements Initializable {

    @FXML
    private MFXButton btnBuscar;
    @FXML
    private MFXTextField txfIdentificador;
    @FXML
    private MFXButton btnVerDeportes;
    @FXML
    private MFXTextField txfNombre;
    @FXML
    private ImageView imvPhoto;
    @FXML
    private MFXButton btnBuscarImagen;
    @FXML
    private MFXButton btnGuardar;
    @FXML
    private MFXButton btnEliminar;
    @FXML
    private MFXButton btnNuevo;

    private Deporte deporte;
    private File imagen;
    private Boolean imagenCargada = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEliminar.setDisable(true);
        this.deporte = new Deporte();
    }

    @Override
    public void initialize() {
        // TODO:
    }

    @FXML
    private void onActionBtnBuscar(ActionEvent event) {
        if (txfIdentificador.getText().isBlank() || txfIdentificador.getText().isEmpty()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Id", "Debe ingresar un id para buscar un deporte");
            return;
        }

        Respuesta respuestaBuscarDeporte = RegistroDeporte.getInstance().buscarDeporte(Integer.parseInt(txfIdentificador.getText()));
        if (!respuestaBuscarDeporte.getEstado()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Deporte", respuestaBuscarDeporte.getMensaje());
        } else {
            deporte = (Deporte) respuestaBuscarDeporte.getResultado("deporteEncontrado");
            String imagenBalonURL = deporte.getImagenURL();
            txfIdentificador.setText(String.valueOf(deporte.getId()));
            txfIdentificador.setEditable(false);
            txfNombre.setText(deporte.getNombre());
            imvPhoto.setImage(new Image(new File(imagenBalonURL).toURI().toString()));
            imagenCargada = true;
            btnEliminar.setDisable(false);
            btnGuardar.setText("Actualizar");
        }
    }

    @FXML
    private void onActionBtnGuardar(ActionEvent event) {
        if (txfNombre.getText().isBlank() || txfNombre.getText().isEmpty()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Guardar Deporte", "Debe llenar el espacio nombre");
            return;
        }
        if (!imagenCargada) {
            new Mensaje().show(Alert.AlertType.ERROR, "No hay imagen", "Debe subir una imagen");
            return;
        }

        deporte = new Deporte();

        if (txfIdentificador.getText().isBlank() || txfIdentificador.getText().isBlank()) {
            deporte.setId(0);
        }

        deporte.setNombre(txfNombre.getText());
        Respuesta respuestaGuardarDeporte = RegistroDeporte.getInstance().guardarDeporte(deporte, imagen);
        if (!respuestaGuardarDeporte.getEstado()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Guardar deporte", respuestaGuardarDeporte.getMensaje());
        } else {
            reiniciarVentana();
            deporte = null;
            new Mensaje().show(Alert.AlertType.INFORMATION, "Guardar deporte", respuestaGuardarDeporte.getMensaje());
        }
    }

    @FXML
    private void onActionBtnEliminar(ActionEvent event) {
        if (deporte == null) {
            new Mensaje().show(Alert.AlertType.ERROR, "Eliminar deporte", "Debes buscar un deporte antes de eliminarlo");
            return;
        }

        Respuesta respuestaEliminarDeporte = RegistroDeporte.getInstance().eliminarDeporte(deporte.getId());

        if (respuestaEliminarDeporte.getEstado()) {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Eliminar Deporte", respuestaEliminarDeporte.getMensaje());
            reiniciarVentana();
            deporte = null;
        } else {
            new Mensaje().show(Alert.AlertType.ERROR, "Eliminar Deporte", respuestaEliminarDeporte.getMensaje());
        }
    }

    @FXML
    private void onActionBtnNuevo(ActionEvent event) {
        reiniciarVentana();
    }

    @FXML
    private void onActionBtnVerDeportes(ActionEvent event) {
        //TODO: Desplegar una tabla con todos los deportes registrados
    }

    @FXML
    private void onActionBtnBuscarImagen(ActionEvent event) {
        seleccionarImagen();
    }

    @FXML
    private void onDragDroppedStackPhoto(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;

        if (db.hasImage()) {
            Image image = db.getImage();
            imvPhoto.setImage(image);
            success = true;
        }

        event.setDropCompleted(success);
        event.consume();
    }

    @FXML
    private void onDragOverStackPhoto(DragEvent event) {
        if (event.getGestureSource() != imvPhoto && event.getDragboard().hasImage()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
    }

    private void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione una imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de imagen", "*.png", "*.jpg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            imvPhoto.setImage(new Image(selectedFile.toURI().toString()));
            imagenCargada = true;
            imagen = selectedFile;
          
            deporte.setImagenURL(selectedFile.getAbsolutePath());
            }
    }

    private void reiniciarVentana() {
        txfIdentificador.clear();
        txfIdentificador.setEditable(true);
        btnGuardar.setText("Guardar");
        txfNombre.clear();
        //imvPhoto.setImage(new Image("../resources/img/camara_icon.png"));
        imagenCargada = false;
    }
}
