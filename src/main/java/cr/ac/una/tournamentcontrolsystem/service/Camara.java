package cr.ac.una.tournamentcontrolsystem.service;

import com.github.sarxos.webcam.Webcam;
import cr.ac.una.tournamentcontrolsystem.util.Mensaje;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

public class Camara {
    Mensaje mensaje = new Mensaje();
    Webcam webcam = Webcam.getDefault();
    
    public void encenderCamara(){
        ExecutorService executor = Executors.newSingleThreadExecutor();

        if (webcam != null) {
            webcam.setViewSize(new Dimension(640, 480));
            webcam.open();

            executor.submit(() -> {
                while (webcam.isOpen()) {
                    try {
                        BufferedImage bufferedImage = webcam.getImage();
                        Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);

                        Thread.sleep(1000 / 30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
           mensaje.show(Alert.AlertType.ERROR, "Error", "La cámara no está encendida.");
        }
    }
    
    public void apagarCamara(){
        if (webcam != null) {
            webcam.close();
            mensaje.show(Alert.AlertType.INFORMATION, "Éxito", "Cámara apagada ");

        } else {
            mensaje.show(Alert.AlertType.ERROR, "Error", "No se encontró la cámara.");
        }
    }
    
    public void volverTomarFoto(){
        tomarFoto();
    }
    
    public void tomarFoto(){
        if (webcam != null) {
            if (!webcam.isOpen()) {
                webcam.open();
            }

            BufferedImage bufferedImage = webcam.getImage();
            Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);

            webcam.close(); 
        } else {
            mensaje.show(Alert.AlertType.ERROR, "Error", "No se encontró la cámara.");
        }
    }
}
