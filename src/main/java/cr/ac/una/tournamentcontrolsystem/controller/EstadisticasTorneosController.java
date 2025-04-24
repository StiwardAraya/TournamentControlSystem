package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Torneo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroTorneo;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * Controlador de la ventana de estadísticas por torneos.
 *
 * @author Stiward Araya C.
 * @author Angie Marks S.
 * @author Kevin Calderón Z.
 */
public class EstadisticasTorneosController extends Controller implements Initializable {

    @FXML
    private MFXComboBox<Torneo> cmbTorneos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void initialize() {
        cargarTorneos();
    }

    private void cargarTorneos() {
        try {
            Respuesta respuesta = RegistroTorneo.getInstance().getTorneos();
            if (respuesta.getEstado()) {
                List<Torneo> torneos = (List<Torneo>) respuesta.getResultado("torneos");
                cmbTorneos.getItems().clear();
                if (torneos != null && !torneos.isEmpty()) {
                    cmbTorneos.getItems().addAll(torneos);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
