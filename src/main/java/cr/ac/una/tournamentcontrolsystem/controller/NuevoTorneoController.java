package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import cr.ac.una.tournamentcontrolsystem.model.Torneo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroDeporte;
import cr.ac.una.tournamentcontrolsystem.service.RegistroEquipo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroTorneo;
import cr.ac.una.tournamentcontrolsystem.util.Mensaje;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.control.Alert;

public class NuevoTorneoController extends Controller implements Initializable {

    @FXML
    private MFXComboBox<Deporte> mcbDeporte;
    @FXML
    private ListView<Equipo> lvEquipos;
    @FXML
    private ListView<Equipo> lvTorneo;
    @FXML
    private MFXButton btnGuardar;
    @FXML
    private MFXButton btnNuevo;
    @FXML
    private MFXTextField txfNombre;
    @FXML
    private MFXTextField txfId;
    @FXML
    private MFXButton btnBuscar;
    
    private Equipo equipo;
    private Deporte deporte;
    private Torneo torneo;  

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.torneo = new Torneo();
        cargarDeportes();
        mcbDeporte.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        cargarEquipos(); 
    });
        
    dragAndDrop(lvEquipos, lvTorneo);
    dragAndDrop(lvTorneo, lvEquipos);
    }

    @Override
    public void initialize() {
        // TODO
    }

    @FXML
    private void onActionBtnGuardar(ActionEvent event) {
        if (txfNombre.getText().isBlank() || txfNombre.getText().isEmpty()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Guardar Torneo", "Debe llenar el espacio nombre");
            return;
        }

        Deporte deporteSeleccionado = mcbDeporte.getSelectionModel().getSelectedItem();
        if (deporteSeleccionado == null) {
            new Mensaje().show(Alert.AlertType.ERROR, "Seleccionar Deporte", "Debe seleccionar un deporte");
            return;
        }

        torneo = new Torneo();

        int cantidadEquipos = lvTorneo.getItems().size(); 

        if (cantidadEquipos <= 1 || !esPotenciaDeDos(cantidadEquipos)) {
            new Mensaje().show(Alert.AlertType.ERROR, "Guardar Torneo", "La cantidad de equipos debe ser una potencia de 2 y mayor que 1.");
            return;
        }

        if (txfId.getText().isBlank()) {
            torneo.setId(0);
        }

        torneo.setNombre(txfNombre.getText());
        Respuesta respuestaGuardarTorneo = RegistroTorneo.getInstance().guardarTorneo(torneo, deporteSeleccionado);
        if (!respuestaGuardarTorneo.getEstado()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Guardar torneo", respuestaGuardarTorneo.getMensaje());
        } else {
            new Mensaje().show(Alert.AlertType.CONFIRMATION, "Guardar torneo", respuestaGuardarTorneo.getMensaje());
        }
    }

    @FXML
    private void onActionBtnNuevo(ActionEvent event) {
        reiniciarVentana();
    }

    @FXML
    private void onActionBtnBuscar(ActionEvent event) {
      if (txfId.getText().isBlank() || txfId.getText().isEmpty()) {
          new Mensaje().show(Alert.AlertType.ERROR, "Id", "Debe ingresar un id para buscar un Torneo");
          return;
      }

      Respuesta respuestaBuscarTorneo = RegistroTorneo.getInstance().buscarTorneo(Integer.parseInt(txfId.getText()));
      if (!respuestaBuscarTorneo.getEstado()) {
          new Mensaje().show(Alert.AlertType.ERROR, "Deporte", respuestaBuscarTorneo.getMensaje());
      } else {
          torneo = (Torneo) respuestaBuscarTorneo.getResultado("torneoEncontrado");
          txfId.setText(String.valueOf(torneo.getId()));
          txfId.setEditable(false); 
          txfNombre.setText(torneo.getNombre());
          btnGuardar.setText("Actualizar");
      }   
    }
    
    private void reiniciarVentana() {
        txfId.clear();
        txfId.setEditable(true);
        btnGuardar.setText("Guardar");
        txfNombre.clear();
        lvEquipos.getItems().clear(); 
        lvTorneo.getItems().clear(); 
        mcbDeporte.getSelectionModel().clearSelection(); 
    }

    private void cargarEquipos() {
        Respuesta respuestaEquipos = RegistroEquipo.getInstance().getEquipos(); 
        lvEquipos.getItems().clear(); 

        Deporte deporteSeleccionado = mcbDeporte.getSelectionModel().getSelectedItem(); 
        System.out.println("Deporte seleccionado: " + deporteSeleccionado);

        if (respuestaEquipos.getEstado()) { 
            List<Equipo> equipos = (List<Equipo>) respuestaEquipos.getResultado("equipos"); 

            if (equipos != null && !equipos.isEmpty()) {
                for (Equipo equipo : equipos) {
                    if (deporteSeleccionado != null && equipo.getDeporte().equals(deporteSeleccionado)) {
                        lvEquipos.getItems().add(equipo);
                    }
                }
            } else {
                new Mensaje().show(Alert.AlertType.WARNING, "Cargar Equipos", "No se encontraron equipos disponibles.");
            }
        } 
    }
    
    private void cargarDeportes() { 
        try {
            Respuesta respuesta = RegistroDeporte.getInstance().getDeportes(); 

            if (respuesta.getEstado()) { 
                List<Deporte> deportes = (List<Deporte>) respuesta.getResultado("deportes"); 

                mcbDeporte.getItems().clear(); 

                if (deportes != null && !deportes.isEmpty()) {
                    mcbDeporte.getItems().addAll(deportes); 
                } 
            } 
        } catch (Exception e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar deportes", e.getMessage()); 
        }
    }

    private void dragAndDrop(ListView<Equipo> source, ListView<Equipo> target) {
        source.setOnDragDetected(event -> {
            Dragboard dragboard = source.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            Equipo equipoSeleccionado = source.getSelectionModel().getSelectedItem();
            if (equipoSeleccionado != null) {
                content.putString(equipoSeleccionado.getNombre());
                dragboard.setContent(content);
                event.consume();
            }
        });

        target.setOnDragOver(event -> {
            if (event.getGestureSource() != target && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        target.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasString()) {
                String equipoNombre = dragboard.getString();
                for (Equipo equipo : source.getItems()) {
                    if (equipo.getNombre().equals(equipoNombre)) {
                        source.getItems().remove(equipo);
                        target.getItems().add(equipo);
                        success = true;
                        break;
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }
    
    private boolean esPotenciaDeDos(int numero) {
        return (numero > 0) && ((numero & (numero - 1)) == 0);
    }

}
