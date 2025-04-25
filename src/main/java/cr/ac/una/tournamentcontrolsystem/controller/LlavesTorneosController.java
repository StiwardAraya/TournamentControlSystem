package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import cr.ac.una.tournamentcontrolsystem.model.EquipoTorneo;
import cr.ac.una.tournamentcontrolsystem.model.Llaves;
import cr.ac.una.tournamentcontrolsystem.model.LlavesTorneo;
import cr.ac.una.tournamentcontrolsystem.model.NodoTorneo;
import cr.ac.una.tournamentcontrolsystem.model.Torneo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroEquipoTorneo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroLlavesTorneos;
import cr.ac.una.tournamentcontrolsystem.service.RegistroTorneo;
import cr.ac.una.tournamentcontrolsystem.util.AppContext;
import cr.ac.una.tournamentcontrolsystem.util.FlowController;
import cr.ac.una.tournamentcontrolsystem.util.Mensaje;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Controlador de la ventana de llaves de torneo
 *
 * @author Stiward Araya C.
 * @author Angie Marks S.
 * @author Kevin Calderón Z.
 */
public class LlavesTorneosController extends Controller implements Initializable {

    @FXML
    private Canvas canvaLlaves;
    @FXML
    private MFXComboBox<Torneo> cmbTorneos;
    @FXML
    private ScrollPane scrollCanva;
    @FXML
    private MFXButton btnImprimir;
    @FXML
    private AnchorPane canvasContainer;
    @FXML
    private AnchorPane root;

    private Torneo torneoActual;

    /**
     * Inicializa el controlador de la vista y configura el comportamiento del
     * combo box de torneos.
     *
     * Este método se ejecuta automáticamente al cargar la interfaz FXML y
     * establece un listener que responde al cambio de selección en el combo box
     * de torneos. Cuando se selecciona un nuevo torneo, se limpia y actualiza
     * el contenedor canvasContainer con el nodo canvaLlaves.
     *
     * Luego, se busca la estructura de llaves del torneo seleccionado a través
     * de RegistroLlavesTorneos. Si la búsqueda tiene éxito, se extraen y
     * dibujan las llaves en la interfaz mediante el método dibujarTorneo. Si la
     * raíz de las llaves ya tiene un equipo asignado (es decir, hay un
     * campeón), se habilita el botón de impresión btnImprimir.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbTorneos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            canvasContainer.getChildren().clear();
            canvasContainer.getChildren().add(canvaLlaves);
            if (newValue != null) {
                torneoActual = newValue;
                Respuesta respuestaBuscarLlave = RegistroLlavesTorneos.getInstance().buscarLlavesTorneo(torneoActual.getId());
                if (!respuestaBuscarLlave.getEstado()) {
                    new Mensaje().show(Alert.AlertType.ERROR, "Torneo", "No se encontró el torneo seleccionado");
                    return;
                } else {
                    LlavesTorneo llavesTorn = (LlavesTorneo) respuestaBuscarLlave.getResultado("llaves");
                    Llaves llavesTorneoActual = llavesTorn.getLlaves();
                    dibujarTorneo(llavesTorneoActual);
                    if (llavesTorneoActual.getRaiz().getEquipo() != null) {
                        btnImprimir.setDisable(false);
                    }
                }

            }
        });
    }

    @Override
    public void initialize() {
        cargarTorneos();
        centrarScroll();
        btnImprimir.setDisable(true);
    }

    /**
     * Carga la lista de torneos disponibles desde la fuente de datos y la
     * muestra en el combo box de torneos.
     *
     * Este método obtiene los torneos. Si la respuesta es exitosa y contiene
     * una lista válida de torneos, se limpia el combo box y se agregan los
     * torneos obtenidos.
     */
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

    /**
     * Dibuja en el lienzo canvaLlaves la estructura de llaves de un torneo a
     * partir de la raíz proporcionada.
     *
     * Este método utiliza el contexto gráfico GraphicsContext del lienzo para
     * limpiar cualquier contenido previo y luego configurar el estilo de
     * dibujo. Calcula la posición inicial y dimensiones de los nodos con base
     * en la cantidad de equipos y la profundidad del árbol de llaves.
     * Finalmente, llama al método dibujarNodo para iniciar el dibujo recursivo
     * de la estructura desde la raíz del torneo.
     *
     * @param llaves Objeto Llaves que contiene la estructura completa del árbol
     * de enfrentamientos del torneo.
     */
    private void dibujarTorneo(Llaves llaves) {
        GraphicsContext gc = canvaLlaves.getGraphicsContext2D();
        gc.clearRect(0, 0, canvaLlaves.getWidth(), canvaLlaves.getHeight());

        gc.setFill(Color.rgb(193, 216, 195));
        gc.setStroke(Color.rgb(26, 17, 16));
        gc.setLineWidth(1);

        double espacioEntreEquipos = llaves.getEquipos().size() * 150;
        double alturaNodo = 75;
        double anchoNodo = 100;
        double xInicio = (canvaLlaves.getWidth() - (alturaNodo * Math.pow(2, getAlturaNodo(llaves.getRaiz())))) / 2;
        double yInicio = 75;

        dibujarNodo(gc, llaves.getRaiz(), xInicio, yInicio, anchoNodo, alturaNodo, espacioEntreEquipos, llaves);
    }

