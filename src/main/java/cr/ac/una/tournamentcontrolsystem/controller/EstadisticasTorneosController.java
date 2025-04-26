package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.EquipoTorneo;
import cr.ac.una.tournamentcontrolsystem.model.Torneo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroEquipoTorneo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroTorneo;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    @FXML
    private TableView<EquipoTorneo> tbTorneos;
    @FXML
    private TableColumn<EquipoTorneo, String> clEquipo;
    @FXML
    private TableColumn<EquipoTorneo, Integer> clPartidosJugados;
    @FXML
    private TableColumn<EquipoTorneo, Integer> clPuntosObtenidos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void initialize() {
        cargarTorneos();

        cmbTorneos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cargarTabla();
        });
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

    private void cargarTabla() {
        Respuesta respuestaEquipoTorneo = RegistroEquipoTorneo.getInstance().getEquiposTorneos();
        tbTorneos.getItems().clear();

        Torneo torneoSeleccionado = cmbTorneos.getSelectionModel().getSelectedItem();

        if (respuestaEquipoTorneo.getEstado()) {
            List<EquipoTorneo> listaEquiposTorneo = (List<EquipoTorneo>) respuestaEquipoTorneo.getResultado("EquiposTorneos");

            if (listaEquiposTorneo != null && !listaEquiposTorneo.isEmpty()) {
                clEquipo.setCellValueFactory(new PropertyValueFactory<>("equipo"));
                clPartidosJugados.setCellValueFactory(new PropertyValueFactory<>("partidosJugados"));
                clPuntosObtenidos.setCellValueFactory(new PropertyValueFactory<>("puntosEquipo"));

                for (EquipoTorneo equipoTorneo : listaEquiposTorneo) {
                    if (torneoSeleccionado != null && equipoTorneo.getTorneo().getId() == torneoSeleccionado.getId()) {
                        tbTorneos.getItems().add(equipoTorneo);
                    }
                }
            }
        }
    }

}
