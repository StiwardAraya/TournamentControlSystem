package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import cr.ac.una.tournamentcontrolsystem.model.EquipoPartido;
import cr.ac.una.tournamentcontrolsystem.model.LlavesTorneo;
import cr.ac.una.tournamentcontrolsystem.model.Partido;
import cr.ac.una.tournamentcontrolsystem.model.Torneo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroEquipo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroEquipoPartido;
import cr.ac.una.tournamentcontrolsystem.service.RegistroLlavesTorneos;
import cr.ac.una.tournamentcontrolsystem.service.RegistroPartido;
import cr.ac.una.tournamentcontrolsystem.service.RegistroTorneo;
import cr.ac.una.tournamentcontrolsystem.util.AppContext;
import cr.ac.una.tournamentcontrolsystem.util.Mensaje;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class PartidoController extends Controller implements Initializable {

    @FXML
    private MFXButton btnEmpezar;
    @FXML
    private Label lblTiempo;
    @FXML
    private Label lblNombreEquipoIzq;
    @FXML
    private Label lblMarcadorEquipoIzq;
    @FXML
    private MFXButton btnFinalizar;
    @FXML
    private StackPane containerBalon;
    @FXML
    private ImageView imvBalon;
    @FXML
    private Label lblNombreEquipoDer;
    @FXML
    private Label lblMarcadorEquipoDer;
    @FXML
    private ImageView imvEquipo1;
    @FXML
    private Circle crlContainerMarcador1;
    @FXML
    private ImageView imvEquipo2;
    @FXML
    private StackPane crlContainerMarcador2;

    private Torneo torneoSeleccionado;
    private Equipo equipo1;
    private Equipo equipo2;
    private Partido partido;
    private Deporte deporte;
    private LlavesTorneo llavesTorneo;
    private EquipoPartido equipoPartido1;
    private EquipoPartido equipoPartido2;
    private Equipo equipoGanador;

    private Timeline cronometro;
    private int tiempoRestanteSegundos;
    private boolean partidoEnCurso = false;
    private boolean arrastre = false;

    private double mouseXOffset;
    private double mouseYOffset;


    /* Pendientes:
    Incluir las imagenes de cada equipo
    Mostrar la imagen del balon del equipo en imvBalon
    Realizar el mapeo
    Verificar que los datos se guarden correctamente en json
    Desarrollar el manejo de desempates
     */
 /* El partido cuenta con la funcionalidad correcta de:
    cronometro(recibe correctamente el tiempo por partido de cada torneo)
    arrastre del balon y anotacion de goles
    muestra correctamente el nombre de ambos equipos en los labels
    guarda el marcador, el equipo ganador y no permite guardar si existe un empate
    Se implementa la funcionalidad del AppContext para acceder a la informacion requerida del torneo y sus enfrentamientos*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void initialize() {
        Integer torneoId = (Integer) AppContext.getInstance().get("torneoIdPartido");

        Respuesta respuestaTorneo = RegistroTorneo.getInstance().buscarTorneo(torneoId);
        if (respuestaTorneo.getEstado() && respuestaTorneo.getResultado("torneoEncontrado") instanceof Torneo) {
            torneoSeleccionado = (Torneo) respuestaTorneo.getResultado("torneoEncontrado");
            tiempoRestanteSegundos = torneoSeleccionado.getTiempoPorPartido() * 60;
        }

        setEntidades();
        infoPartido();
        setupDragAndDrop();

        containerBalon.setStyle("-fx-opacity: 1");
        imvBalon.setImage(new Image(new File(deporte.getImagenURL()).toURI().toString()));
        imvBalon.setStyle("-fx-cursor: hand");

        imvEquipo1.setImage(new Image(new File(equipo1.getFotoURL()).toURI().toString()));
        imvEquipo2.setImage(new Image(new File(equipo2.getFotoURL()).toURI().toString()));
    }

    @FXML
    private void onActionBtnEmpezar(ActionEvent event) {
        if (!partidoEnCurso) {
            partidoEnCurso = true;
            btnEmpezar.setDisable(true);
            btnFinalizar.setDisable(false);
            arrastre = true;

            cronometro = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                if (tiempoRestanteSegundos > 0 && partidoEnCurso) {
                    tiempoRestanteSegundos--;
                    cronometro();
                } else {
                    finalizarPartido();
                }
            }));
            cronometro.setCycleCount(Timeline.INDEFINITE);
            cronometro.play();
        }
    }

    @FXML
    private void onActionBtnFinalizar(ActionEvent event) {
        finalizarPartido();
    }

    private void infoPartido() {
        lblNombreEquipoIzq.setText(equipo1.getNombre());
        lblNombreEquipoDer.setText(equipo2.getNombre());
        lblMarcadorEquipoIzq.setText("0");
        lblMarcadorEquipoDer.setText("0");
        btnEmpezar.setDisable(false);

        cronometro();
    }

    private void finalizarPartido() {
        if (cronometro != null) {
            cronometro.stop();
        }
        partidoEnCurso = false;
        btnEmpezar.setDisable(true);
        btnFinalizar.setDisable(true);
        arrastre = false;

        guardarResultadoPartido();

    }

    private void cronometro() {
        int minutos = tiempoRestanteSegundos / 60;
        int segundos = tiempoRestanteSegundos % 60;
        lblTiempo.setText(String.format("%02d:%02d", minutos, segundos));
    }

    private void setupDragAndDrop() {
        imvBalon.setOnDragDetected((MouseEvent event) -> {
            if (arrastre) {
                Dragboard db = imvBalon.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString("BALON");
                db.setContent(content);
                event.consume();
            }
        });

        imvBalon.setOnMousePressed((MouseEvent event) -> {
            mouseXOffset = event.getSceneX() - imvBalon.getX();
            mouseYOffset = event.getSceneY() - imvBalon.getY();
        });

        imvBalon.setOnMouseDragged((MouseEvent event) -> {
            imvBalon.setTranslateX(event.getSceneX() - mouseXOffset);
            imvBalon.setTranslateY(event.getSceneY() - mouseYOffset);
        });

        lblMarcadorEquipoIzq.setOnDragOver((javafx.scene.input.DragEvent event) -> {
            if (event.getDragboard().hasString() && event.getDragboard().getString().equals("BALON")) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        lblMarcadorEquipoIzq.setOnDragDropped((javafx.scene.input.DragEvent event) -> {
            if (event.getDragboard().hasString() && event.getDragboard().getString().equals("BALON")) {
                incrementarMarcador(lblMarcadorEquipoIzq);
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });

        lblMarcadorEquipoDer.setOnDragOver((javafx.scene.input.DragEvent event) -> {
            if (event.getDragboard().hasString() && event.getDragboard().getString().equals("BALON")) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        lblMarcadorEquipoDer.setOnDragDropped((javafx.scene.input.DragEvent event) -> {
            if (event.getDragboard().hasString() && event.getDragboard().getString().equals("BALON")) {
                incrementarMarcador(lblMarcadorEquipoDer);
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });
    }

    private void incrementarMarcador(Label marcador) {
        try {
            int marcadorActual = Integer.parseInt(marcador.getText());
            marcador.setText(String.valueOf(marcadorActual + 1));
        } catch (NumberFormatException e) {
            marcador.setText("1");
        }
    }

    private void guardarResultadoPartido() {
        registrarGanadorPartido();
        actualizarPuntosTotalesGanador();
        guardarPartido();
        mapearEquiposPartido();
        // Actualizar las entidades de mapeo EquipoTorneo

        // Revisar si con el partido actual ya finalizó el torneo y hacer los cambios correspondientes
    }

    private void setEntidades() {
        equipo1 = (Equipo) AppContext.getInstance().get("equipo1Partido");
        equipo2 = (Equipo) AppContext.getInstance().get("equipo2Partido");
        partido = new Partido(0, torneoSeleccionado);
        deporte = torneoSeleccionado.getDeporte();

        llavesTorneo = (LlavesTorneo) RegistroLlavesTorneos.getInstance().buscarLlavesTorneo(torneoSeleccionado.getId()).getResultado("llaves");
    }

    private void registrarGanadorPartido() {
        if (Integer.parseInt(lblMarcadorEquipoIzq.getText()) > Integer.parseInt(lblMarcadorEquipoDer.getText())) {
            equipo1.setPuntosTotales(equipo1.getPuntosTotales() + 3);

            equipoPartido1 = new EquipoPartido(equipo1, partido, Integer.parseInt(lblMarcadorEquipoIzq.getText()), 3, true);
            equipoPartido2 = new EquipoPartido(equipo2, partido, Integer.parseInt(lblMarcadorEquipoDer.getText()), 0, false);

            equipoGanador = equipo1;
            llavesTorneo.getLlaves().registrarGanador(equipo1);
        } else if (Integer.parseInt(lblMarcadorEquipoIzq.getText()) < Integer.parseInt(lblMarcadorEquipoDer.getText())) {
            equipo2.setPuntosTotales(equipo2.getPuntosTotales() + 3);

            equipoPartido2 = new EquipoPartido(equipo2, partido, Integer.parseInt(lblMarcadorEquipoDer.getText()), 3, true);
            equipoPartido1 = new EquipoPartido(equipo1, partido, Integer.parseInt(lblMarcadorEquipoIzq.getText()), 0, false);

            equipoGanador = equipo2;
            llavesTorneo.getLlaves().registrarGanador(equipo2);
        } else {
            // TODO: funcion de desempate
            new Mensaje().show(Alert.AlertType.INFORMATION, "Empate", "Se debe desempatar");
        }

        RegistroLlavesTorneos.getInstance().guardarLlavesTorneo(llavesTorneo);
    }

    private void actualizarPuntosTotalesGanador() {
        Respuesta respuestaActualizarPuntosTotales = RegistroEquipo.getInstance().guardarEquipo(equipoGanador, new Image(new File(equipoGanador.getFotoURL()).toURI().toString()));
        if (!respuestaActualizarPuntosTotales.getEstado()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Actualizar puntos", "Error al actualizar los puntos del ganador");
        }
    }

    private void mapearEquiposPartido() {
        Respuesta respuestaMapeo1 = RegistroEquipoPartido.getInstance().guardarEquipoPartido(equipoPartido1);
        Respuesta respuestaMapeo2 = RegistroEquipoPartido.getInstance().guardarEquipoPartido(equipoPartido2);
        if (!respuestaMapeo1.getEstado() || !respuestaMapeo2.getEstado()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Mapeo", "Error al guardar el partido");
        }
    }

    private void guardarPartido() {
        Respuesta respuestaGuardarPartido = RegistroPartido.getInstance().guardarPartido(partido);
        if (!respuestaGuardarPartido.getEstado()) {
            new Mensaje().show(Alert.AlertType.ERROR, "guardar partido", "Error al guardar el partido");
        }
    }
}
