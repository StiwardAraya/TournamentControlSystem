package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import cr.ac.una.tournamentcontrolsystem.model.EquipoTorneo;
import cr.ac.una.tournamentcontrolsystem.model.Torneo;
import cr.ac.una.tournamentcontrolsystem.util.AppContext;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 * Controlador del generador de certificado
 *
 * @author Stiward Araya C.
 * @author Angie Marks S.
 * @author Kevin Calderón Z.
 */
public class CertificadoGanadorController extends Controller implements Initializable {

    @FXML
    private Label lblEquipo;
    @FXML
    private Label lblPartidosJugados;
    @FXML
    private Label lblPuntosObtenidos;
    @FXML
    private Label lblPosicionFinal;
    @FXML
    private Label lblTorneo;
    @FXML
    private AnchorPane anchorCertificado;
    @FXML
    private ImageView imvEquipo;
    @FXML
    private StackPane containerEquipo;

    String nombreEquipoGanador;
    String nombreTorneo;
    Integer partidosJugados;
    Integer puntosObtenidos;
    Integer posicionFinal;

    Equipo equipo;
    Torneo torneo;
    EquipoTorneo equipoTorneo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void initialize() {
        cargarDatos();
        establecerDatos();
        configurarComponentes();
    }

    private void cargarDatos() {
        nombreEquipoGanador = (String) AppContext.getInstance().get("nombreEquipoGanador");
        nombreTorneo = (String) AppContext.getInstance().get("nombreTorneo");
        partidosJugados = (Integer) AppContext.getInstance().get("partidosJugados");
        puntosObtenidos = (Integer) AppContext.getInstance().get("puntosObtenidos");
        posicionFinal = (Integer) AppContext.getInstance().get("posicionFinal");
    }

    private void establecerDatos() {
        equipo = new Equipo();
        torneo = new Torneo();
        equipoTorneo = new EquipoTorneo(puntosObtenidos, posicionFinal, partidosJugados, equipo, torneo);
        equipo.setNombre(nombreEquipoGanador);
        torneo.setNombre(nombreTorneo);
        infoGanador(equipoTorneo);
    }

    private void configurarComponentes() {
        containerEquipo.setStyle("-fx-background-color: transparent; -fx-opacity: 1;");
        imvEquipo.setImage(new Image(new File(equipo.getFotoURL()).toURI().toString()));
        imvEquipo.setStyle("-fx-cursor: hand; -fx-opacity: 1;");
    }

    /**
     * Permite al usuario guardar el certificado mostrado en la interfaz como un
     * archivo PDF.
     *
     * Este método abre un cuadro de diálogo para seleccionar la ubicación y el
     * nombre del archivo, captura una instantánea del nodo anchorCertificado
     * como imagen, la ajusta a un tamaño específico y la inserta centrada en
     * una página PDF utilizando Apache PDFBox.
     *
     * El PDF resultante se guarda en la ruta seleccionada por el usuario. Se
     * utiliza un archivo temporal intermedio en formato PNG para convertir la
     * imagen de JavaFX en un objeto compatible con PDFBox.
     *
     * Si ocurre algún error durante el proceso de captura, redimensionamiento o
     * guardado del PDF, se imprime la traza del error en la consola.
     */
    private void descargarCertificado() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Certificado como PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
        File file = fileChooser.showSaveDialog(anchorCertificado.getScene().getWindow());
        if (file != null) {
            try {
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);

                WritableImage image = anchorCertificado.snapshot(new SnapshotParameters(), null);
                File tempFile = File.createTempFile("temp", null);
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", tempFile);

                double nuevoAnchoCm = 20.05;
                double nuevoAltoCm = 15.27;
                BufferedImage bufferedImage = ImageIO.read(tempFile);
                BufferedImage imagenAjustada = ajustarTamano(bufferedImage, nuevoAnchoCm, nuevoAltoCm);

                tempFile.delete();

                float ejeX = (float) ((page.getMediaBox().getWidth() - (nuevoAnchoCm * 72 / 2.54)) / 2);
                float ejeY = (float) ((page.getMediaBox().getHeight() - (nuevoAltoCm * 72 / 2.54)) / 2);

                PDImageXObject pdImage = LosslessFactory.createFromImage(document, imagenAjustada);
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true)) {
                    contentStream.drawImage(pdImage, ejeX, ejeY, (float) (nuevoAnchoCm * 72 / 2.54), (float) (nuevoAltoCm * 72 / 2.54));
                }

                document.save(file);
                document.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Ajusta el ancho de una imagen BufferedImage a un nuevo valor en
     * centímetros, manteniendo su altura original en píxeles.
     *
     * Se crea una nueva imagen con el ancho especificado y se dibuja la imagen
     * original escalada proporcionalmente en el eje horizontal. La altura no se
     * modifica.
     *
     * @param originalImage La imagen original que se va a redimensionar.
     * @param nuevoAnchoCm El nuevo ancho deseado en centímetros.
     * @param nuevoAltoCm No se utiliza en este método; la altura se mantiene
     * como en la imagen original.
     *
     * @return Una nueva imagen BufferedImage con el ancho ajustado.
     */
    private BufferedImage ajustarTamano(BufferedImage originalImage, double nuevoAnchoCm, double nuevoAltoCm) {
        double dpi = 72;
        int nuevoAnchoPx = (int) (nuevoAnchoCm * dpi / 2.54);
        int nuevoAltoPx = originalImage.getHeight();
        BufferedImage imagenAjustada = new BufferedImage(nuevoAnchoPx, nuevoAltoPx, originalImage.getType());
        Graphics2D graphics = imagenAjustada.createGraphics();
        graphics.drawImage(originalImage, 0, 0, nuevoAnchoPx, nuevoAltoPx, null);
        graphics.dispose();
        return imagenAjustada;
    }

    /**
     * Muestra en la interfaz gráfica la información del equipo ganador de un
     * torneo.
     *
     * Este método toma un objeto EquipoTorneo y actualiza varios elementos
     * visuales de la interfaz para reflejar los datos del equipo: nombre del
     * equipo, nombre del torneo, cantidad de partidos jugados, puntos obtenidos
     * y posición final. Si el equipo tiene una foto asociada, esta se carga y
     * se muestra en el componente de imagen {@code imvEquipo}.
     *
     * No realiza ninguna acción si el parámetro equipoTorneo es null.
     *
     * @param equipoTorneo Objeto que contiene la información del equipo y del
     * torneo.
     */
    private void infoGanador(EquipoTorneo equipoTorneo) {
        if (equipoTorneo != null) {
            lblEquipo.setText(equipoTorneo.getEquipo().getNombre());
            lblTorneo.setText(equipoTorneo.getTorneo().getNombre());
            lblPartidosJugados.setText(String.valueOf(equipoTorneo.getPartidosJugados()));
            lblPuntosObtenidos.setText(String.valueOf(equipoTorneo.getPuntosEquipo()));
            lblPosicionFinal.setText(String.valueOf(equipoTorneo.getPosicionFinal()));

            String imagenUrl = equipoTorneo.getEquipo().getFotoURL();
            if (imagenUrl != null) {
                Image image = new Image(new File(imagenUrl).toURI().toString());
                imvEquipo.setImage(image);
            }
        }
    }

    // EVENTOS
    @FXML
    private void onActionBtnDescargar(ActionEvent event) {
        descargarCertificado();
    }
}
