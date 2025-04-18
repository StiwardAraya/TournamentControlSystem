package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import cr.ac.una.tournamentcontrolsystem.model.EquipoTorneo;
import cr.ac.una.tournamentcontrolsystem.model.Llaves;
import cr.ac.una.tournamentcontrolsystem.model.LlavesTorneo;
import cr.ac.una.tournamentcontrolsystem.model.Torneo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroDeporte;
import cr.ac.una.tournamentcontrolsystem.service.RegistroEquipo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroEquipoTorneo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroLlavesTorneos;
import cr.ac.una.tournamentcontrolsystem.service.RegistroTorneo;
import cr.ac.una.tournamentcontrolsystem.util.Formato;
import cr.ac.una.tournamentcontrolsystem.util.Mensaje;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

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
    private ImageView imvImagenBalon;
    @FXML
    private MFXTextField txfTiempoPorPartido;
    @FXML
    private VBox minibalonContainer;

    private Equipo equipo;
    private Deporte deporte;
    private Torneo torneo;
    private EquipoTorneo equipoTorneo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        torneo = new Torneo();
        deporte = new Deporte();
        equipo = new Equipo();
        txfTiempoPorPartido.setTextFormatter(Formato.getInstance().integerFormat());

        mcbDeporte.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cargarEquipos();
        });

        mcbDeporte.valueProperty().addListener((observable, oldValue, newValue) -> {
            Deporte deporteCombo = mcbDeporte.getSelectedItem();
            if (deporteCombo != null) {
                String imagenBalonURL = deporteCombo.getImagenURL();
                imvImagenBalon.setImage(new Image(new File(imagenBalonURL).toURI().toString()));
                minibalonContainer.setStyle("-fx-opacity: 1");
            }
        });

        dragAndDrop(lvEquipos, lvTorneo);
        dragAndDrop(lvTorneo, lvEquipos);
    }

    @Override
    public void initialize() {
        cargarDeportes();
    }

    @FXML
    private void onActionBtnGuardar(ActionEvent event) {
        if (txfNombre.getText().isBlank() || txfNombre.getText().isEmpty()) {
            txfNombre.setStyle("-fx-border-color: #D21F3C");
            shake(txfNombre);
            return;
        }

        if (mcbDeporte.getSelectionModel().getSelectedItem() == null) {
            mcbDeporte.setStyle("-fx-border-color: #D21F3C");
            shake(mcbDeporte);
            return;
        }

        if (txfTiempoPorPartido.getText().isBlank() || txfTiempoPorPartido.getText().isEmpty()) {
            txfTiempoPorPartido.setStyle("-fx-border-color: #D21F3C");
            shake(txfTiempoPorPartido);
            return;
        }

        if (lvTorneo.getItems().size() <= 1 || !esPotenciaDeDos(lvTorneo.getItems().size())) {
            new Mensaje().show(Alert.AlertType.ERROR, "Guardar Torneo", "La cantidad de equipos debe ser una potencia de 2 y mayor que 1.");
            return;
        }

        torneo.setId(0);
        torneo.setNombre(txfNombre.getText());
        torneo.setTiempoPorPartido(Integer.parseInt(txfTiempoPorPartido.getText()));

        Respuesta respuestaGuardarTorneo = RegistroTorneo.getInstance().guardarTorneo(torneo, mcbDeporte.getSelectionModel().getSelectedItem());

        if (!respuestaGuardarTorneo.getEstado()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Guardar torneo", respuestaGuardarTorneo.getMensaje());
        } else {
            new Mensaje().show(Alert.AlertType.CONFIRMATION, "Guardar torneo", respuestaGuardarTorneo.getMensaje());
            guardarMapeoEquiposTorneos();
            crearLlaves();
            reiniciarVentana();
        }
    }

    @FXML
    private void onActionBtnNuevo(ActionEvent event) {
        reiniciarVentana();
    }

    private void reiniciarVentana() {
        txfTiempoPorPartido.clear();
        txfTiempoPorPartido.setStyle("");
        btnGuardar.setText("Guardar");
        txfNombre.clear();
        txfNombre.setStyle("");
        lvEquipos.getItems().clear();
        lvTorneo.getItems().clear();
        mcbDeporte.getSelectionModel().clearSelection();
        mcbDeporte.setStyle("");
        imvImagenBalon.setImage(null);
        minibalonContainer.setStyle("");
        torneo = new Torneo();
        equipo = new Equipo();
        deporte = new Deporte();
    }

    private void cargarEquipos() {
        Respuesta respuestaEquipos = RegistroEquipo.getInstance().getEquipos();

        lvEquipos.getItems().clear();
        lvTorneo.getItems().clear();

        Deporte deporteSeleccionado = mcbDeporte.getSelectionModel().getSelectedItem();

        if (respuestaEquipos.getEstado()) {
            List<Equipo> equipos = (List<Equipo>) respuestaEquipos.getResultado("equipos");

            if (equipos != null && !equipos.isEmpty()) {
                for (Equipo equipo : equipos) {
                    if (deporteSeleccionado != null && equipo.getDeporte().equals(deporteSeleccionado) && !equipo.isEnTorneoActivo()) {
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

    private void shake(Node element) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(100), element);
        shake.setFromX(0);
        shake.setToX(10);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.play();
    }

    private void guardarMapeoEquiposTorneos() {
        ObservableList<Equipo> equiposInscritosLista = lvTorneo.getItems();
        ObservableList<Equipo> equiposInscritos = FXCollections.observableList(equiposInscritosLista);

        for (Equipo equipoInscrito : equiposInscritos) {
            actualizarEquipo(equipoInscrito.getId());
            equipoInscrito.setEnTorneoActivo(true);
            Respuesta respuestaGuardarMapeo = RegistroEquipoTorneo.getInstance().guardarEquipoTorneo(new EquipoTorneo(0, 0, 0, equipoInscrito, torneo));
            if (!respuestaGuardarMapeo.getEstado()) {
                new Mensaje().show(Alert.AlertType.ERROR, "Error de inscripcion", "Ocurrio un error al inscribir el equipo: " + equipoInscrito.toString());
            }
        }
    }

    private void crearLlaves() {
        ObservableList<Equipo> equiposInscritos = lvTorneo.getItems();
        ObservableList<Equipo> copiaEquipos = FXCollections.observableArrayList(equiposInscritos);
        Llaves llaves = new Llaves(copiaEquipos);
        LlavesTorneo llavesTorneo = new LlavesTorneo(torneo.getId(), llaves);
        Respuesta respuestaGuardarLlavesTorneo = RegistroLlavesTorneos.getInstance().guardarLlavesTorneo(llavesTorneo);
        if (!respuestaGuardarLlavesTorneo.getEstado()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Llaves de torneo", "Error al construir las llaves del torneo");
        }
    }

    private void actualizarEquipo(int idEquipoInscrito) {
        Equipo equipoInscrito = (Equipo) RegistroEquipo.getInstance().buscarEquipo(idEquipoInscrito).getResultado("equipoEncontrado");
        equipoInscrito.setEnTorneoActivo(true);
        RegistroEquipo.getInstance().guardarEquipo(equipoInscrito, new Image(new File(equipoInscrito.getFotoURL()).toURI().toString()));
    }
}
