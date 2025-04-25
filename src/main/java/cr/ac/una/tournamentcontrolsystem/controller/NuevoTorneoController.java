package cr.ac.una.tournamentcontrolsystem.controller;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import cr.ac.una.tournamentcontrolsystem.model.EquipoTorneo;
import cr.ac.una.tournamentcontrolsystem.model.Llaves;
import cr.ac.una.tournamentcontrolsystem.model.LlavesTorneo;
import cr.ac.una.tournamentcontrolsystem.model.Torneo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroDeporte;
import cr.ac.una.tournamentcontrolsystem.service.RegistroEquipo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroEquipoTorneo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroLlavesTorneos;
import cr.ac.una.tournamentcontrolsystem.service.RegistroTorneo;
import cr.ac.una.tournamentcontrolsystem.util.Formato;
import cr.ac.una.tournamentcontrolsystem.util.Mensaje;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controlador de la ventana de creación de torneos
 *
 * @author Stiward Araya C.
 * @author Angie Marks S.
 * @author Kevin Calderon Z.
 */
public class NuevoTorneoController extends Controller implements Initializable {

    @FXML
    private MFXComboBox<Deporte> mcbDeporte;
    @FXML
    private ListView<Equipo> lvEquipos;
    @FXML
    private ListView<Equipo> lvTorneo;
    @FXML
    private MFXButton btnGuardar;
    @FXML
    private MFXTextField txfNombre;
    @FXML
    private ImageView imvImagenBalon;
    @FXML
    private MFXTextField txfTiempoPorPartido;
    @FXML
    private VBox minibalonContainer;

    private Equipo equipo;
    private Deporte deporte;
    private Torneo torneo;
    private EquipoTorneo equipoTorneo;

    /**
     * Inicializa la interfaz gráfica del controlador al ser cargado.
     *
     * Este método configura los componentes de la vista al inicializarse: -
     * Crea instancias vacías de Torneo, Deporte y Equipo. - Aplica un formato
     * de texto para permitir solo números enteros en el campo de tiempo por
     * partido. - Agrega un listener al ComboBox de deportes para cargar los
     * equipos correspondientes cada vez que se seleccione un nuevo deporte. -
     * Muestra la imagen asociada al deporte seleccionado en un ImageView,
     * activando su contenedor. - Configura la funcionalidad de arrastrar y
     * soltar entre las listas de equipos y equipos del torneo.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        torneo = new Torneo();
        deporte = new Deporte();
        equipo = new Equipo();
        txfTiempoPorPartido.delegateSetTextFormatter(Formato.getInstance().integerFormat());

        mcbDeporte.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cargarEquipos();
        });

        mcbDeporte.valueProperty().addListener((observable, oldValue, newValue) -> {
            Deporte deporteCombo = mcbDeporte.getSelectedItem();
            if (deporteCombo != null) {
                String imagenBalonURL = deporteCombo.getImagenURL();
                imvImagenBalon.setImage(new Image(new File(imagenBalonURL).toURI().toString()));
                minibalonContainer.setStyle("-fx-opacity: 1");
            }
        });

        dragAndDrop(lvEquipos, lvTorneo);
        dragAndDrop(lvTorneo, lvEquipos);
    }

    @Override
    public void initialize() {
        cargarDeportes();
    }

    /**
     * Regresa los componentes de la ventana a sus valores por defecto
     */
    private void reiniciarVentana() {
        txfTiempoPorPartido.clear();
        txfTiempoPorPartido.setStyle("");
        btnGuardar.setText("Guardar");
        txfNombre.clear();
        txfNombre.setStyle("");
        lvEquipos.getItems().clear();
        lvEquipos.getItems().removeAll();
        lvTorneo.getItems().clear();
        lvTorneo.getItems().removeAll();
        mcbDeporte.getSelectionModel().clearSelection();
        mcbDeporte.setStyle("");
        imvImagenBalon.setImage(null);
        minibalonContainer.setStyle("");
        torneo = new Torneo();
        equipo = new Equipo();
        deporte = new Deporte();
    }

