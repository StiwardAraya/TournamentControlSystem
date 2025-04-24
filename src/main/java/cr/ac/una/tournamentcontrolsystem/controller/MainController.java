package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.util.FlowController;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controlador de menú principal
 *
 * @author Stiward Araya C
 * @author Angie Marks S.
 * @author Kevin Calderón Z.
 */
public class MainController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private ImageView imvMain;

    private List<Image> imagenes;
    private int indiceActualImagen = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setWindow();
        cargarImagenes();
        iniciarCambioDeImagen();
    }

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionBtnMantenimiento(ActionEvent event) {
        FlowController.getInstance().goMain("MantenimientoView");
        ((Stage) root.getScene().getWindow()).close();
    }

    @FXML
    private void onActionBtnTorneos(ActionEvent event) {
        FlowController.getInstance().goMain("TorneosView");
        ((Stage) root.getScene().getWindow()).close();
    }

    @FXML
    private void onActionBtnEstadisticas(ActionEvent event) {
        FlowController.getInstance().goMain("EstadisticasView");
        ((Stage) root.getScene().getWindow()).close();
    }

    @FXML
    private void onActionBtnAcercaDe(ActionEvent event) {
        //TODO: Abrir web acerca de
    }

    /**
     * Carga las imagenes de fondo en una lista
     */
    private void cargarImagenes() {
        imagenes = new ArrayList<>();
        imagenes.add(new Image(getClass().getResourceAsStream("/cr/ac/una/tournamentcontrolsystem/resources/img/futbol_bg_image.jpg")));
        imagenes.add(new Image(getClass().getResourceAsStream("/cr/ac/una/tournamentcontrolsystem/resources/img/golf_bg_image.jpg")));
        imagenes.add(new Image(getClass().getResourceAsStream("/cr/ac/una/tournamentcontrolsystem/resources/img/futbolam_bg_image.jpg")));
        imagenes.add(new Image(getClass().getResourceAsStream("/cr/ac/una/tournamentcontrolsystem/resources/img/basket_bg_image.jpg")));
        imagenes.add(new Image(getClass().getResourceAsStream("/cr/ac/una/tournamentcontrolsystem/resources/img/tennis_bg_image.jpg")));
        imagenes.add(new Image(getClass().getResourceAsStream("/cr/ac/una/tournamentcontrolsystem/resources/img/volleyball_bg_image.jpg")));
    }

    /**
     * setea el timeline para cambiar las cada 5 segundos Y define el timeline
     * como indefinido para que no termine mientras la ventana está abierta
     */
    private void iniciarCambioDeImagen() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> cambiarImagen()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void setWindow() {
        imvMain.fitWidthProperty().bind(root.widthProperty());
        imvMain.fitHeightProperty().bind(root.heightProperty());
        imvMain.preserveRatioProperty().setValue(false);
    }

    /**
     * Animación de cambio de imagen en el ImageView del fondo
     */
    private void cambiarImagen() {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), imvMain);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            indiceActualImagen = (indiceActualImagen + 1) % imagenes.size();
            imvMain.setImage(imagenes.get(indiceActualImagen));
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), imvMain);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }

}
