package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.LlavesTorneo;
import cr.ac.una.tournamentcontrolsystem.model.Torneo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroLlavesTorneos;
import cr.ac.una.tournamentcontrolsystem.service.RegistroTorneo;
import cr.ac.una.tournamentcontrolsystem.util.AppContext;
import cr.ac.una.tournamentcontrolsystem.util.FlowController;
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
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LlavesTorneosController extends Controller implements Initializable {

    @FXML
    private ToggleGroup tggActivo;
    @FXML
    private Canvas canvaLlaves;
    @FXML
    private MFXComboBox<Torneo> cmbTorneos;
    private Torneo torneoActual;

    private static final double NODO_ALTURA = 40;
    private static final double ESPACIO_VERTICAL = 20;
    private static final double ESPACIO_HORIZONTAL = 120;
    private static final double DESPLAZAMIENTO_DERECHA = 80;
    private static final double LINEA_OFFSET_Y = 5;
    private static final double LINEA_GROSOR = 2;
    private static final double TEXTO_DESPLAZAMIENTO_FINAL = -165 + DESPLAZAMIENTO_DERECHA;

    private GraphicsContext gc;
    private double inicioX;
    private Pane paneContenedor;
    @FXML
    private ScrollPane scrollCanva;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        paneContenedor = new Pane();
        paneContenedor.getChildren().add(canvaLlaves); 
        scrollCanva.setContent(paneContenedor); 

        cmbTorneos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                torneoActual = newValue; 
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
        cargarTorneos();
    }

    private void cargarTorneos() {
        try {
            Respuesta respuesta = RegistroTorneo.getInstance().getTorneos();
            if (respuesta.getEstado()) {
                List<Torneo> torneos = (List<Torneo>) respuesta.getResultado("torneos");
                cmbTorneos.getItems().clear();
                if (torneos != null && !torneos.isEmpty()) {
                    cmbTorneos.getItems().addAll(torneos);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

private void dibujarLlave(LlavesTorneo llavesTorneo) {
        gc = canvaLlaves.getGraphicsContext2D();   // Obtiene el contexto gráfico 2D del canvas donde se dibujará la llave
        gc.clearRect(0, 0, canvaLlaves.getWidth(), canvaLlaves.getHeight()); //Limpia el canva

        Map<String, Map<String, Object>> llavesData = llavesTorneo.getLlaves(); //Obtiene la info de las llaves
        List<String> nombresEquipos = new ArrayList<>(); // Crea una lista para almacenar los nombres de los equipos participantes
        
        for (Map<String, Object> equipo : llavesData.values()) {
            nombresEquipos.add((String) equipo.get("nombre"));  // Extrae el nombre del equipo del mapa de datos del equipo y lo añade a la lista de nombres
        }

        // Define la coordenada X inicial para dibujar la primera ronda de equipos, aplicando un desplazamiento hacia la derecha.
        inicioX = 250 + DESPLAZAMIENTO_DERECHA;
       
        Map<Integer, List<Point>> rondas = new HashMap<>();  // Crea un mapa para almacenar las coordenadas de los nodos por cada ronda
        List<Point> puntosRonda = new ArrayList<>();  // Crea una lista para almacenar los puntos de los nodos de la ronda actual que se está dibujando
        // Define la coordenada de posicionamiento
        double yInicial = 50;
        double y = yInicial;
        double currentX = inicioX;
        
        // Inicializa el contador de la ronda actual en 0 (la primera ronda de equipos)
        int ronda = 0;
        int participantes = nombresEquipos.size();

        gc.setFill(Color.BLACK);
        for (String nombre : nombresEquipos) {
            gc.fillText(nombre, currentX, y);  // Dibuja el nombre del equipo en la posición actual el canva
            puntosRonda.add(new Point((int) currentX, (int) y));  // Crea un nuevo punto con las coordenadas del nombre del equipo y lo añade a la lista de puntos de la ronda actual
            y += NODO_ALTURA + ESPACIO_VERTICAL;
        }
        rondas.put(ronda, puntosRonda);
      
        double anchoMaximo = inicioX;
        double altoMaximo = y;

        // Crear botones para iniciar la primera ronda.
        // Obtiene la lista de coordenadas de los equipos en la ronda inicial (ronda 0).
        List<Point> coordenadasRondaCero = rondas.get(0);
        if (coordenadasRondaCero != null && coordenadasRondaCero.size() > 1 && torneoActual != null) {
            botonEnfrentamiento(coordenadasRondaCero, nombresEquipos);
        }
        
        /*<<<<<<<<<<<ESTO SERVIRA PARA SEGUIR DIBUJANDO  RONDAS CUANDO SE OBTENGAN TODOS LOS GANADORES>>>>>>>>>>*/
        ronda = 1;  // Incrementa el contador de la ronda para comenzar a dibujar las siguientes
        // Continúa el bucle mientras haya más de un participante para seguir dibujando hasta obtener 1 ganador
        while (participantes > 1) {
            List<Point> puntosAnteriores = rondas.get(ronda - 1);   // Obtiene la lista de puntos de la ronda anterior
            List<Point> nuevosPuntos = new ArrayList<>();  // Crea una nueva lista para almacenar los puntos de los nodos de la ronda actual
            currentX -= ESPACIO_HORIZONTAL;
            anchoMaximo = Math.max(anchoMaximo, currentX);
            
            for (int i = 0; i < puntosAnteriores.size(); i += 2) { // Itera sobre los puntos de la ronda anterior tomando los puntos de dos en dos para representar los enfrentamientos
                if (i + 1 < puntosAnteriores.size()) { // Verifica si hay un segundo punto disponible para formar un enfrentamiento
                    // Obtiene los dos puntos (equipos) que se enfrentan en la ronda anterior
                    Point p1 = puntosAnteriores.get(i);
                    Point p2 = puntosAnteriores.get(i + 1);
                    int midY = (p1.y + p2.y) / 2;
                    nuevosPuntos.add(new Point((int) currentX, midY));
                } 
            }

            // Almacena la lista de puntos (equipos) de la ronda actual en el mapa de rondas.
            rondas.put(ronda, nuevosPuntos);
            participantes = participantes / 2;  // Actualiza el número de participantes para la siguiente ronda 
            ronda++;
        } 
        dibujarLineas(rondas);
      
         // Dibuja la palabra "Enfrentamiento" en las coordenadas del punto final.
        Point finalPoint = rondas.get(rondas.size() - 1).get(0);
        gc.setFill(Color.BLACK);
        gc.fillText("Enfrentamiento", finalPoint.x + TEXTO_DESPLAZAMIENTO_FINAL, finalPoint.y + LINEA_OFFSET_Y);

    }

    private void dibujarLineas(Map<Integer, List<Point>> rondas) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        for (int i = 0; i < rondas.size() - 1; i++) {
            List<Point> puntosActuales = rondas.get(i);
            List<Point> puntosSiguientes = rondas.get(i + 1);

            for (int j = 0; j < puntosActuales.size(); j += 2) {
                if (j + 1 < puntosActuales.size() && j / 2 < puntosSiguientes.size()) {
                    Point p1 = puntosActuales.get(j);
                    Point p2 = puntosActuales.get(j + 1);
                    Point pSiguiente = puntosSiguientes.get(j / 2);

                    int midY = (p1.y + p2.y) / 2;

                    gc.strokeLine(p1.x, p1.y, p1.x - ESPACIO_HORIZONTAL / 2, p1.y); // Línea horizontal desde el equipo 1
                    gc.strokeLine(p2.x, p2.y, p2.x - ESPACIO_HORIZONTAL / 2, p2.y); // Línea horizontal desde el equipo 2
                    gc.strokeLine(p1.x - ESPACIO_HORIZONTAL / 2, p1.y, p1.x - ESPACIO_HORIZONTAL / 2, p2.y); // Línea vertical conectando las horizontales
                    gc.strokeLine(p1.x - ESPACIO_HORIZONTAL / 2, midY, pSiguiente.x, midY); // Línea horizontal hacia la siguiente ronda
                }
            }
        }
    }

    private void botonEnfrentamiento(List<Point> coordenadasRondaCero, List<String> nombresEquipos) {
        for (int i = 0; i < coordenadasRondaCero.size(); i += 2) {
            if (i + 1 < coordenadasRondaCero.size()) {
                Point p1 = coordenadasRondaCero.get(i);
                Point p2 = coordenadasRondaCero.get(i + 1);
                double centroY = (p1.y + p2.y) / 2.0;

                Button btnPartido = new Button("Start");
                double botonX = inicioX + ESPACIO_HORIZONTAL / 2.0 - 15;
                double botonY = centroY - 10;
                btnPartido.setLayoutX(botonX);
                btnPartido.setLayoutY(botonY);
                final int equipo1Index = i;
                final int equipo2Index = i + 1;

                /*Al darle click al boton de start, se compila la informacion del enfrentamiento del AppContext de 
                los dos equipos que se enfrentaran y su tiempo por torneo, y se transfiere a Partido
                */
                btnPartido.setOnAction(event -> {
                    if (equipo1Index < nombresEquipos.size() && equipo2Index < nombresEquipos.size() && torneoActual != null) {
                        String equipo1Nombre = nombresEquipos.get(equipo1Index);
                        String equipo2Nombre = nombresEquipos.get(equipo2Index);

                        AppContext.getInstance().set("equipo1Partido", equipo1Nombre);
                        AppContext.getInstance().set("equipo2Partido", equipo2Nombre);
                        AppContext.getInstance().set("torneoIdPartido", torneoActual.getId());// Guarda el ID del torneo actual
                        ((Button) event.getSource()).setDisable(true);
                        FlowController.getInstance().goViewInWindowModal("PartidoView", ((Stage) scrollCanva.getScene().getWindow()), Boolean.FALSE);
                    }
                });
                paneContenedor.getChildren().add(btnPartido);
            }
        }
    }
}