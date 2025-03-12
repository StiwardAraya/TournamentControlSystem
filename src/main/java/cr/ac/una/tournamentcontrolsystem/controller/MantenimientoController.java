package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MantenimientoController extends Controller implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private MFXButton btnDeportes;
    @FXML
    private MFXButton btnEquipos;
    @FXML
    private MFXButton btnRegresar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void initialize() {
        //TODO
    }

    @FXML
    private void onActionBtnDeportes(ActionEvent event) {
        //TODO: Abrir view CRUD de deportes
    }

    @FXML
    private void onActionBtnEquipos(ActionEvent event) {
        //TODO: Abrir view CRUD de equipos
    }

    @FXML
    private void onActionBtnRegresar(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("MainView");
        ((Stage) root.getScene().getWindow()).close();
    }

}
