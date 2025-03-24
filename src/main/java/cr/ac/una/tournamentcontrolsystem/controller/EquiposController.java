package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import cr.ac.una.tournamentcontrolsystem.service.RegistroDeporte;
import cr.ac.una.tournamentcontrolsystem.service.RegistroEquipo;
import cr.ac.una.tournamentcontrolsystem.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EquiposController extends Controller implements Initializable {

    @FXML
    private MFXTextField txfIdentificador;
    @FXML
    private MFXButton btnBuscar;
    @FXML
    private MFXButton btnVerEquipos;
    @FXML
    private MFXTextField txfNombre;
    @FXML
    private ImageView imvFoto;
    @FXML
    private MFXButton btnBuscarImagen;
    @FXML
    private MFXButton btnTomarFoto;
    @FXML
    private MFXComboBox<Deporte> cmbDeporte;
    @FXML
    private MFXButton btnGuardar;
    @FXML
    private MFXButton btnEliminar;
    @FXML
    private MFXButton btnNuevo;
    
    RegistroEquipo registroEquipo = RegistroEquipo.getInstance();
    RegistroDeporte registroDeporte = RegistroDeporte.getInstance();

    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEliminar.setDisable(true);
        loadDeportes();
    }

    @Override
    public void initialize() {
        // TODO
    }
    
    public void loadDeportes() { 
        List<Deporte> deportes = registroDeporte.getDeportes(); 
        System.out.println("Deportes disponibles: " + deportes); 

        cmbDeporte.getItems().clear();

        if (deportes != null && !deportes.isEmpty()) {
            cmbDeporte.getItems().addAll(deportes);
        } else {
            System.out.println("No hay deportes disponibles para mostrar."); 
        }
    }

    @FXML
    private void onActionBtnBuscar(ActionEvent event) {
        int idEquipo = Integer.parseInt(txfIdentificador.getText());
        registroEquipo.buscarEquipo(idEquipo);
        btnEliminar.setDisable(false);
    }
    
    @FXML
    private void onActionBtnVerEquipos(ActionEvent event) {
         //TODO: Desplegar una tabla con todos los equipos registrados
    }

  
    @FXML
    private void onDragDroppedStackPhoto(DragEvent event) {
        // TODO: Cargar en el imageView la imagen arrastrada
    }

    @FXML
    private void onActionBtnBuscarImagen(ActionEvent event) {
        registroEquipo.seleccionarImagen();
    }

    @FXML
    private void onActionBtnTomarFoto(ActionEvent event) {
        FlowController.getInstance().goViewInWindowModal("CamaraView", ((Stage) root.getScene().getWindow()), Boolean.FALSE);
    }

    @FXML
    private void onActionBtnGuardar(ActionEvent event) {
        String nombreEquipo = txfNombre.getText();
        Deporte deporteSeleccionado = cmbDeporte.getValue();

        System.out.println("Deporte seleccionado: " + deporteSeleccionado);

        if (!txfIdentificador.getText().isEmpty()) {
            int idEquipo = Integer.parseInt(txfIdentificador.getText());
            registroDeporte.editarDeporte(idEquipo, nombreEquipo);
        } else {
            registroEquipo.agregarEquipo(nombreEquipo, deporteSeleccionado);
        }
    }   

    @FXML
    private void onActionBtnEliminar(ActionEvent event) {
        int idEquipo = Integer.parseInt(txfIdentificador.getText());
        registroEquipo.eliminarEquipo(idEquipo);
    }

    @FXML
    private void onActionBtnNuevo(ActionEvent event) {
        txfIdentificador.clear();
        txfNombre.clear();
    }

}
