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
 * FXML Controller class
 *
 * @author Angie Marks
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
                  if (equipoSeleccionado != null && equipoTorneo.getEquipo().equals(equipoSeleccionado)) {
                      tbEquipos.getItems().add(equipoTorneo);
                  } 
              }
          }
        }
    }  
}
