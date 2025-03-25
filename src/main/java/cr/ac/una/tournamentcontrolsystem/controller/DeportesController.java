package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.service.RegistroDeporte;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

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
    
   RegistroDeporte registroDeporte = RegistroDeporte.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEliminar.setDisable(true);
        imvPhoto.setOnDragOver(event -> onDragOverImage(event));
        imvPhoto.setOnDragDropped(event -> onDragDroppedImage(event));
    }

    @Override
    public void initialize() {
        //TODO
    }
    
    private void onDragOverImage(DragEvent event) {
       
        if (event.getGestureSource() != imvPhoto && event.getDragboard().hasImage()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
    }

    private void onDragDroppedImage(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;

        if (db.hasImage()) {
            Image image = db.getImage();
            imvPhoto.setImage(image);
            success = true;
        }

        event.setDropCompleted(success);
        event.consume();
    }

    @FXML
    private void onActionBtnBuscar(ActionEvent event) {
        int idDeporte = Integer.parseInt(txfIdentificador.getText());
        registroDeporte.buscarDeporte(idDeporte);
        btnEliminar.setDisable(false);
    }

    @FXML
    private void onActionBtnVerDeportes(ActionEvent event) {
        //TODO: Desplegar una tabla con todos los deportes registrados
    }

    @FXML
    private void onActionBtnBuscarImagen(ActionEvent event) {
        registroDeporte.seleccionarImagen();
    }

    @FXML
    private void onActionBtnGuardar(ActionEvent event) {
      String nombreDeporte = txfNombre.getText();

      if (!txfIdentificador.getText().isEmpty()) {
          int idDeporte = Integer.parseInt(txfIdentificador.getText());
          registroDeporte.editarDeporte(idDeporte, nombreDeporte);
      } else {
          registroDeporte.agregarDeporte(nombreDeporte);
      }
    }
    
    @FXML
    private void onActionBtnEliminar(ActionEvent event) {
        int idDeporte = Integer.parseInt(txfIdentificador.getText());
        registroDeporte.eliminarDeporte(idDeporte);
    }

    @FXML
    private void onActionBtnNuevo(ActionEvent event) {
       txfNombre.clear();
       txfIdentificador.clear();
    }

    @FXML
    private void onDragDroppedStackPhoto(DragEvent event) {
        //TODO: Cargar en el ImageView de foto la imagen arrastrada
    }

}
