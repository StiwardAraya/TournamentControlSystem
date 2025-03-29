package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class NuevoTorneoController extends Controller implements Initializable {

    @FXML
    private MFXComboBox<Deporte> mcbDeporte;
    @FXML
    private ListView<Equipo> lvEquipos;
    @FXML
    private ListView<Equipo> lvTorneo;
    @FXML
    private MFXButton btnGuardar;
    @FXML
    private MFXButton btnNuevo;
    @FXML
    private MFXTextField txfNombre;
    @FXML
    private MFXTextField txfId;
    @FXML
    private MFXButton btnBuscar;

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

    @FXML
    private void onActionBtnBuscar(ActionEvent event) {
    }

}
