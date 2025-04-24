package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.util.FlowController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controlador de la ventana principal de torneos
 *
 * @author Stiward Araya C.
 * @author Angie Marks S.
 * @author Kevin Calderon Z.
 */
public class TorneosController extends Controller implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private ImageView imvTorneos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        shakeLogo(imvTorneos);
    }

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionBtnNuevo(ActionEvent event) {
        FlowController.getInstance().goView("NuevoTorneoView");
    }

    @FXML
    private void onActionBtnPosiciones(ActionEvent event) {
        FlowController.getInstance().goView("LlavesTorneosView");
    }

    @FXML
    private void onActionBtnPartido(ActionEvent event) {
    }

    @FXML
    private void onActionBtnRegresar(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("MainView");
        ((Stage) root.getScene().getWindow()).close();
    }

    private void shakeLogo(ImageView logo) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(100), logo);
        shake.setFromX(0);
        shake.setToX(10);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.play();
    }

}