    /**
     * Carga los equipos disponibles en la lista de selección según el deporte
     * seleccionado.
     *
     * Este método obtiene todos los equipos registrados mediante
     * RegistroEquipo, limpia las listas gráficas de equipos disponibles y
     * equipos en el torneo, y filtra aquellos equipos que: - Coinciden con el
     * deporte actualmente seleccionado en el ComboBox mcbDeporte - No se
     * encuentran ya participando en un torneo activo.
     *
     * Si no se encuentran equipos disponibles, se muestra un mensaje de
     * advertencia al usuario.
     */
    private void cargarEquipos() {
        Respuesta respuestaEquipos = RegistroEquipo.getInstance().getEquipos();

        lvEquipos.getItems().clear();
        lvEquipos.getItems().removeAll();
        lvTorneo.getItems().clear();
        lvTorneo.getItems().removeAll();

        Deporte deporteSeleccionado = mcbDeporte.getSelectionModel().getSelectedItem();

        if (respuestaEquipos.getEstado()) {
            List<Equipo> equipos = (List<Equipo>) respuestaEquipos.getResultado("equipos");

            if (equipos != null && !equipos.isEmpty()) {
                for (Equipo equipo : equipos) {
                    if (deporteSeleccionado != null && equipo.getDeporte().equals(deporteSeleccionado) && !equipo.isEnTorneoActivo()) {
                        lvEquipos.getItems().add(equipo);
                    }
                }
            } else {
                new Mensaje().show(Alert.AlertType.WARNING, "Cargar Equipos", "No se encontraron equipos disponibles.");
            }
        }
    }

    /**
     * Carga los deportes disponibles en el ComboBox.
     *
     * Este método consulta a RegistroDeporte para obtener la lista de deportes
     * registrados. Si la respuesta es exitosa, limpia las opciones actuales del
     * ComboBox y agrega los deportes recuperados.
     *
     * En caso de error al obtener los datos, muestra un mensaje de alerta con
     * la descripción del problema.
     */
    private void cargarDeportes() {
        try {
            Respuesta respuesta = RegistroDeporte.getInstance().getDeportes();

            if (respuesta.getEstado()) {
                List<Deporte> deportes = (List<Deporte>) respuesta.getResultado("deportes");

                mcbDeporte.getItems().clear();

                if (deportes != null && !deportes.isEmpty()) {
                    mcbDeporte.getItems().addAll(deportes);
                }
            }
        } catch (Exception e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar deportes", e.getMessage());
        }
    }

