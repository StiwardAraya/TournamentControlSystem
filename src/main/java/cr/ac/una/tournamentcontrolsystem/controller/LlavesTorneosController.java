package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.LlavesTorneo;
import cr.ac.una.tournamentcontrolsystem.model.Torneo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroLlavesTorneos;
import cr.ac.una.tournamentcontrolsystem.service.RegistroTorneo;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;

public class LlavesTorneosController extends Controller implements Initializable {

    @FXML
    private ToggleGroup tggActivo;
    @FXML
    private Canvas canvaLlaves;
    @FXML
    private MFXComboBox<Torneo> cmbTorneos;

    private static final double NODO_ALTURA = 40;
    private static final double ESPACIO_VERTICAL = 20;
    private static final double ESPACIO_HORIZONTAL = 120;
    private static final double DESPLAZAMIENTO_DERECHA = 80;
    private static final double LINEA_OFFSET_Y = 5;
    private static final double LINEA_GROSOR = 2;
    private static final double TEXTO_DESPLAZAMIENTO_FINAL = -165 + DESPLAZAMIENTO_DERECHA;

    private GraphicsContext gc;
    private double inicioX;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbTorneos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Respuesta respuestaLlave = RegistroLlavesTorneos.getInstance().buscarLlavesTorneo(newValue.getId());
                if (respuestaLlave.getEstado()) {
                    LlavesTorneo llavesTorneo = (LlavesTorneo) respuestaLlave.getResultado("llaves");
                    dibujarLlave(llavesTorneo);
                }
            }
        });
    }

    @Override
    public void initialize() {
        RegistroLlavesTorneos.getInstance().cargarLlaves();
        cargarGraficar();
    }

    private void cargarGraficar() {
        try {
            Respuesta respuesta = RegistroTorneo.getInstance().getTorneos();
            if (respuesta.getEstado()) {
                List<Torneo> torneos = (List<Torneo>) respuesta.getResultado("torneos");
                cmbTorneos.getItems().clear();
                if (torneos != null && !torneos.isEmpty()) {
                    cmbTorneos.getItems().addAll(torneos);
                }

                // Aquí se grafica la llave del torneo seleccionado
                Torneo torneoSeleccionado = cmbTorneos.getSelectionModel().getSelectedItem();
                if (torneoSeleccionado != null) {
                    Respuesta respuestaLlave = RegistroLlavesTorneos.getInstance().buscarLlavesTorneo(torneoSeleccionado.getId());
                    if (respuestaLlave.getEstado()) {
                        LlavesTorneo llavesTorneo = (LlavesTorneo) respuestaLlave.getResultado("llaves");
                        dibujarLlave(llavesTorneo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dibujarLlave(LlavesTorneo llavesTorneo) {
        gc = canvaLlaves.getGraphicsContext2D();
        gc.clearRect(0, 0, canvaLlaves.getWidth(), canvaLlaves.getHeight());

        Map<String, Map<String, Object>> llavesData = llavesTorneo.getLlaves();
        List<String> nombresEquipos = new ArrayList<>();
        for (Map<String, Object> equipo : llavesData.values()) {
            nombresEquipos.add((String) equipo.get("nombre"));
        }

        inicioX = 250 + DESPLAZAMIENTO_DERECHA;
        Map<Integer, List<Point>> rondas = new HashMap<>();
        List<Point> puntosRonda = new ArrayList<>();
        double y = 50;
        double currentX = inicioX;
        int ronda = 0;
        int participantes = nombresEquipos.size();

        gc.setFill(Color.BLACK);
        for (String nombre : nombresEquipos) {
            gc.fillText(nombre, currentX, y);
            puntosRonda.add(new Point((int) currentX, (int) y));
            y += NODO_ALTURA + ESPACIO_VERTICAL;
        }
        rondas.put(ronda, puntosRonda);

        ronda = 1;
        while (participantes > 1) {
            List<Point> puntosAnteriores = rondas.get(ronda - 1);
            List<Point> nuevosPuntos = new ArrayList<>();
            currentX -= ESPACIO_HORIZONTAL;
            y = 50;

            for (int i = 0; i < puntosAnteriores.size(); i += 2) {
                if (i + 1 < puntosAnteriores.size()) {
                    Point p1 = puntosAnteriores.get(i);
                    Point p2 = puntosAnteriores.get(i + 1);
                    int midY = (p1.y + p2.y) / 2;
                    nuevosPuntos.add(new Point((int) currentX, midY));
                } else if (participantes % 2 != 0) {
                    nuevosPuntos.add(puntosAnteriores.get(i));
                }
            }
            rondas.put(ronda, nuevosPuntos);
            participantes = (participantes + 1) / 2;
            ronda++;
        }

        // Aquí dibuja las líneas que conectan la llave
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(LINEA_GROSOR);
        for (int i = 0; i < rondas.size() - 1; i++) {
            List<Point> puntosActuales = rondas.get(i);
            List<Point> puntosSiguientes = rondas.get(i + 1);
            double xIntermedio = puntosSiguientes.get(0).x + 20;

            int siguienteIndice = 0;
            for (int j = 0; j < puntosActuales.size(); j += 2) {
                if (j + 1 < puntosActuales.size() && siguienteIndice < puntosSiguientes.size()) {
                    Point p1 = puntosActuales.get(j);
                    Point p2 = puntosActuales.get(j + 1);
                    Point pFinal = puntosSiguientes.get(siguienteIndice++);
                    double medioY = (p1.y + p2.y) / 2.0;

                    gc.strokeLine(p1.x, p1.y + LINEA_OFFSET_Y, xIntermedio, p1.y + LINEA_OFFSET_Y);
                    gc.strokeLine(p2.x, p2.y + LINEA_OFFSET_Y, xIntermedio, p2.y + LINEA_OFFSET_Y);
                    gc.strokeLine(xIntermedio, p1.y + LINEA_OFFSET_Y, xIntermedio, p2.y + LINEA_OFFSET_Y);
                    gc.strokeLine(xIntermedio, medioY + LINEA_OFFSET_Y, pFinal.x, pFinal.y + LINEA_OFFSET_Y);
                } else if (siguienteIndice < puntosSiguientes.size()) {
                    Point p1 = puntosActuales.get(j);
                    Point pFinal = puntosSiguientes.get(siguienteIndice++);
                    gc.strokeLine(p1.x, p1.y + LINEA_OFFSET_Y, pFinal.x, pFinal.y + LINEA_OFFSET_Y);
                }
            }
        }

        Point finalPoint = rondas.get(rondas.size() - 1).get(0);
        gc.setFill(Color.BLACK);
        gc.fillText("Enfrentamiento", finalPoint.x + TEXTO_DESPLAZAMIENTO_FINAL, finalPoint.y + LINEA_OFFSET_Y);
    }
}
