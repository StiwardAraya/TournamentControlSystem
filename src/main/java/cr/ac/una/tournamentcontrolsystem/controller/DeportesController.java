package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import cr.ac.una.tournamentcontrolsystem.service.RegistroDeporte;
import cr.ac.una.tournamentcontrolsystem.util.Mensaje;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.net.URL;
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
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

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
    @FXML
    private VBox containerPhoto;

    private Deporte deporte;
    private Image imagen;
    private Boolean imagenCargada = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEliminar.setDisable(true);
        this.deporte = new Deporte();
        imvPhoto.fitWidthProperty().bind(containerPhoto.widthProperty().subtract(10));
        imvPhoto.fitHeightProperty().bind(containerPhoto.heightProperty().subtract(10));
        imvPhoto.preserveRatioProperty().setValue(false);
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

        if (txfIdentificador.getText().isBlank() || txfIdentificador.getText().isEmpty()) {
            deporte.setId(0);
        }

        deporte.setNombre(txfNombre.getText());
        Respuesta respuestaGuardarDeporte = RegistroDeporte.getInstance().guardarDeporte(deporte, imagen);
        if (!respuestaGuardarDeporte.getEstado()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Guardar deporte", respuestaGuardarDeporte.getMensaje());
        } else {
            reiniciarVentana();
            deporte = new Deporte();
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
        boolean estado = false;

        if (db.hasImage()) {
            Image image = db.getImage();
            imvPhoto.setImage(image);
            containerPhoto.setStyle("-fx-background-color: #FFFFFF; -fx-background-image: none; -fx-opacity: 1;");
            imagenCargada = true;
            imagen = image;
            estado = true;
        } else if (db.hasFiles()) {
            for (File file : db.getFiles()) {
                if (isImage(file)) {
                    Image image = new Image(file.toURI().toString());
                    imvPhoto.setImage(image);
                    containerPhoto.setStyle("-fx-background-color: #FFFFFF; -fx-background-image: none; -fx-opacity: 1;");
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

    private void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(imvPhoto.getScene().getWindow());
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            imvPhoto.setImage(image);
            containerPhoto.setStyle("-fx-background-color: #FFFFFF; -fx-background-image: none; -fx-opacity: 1;");
            imagenCargada = true;
            imagen = image;
        }
    }

    private void reiniciarVentana() {
        txfIdentificador.clear();
        txfIdentificador.setEditable(true);
        btnGuardar.setText("Guardar");
        txfNombre.clear();
        imvPhoto.setImage(null);
        containerPhoto.setStyle("");
        imagenCargada = false;
    }

    private boolean isImage(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg");
    }
}
