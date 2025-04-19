package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Angie Marks
 */
public class EstadisticasController extends Controller implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private MFXButton btnTorneos;
    @FXML
    private MFXButton btnRegresar;
    private ImageView imvEstadisticasIcon;
    @FXML
    private MFXButton btnEquipos;
    @FXML
    private MFXButton btnRankingGlobal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @Override
    public void initialize() {
        //TODO
    }
    
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
