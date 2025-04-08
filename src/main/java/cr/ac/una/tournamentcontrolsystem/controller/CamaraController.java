package cr.ac.una.tournamentcontrolsystem.controller;

import com.github.sarxos.webcam.Webcam;
import cr.ac.una.tournamentcontrolsystem.util.AppContext;
import cr.ac.una.tournamentcontrolsystem.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.scene.control.Alert;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCapturar.setDisable(true);
        btnGuardar.setDisable(true);
    }
    
    @Override
    public void initialize() {
        //TODO
    }

    @FXML
    private void onActionBtnCapturar(ActionEvent event) {
        capturar = !capturar;
        btnCapturar.setText(capturar ? "Capturar" : "Repetir");

        if (capturar) {
            tomarFoto();
        } else {
            repetirFoto();
        }
    }

    @FXML
    private void onActionBtnGuardar(ActionEvent event) {
        AppContext.getInstance().set("capturedImage", lastCapturedFXImage);
        new Mensaje().show(Alert.AlertType.INFORMATION, "Exito", "Su fotografia ha sido guardada");
    }

    @FXML
    private void onActionBtnEncenderApagar(ActionEvent event) {
        cameraOn = !cameraOn;
        btnEncenderApagar.setText(cameraOn ? "Encender" : "Apagar");
        if (cameraOn) {
            encenderCamara();
        } else {
            apagarCamara();
        }
    }

    private void encenderCamara() {
        executor = Executors.newSingleThreadExecutor();
        capturing = true;

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
                            imvCamara.setStyle("-fx-background-color: #FFFFFF; -fx-background-image: none; -fx-opacity: 1;");
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
    
    private void apagarCamara(){
        if (webcam != null && webcam.isOpen()){
            webcam.close();
            capturing = false;
            Platform.runLater(() -> {
                imvCamara.setImage(null);
            });
        }
        btnCapturar.setDisable(true);
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