    /**
     * Habilita la funcionalidad de arrastrar y soltar (drag and drop) entre dos
     * ListView de Equipo.
     *
     * Este método configura los eventos necesarios para permitir que un usuario
     * arrastre un elemento desde la lista de origen y lo suelte en la lista de
     * destino. Utiliza el nombre del equipo como identificador para transferir
     * los datos mediante el portapapeles interno de JavaFX.
     *
     * @param source la ListView de origen desde donde se arrastra el equipo.
     * @param target la ListView de destino donde se suelta el equipo.
     */
    private void dragAndDrop(ListView<Equipo> source, ListView<Equipo> target) {
        source.setOnDragDetected(event -> {
            Dragboard dragboard = source.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            Equipo equipoSeleccionado = source.getSelectionModel().getSelectedItem();
            if (equipoSeleccionado != null) {
                content.putString(equipoSeleccionado.getNombre());
                dragboard.setContent(content);
                event.consume();
            }
        });

        target.setOnDragOver(event -> {
            if (event.getGestureSource() != target && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        target.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasString()) {
                String equipoNombre = dragboard.getString();
                for (Equipo equipo : source.getItems()) {
                    if (equipo.getNombre().equals(equipoNombre)) {
                        source.getItems().remove(equipo);
                        target.getItems().add(equipo);
                        success = true;
                        break;
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    /**
     * Verifica si un numero es potencia de dos
     *
     * @param numero
     * @return valor de la validación
     */
    private boolean esPotenciaDeDos(int numero) {
        return (numero > 0) && ((numero & (numero - 1)) == 0);
    }

    /**
     * Animación de javaFX, sacude el elemento Nodo que ingrese por parámetro
     *
     * @param element
     */
    private void shake(Node element) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(100), element);
        shake.setFromX(0);
        shake.setToX(10);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.play();
    }

    /**
     * Guarda la relación (mapeo) entre los equipos inscritos y el torneo
     * actual.
     *
     * Este método toma los equipos listados en la vista {@code lvTorneo}, marca
     * cada uno como participante en un torneo activo, y guarda el objeto
     * EquipoTorneo correspondiente en el sistema mediante el servicio
     * RegistroEquipoTorneo. Si ocurre algún error durante el guardado, se
     * muestra un mensaje de error en pantalla.
     *
     * Nota: También se llama al método actualizarEquipo para cada equipo antes
     * de marcarlo como activo.
     */
    private void guardarMapeoEquiposTorneos() {
        ObservableList<Equipo> equiposInscritosLista = lvTorneo.getItems();
        ObservableList<Equipo> equiposInscritos = FXCollections.observableList(equiposInscritosLista);

        for (Equipo equipoInscrito : equiposInscritos) {
            actualizarEquipo(equipoInscrito.getId());
            equipoInscrito.setEnTorneoActivo(true);
            Respuesta respuestaGuardarMapeo = RegistroEquipoTorneo.getInstance().guardarEquipoTorneo(new EquipoTorneo(0, 0, 0, equipoInscrito, torneo));
            if (!respuestaGuardarMapeo.getEstado()) {
                new Mensaje().show(Alert.AlertType.ERROR, "Error de inscripcion", "Ocurrio un error al inscribir el equipo: " + equipoInscrito.toString());
            }
        }
    }

    /**
     * Crea la estructura de llaves (bracket) del torneo actual a partir de los
     * equipos inscritos.
     *
     * Este método obtiene la lista de equipos inscritos en el torneo desde
     * lvTorneo, crea una copia de dicha lista y construye un objeto Llaves que
     * representa la estructura del torneo. Posteriormente, se encapsula en un
     * objeto LlavesTorneo con el ID del torneo actual y se guarda usando el
     * servicio RegistroLlavesTorneos.
     */
    private void crearLlaves() {
        ObservableList<Equipo> equiposInscritos = lvTorneo.getItems();
        ObservableList<Equipo> copiaEquipos = FXCollections.observableArrayList(equiposInscritos);
        Llaves llaves = new Llaves(copiaEquipos);
        LlavesTorneo llavesTorneo = new LlavesTorneo(torneo.getId(), llaves);
        Respuesta respuestaGuardarLlavesTorneo = RegistroLlavesTorneos.getInstance().guardarLlavesTorneo(llavesTorneo);
        if (!respuestaGuardarLlavesTorneo.getEstado()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Llaves de torneo", "Error al construir las llaves del torneo");
        }
    }

    /**
     * Actualiza el estado de un equipo para indicar que está participando en un
     * torneo activo.
     *
     * Este método busca un equipo por su ID mediante el servicio
     * RegistroEquipo, establece su estado como inscrito en un torneo activo
     * (`enTorneoActivo = true`) y guarda nuevamente la información del equipo
     * junto con su imagen.
     *
     * @param idEquipoInscrito ID del equipo que se desea actualizar.
     */
    private void actualizarEquipo(int idEquipoInscrito) {
        Equipo equipoInscrito = (Equipo) RegistroEquipo.getInstance().buscarEquipo(idEquipoInscrito).getResultado("equipoEncontrado");
        equipoInscrito.setEnTorneoActivo(true);
        RegistroEquipo.getInstance().guardarEquipo(equipoInscrito, new Image(new File(equipoInscrito.getFotoURL()).toURI().toString()));
    }

    // EVENTOS
    @FXML
    private void onActionBtnGuardar(ActionEvent event) {
        if (txfNombre.getText().isBlank() || txfNombre.getText().isEmpty()) {
            txfNombre.setStyle("-fx-border-color: #D21F3C");
            shake(txfNombre);
            return;
        }

        if (mcbDeporte.getSelectionModel().getSelectedItem() == null) {
            mcbDeporte.setStyle("-fx-border-color: #D21F3C");
            shake(mcbDeporte);
            return;
        }

        if (txfTiempoPorPartido.getText().isBlank() || txfTiempoPorPartido.getText().isEmpty()) {
            txfTiempoPorPartido.setStyle("-fx-border-color: #D21F3C");
            shake(txfTiempoPorPartido);
            return;
        }

        if (lvTorneo.getItems().size() <= 1 || !esPotenciaDeDos(lvTorneo.getItems().size())) {
            new Mensaje().show(Alert.AlertType.ERROR, "Guardar Torneo", "La cantidad de equipos debe ser una potencia de 2 y mayor que 1.");
            return;
        }

        torneo.setId(0);
        torneo.setNombre(txfNombre.getText());
        torneo.setTiempoPorPartido(Integer.parseInt(txfTiempoPorPartido.getText()));

        Respuesta respuestaGuardarTorneo = RegistroTorneo.getInstance().guardarTorneo(torneo, mcbDeporte.getSelectionModel().getSelectedItem());

        if (!respuestaGuardarTorneo.getEstado()) {
            new Mensaje().show(Alert.AlertType.ERROR, "Guardar torneo", respuestaGuardarTorneo.getMensaje());
        } else {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Guardar torneo", respuestaGuardarTorneo.getMensaje());
            guardarMapeoEquiposTorneos();
            crearLlaves();
            reiniciarVentana();
        }
    }

    @FXML
    private void onActionBtnNuevo(ActionEvent event) {
        reiniciarVentana();
    }

}
