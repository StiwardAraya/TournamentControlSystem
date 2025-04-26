package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import cr.ac.una.tournamentcontrolsystem.model.EquipoTorneo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroEquipo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroEquipoTorneo;
import cr.ac.una.tournamentcontrolsystem.util.Mensaje;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controlador de la ventana de estadísticas por equipos.
 *
 * @author Stiward Araya C.
 * @author Angie Marks S.
 * @author Kevin Calderón Z.
 */
public class EstadisticasEquiposController extends Controller implements Initializable {

    @FXML
    private MFXComboBox<Equipo> cmbEquipos;
    @FXML
    private TableView<EquipoTorneo> tbEquipos;
    @FXML
    private TableColumn<EquipoTorneo, String> clTorneo;
    @FXML
    private TableColumn<EquipoTorneo, Integer> clPartidosJugados;
    @FXML
    private TableColumn<EquipoTorneo, Integer> clPosicion;
    @FXML
    private TableColumn<EquipoTorneo, Integer> clPuntos;

    private ObservableList<EquipoTorneo> equipoTorneo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }

    @Override
    public void initialize() {
        cargarEquipos();
        cargarTabla();

        cmbEquipos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cargarTabla();
        });
    }

    /**
     * Carga la lista de equipos disponibles desde la capa de datos y la muestra
     * en el comboBox de equipos.
     *
     * Este método obtiene los equipos a través de Registro Equipo, si la
     * respuesta es exitosa, limpia el combo box y agrega los equipos obtenidos.
     */
    private void cargarEquipos() {
        try {
            Respuesta respuesta = RegistroEquipo.getInstance().getEquipos();

            if (respuesta.getEstado()) {
                List<Equipo> equipos = (List<Equipo>) respuesta.getResultado("equipos");

                cmbEquipos.getItems().clear();

                if (equipos != null && !equipos.isEmpty()) {
                    cmbEquipos.getItems().addAll(equipos);
                }
            }
        } catch (Exception e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar equipos", e.getMessage());
        }
    }

    /**
     * Carga y muestra en la tabla los datos de participación en torneos del
     * equipo seleccionado.
     *
     * Este método obtiene la lista de registros EquipoTorneo desde la clase
     * RegistroEquipoTorneo. Si la respuesta es válida, limpia la tabla
     * tbEquipos, configura las columnas para mostrar el nombre del torneo,
     * partidos jugados, posición final y puntos obtenidos.
     *
     * Luego filtra los registros para mostrar solo aquellos que correspondan al
     * equipo seleccionado en el combo box cmbEquipos.
     */
    private void cargarTabla() {
        Respuesta respuestaEquipoTorneo = RegistroEquipoTorneo.getInstance().getEquiposTorneos();
        tbEquipos.getItems().clear();

        Equipo equipoSeleccionado = cmbEquipos.getSelectionModel().getSelectedItem();

        if (respuestaEquipoTorneo.getEstado()) {
            List<EquipoTorneo> listaEquiposTorneo = (List<EquipoTorneo>) respuestaEquipoTorneo.getResultado("EquiposTorneos");

            if (listaEquiposTorneo != null && !listaEquiposTorneo.isEmpty()) {
                clTorneo.setCellValueFactory(new PropertyValueFactory<>("torneo"));
                clPartidosJugados.setCellValueFactory(new PropertyValueFactory<>("partidosJugados"));
                clPosicion.setCellValueFactory(new PropertyValueFactory<>("posicionFinal"));
                clPuntos.setCellValueFactory(new PropertyValueFactory<>("puntosEquipo"));

                for (EquipoTorneo equipoTorneo : listaEquiposTorneo) {
                    if (equipoSeleccionado != null && equipoTorneo.getEquipo().getId() == equipoSeleccionado.getId()) {
                        tbEquipos.getItems().add(equipoTorneo);
                    }
                }
            }
        }
    }
}
