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
import javafx.scene.layout.VBox;
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
    private Image imagen;
    private Boolean imagenCargada = false;

    @FXML
    private AnchorPane root;
    @FXML
    private VBox containerFoto;
    @FXML
    private ImageView imvBalonDeporte;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEliminar.setDisable(true);
        equipo = new Equipo();
        deporte = new Deporte();
        imvFoto.fitWidthProperty().bind(containerFoto.widthProperty().subtract(10));
        imvFoto.fitHeightProperty().bind(containerFoto.heightProperty().subtract(10));
        imvFoto.preserveRatioProperty().setValue(false);

        cmbDeporte.valueProperty().addListener((observable, oldValue, newValue) -> {
            imvBalonDeporte.fitWidthProperty().set(40);
            imvBalonDeporte.fitHeightProperty().set(40);
            Deporte deporteCombo = cmbDeporte.getSelectedItem();
            if (deporteCombo != null) {
                String imagenBalonURL = deporteCombo.getImagenURL();
                imvBalonDeporte.setImage(new Image(new File(imagenBalonURL).toURI().toString()));
            }
        });
    }

    @Override
    public void initialize() {
        loadDeportes();
        reiniciarVentana();
    }

    @FXML
    private void onActionBtnBuscar(ActionEvent event) {
        if (txfIdentificador.getText().isBlank() || txfIdentificador.getText().isEmpty()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Id", "Debe ingresar un id para buscar un equipo");
            return;
        }

        Respuesta respuestaBuscarEquipo = RegistroEquipo.getInstance().buscarEquipo(Integer.parseInt(txfIdentificador.getText()));
        if (!respuestaBuscarEquipo.getEstado()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Equipo", respuestaBuscarEquipo.getMensaje());
        } else {
            equipo = (Equipo) respuestaBuscarEquipo.getResultado("equipoEncontrado");
            String imagenEquipoURL = equipo.getFotoURL();
            txfIdentificador.setText(String.valueOf(equipo.getId()));
            txfIdentificador.setEditable(false);
            txfNombre.setText(equipo.getNombre());
            cmbDeporte.selectItem(equipo.getDeporte());
            imvFoto.setImage(new Image(new File(imagenEquipoURL).toURI().toString()));
            containerFoto.setStyle("-fx-background-color: #FFFFFF; -fx-background-image: none; -fx-opacity: 1;");
            imagenCargada = true;
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
        boolean estado = false;

        if (db.hasImage()) {
            Image image = db.getImage();
            imvFoto.setImage(image);
            containerFoto.setStyle("-fx-background-color: #FFFFFF; -fx-background-image: none; -fx-opacity: 1;");
            imagenCargada = true;
            imagen = image;
            estado = true;
        } else if (db.hasFiles()) {
            for (File file : db.getFiles()) {
                if (isImage(file)) {
                    Image image = new Image(file.toURI().toString());
                    imvFoto.setImage(image);
                    containerFoto.setStyle("-fx-background-color: #FFFFFF; -fx-background-image: none; -fx-opacity: 1;");
                    imagenCargada = true;
                    imagen = image;
                    estado = true;
                    break;
                }
            }
        }

        event.setDropCompleted(estado);
        event.consume();
    }

    @FXML
    private void onDragOverStackPhoto(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasImage() || (db.hasFiles() && db.getFiles().stream().anyMatch(this::isImage))) {
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
        if (!imagenCargada) {
            new Mensaje().show(Alert.AlertType.ERROR, "No hay imagen", "Debe subir una imagen");
            return;
        }

        if (cmbDeporte.getSelectedItem() == null) {
            new Mensaje().show(Alert.AlertType.ERROR, "No se ha seleccionado deporte", "Debe seleccionar un deporte");
            return;
        }

        if (txfIdentificador.getText().isBlank() || txfIdentificador.getText().isBlank()) {
            equipo.setId(0);
        }

        equipo.setNombre(txfNombre.getText());
        equipo.setDeporte(cmbDeporte.getSelectedItem());
        Respuesta respuestaGuardarEquipo = RegistroEquipo.getInstance().guardarEquipo(equipo, imagen);
        if (!respuestaGuardarEquipo.getEstado()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Guardar equipo", respuestaGuardarEquipo.getMensaje());
        } else {
            reiniciarVentana();
            deporte = null;
            equipo = null;
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
            reiniciarVentana();
            equipo = null;
            deporte = null;
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
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(imvFoto.getScene().getWindow());
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            imvFoto.setImage(image);
            containerFoto.setStyle("-fx-background-color: #FFFFFF; -fx-background-image: none; -fx-opacity: 1;");
            imagenCargada = true;
            imagen = image;
        }
    }

    private void reiniciarVentana() {
        txfIdentificador.clear();
        txfIdentificador.setEditable(true);
        btnGuardar.setText("Guardar");
        btnEliminar.setDisable(true);
        txfNombre.clear();
        imvFoto.setImage(null);
        imvBalonDeporte.setImage(null);
        imvBalonDeporte.fitWidthProperty().set(0);
        imvBalonDeporte.fitHeightProperty().set(0);
        imagenCargada = false;
        containerFoto.setStyle("");
        cmbDeporte.clear();
        deporte = new Deporte();
        equipo = new Equipo();
    }

    private void loadDeportes() {
        List<Deporte> deportes = (List<Deporte>) RegistroDeporte.getInstance().getDeportes().getResultado("deportes");
        cmbDeporte.getItems().clear();

        if (deportes != null && !deportes.isEmpty()) {
            cmbDeporte.getItems().addAll(deportes);
        } else {
            new Mensaje().show(Alert.AlertType.WARNING, "Deportes", "No hay deportes registrados");
        }
    }

    private boolean isImage(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg");
    }

}
