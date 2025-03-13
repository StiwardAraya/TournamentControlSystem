package cr.ac.una.tournamentcontrolsystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;

public class DeportesController extends Controller implements Initializable {

    @FXML
    private MFXButton btnBuscar;
    @FXML
    private MFXTextField txfIdentificador;
    @FXML
    private MFXButton btnVerDeportes;
    @FXML
    private MFXTextField txfNombre;
    @FXML
    private ImageView imvPhoto;
    @FXML
    private MFXButton btnBuscarImagen;
    @FXML
    private MFXButton btnGuardar;
    @FXML
    private MFXButton btnEliminar;
    @FXML
    private MFXButton btnNuevo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }

    @Override
    public void initialize() {
        //TODO
    }

    @FXML
    private void onActionBtnBuscar(ActionEvent event) {
        //TODO: Buscar un deporte por id
    }

    @FXML
    private void onActionBtnVerDeportes(ActionEvent event) {
        //TODO: Desplegar una tabla con todos los deportes registrados
    }

    @FXML
    private void onActionBtnBuscarImagen(ActionEvent event) {
        //TODO: Abrir el explorador de archivos
    }

    @FXML
    private void onActionBtnGuardar(ActionEvent event) {
        //TODO: Guardar los cambios (modificar o agregar)
    }

    @FXML
    private void onActionBtnEliminar(ActionEvent event) {
        //TODO: Eliminar el registro cargado
    }

    @FXML
    private void onActionBtnNuevo(ActionEvent event) {
        //TODO: Resetear toda la ventana
    }

    @FXML
    private void onDragDroppedStackPhoto(DragEvent event) {
        //TODO: Cargar en el ImageView de foto la imagen arrastrada
    }

}
