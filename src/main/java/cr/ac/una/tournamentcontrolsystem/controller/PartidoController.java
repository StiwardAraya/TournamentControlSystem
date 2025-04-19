package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import cr.ac.una.tournamentcontrolsystem.model.Partido;
import cr.ac.una.tournamentcontrolsystem.model.Torneo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroPartido;
import cr.ac.una.tournamentcontrolsystem.service.RegistroTorneo;
import cr.ac.una.tournamentcontrolsystem.util.AppContext;
import cr.ac.una.tournamentcontrolsystem.util.Mensaje;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
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

    private Torneo torneoSeleccionado;
    private Equipo equipo1;
    private Equipo equipo2;
    private Partido partido;
    private Timeline cronometro;
    private int tiempoRestanteSegundos;
    private boolean partidoEnCurso = false;
    private boolean arrastre = false;

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
            this.torneoSeleccionado = (Torneo) respuestaTorneo.getResultado("torneoEncontrado");
            tiempoRestanteSegundos = torneoSeleccionado.getTiempoPorPartido() * 60;
        }

        infoPartido();
        setupDragAndDrop();
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
        String equipoIzqNombre = (String) AppContext.getInstance().get("equipo1Partido");
        String equipoDerNombre = (String) AppContext.getInstance().get("equipo2Partido");

        lblNombreEquipoIzq.setText(equipoIzqNombre);
        lblNombreEquipoDer.setText(equipoDerNombre);
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

        lblMarcadorEquipoIzq.setText("0");
        lblMarcadorEquipoDer.setText("0");

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
        int marcadorEquipoIzq = Integer.parseInt(lblMarcadorEquipoIzq.getText());
        int marcadorEquipoDer = Integer.parseInt(lblMarcadorEquipoDer.getText());

        Equipo equipoIzq = new Equipo();
        equipoIzq.setNombre(lblNombreEquipoIzq.getText());
        Equipo equipoDer = new Equipo();
        equipoDer.setNombre(lblNombreEquipoDer.getText());

        Partido partido = new Partido();
        partido.setTorneo(torneoSeleccionado);

        Equipo ganador = null;

        if (marcadorEquipoIzq > marcadorEquipoDer) {
            ganador = equipoIzq;
            partido.setGanador(ganador);
            new Mensaje().show(Alert.AlertType.INFORMATION, "Partido finalizado", "Ganador: " + equipoIzq.getNombre());
        } else if (marcadorEquipoIzq < marcadorEquipoDer) {
            ganador = equipoDer;
            partido.setGanador(ganador);
            new Mensaje().show(Alert.AlertType.INFORMATION, "Partido finalizado", "Ganador: " + equipoDer.getNombre());
        } else {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Empate", "Debe de desempatar.");
            return;
        }

        Respuesta respuestaGuardarPartido = RegistroPartido.getInstance().guardarPartido(partido);
        if (!respuestaGuardarPartido.getEstado()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al guardar partido", respuestaGuardarPartido.getMensaje());
        } else {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Partido guardado", "El partido se ha guardado correctamente.");
        }

    }
}
