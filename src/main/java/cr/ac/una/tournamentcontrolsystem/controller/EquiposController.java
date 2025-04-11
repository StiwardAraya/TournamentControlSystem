package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroDeporte;
import cr.ac.una.tournamentcontrolsystem.service.RegistroEquipo;
import cr.ac.una.tournamentcontrolsystem.util.AppContext;
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
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import javafx.util.Duration;

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
    @FXML
    private AnchorPane root;
    @FXML
    private VBox containerFoto;
    @FXML
    private ImageView imvBalonDeporte;
    @FXML
    private VBox minibalonContainer;

    private Equipo equipo;
    private Deporte deporte;
    private Image imagen;
    private Boolean imagenCargada = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEliminar.setDisable(true);
        equipo = new Equipo();
        deporte = new Deporte();

        cmbDeporte.valueProperty().addListener((observable, oldValue, newValue) -> {
            Deporte deporteCombo = cmbDeporte.getSelectedItem();
            if (deporteCombo != null) {
                String imagenBalonURL = deporteCombo.getImagenURL();
                imvBalonDeporte.setImage(new Image(new File(imagenBalonURL).toURI().toString()));
                minibalonContainer.setStyle("-fx-opacity: 1");
            }
        });
    }

    @Override
    public void initialize() {
        loadDeportes();
    }

    @FXML
    private void onActionBtnBuscar(ActionEvent event) {
        if (txfIdentificador.getText().isBlank() || txfIdentificador.getText().isEmpty()) {
            txfIdentificador.setStyle("-mfx-main: #ffa725; -fx-border-color: #D21F3C;");
            shake(txfIdentificador);
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
            deporte = equipo.getDeporte();
            cmbDeporte.selectItem(deporte);
            imagen = new Image(new File(imagenEquipoURL).toURI().toString());
            imvFoto.setImage(imagen);
            containerFoto.setStyle("-fx-background-color: #FFFFFF; -fx-background-image: none; -fx-opacity: 1;");
            imagenCargada = true;
            btnEliminar.setDisable(false);
            btnGuardar.setText("Actualizar");
        }
    }

    @FXML
    private void onActionBtnGuardar(ActionEvent event) {
        if (txfNombre.getText().isBlank() || txfNombre.getText().isEmpty()) {
            txfNombre.setStyle("-mfx-main: #ffa725; -fx-border-color: #D21F3C;");
            shake(txfNombre);
            return;
        }
        if (!imagenCargada) {
            containerFoto.setStyle("-fx-background-color: #D21F3C;");
            shake(containerFoto);
            return;
        }

        if (cmbDeporte.getSelectedItem() == null) {
            cmbDeporte.setStyle("-mfx-main: #ffa725; -fx-border-color: #D21F3C;");
            shake(cmbDeporte);
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
            txfIdentificador.setStyle("-mfx-main: #ffa725; -fx-border-color: #D21F3C;");
            shake(txfIdentificador);
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

    // FIXME: encontrar la forma de apagar el hilo de executor para que la camara se apague cuando se cierra la ventana
    @FXML
    private void onActionBtnTomarFoto(ActionEvent event) {
        FlowController.getInstance().goViewInWindowModal("CamaraView", ((Stage) root.getScene().getWindow()), Boolean.FALSE);
        Image imagenCamara = (Image) AppContext.getInstance().get("capturedImage");
        if (imagenCamara != null) {
            imvFoto.setImage(imagenCamara);
            containerFoto.setStyle("-fx-background-color: #FFFFFF; -fx-background-image: none; -fx-opacity: 1;");
            imagenCargada = true;
            imagen = imagenCamara;
        }
    }

    @FXML
    private void onActionBtnNuevo(ActionEvent event) {
        reiniciarVentana();
    }

    @FXML
    private void onActionBtnVerEquipos(ActionEvent event) {
        // TODO: Abrir tabla de equipos
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
        AppContext.getInstance().set("capturedImage", null);
        txfIdentificador.clear();
        txfIdentificador.setEditable(true);
        txfNombre.setStyle("");
        txfIdentificador.setStyle("");
        cmbDeporte.setStyle("");
        btnGuardar.setText("Guardar");
        btnEliminar.setDisable(true);
        txfNombre.clear();
        imvFoto.setImage(null);
        imvBalonDeporte.setImage(null);
        imagenCargada = false;
        containerFoto.setStyle("");
        minibalonContainer.setStyle("-fx-opacity: .3");
        cmbDeporte.getSelectionModel().clearSelection();
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

    private void shake(Node element) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(100), element);
        shake.setFromX(0);
        shake.setToX(10);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.play();
    }
}