    /**
     * Dibuja un nodo del árbol de llaves del torneo en el GraphicsContext del
     * lienzo.
     *
     * Este método representa visualmente un nodo del torneo con su imagen y
     * nombre del equipo (si lo tiene), líneas de conexión hacia sus hijos
     * izquierdo y derecho (si existen), y un botón interactivo para iniciar el
     * partido entre los equipos hijos si ambos están definidos. El botón abre
     * la vista de gestión del partido y actualiza la estructura de llaves si es
     * necesario.
     *
     * El método se llama de forma recursiva para cada hijo del nodo, generando
     * un árbol visual completo.
     *
     * @param gc Contexto gráfico del lienzo donde se dibujan los elementos.
     * @param nodo Nodo actual del torneo que se está dibujando.
     * @param x Posición horizontal inicial del nodo.
     * @param y Posición vertical inicial del nodo.
     * @param anchoNodo Ancho visual del nodo que representa al equipo.
     * @param alturaNodo Altura visual del nodo.
     * @param espacioEntreEquipos Espacio horizontal entre nodos hermanos en la
     * jerarquía.
     * @param llaves Estructura completa de llaves del torneo, usada para
     * actualizar después de un partido.
     */
    private void dibujarNodo(GraphicsContext gc, NodoTorneo nodo, double x, double y, double anchoNodo, double alturaNodo, double espacioEntreEquipos, Llaves llaves) {
        if (nodo == null) {
            return;
        }

        gc.fillRoundRect(x, y, anchoNodo, alturaNodo, 5, 5);

        if (nodo.getEquipo() != null) {
            double imagenX = x + 5;
            double imagenY = y + 5;
            double imagenAncho = anchoNodo - 10;
            double imagenAlto = alturaNodo - 10;
            gc.drawImage(new Image(new File(nodo.getEquipo().getFotoURL()).toURI().toString()), imagenX, imagenY, imagenAncho, imagenAlto);

            String nombreEquipo = nodo.getEquipo().getNombre();
            double textWidth = gc.getFont().getSize() * nombreEquipo.length() / 2;
            gc.fillRect(x, y + (alturaNodo - (alturaNodo * 0.25)), anchoNodo, alturaNodo * 0.25);
            gc.strokeText(nombreEquipo, x + (anchoNodo / 2) - (textWidth / 2), y + alturaNodo - 5);
            gc.setFill(Color.rgb(193, 216, 195));
        }

        double hijoY = y + alturaNodo + 50;
        double hijoX = x - (nodo.getHijos().size() - 1) * (espacioEntreEquipos / 2);

        if (nodo.getIzquierdo() != null) {
            gc.strokeLine(x + anchoNodo / 2, y + alturaNodo, hijoX + anchoNodo / 2, y + alturaNodo);
            gc.strokeLine(hijoX + anchoNodo / 2, y + alturaNodo, hijoX + anchoNodo / 2, hijoY);
            dibujarNodo(gc, nodo.getIzquierdo(), hijoX, hijoY, anchoNodo, alturaNodo, espacioEntreEquipos / 2, llaves);
        }

        if (nodo.getDerecho() != null) {
            gc.strokeLine(x + anchoNodo / 2, y + alturaNodo, hijoX + espacioEntreEquipos + anchoNodo / 2, y + alturaNodo);
            gc.strokeLine(hijoX + espacioEntreEquipos + anchoNodo / 2, y + alturaNodo, hijoX + espacioEntreEquipos + anchoNodo / 2, hijoY);
            dibujarNodo(gc, nodo.getDerecho(), hijoX + espacioEntreEquipos, hijoY, anchoNodo, alturaNodo, espacioEntreEquipos / 2, llaves);
        }

        if (nodo.getIzquierdo() != null && nodo.getIzquierdo().getEquipo() != null
                && nodo.getDerecho() != null && nodo.getDerecho().getEquipo() != null) {

            VBox panelBoton = new VBox();

            panelBoton.setLayoutX(x);
            panelBoton.setLayoutY(y);
            panelBoton.setPrefSize(anchoNodo, alturaNodo);
            panelBoton.setAlignment(Pos.CENTER);

            MFXButton button = new MFXButton("Partido");
            button.setOnAction(event -> {
                AppContext.getInstance().set("equipo1Partido", nodo.getIzquierdo().getEquipo());
                AppContext.getInstance().set("equipo2Partido", nodo.getDerecho().getEquipo());
                AppContext.getInstance().set("torneoIdPartido", cmbTorneos.getSelectedItem().getId());
                AppContext.getInstance().set("llaves", llaves);
                FlowController.getInstance().goViewInWindowModal("PartidoView", ((Stage) root.getScene().getWindow()), Boolean.FALSE);
                actualizarLlaves();

                Llaves llavesTorneoActual = ((LlavesTorneo) RegistroLlavesTorneos.getInstance().buscarLlavesTorneo(torneoActual.getId()).getResultado("llaves")).getLlaves();
                if (llavesTorneoActual.getRaiz().getEquipo() != null) {
                    btnImprimir.setDisable(false);
                }
            });

            panelBoton.getChildren().add(button);

            canvasContainer.getChildren().add(panelBoton);
        }
    }

