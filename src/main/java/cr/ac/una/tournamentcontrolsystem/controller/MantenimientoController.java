package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MantenimientoController extends Controller implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private MFXButton btnDeportes;
    @FXML
    private MFXButton btnEquipos;
    @FXML
    private MFXButton btnRegresar;
    @FXML
    private ImageView imvMantenimientoIcon;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startIconRotation();
    }

    @Override
    public void initialize() {
        //TODO
    }

    @FXML
    private void onActionBtnDeportes(ActionEvent event) {
        FlowController.getInstance().goView("DeportesView");
    }

    @FXML
    private void onActionBtnEquipos(ActionEvent event) {
        FlowController.getInstance().goView("EquiposView");
    }

    @FXML
    private void onActionBtnRegresar(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("MainView");
        ((Stage) root.getScene().getWindow()).close();
    }

    private void startIconRotation() {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(8), imvMantenimientoIcon);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.setInterpolator(javafx.animation.Interpolator.LINEAR);
        rotateTransition.play();
    }

}
