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
import javafx.scene.control.Button;
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
 * FXML Controller class
 *
 * @author Angie Marks
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
    private Button btnDescargar;
    @FXML
    private AnchorPane anchorCertificado;
    @FXML
    private ImageView imvEquipo;
    @FXML
    private StackPane containerEquipo;
    
    /*Falta cargrar la imagen del equipo en imvEquipo, todo lo demas funciona correctamente*/

    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        String nombreEquipoGanador = (String) AppContext.getInstance().get("nombreEquipoGanador");
        String nombreTorneo = (String) AppContext.getInstance().get("nombreTorneo");
        Integer partidosJugados = (Integer) AppContext.getInstance().get("partidosJugados");
        Integer puntosObtenidos = (Integer) AppContext.getInstance().get("puntosObtenidos");
        Integer posicionFinal = (Integer) AppContext.getInstance().get("posicionFinal");

        Equipo equipo = new Equipo(); 
        equipo.setNombre(nombreEquipoGanador); 
        Torneo torneo = new Torneo();
        torneo.setNombre(nombreTorneo); 
        EquipoTorneo equipoTorneo = new EquipoTorneo(puntosObtenidos, posicionFinal, partidosJugados, equipo, torneo);
        infoGanador(equipoTorneo);
       
        containerEquipo.setStyle("-fx-background-color: transparent; -fx-opacity: 1;"); 
        imvEquipo.setImage(new Image(new File(equipo.getFotoURL()).toURI().toString()));
        imvEquipo.setStyle("-fx-cursor: hand; -fx-opacity: 1;");
    }    

    @Override
    public void initialize() {
        //TODO
    }

    @FXML
    private void onActionBtnDescargar(ActionEvent event) {
        descargarCertificado();
    }
    
    private void descargarCertificado(){
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
 }
