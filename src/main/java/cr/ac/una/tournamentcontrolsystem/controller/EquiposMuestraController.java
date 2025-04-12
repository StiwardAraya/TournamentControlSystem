package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroEquipo;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class EquiposMuestraController extends Controller implements Initializable {

    @FXML
    private TableView<Equipo> tbEquipos;
    @FXML
    private TableColumn<Equipo, Integer> clId;
    @FXML
    private TableColumn<Equipo, String> clNombre;
    @FXML
    private TableColumn<Equipo, Deporte> clDeporte;
    @FXML
    private TableColumn<Equipo, Integer> clPuntos;
    @FXML
    private TableColumn<Equipo, String> clJugando;

    private ObservableList<Equipo> equipos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void initialize() {
        ArrayList<Equipo> listaEquipos = (ArrayList<Equipo>) RegistroEquipo.getInstance().getEquipos().getResultado("equipos");
        if (listaEquipos != null) {
            equipos = FXCollections.observableArrayList(listaEquipos);
            clId.setCellValueFactory(new PropertyValueFactory<>("id"));
            clNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            clDeporte.setCellValueFactory(new PropertyValueFactory<>("deporte"));
            clPuntos.setCellValueFactory(new PropertyValueFactory<>("puntosTotales"));
            clJugando.setCellValueFactory(cellData -> {
                boolean enTorneoActivo = cellData.getValue().isEnTorneoActivo();
                return new SimpleStringProperty(enTorneoActivo ? "Sí" : "No");
            });
            tbEquipos.setItems(equipos);
        }
    }

}
