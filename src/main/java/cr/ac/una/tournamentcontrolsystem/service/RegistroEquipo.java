package cr.ac.una.tournamentcontrolsystem.service;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import cr.ac.una.tournamentcontrolsystem.util.Mensaje;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class RegistroEquipo {

    private List<Equipo> equipos;
    private int contadorId;
    private Equipo equipo;
    private Deporte deporteSeleccionado;
    private static RegistroEquipo instance;
    Mensaje mensaje = new Mensaje();
    GestorArchivo gestorArchivo = GestorArchivo.getInstance();

    public RegistroEquipo() {
        this.equipos = new ArrayList<>();
        this.contadorId = 0;
        this.equipo = new Equipo();
        this.deporteSeleccionado = new Deporte();
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public static RegistroEquipo getInstance() {
        if (instance == null) {
            synchronized (RegistroEquipo.class) {
                if (instance == null) {
                    instance = new RegistroEquipo();
                }
            }
        }
        return instance;
    }

    public void mostrarDeportes() {
        for (Equipo equipo : equipos) {
            System.out.println(equipo);
        }
    }

    public void agregarEquipo(String nombreEquipo, Deporte deporteSeleccionado) {
        if (nombreEquipo == null || nombreEquipo.isEmpty()) {
            mensaje.show(Alert.AlertType.ERROR, "Error", "Debe de ingresar el nombre del equipo ");
            return;
        }

        for (Equipo e : equipos) {
            if (e.getNombre().equalsIgnoreCase(nombreEquipo)) {
                mensaje.show(Alert.AlertType.ERROR, "Error", "Equipo ya existe");
                return;
            }
        }

        if (deporteSeleccionado == null) {
            mensaje.show(Alert.AlertType.ERROR, "Error", "Debe seleccionar un deporte.");
            return;
        }

        if (!hayImagenCargada()) {
            mensaje.show(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar la imagen del balón.");
            return;
        }

        Equipo nuevoEquipo = new Equipo();
        nuevoEquipo.setNombre(nombreEquipo);
        nuevoEquipo.setId(++contadorId);
        nuevoEquipo.setDeporte(deporteSeleccionado);

        equipos.add(nuevoEquipo);
        guardarImagen(nuevoEquipo.getId());
        gestorArchivo.guardarEquipoArchivo(equipos);

        mensaje.show(Alert.AlertType.INFORMATION, "Éxito", "Equipo agregado exitosamente. Equipo: " + nuevoEquipo.getNombre() + ", ID: " + nuevoEquipo.getId());
    }

    public Equipo buscarEquipo(int idEquipo) {
        for (Equipo e : equipos) {
            if (e.getId() == idEquipo) {
                mensaje.show(Alert.AlertType.INFORMATION, "Equipo Encontrado", "ID: " + e.getId() + ", Nombre: " + e.getNombre());
                return e;
            }
        }

        mensaje.show(Alert.AlertType.ERROR, "Error", "El ID ingresado no coincide con ningún equipo.");

        return null;
    }

    public void eliminarEquipo(int idEquipo) {
        Equipo equipoEliminar = null;

        for (Equipo e : equipos) {
            if (e.getId() == idEquipo) {
                equipoEliminar = e;
                break;
            }
        }

        if (equipoEliminar != null) {
            equipos.remove(equipoEliminar);
            mensaje.show(Alert.AlertType.INFORMATION, "Equipo Eliminado", "El equipo con ID " + idEquipo + " ha sido eliminado.");
        } else {
            mensaje.show(Alert.AlertType.ERROR, "Error", "No se encontró un equipo con el ID proporcionado.");
        }
    }

    public void editarEquipo(int idEquipo, String nuevoNombreEquipo) {
        Equipo equipo = buscarEquipo(idEquipo);

        if (equipo != null) {
            equipo.setNombre(nuevoNombreEquipo);

            mensaje.show(Alert.AlertType.INFORMATION, "Cambios guardados exitosamente", "Nuevo nombre del equipo: " + nuevoNombreEquipo);
        } else {
            mensaje.show(Alert.AlertType.ERROR, "Error", "No se encontró un equipo con el ID proporcionado.");
        }
    }

    public void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione una imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de imagen", "*.png", "*.jpg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            if (equipo != null) {
                equipo.setFotoURL(selectedFile.getAbsolutePath());

                System.out.println("Ruta de la imagen seleccionada: " + selectedFile.getAbsolutePath());
            } else {
                System.out.println("No se seleccionó ninguna imagen");
            }
        }
    }

    public void guardarImagen(int idEquipo) {
        String imagenURL = equipo.getFotoURL();
        Path imagenSeleccionadaPath = Paths.get(imagenURL);

        String extension = "";
        String nombreImagen = imagenSeleccionadaPath.getFileName().toString();
        int index = nombreImagen.lastIndexOf('.');
        if (index > 0) {
            extension = nombreImagen.substring(index);
        }

        Path nuevaImagenPath = Paths.get("Imagenes Equipos", idEquipo + extension);
        System.out.println("Ruta de la nueva imagen: " + nuevaImagenPath.toString());

        try {
            Files.copy(imagenSeleccionadaPath, nuevaImagenPath);
            System.out.println("Imagen guardada exitosamente en la carpeta Imagenes Equipos");
        } catch (IOException e) {
            System.err.println("No se pudo guardar la imagen en la carpeta Imagenes Equipos");
        }
    }

    public boolean hayImagenCargada() {
        if (equipo != null) {
            String imagen = equipo.getFotoURL();
            return imagen != null && !imagen.isEmpty();
        }

        return false;
    }
}
