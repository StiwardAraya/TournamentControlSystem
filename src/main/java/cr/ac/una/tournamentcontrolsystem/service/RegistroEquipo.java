package cr.ac.una.tournamentcontrolsystem.service;

import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

public class RegistroEquipo {

    private List<Equipo> equipos;
    private static RegistroEquipo INSTANCE;
    static final Logger logger = Logger.getLogger(RegistroEquipo.class.getName());
    int lastId = getLastId();

    private RegistroEquipo() {
        Respuesta respuestaGetEquipos = getEquipos();
        if (respuestaGetEquipos.getEstado()) {
            equipos = (List<Equipo>) respuestaGetEquipos.getResultado("equipos");
        } else {
            equipos = new ArrayList<>();
        }

        lastId = getLastId();

        if (lastId == -1) {
            lastId = 0;
        }
    }

    public static RegistroEquipo getInstance() {
        if (INSTANCE == null) {
            synchronized (RegistroEquipo.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RegistroEquipo();
                }
            }
        }
        return INSTANCE;
    }

    public Respuesta buscarEquipo(int idEquipo) {
        for (Equipo equipo : equipos) {
            if (equipo.getId() == idEquipo) {
                return new Respuesta(true, "Equipo encontrado con exito", "Equipo cargado", "equipoEncontrado", equipo);
            }
        }

        return new Respuesta(false, "El equipo no se encuentra registrado", "Equipo no existente");
    }

    public Respuesta getEquipos() {
        return GestorArchivo.getInstance().cargarEquipos();
    }

    public Respuesta guardarEquipo(Equipo equipo, Image selectedImage) {

        for (Equipo d : equipos) {
            if (equipo.getNombre().equals(d.getNombre()) && equipo.getId() != d.getId()) {
                return new Respuesta(false, "Ya existe un equipo con ese nombre", "Equipo repetido");
            }
        }

        if (buscarEquipo(equipo.getId()).getEstado()) {
            equipo.setFotoURL(guardarImagen(equipo.getId(), selectedImage).toString());
            int indexEquipoEncontrado = equipos.indexOf(equipo);
            equipos.set(indexEquipoEncontrado, equipo);
            if (GestorArchivo.getInstance().persistEquipos(equipos).getEstado()) {
                return new Respuesta(true, "Equipo actualizado con exito!", null);
            } else {
                return new Respuesta(false, "No se pudo actualizar el equipo", "Error al guardar la lista de equipos");
            }
        }

        equipo.setId(lastId + 1);
        lastId++;
        equipo.setFotoURL(guardarImagen(equipo.getId(), selectedImage).toString());
        equipos.add(equipo);
        if (GestorArchivo.getInstance().persistEquipos(equipos).getEstado()) {
            return new Respuesta(true, "Equipo agregado con exito!", null);
        } else {
            return new Respuesta(false, "No se pudo agregar el equipo", "Error al guardar la lista de equipos");
        }
    }

    public Respuesta eliminarEquipo(int idEquipo) {
        Respuesta respuestaBuscarEquipo = buscarEquipo(idEquipo);
        if (!respuestaBuscarEquipo.getEstado()) {
            return respuestaBuscarEquipo;
        }

        eliminarImagen((Equipo) respuestaBuscarEquipo.getResultado("equipoEncontrado"));
        equipos.remove((Equipo) respuestaBuscarEquipo.getResultado("equipoEncontrado"));

        if (GestorArchivo.getInstance().persistEquipos(equipos).getEstado()) {
            return new Respuesta(true, "Equipo eliminado con exito!", null);
        } else {
            return new Respuesta(false, "No se pudo eliminar el equipo", "Error al guardar la lista de equipos");
        }
    }

    private Path guardarImagen(int equipoId, Image selectedImage) {
        String carpeta = "FotosEquipos";

        File ruta = new File(carpeta);
        if (!ruta.exists()) {
            ruta.mkdirs();
        }

        String extension = ".png";
        String nombreArchivo = equipoId + extension;
        File archivoImagen = new File(ruta, nombreArchivo);

        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(selectedImage, null);
            ImageIO.write(bufferedImage, "png", archivoImagen);
            return archivoImagen.toPath();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al guardar la foto");
            return null;
        }
    }

    private void eliminarImagen(Equipo equipo) {
        String fotoURL = equipo.getFotoURL();
        if (fotoURL == null || fotoURL.isEmpty()) {
            logger.log(Level.WARNING, "No se puede eliminar la imagen: la URL está vacía o es nula.");
            return;
        }

        Path imagenPath = Paths.get(fotoURL);

        try {
            Files.delete(imagenPath);
            logger.log(Level.INFO, "Imagen eliminada con éxito: " + imagenPath);
        } catch (NoSuchFileException e) {
            logger.log(Level.WARNING, "No se encontró el archivo para eliminar: " + imagenPath, e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al eliminar la imagen: " + imagenPath, e);
        }
    }

    private int getLastId() {
        if (equipos != null && !equipos.isEmpty()) {
            return equipos.getLast().getId();
        }
        return -1;
    }

}
