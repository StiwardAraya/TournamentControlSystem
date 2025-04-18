package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import cr.ac.una.tournamentcontrolsystem.model.EquipoTorneo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroEquipo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroEquipoTorneo;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Angie Marks
 */
public class RankingGlobalController extends Controller implements Initializable {

    @FXML
    private TableView<Equipo> tbRankingEquipos;
    @FXML
    private TableColumn<Equipo, String> clEquipo;
    @FXML
    private TableColumn<Equipo, String> clJugando;
    @FXML
    private TableColumn<Equipo, Integer> clPuntosTotales;
    @FXML
    private TableColumn<Equipo, Integer> clId;
    @FXML
    private TableColumn<Equipo, String> clDeporte;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarRanking();
    }

    @Override
    public void initialize() {
        //TODO
    }

    private void cargarRanking() {
        Respuesta respuesta = RegistroEquipo.getInstance().getEquipos();

        if (respuesta.getEstado()) {
            List<Equipo> equipos = (List<Equipo>) respuesta.getResultado("equipos");

            if (equipos != null && !equipos.isEmpty()) {
                clEquipo.setCellValueFactory(new PropertyValueFactory<>("nombre")); 
                clDeporte.setCellValueFactory(new PropertyValueFactory<>("deporte")); 
                clId.setCellValueFactory(new PropertyValueFactory<>("id"));
                clPuntosTotales.setCellValueFactory(new PropertyValueFactory<>("puntosTotales")); 
                clJugando.setCellValueFactory(cellData -> {
                    boolean enTorneoActivo = cellData.getValue().isEnTorneoActivo();
                    return new SimpleStringProperty(enTorneoActivo ? "Sí" : "No");
                });

                Collections.sort(equipos, (et1, et2) -> Integer.compare(et2.getPuntosTotales(), et1.getPuntosTotales()));
                ObservableList<Equipo> rankingObservableList = FXCollections.observableArrayList(equipos);

                tbRankingEquipos.setItems(rankingObservableList);
            } else {
                tbRankingEquipos.setItems(FXCollections.emptyObservableList());
            }
        } 
    }
}