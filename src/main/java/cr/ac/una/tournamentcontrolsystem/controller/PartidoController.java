package cr.ac.una.tournamentcontrolsystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class PartidoController extends Controller implements Initializable {

    @FXML
    private MFXButton btnEmpezar;
    @FXML
    private Label lblTiempo;
    @FXML
    private Label lblNombreEquipoIzq;
    @FXML
    private Label lblMarcadorEquipoIzq;
    @FXML
    private MFXButton btnFinalizar;
    @FXML
    private StackPane containerBalon;
    @FXML
    private ImageView imvBalon;
    @FXML
    private Label lblNombreEquipoDer;
    @FXML
    private Label lblMarcadorEquipoDer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void initialize() {
        // TODO
    }

    @FXML
    private void onActionBtnEmpezar(ActionEvent event) {
    }

    @FXML
    private void onActionBtnFinalizar(ActionEvent event) {
    }

}