    /**
     * Obtiene la altura de un nodo
     *
     * @param nodo
     * @return cantidad en pixeles del alto de un nodo
     */
    private int getAlturaNodo(NodoTorneo nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + Math.max(getAlturaNodo(nodo.getIzquierdo()), getAlturaNodo(nodo.getDerecho()));
    }

    /**
     * Centra la posición horizontal del scrollPane
     */
    private void centrarScroll() {
        double contenidoAncho = scrollCanva.getContent().getBoundsInLocal().getWidth();
        double scrollPaneAncho = scrollCanva.getViewportBounds().getWidth();
        double scrollPos = (contenidoAncho - scrollPaneAncho) / 2;
        scrollCanva.setHvalue(scrollPos / (contenidoAncho - scrollPaneAncho));
    }

    /**
     * Permite el acceso a la vista del certificado del equipo ganador del
     * torneo actual.
     *
     * Este método recupera la estructura de llaves del torneo actualmente
     * seleccionado y verifica si existe un equipo ganador (en la raíz del
     * árbol). Luego, busca los detalles del equipo ganador dentro de la lista
     * de EquipoTorneo, y si encuentra coincidencia con el torneo actual, guarda
     * los datos relevantes en el AppContext para ser usados en la vista del
     * certificado.
     *
     * Si toda la información es válida y completa, se abre una nueva ventana
     * modal con la vista del certificado del equipo ganador.
     */
    private void accesoCertificado() {

        if (torneoActual != null) {
            Respuesta respuestaBuscarLlave = RegistroLlavesTorneos.getInstance().buscarLlavesTorneo(torneoActual.getId());

            if (respuestaBuscarLlave.getEstado()) {
                LlavesTorneo llavesTorneo = (LlavesTorneo) respuestaBuscarLlave.getResultado("llaves");
                Llaves llaves = llavesTorneo.getLlaves();

                if (llaves != null) {
                    NodoTorneo raiz = llaves.getRaiz();

                    if (raiz != null && raiz.getEquipo() != null) {
                        Equipo equipoGanador = raiz.getEquipo();
                        Respuesta respuestaEquiposTorneos = RegistroEquipoTorneo.getInstance().getEquiposTorneos();

                        if (respuestaEquiposTorneos.getEstado()) {
                            List<EquipoTorneo> equiposTorneos = (List<EquipoTorneo>) respuestaEquiposTorneos.getResultado("EquiposTorneos");
                            EquipoTorneo equipoTorneoGanador = null;

                            for (EquipoTorneo et : equiposTorneos) {
                                if (et.getEquipo().getId() == equipoGanador.getId() && et.getTorneo().getId() == torneoActual.getId()) {
                                    equipoTorneoGanador = et;
                                    break;
                                }
                            }

                            if (equipoTorneoGanador != null) {
                                AppContext.getInstance().set("equipoGanador", equipoGanador);
                                AppContext.getInstance().set("equipoTorneoGanador", equipoTorneoGanador);
                                AppContext.getInstance().set("torneo", equipoTorneoGanador.getTorneo());
                                FlowController.getInstance().goViewInWindowModal("CertificadoGanadorView", ((Stage) root.getScene().getWindow()), Boolean.FALSE);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Actualiza los cambios de las llaves de torneo
     */
    private void actualizarLlaves() {
        canvasContainer.getChildren().clear();
        canvasContainer.getChildren().add(canvaLlaves);
        torneoActual = cmbTorneos.getSelectedItem();
        Respuesta respuestaBuscarLlave = RegistroLlavesTorneos.getInstance().buscarLlavesTorneo(torneoActual.getId());
        if (!respuestaBuscarLlave.getEstado()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Torneo", "No se encontró el torneo seleccionado");
        } else {
            LlavesTorneo llavesTorn = (LlavesTorneo) respuestaBuscarLlave.getResultado("llaves");
            Llaves llavesTorneoActual = llavesTorn.getLlaves();
            dibujarTorneo(llavesTorneoActual);
        }
    }

    // EVENTOS
    @FXML
    private void onActionImprimirCertificado(ActionEvent event) {
        accesoCertificado();
    }
}
