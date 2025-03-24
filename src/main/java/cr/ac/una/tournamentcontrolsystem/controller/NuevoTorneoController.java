package cr.ac.una.tournamentcontrolsystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class NuevoTorneoController extends Controller implements Initializable {

    @FXML
    private MFXComboBox<?> mcbDeporte;
    @FXML
    private ListView<?> lvEquipos;
    @FXML
    private ListView<?> lvTorneo;
    @FXML
    private MFXButton btnGuardar;
    @FXML
    private MFXButton btnNuevo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void initialize() {
        // TODO
    }

    @FXML
    private void onActionBtnGuardar(ActionEvent event) {
    }

    @FXML
    private void onActionBtnNuevo(ActionEvent event) {
    }

}
