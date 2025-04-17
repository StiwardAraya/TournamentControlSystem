package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Torneo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroTorneo;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

/*Hay que graficar el arbol completo
El arbol se mostrara completo hasta tener un nivel donde solo exista un nodo, este sera el ganador
Metodos a realizar:
calcularNumeroNodos: este se realizara en base a la cantidad de equipos inscritos en el torneo. Es util para definir el tamano del arbol
posicionarNodos: los primeros nodos que se mostraran son los equipos del torneo, estos estaran en el nivel 1 del arbol
avanzar: este metodo permitira que los equipos ganadores avancen de nivel dentro del arbol hasta determinar el ganador
botonStart: crea un boton en medio de los equipos que se enfrentaran. Este boton abre la ventana de PartidosView con el flow controller (Partidos ya esta funcionando)
Al llegar al nodo final, el equipo que este en esta posicion sera el ganador. El ganador debe de almacenarse y se debe de mostrar la animacion de ganador
 */
public class LlavesTorneosController extends Controller implements Initializable {

    @FXML
    private Canvas canvaLlaves;
    @FXML
    private MFXComboBox<Torneo> cmbTorneos;
    private Torneo torneoActual;

    private static final double NODO_ALTURA = 40;
    private static final double ESPACIO_VERTICAL = 60;
    private static final double ESPACIO_HORIZONTAL = 150;
    private static final double DESPLAZAMIENTO_NODO = 80;
    private static final double LINEA_GROSOR = 2;
    private static final double TEXTO_OFFSET_X = 10;
    private static final double TEXTO_OFFSET_Y = 25;
    private static final double NODO_ANCHO = 100;

    private GraphicsContext gc;
    private Pane paneContenedor;
    @FXML
    private ScrollPane scrollCanva;
    @FXML
    private MFXButton btnActualizar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        paneContenedor = new Pane();
        paneContenedor.getChildren().add(canvaLlaves);
        scrollCanva.setContent(paneContenedor);

        cmbTorneos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                torneoActual = newValue;
                //metodo para dibujar la llave
            }
        });
    }

    @Override
    public void initialize() {
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

    @FXML
    private void onActionBtnActualizar(ActionEvent event) {
        //Metodo para actualizar la llave
    }
}
