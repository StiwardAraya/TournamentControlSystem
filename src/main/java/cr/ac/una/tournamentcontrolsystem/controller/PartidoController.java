package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import cr.ac.una.tournamentcontrolsystem.model.EquipoPartido;
import cr.ac.una.tournamentcontrolsystem.model.EquipoTorneo;
import cr.ac.una.tournamentcontrolsystem.model.LlavesTorneo;
import cr.ac.una.tournamentcontrolsystem.model.Partido;
import cr.ac.una.tournamentcontrolsystem.model.Torneo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroEquipo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroEquipoPartido;
import cr.ac.una.tournamentcontrolsystem.service.RegistroEquipoTorneo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroLlavesTorneos;
import cr.ac.una.tournamentcontrolsystem.service.RegistroPartido;
import cr.ac.una.tournamentcontrolsystem.service.RegistroTorneo;
import cr.ac.una.tournamentcontrolsystem.util.AppContext;
import cr.ac.una.tournamentcontrolsystem.util.Mensaje;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
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
    private Circle crlContainerMarcador2;
    @FXML
    private AnchorPane root;
    @FXML
    private StackPane stackEquipo1;
    @FXML
    private StackPane stackEquipo2;

    private Torneo torneoSeleccionado;
    private Equipo equipo1;
    private Equipo equipo2;
    private Partido partido;
    private Deporte deporte;
    private LlavesTorneo llavesTorneo;
    private EquipoPartido equipoPartido1;
    private EquipoPartido equipoPartido2;
    private Equipo equipoGanador;
    private Equipo equipoPerdedor;

    private Timeline cronometro;
    private int tiempoRestanteSegundos;
    private boolean partidoEnCurso = false;
    private boolean arrastre = false;
    private Label labelCampeon;

    private double mouseXOffset;
    private double mouseYOffset;
    private Circle objetivo;

    private int rondaDesempate = 1;
    private int puntosDesempateEquipo1 = 0;
    private int puntosDesempateEquipo2 = 0;
    private boolean turnoDesempateEquipo1 = true;
    private Timeline turnoTimerDesempate;
    private Timeline aparecerDesaparecerTimerDesempate;
    private final Mensaje mensaje = new Mensaje();
    private boolean puntoRegistrado = false;
    private boolean objetivoVisible = false;
    private boolean turnoEquipo1Finalizado = false;
    private boolean turnoEquipo2Finalizado = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        objetivo = new Circle(20, Color.RED);
        objetivo.setVisible(false);
        root.getChildren().add(objetivo);
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

        containerBalon.getChildren().remove(labelCampeon);
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
                    try {
                        finalizarPartido();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PartidoController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }));
            cronometro.setCycleCount(Timeline.INDEFINITE);
            cronometro.play();
        }
    }

    @FXML
    private void onActionBtnFinalizar(ActionEvent event) throws InterruptedException {
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

    private void finalizarPartido() throws InterruptedException {
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

        crlContainerMarcador1.setOnDragOver((javafx.scene.input.DragEvent event) -> {
            if (event.getDragboard().hasString() && event.getDragboard().getString().equals("BALON")) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        crlContainerMarcador1.setOnDragDropped((javafx.scene.input.DragEvent event) -> {
            if (event.getDragboard().hasString() && event.getDragboard().getString().equals("BALON")) {
                incrementarMarcador(lblMarcadorEquipoIzq);
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });

        crlContainerMarcador2.setOnDragOver((javafx.scene.input.DragEvent event) -> {
            if (event.getDragboard().hasString() && event.getDragboard().getString().equals("BALON")) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        crlContainerMarcador2.setOnDragDropped((javafx.scene.input.DragEvent event) -> {
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

    private void guardarResultadoPartido() throws InterruptedException {
        registrarGanadorPartido();
        actualizarPuntosTotalesGanador();
        guardarPartido();
        mapearEquiposPartido();
        actualizarMapeoEquipoTorneo(equipoGanador, 3);
        actualizarMapeoEquipoTorneo(equipoPerdedor, 0);

        if (llavesTorneo.getLlaves().getRaiz().getEquipo() != null) {
            animacionGanadorTorneo();
            finalizarTorneo();
        } else {
            animacionGanadorPartido();
        }
    }

    private void setEntidades() {
        equipo1 = (Equipo) AppContext.getInstance().get("equipo1Partido");
        equipo2 = (Equipo) AppContext.getInstance().get("equipo2Partido");
        partido = new Partido(0, torneoSeleccionado);
        deporte = torneoSeleccionado.getDeporte();

        llavesTorneo = (LlavesTorneo) RegistroLlavesTorneos.getInstance().buscarLlavesTorneo(torneoSeleccionado.getId()).getResultado("llaves");
    }

    private Equipo registrarGanadorPartido() {
        if (Integer.parseInt(lblMarcadorEquipoIzq.getText()) > Integer.parseInt(lblMarcadorEquipoDer.getText())) {
            ganadorEquipoIzq();
        } else if (Integer.parseInt(lblMarcadorEquipoIzq.getText()) < Integer.parseInt(lblMarcadorEquipoDer.getText())) {
            ganadorEquipoDer();
        } else {
            mensaje.show(Alert.AlertType.CONFIRMATION, "Desempate", "Haz click en el circulo rojo para acumular puntos y ganar, comienzan: " + equipo1.getNombre());
            iniciarDesempate();
        }

        RegistroLlavesTorneos.getInstance().guardarLlavesTorneo(llavesTorneo);
        return null;
    }

    private void actualizarPuntosTotalesGanador() {
        Respuesta respuestaActualizarPuntosTotales = RegistroEquipo.getInstance().guardarEquipo(equipoGanador, new Image(new File(equipoGanador.getFotoURL()).toURI().toString()));
        if (!respuestaActualizarPuntosTotales.getEstado()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Actualizar puntos", "Error al actualizar los puntos del ganador");
            return;
        }
        llavesTorneo.getLlaves().editarEquipo(equipoGanador);
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

    private void actualizarMapeoEquipoTorneo(Equipo equipo, int puntosGanados) {
        EquipoTorneo et = (EquipoTorneo) RegistroEquipoTorneo.getInstance().buscarEquipoTorneo(torneoSeleccionado.getId(), equipo.getId()).getResultado("equipoTorneoEncontrado");
        et.setPuntosEquipo(et.getPuntosEquipo() + puntosGanados);
        et.setPartidosJugados(et.getPartidosJugados() + 1);
        et.setEquipo(equipo);
        Respuesta respuestaGuardarEquipoTorneo = RegistroEquipoTorneo.getInstance().guardarEquipoTorneo(et);
        if (!respuestaGuardarEquipoTorneo.getEstado()) {
            new Mensaje().show(Alert.AlertType.ERROR, "actualizar datos de torneo", respuestaGuardarEquipoTorneo.getMensaje());
        }
    }

    private void ganadorEquipoIzq() {
        equipo1.setPuntosTotales(equipo1.getPuntosTotales() + 3);

        equipoPartido1 = new EquipoPartido(equipo1, partido, Integer.parseInt(lblMarcadorEquipoIzq.getText()), 3, true);
        equipoPartido2 = new EquipoPartido(equipo2, partido, Integer.parseInt(lblMarcadorEquipoDer.getText()), 0, false);

        equipoGanador = equipo1;
        equipoPerdedor = equipo2;

        llavesTorneo.getLlaves().registrarGanador(equipo1);
    }

    private void ganadorEquipoDer() {
        equipo2.setPuntosTotales(equipo2.getPuntosTotales() + 3);

        equipoPartido2 = new EquipoPartido(equipo2, partido, Integer.parseInt(lblMarcadorEquipoDer.getText()), 3, true);
        equipoPartido1 = new EquipoPartido(equipo1, partido, Integer.parseInt(lblMarcadorEquipoIzq.getText()), 0, false);

        equipoGanador = equipo2;
        equipoPerdedor = equipo1;

        llavesTorneo.getLlaves().registrarGanador(equipo2);
    }

    private void finalizarTorneo() {
        finalizarEquipos();
        finalizarTorneos();
    }

    private void finalizarEquipos() {
        List<Equipo> equipos = llavesTorneo.getLlaves().getEquipos();
        for (Equipo e : equipos) {
            e.setEnTorneoActivo(false);
            Respuesta respuestaActualizarEquipos = RegistroEquipo.getInstance().guardarEquipo(e, new Image(new File(e.getFotoURL()).toURI().toString()));
            if (!respuestaActualizarEquipos.getEstado()) {
                new Mensaje().show(Alert.AlertType.ERROR, "Actualizar equipo", "Ocurrió un error al actualizar los datos del equipo: " + e.getNombre());
            }
        }
    }

    private void finalizarTorneos() {
        torneoSeleccionado.setFinalizado(true);
        if (!RegistroTorneo.getInstance().guardarTorneo(torneoSeleccionado, deporte).getEstado()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Torneo", "Error al actualizar el torneo");
        }
    }

    private void iniciarDesempate() {
        rondaDesempate = 1;
        puntosDesempateEquipo1 = 0;
        puntosDesempateEquipo2 = 0;
        turnoDesempateEquipo1 = true;
        iniciarRondaDesempate();
    }

    private void iniciarRondaDesempate() {
        actualizarColorTurno();
        puntoRegistrado = false;

        turnoTimerDesempate = new Timeline(new KeyFrame(Duration.seconds(30), evt -> {
            Platform.runLater(() -> {
                if (turnoDesempateEquipo1) {
                    turnoDesempateEquipo1 = false;
                    turnoEquipo1Finalizado = true;
                    finalizarColorTurno();
                } else {
                    finalizarColorTurno();
                    turnoEquipo2Finalizado = true;
                }

                if (turnoEquipo1Finalizado && turnoEquipo2Finalizado) {
                    finalizarDesempate();
                } else {
                    iniciarRondaDesempate();
                }
            });
        }));

        turnoTimerDesempate.setCycleCount(1);
        turnoTimerDesempate.play();

        Timeline secuenciaCirculo = new Timeline();
        for (int i = 0; i < 8; i++) {
            double inicio = i * 4.0;
            secuenciaCirculo.getKeyFrames().addAll(
                    new KeyFrame(Duration.seconds(inicio), evt -> mostrarObjetivoDesempate()),
                    new KeyFrame(Duration.seconds(inicio + 0.8), evt -> objetivo.setVisible(false))
            );
        }
        secuenciaCirculo.setCycleCount(Timeline.INDEFINITE);
        secuenciaCirculo.play();
        aparecerDesaparecerTimerDesempate = secuenciaCirculo;

        Timeline temporizadorAnimacion = new Timeline(new KeyFrame(Duration.seconds(30), evt -> {
            if (aparecerDesaparecerTimerDesempate != null) {
                aparecerDesaparecerTimerDesempate.stop();
            }
        }));
        temporizadorAnimacion.setCycleCount(1);
        temporizadorAnimacion.play();

        objetivo.setOnMouseClicked(clickEvent -> eventoCirculo());
    }

    private void eventoCirculo() {
        if (turnoTimerDesempate != null
                && turnoTimerDesempate.getStatus() == Timeline.Status.RUNNING
                && objetivo.isVisible()
                && !puntoRegistrado) {

            puntoRegistrado = true;

            if (turnoDesempateEquipo1) {
                puntosDesempateEquipo1++;
                actualizarMarcadorDesempate(lblMarcadorEquipoIzq);
            } else {
                puntosDesempateEquipo2++;
                actualizarMarcadorDesempate(lblMarcadorEquipoDer);
            }

            objetivo.setVisible(false);
        }
    }

    private void actualizarMarcadorDesempate(Label marcadorLabel) {
        try {
            int puntosActuales = Integer.parseInt(marcadorLabel.getText());
            marcadorLabel.setText(String.valueOf(puntosActuales + 1));
        } catch (NumberFormatException e) {
            marcadorLabel.setText("1");
        }
    }

    private void mostrarObjetivoDesempate() {
        double x = Math.random() * (root.getWidth() - 50);
        double y = Math.random() * (root.getHeight() - 50);
        objetivo.setTranslateX(x);
        objetivo.setTranslateY(y);
        objetivo.setFill(Color.RED);
        objetivo.setVisible(true);
        puntoRegistrado = false;
    }

    private void finalizarDesempate() {
        lblNombreEquipoIzq.setStyle("-fx-text-fill: black;");
        lblMarcadorEquipoIzq.setStyle("-fx-text-fill: black;");
        lblNombreEquipoDer.setStyle("-fx-text-fill: black;");
        lblMarcadorEquipoDer.setStyle("-fx-text-fill: black;");

        if (puntosDesempateEquipo1 > puntosDesempateEquipo2) {
            ganadorEquipoIzq();
            mensaje.show(Alert.AlertType.INFORMATION, "Ganador", "¡El equipo " + equipoGanador + " ha ganado el desempate con " + puntosDesempateEquipo1 + " puntos!");
        } else if (puntosDesempateEquipo2 > puntosDesempateEquipo1) {
            ganadorEquipoDer();
            mensaje.show(Alert.AlertType.INFORMATION, "Ganador", "¡El equipo " + equipoGanador + " ha ganado el desempate con " + puntosDesempateEquipo2 + " puntos!");
        } else {
            Random random = new Random();
            boolean ganadorAleatorio = random.nextBoolean();
            equipoGanador = ganadorAleatorio ? equipo1 : equipo2;
            if (equipoGanador.equals(equipo1)) {
                ganadorEquipoIzq();
            } else {
                ganadorEquipoDer();
            }
            mensaje.show(Alert.AlertType.INFORMATION, "Ganador", "¡Empate en el desempate! El ganador se ha elegido al azar: " + equipoGanador);
        }
    }

    private void actualizarColorTurno() {
        if (turnoDesempateEquipo1) {
            lblNombreEquipoIzq.setStyle("-fx-text-fill: lightgreen;");
            lblMarcadorEquipoIzq.setStyle("-fx-text-fill: lightgreen;");
        } else {
            lblNombreEquipoDer.setStyle("-fx-text-fill: lightgreen;");
            lblMarcadorEquipoDer.setStyle("-fx-text-fill: lightgreen;");
        }
    }

    private void finalizarColorTurno() {
        if (!turnoDesempateEquipo1) {
            lblNombreEquipoIzq.setStyle("-fx-text-fill: lightcoral;");
            lblMarcadorEquipoIzq.setStyle("-fx-text-fill: lightcoral;");
        } else {
            lblNombreEquipoDer.setStyle("-fx-text-fill: lightcoral;");
            lblMarcadorEquipoDer.setStyle("-fx-text-fill: lightcoral;");
        }
    }

    private void animacionGanadorTorneo() {
        String textoGanador = equipoGanador.getNombre() + " CAMPEÓN!";
        labelCampeon = new Label(textoGanador);

        labelCampeon.setStyle("-fx-font-size: 20; -fx-text-fill: #FFA725; -fx-font-weight: bold; -fx-background-color: #FFFAF0; -fx-background-radius: 10px; -fx-padding: 10; -fx-font-family: \"Big Shoulders\"; ");

        containerBalon.getChildren().add(labelCampeon);

        String soundPath = getClass().getResource("../resources/sound/ganadorTorneo.mp3").toExternalForm();
        Media sound = new Media(soundPath);
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

        Timeline animacion = new Timeline();
        animacion.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(labelCampeon.scaleXProperty(), 0), new KeyValue(labelCampeon.scaleYProperty(), 0)),
                new KeyFrame(Duration.seconds(3), new KeyValue(labelCampeon.scaleXProperty(), 1), new KeyValue(labelCampeon.scaleYProperty(), 1))
        );
        animacion.play();
    }

    private void animacionGanadorPartido() throws InterruptedException {
        String textoGanador = equipoGanador.getNombre() + " Pasa la ronda!";
        labelCampeon = new Label(textoGanador);

        labelCampeon.setStyle("-fx-font-size: 20; -fx-text-fill: #FFA725; -fx-font-weight: bold; -fx-background-color: #FFFAF0; -fx-background-radius: 10px; -fx-padding: 20; -fx-font-family: \"Big Shoulders\"; ");

        containerBalon.getChildren().add(labelCampeon);

        String soundPath = getClass().getResource("../resources/sound/ganadorPartido.mp3").toExternalForm();
        Media sound = new Media(soundPath);
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

        Timeline animacion = new Timeline();
        animacion.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(labelCampeon.scaleXProperty(), 0), new KeyValue(labelCampeon.scaleYProperty(), 0)),
                new KeyFrame(Duration.seconds(3), new KeyValue(labelCampeon.scaleXProperty(), 1), new KeyValue(labelCampeon.scaleYProperty(), 1))
        );
        animacion.play();
    }
}
