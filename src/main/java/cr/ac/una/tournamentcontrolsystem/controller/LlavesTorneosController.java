package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Llaves;
import cr.ac.una.tournamentcontrolsystem.model.LlavesTorneo;
import cr.ac.una.tournamentcontrolsystem.model.NodoTorneo;
import cr.ac.una.tournamentcontrolsystem.model.Torneo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroLlavesTorneos;
import cr.ac.una.tournamentcontrolsystem.service.RegistroTorneo;
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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class LlavesTorneosController extends Controller implements Initializable {

    @FXML
    private Canvas canvaLlaves;
    @FXML
    private MFXComboBox<Torneo> cmbTorneos;
    @FXML
    private ScrollPane scrollCanva;
    @FXML
    private MFXButton btnActualizar;

    private Torneo torneoActual;
    @FXML
    private MFXButton btnImprimir;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbTorneos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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

    private void dibujarTorneo(Llaves llaves) {

        // Reinicio del canva
        GraphicsContext gc = canvaLlaves.getGraphicsContext2D();
        gc.clearRect(0, 0, canvaLlaves.getWidth(), canvaLlaves.getHeight());

        // Estilos
        gc.setFill(Color.rgb(193, 216, 195));
        gc.setStroke(Color.rgb(26, 17, 16));
        gc.setLineWidth(1);

        // Dimensiones
        double espacioEntreEquipos = 300;
        double alturaNodo = 75;
        double anchoNodo = 100;
        double alturaPorNivel = 200;
        double xInicio = (canvaLlaves.getWidth() - (alturaNodo * Math.pow(2, getAlturaNodo(llaves.getRaiz())))) / 2;
        double yInicio = 75;

        dibujarNodo(gc, llaves.getRaiz(), xInicio, yInicio, anchoNodo, alturaNodo, espacioEntreEquipos);
    }

    private void dibujarNodo(GraphicsContext gc, NodoTorneo nodo, double x, double y, double anchoNodo, double alturaNodo, double espacioEntreEquipos) {
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
            dibujarNodo(gc, nodo.getIzquierdo(), hijoX, hijoY, anchoNodo, alturaNodo, espacioEntreEquipos / 1.8);
        }

        if (nodo.getDerecho() != null) {
            gc.strokeLine(x + anchoNodo / 2, y + alturaNodo, hijoX + espacioEntreEquipos + anchoNodo / 2, y + alturaNodo);
            gc.strokeLine(hijoX + espacioEntreEquipos + anchoNodo / 2, y + alturaNodo, hijoX + espacioEntreEquipos + anchoNodo / 2, hijoY);
            dibujarNodo(gc, nodo.getDerecho(), hijoX + espacioEntreEquipos, hijoY, anchoNodo, alturaNodo, espacioEntreEquipos / 1.8);
        }
    }

    private int getAlturaNodo(NodoTorneo nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + Math.max(getAlturaNodo(nodo.getIzquierdo()), getAlturaNodo(nodo.getDerecho()));
    }

    private void centrarScroll() {
        double contenidoAncho = scrollCanva.getContent().getBoundsInLocal().getWidth();
        double scrollPaneAncho = scrollCanva.getViewportBounds().getWidth();
        double scrollPos = (contenidoAncho - scrollPaneAncho) / 2;
        scrollCanva.setHvalue(scrollPos / (contenidoAncho - scrollPaneAncho));
    }

    @FXML
    private void onActionBtnActualizar(ActionEvent event) {
        //Metodo para actualizar la llave
    }

    @FXML
    private void onActionImprimirCertificado(ActionEvent event) {
    }
}
