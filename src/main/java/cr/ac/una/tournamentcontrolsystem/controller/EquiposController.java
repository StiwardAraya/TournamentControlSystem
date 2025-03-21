package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
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
    private MFXComboBox<?> cmbDeporte;
    @FXML
    private MFXButton btnGuardar;
    @FXML
    private MFXButton btnEliminar;
    @FXML
    private MFXButton btnNuevo;
    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEliminar.setDisable(true);
    }

    @Override
    public void initialize() {
        // TODO
    }

    @FXML
    private void onActionBtnBuscar(ActionEvent event) {
        // TODO: Buscar un equipo por id
    }

    @FXML
    private void onActionBtnVerEquipos(ActionEvent event) {
        // TODO: Desplegar una tabla con todos los equipos registrados
    }

    @FXML
    private void onDragDroppedStackPhoto(DragEvent event) {
        // TODO: Cargar en el imageView la imagen arrastrada
    }

    @FXML
    private void onActionBtnBuscarImagen(ActionEvent event) {
        // TODO: Abrir el explorador de archivos
    }

    @FXML
    private void onActionBtnTomarFoto(ActionEvent event) {
        FlowController.getInstance().goViewInWindowModal("CamaraView", ((Stage) root.getScene().getWindow()), Boolean.FALSE);
    }

    @FXML
    private void onActionBtnGuardar(ActionEvent event) {
        // TODO: Guardar los cambios
    }

    @FXML
    private void onActionBtnEliminar(ActionEvent event) {
        // TODO: Eliminar el objeto cargado
    }

    @FXML
    private void onActionBtnNuevo(ActionEvent event) {
        // TODO: Reiniciar la ventana
    }

}
