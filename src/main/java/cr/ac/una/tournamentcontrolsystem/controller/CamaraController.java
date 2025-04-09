package cr.ac.una.tournamentcontrolsystem.controller;

import com.github.sarxos.webcam.Webcam;
import cr.ac.una.tournamentcontrolsystem.util.AppContext;
import cr.ac.una.tournamentcontrolsystem.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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

    private boolean cameraOn = false;
    private boolean capturar = false;
    private volatile boolean capturing = true;
    private ExecutorService executor;
    private Image lastCapturedFXImage;
    private Mensaje mensaje;
    Webcam webcam = Webcam.getDefault();
    @FXML
    private AnchorPane camContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCapturar.setDisable(true);
        btnGuardar.setDisable(true);
    }

    @Override
    public void initialize() {
    }

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

    private void switchCamara(boolean value) {
        executor = Executors.newSingleThreadExecutor();
        if (value) {
            encenderCamara();
        } else {
            apagarCamara();
        }
    }

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
                        e.printStackTrace();
                    }
                }
            });
        }
        btnCapturar.setDisable(false);
        btnGuardar.setDisable(false);
    }

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
                e.printStackTrace();
            }
        }
    }

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
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
