package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import cr.ac.una.tournamentcontrolsystem.service.RegistroDeporte;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controlador de la ventana que despliega una tabla mostrando todos los
 * deportes guardados.
 *
 * @author Stiward Araya C.
 * @author Angie Marks S.
 * @author Kevin Calderón Z.
 */
public class DeportesMuestraController extends Controller implements Initializable {

    @FXML
    private TableView<Deporte> tbDeportes;
    @FXML
    private TableColumn<Deporte, Integer> clId;
    @FXML
    private TableColumn<Deporte, String> clNombre;

    private ObservableList<Deporte> deportes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @Override
    public void initialize() {
        clNombre.prefWidthProperty().bind(tbDeportes.widthProperty().subtract(clId.prefWidthProperty()).subtract(10));
        ArrayList<Deporte> listaDeportes = (ArrayList<Deporte>) RegistroDeporte.getInstance().getDeportes().getResultado("deportes");

        if (listaDeportes != null) {
            deportes = FXCollections.observableArrayList(listaDeportes);
            clId.setCellValueFactory(new PropertyValueFactory<>("id"));
            clNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            tbDeportes.setItems(deportes);
        }
    }

}
