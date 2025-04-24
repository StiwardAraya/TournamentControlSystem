package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.util.FlowController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Controlador de la ventana principal de estadísticas.
 *
 * @author Stiward Araya C.
 * @author Angie Marks S.
 * @author Kevin Calderón Z.
 */
public class EstadisticasController extends Controller implements Initializable {

    @FXML
    private BorderPane root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void initialize() {
        //TODO
    }

    // EVENTOS
    @FXML
    private void onActionBtnEquipos(ActionEvent event) {
        FlowController.getInstance().goView("EstadisticasEquiposView");
    }

    @FXML
    private void onActionBtnTorneos(ActionEvent event) {
        FlowController.getInstance().goView("EstadisticasTorneosView");
    }

    @FXML
    private void onActionBtnRankingGlobal(ActionEvent event) {
        FlowController.getInstance().goView("RankingGlobalView");
    }

    @FXML
    private void onActionBtnRegresar(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("MainView");
        ((Stage) root.getScene().getWindow()).close();
    }

}
