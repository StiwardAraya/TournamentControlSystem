package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import cr.ac.una.tournamentcontrolsystem.service.RegistroDeporte;
import cr.ac.una.tournamentcontrolsystem.util.FlowController;
import cr.ac.una.tournamentcontrolsystem.util.Mensaje;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.net.URL;
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
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 * Controlador del CRUD de deportes
 *
 * @author Stiward Araya C.
 * @author Angie Marks S.
 * @author Kevin Calderón Z.
 */
public class DeportesController extends Controller implements Initializable {

    @FXML
    private MFXTextField txfIdentificador;
    @FXML
    private MFXTextField txfNombre;
    @FXML
    private ImageView imvPhoto;
    @FXML
    private MFXButton btnGuardar;
    @FXML
    private MFXButton btnEliminar;
    @FXML
    private VBox containerPhoto;

    private Deporte deporte;
    private Image imagen;
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

    /**
     * Abre un cuadro de diálogo para que el usuario seleccione una imagen desde
     * su sistema de archivos, y la muestra en la interfaz gráfica.
     *
     * Este método permite seleccionar archivos con extensiones PNG, JPG o JPEG.
     * Una vez seleccionada, la imagen se carga en el componente imvPhoto, se
     * actualizan los estilos del contenedor containerPhoto, y se marca la
     * imagen como cargada mediante el valor imagenCargada.
     */
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

    /**
     * Regresa la ventana a sus configuraciones por defecto.
     */
    private void reiniciarVentana() {
        txfIdentificador.clear();
        txfIdentificador.setEditable(true);
        btnGuardar.setText("Guardar");
        btnEliminar.setDisable(true);
        txfNombre.clear();
        txfNombre.setStyle("");
        imvPhoto.setImage(null);
        containerPhoto.setStyle("");
        txfIdentificador.setStyle("");
        imagenCargada = false;
        deporte = new Deporte();
    }

    /**
     * Revisa si un archivo es una imagen.
     *
     * @param file
     * @return valor del archivo
     */
    private boolean isImage(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg");
    }

    /**
     * Animación de javaFX, sacude el elemento Nodo que ingrese por parámetro
     *
     * @param element
     */
    private void shake(Node element) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(100), element);
        shake.setFromX(0);
        shake.setToX(10);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.play();
    }

    // EVENTOS
    @FXML
    private void onActionBtnBuscar(ActionEvent event) {
        if (txfIdentificador.getText().isBlank() || txfIdentificador.getText().isEmpty()) {
            txfIdentificador.setStyle("-mfx-main: #ffa725; -fx-border-color: #D21F3C;");
            shake(txfIdentificador);
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
            imagen = new Image(new File(imagenBalonURL).toURI().toString());
            imvPhoto.setImage(imagen);
            containerPhoto.setStyle("-fx-background-color: #FFFFFF; -fx-background-image: none; -fx-opacity: 1;");
            imagenCargada = true;
            btnEliminar.setDisable(false);
            btnGuardar.setText("Actualizar");
        }
    }

    @FXML
    private void onActionBtnGuardar(ActionEvent event) {
        if (txfNombre.getText().isBlank() || txfNombre.getText().isEmpty()) {
            txfNombre.setStyle("-mfx-main: #FFA725; -fx-border-color: #D21F3C;");
            shake(txfNombre);
            return;
        }

        if (!imagenCargada) {
            containerPhoto.setStyle("-fx-background-color: #D21F3C");
            shake(containerPhoto);
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
            new Mensaje().show(Alert.AlertType.INFORMATION, "Guardar deporte", respuestaGuardarDeporte.getMensaje());
        }
    }

    @FXML
    private void onActionBtnEliminar(ActionEvent event) {
        if (deporte == null) {
            txfIdentificador.setStyle("-mfx-main: #ffa725; -fx-border-color: #D21F3C;");
            shake(txfIdentificador);
            return;
        }

        Respuesta respuestaEliminarDeporte = RegistroDeporte.getInstance().eliminarDeporte(deporte.getId());

        if (respuestaEliminarDeporte.getEstado()) {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Eliminar Deporte", respuestaEliminarDeporte.getMensaje());
            reiniciarVentana();
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
        FlowController.getInstance().goViewInWindow("DeportesMuestraView");
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

}
