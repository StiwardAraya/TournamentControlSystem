package cr.ac.una.tournamentcontrolsystem.controller;

import com.github.sarxos.webcam.Webcam;
import cr.ac.una.tournamentcontrolsystem.util.AppContext;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Clase controlador de la ventana de cámara
 *
 * @author Stiward Araya C.
 * @author Angie Marks S.
 * @author Kevin Calderón Z.
 */
public class CamaraController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private ImageView imvCamara;
    @FXML
    private MFXButton btnCapturar;
    @FXML
    private MFXButton btnGuardar;
    @FXML
    private MFXButton btnEncenderApagar;
    @FXML
    private AnchorPane camContainer;

    private boolean cameraOn = false;
    private boolean capturar = false;
    private volatile boolean capturing = true;
    private ExecutorService executor;
    private Image lastCapturedFXImage;
    Webcam webcam = Webcam.getDefault();

    static final Logger logger = Logger.getLogger(CamaraController.class.getName());

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCapturar.setDisable(true);
        btnGuardar.setDisable(true);
    }

    @Override
    public void initialize() {
    }

    /**
     * Enciende o apaga la cámara dependiendo del valor booleano que recibe
     *
     * @param value
     */
    private void switchCamara(boolean value) {
        executor = Executors.newSingleThreadExecutor();
        if (value) {
            encenderCamara();
        } else {
            apagarCamara();
        }
    }

    /**
     * Inicia la captura de video desde la cámara web y muestra la imagen en
     * tiempo real en el componente de interfaz gráfica. Este método configura
     * la cámara con una resolución de 640x480 píxeles, la abre y comienza a
     * capturar imágenes en un hilo separado a 30 fotogramas por segundo. Las
     * imágenes capturadas se muestran en el componente imvCamara.
     *
     * También actualiza el estilo del contenedor de la cámara y habilita los
     * botones de captura y guardado.
     */
    private void encenderCamara() {
        capturing = true;
        camContainer.setStyle("-fx-background-color: #FFFFFF; -fx-background-image: none; -fx-opacity: 1;");

        if (webcam != null) {
            webcam.setViewSize(new Dimension(640, 480));
            webcam.open();

            executor.submit(() -> {
                while (webcam.isOpen() && capturing) {
                    try {
                        BufferedImage bufferedImage = webcam.getImage();
                        Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);

                        Platform.runLater(() -> {
                            imvCamara.setImage(fxImage);
                        });

                        Thread.sleep(1000 / 30);
                    } catch (InterruptedException e) {
                        logger.log(Level.SEVERE, "[CamaraController.encenderCamara]: Error al iniciar hilo", e);
                    }
                }
            });
        }
        btnCapturar.setDisable(false);
        btnGuardar.setDisable(false);
    }

    /**
     * Detiene la captura de video desde la cámara web.
     *
     * Si la cámara está abierta, este método la cierra y detiene el proceso de
     * captura. También limpia la imagen mostrada en la interfaz, apaga el
     * ejecutor de tareas, restablece los estilos del contenedor de la cámara y
     * actualiza el estado de los botones de la interfaz gráfica.
     */
    private void apagarCamara() {
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
            capturing = false;
            Platform.runLater(() -> {
                imvCamara.setImage(null);
            });
        }
        executor.shutdown();

        btnCapturar.setDisable(true);
        btnEncenderApagar.setText("Encender");
        btnCapturar.setText("Capturar");
        camContainer.setStyle("");
        btnGuardar.setDisable(true);
    }

    /**
     * Captura una imagen estática desde la cámara web y la muestra en la
     * interfaz gráfica. Este método detiene temporalmente la captura continua
     * de video, obtiene un único fotograma de la cámara y lo convierte en una
     * imagen compatible con JavaFX. La imagen resultante se guarda en
     * lastCapturedFXImage y se muestra en el componente imvCamara.
     *
     * Si ocurre algún error durante la captura, se imprime la traza del error.
     */
    private void tomarFoto() {
        if (webcam != null && webcam.isOpen()) {
            try {
                capturing = false;
                BufferedImage bufferedImage = webcam.getImage();
                lastCapturedFXImage = SwingFXUtils.toFXImage(bufferedImage, null);

                Platform.runLater(() -> {
                    imvCamara.setImage(lastCapturedFXImage);
                });
            } catch (Exception e) {
                logger.log(Level.SEVERE, "[CamaraController.tomarFoto]: Error al capturar la foto", e);
            }
        }
    }

    /**
     * Reanuda la captura continua de video desde la cámara web después de haber
     * tomado una foto. Este método vuelve a activar el proceso de captura de
     * imágenes en tiempo real si la cámara está abierta, utilizando un hilo
     * separado para obtener y mostrar fotogramas a aproximadamente 30 cuadros
     * por segundo en el componente imvCamara.
     *
     * Este método es útil para retomar el video en vivo tras una captura
     * estática.
     */
    private void repetirFoto() {
        if (webcam != null && webcam.isOpen()) {
            capturing = true;
            executor.submit(() -> {
                while (webcam.isOpen() && capturing) {
                    try {
                        BufferedImage bufferedImage = webcam.getImage();
                        Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);

                        Platform.runLater(() -> {
                            imvCamara.setImage(fxImage);
                        });

                        Thread.sleep(1000 / 30);
                    } catch (InterruptedException e) {
                        logger.log(Level.SEVERE, "[CamaraController.repetirFoto]: Error al iniciar hilo", e);
                    }
                }
            });
        }
    }

    //EVENTOS
    @FXML
    private void onActionBtnCapturar(ActionEvent event) {
        capturar = !capturar;
        btnCapturar.setText(!capturar ? "Capturar" : "Repetir");

        if (capturar) {
            tomarFoto();
        } else {
            repetirFoto();
        }
    }

    @FXML
    private void onActionBtnGuardar(ActionEvent event) {
        switchCamara(false);
        AppContext.getInstance().set("capturedImage", lastCapturedFXImage);
        ((Stage) root.getScene().getWindow()).close();

    }

    @FXML
    private void onActionBtnEncenderApagar(ActionEvent event) {
        cameraOn = !cameraOn;
        btnEncenderApagar.setText(!cameraOn ? "Encender" : "Apagar");
        switchCamara(cameraOn);
    }
}
