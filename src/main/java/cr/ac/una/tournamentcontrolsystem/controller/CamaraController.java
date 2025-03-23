package cr.ac.una.tournamentcontrolsystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class CamaraController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private MFXButton btnCancelar;
    @FXML
    private ImageView imvCamara;
    @FXML
    private MFXButton btnCapturar;
    @FXML
    private MFXButton btnGuardar;
    @FXML
    private MFXButton btnEncenderApagar;

    private boolean cameraOn = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCapturar.setDisable(true);
        btnGuardar.setDisable(true);
    }

    @Override
    public void initialize() {
        // TODO
    }

    @FXML
    private void onActionBtnCancelar(ActionEvent event) {
    }

    @FXML
    private void onActionBtnCapturar(ActionEvent event) {
    }

    @FXML
    private void onActionBtnGuardar(ActionEvent event) {
    }

    @FXML
    private void onActionBtnEncenderApagar(ActionEvent event) {
        cameraOn = !cameraOn;
        btnEncenderApagar.setText(cameraOn ? "Encender" : "Apagar");
        if (cameraOn) {
            // TODO: codigo para apagar la camara
        } else {
            // TODO: codigo para encender la camara
        }
    }

}
