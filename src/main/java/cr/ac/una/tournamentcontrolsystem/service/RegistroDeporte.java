package cr.ac.una.tournamentcontrolsystem.service;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
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

public class RegistroDeporte {

    private List<Deporte> deportes;
    private static RegistroDeporte INSTANCE;
    static final Logger logger = Logger.getLogger(RegistroDeporte.class.getName());
    int lastId;

    private RegistroDeporte() {
        Respuesta respuestaGetDeportes = getDeportes();
        if (respuestaGetDeportes.getEstado()) {
            deportes = (List<Deporte>) respuestaGetDeportes.getResultado("deportes");
        } else {
            deportes = new ArrayList<>();
        }

        lastId = getLastId();

        if (lastId == -1) {
            lastId = 0;
        }
    }

    public static RegistroDeporte getInstance() {
        if (INSTANCE == null) {
            synchronized (RegistroDeporte.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RegistroDeporte();
                }
            }
        }
        return INSTANCE;
    }

    public Respuesta buscarDeporte(int idDeporte) {
        for (Deporte deporte : deportes) {
            if (deporte.getId() == idDeporte) {
                return new Respuesta(true, "Deporte encontrado con exito", "Deporte cargado", "deporteEncontrado", deporte);
            }
        }

        return new Respuesta(false, "El deporte no se encuentra registrado", "Deporte no existente");
    }

    public Respuesta getDeportes() {
        return GestorArchivo.getInstance().cargarDeportes();
    }

    public Respuesta guardarDeporte(Deporte deporte, Image selectedImage) {

        for (Deporte d : deportes) {
            if (deporte.getNombre().equals(d.getNombre()) && deporte.getId() != d.getId()) {
                return new Respuesta(false, "Ya existe un deporte con ese nombre", "Deporte repetido");
            }
        }

        if (buscarDeporte(deporte.getId()).getEstado()) {
            deporte.setImagenURL(guardarImagen(deporte.getId(), selectedImage).toString());
            int indexDeporteEncontrado = deportes.indexOf(deporte);
            deportes.set(indexDeporteEncontrado, deporte);
            if (GestorArchivo.getInstance().persistDeportes(deportes).getEstado()) {
                return new Respuesta(true, "Deporte actualizado con exito!", null);
            } else {
                return new Respuesta(false, "No se pudo actualizar el deporte", "Error al guardar la lista de deportes");
            }
        }

        deporte.setId(lastId + 1);
        lastId++;
        deporte.setImagenURL(guardarImagen(deporte.getId(), selectedImage).toString());
        deportes.add(deporte);
        if (GestorArchivo.getInstance().persistDeportes(deportes).getEstado()) {
            return new Respuesta(true, "Deporte agregado con exito!", null);
        } else {
            return new Respuesta(false, "No se pudo agregar el deporte", "Error al guardar la lista de deportes");
        }
    }

    public Respuesta eliminarDeporte(int idDeporte) {
        Respuesta respuestaBuscarDeporte = buscarDeporte(idDeporte);
        if (!respuestaBuscarDeporte.getEstado()) {
            return respuestaBuscarDeporte;
        }

        eliminarImagen((Deporte) respuestaBuscarDeporte.getResultado("deporteEncontrado"));
        deportes.remove((Deporte) respuestaBuscarDeporte.getResultado("deporteEncontrado"));

        if (GestorArchivo.getInstance().persistDeportes(deportes).getEstado()) {
            return new Respuesta(true, "Deporte eliminado con exito!", null);
        } else {
            return new Respuesta(false, "No se pudo eliminar el deporte", "Error al guardar la lista de deportes");
        }
    }

    private Path guardarImagen(int deporteId, Image selectedImage) {
        String carpeta = "ImagenesBalon";

        File ruta = new File(carpeta);
        if (!ruta.exists()) {
            ruta.mkdirs();
        }

        String extension = ".png";
        String nombreArchivo = deporteId + extension;
        File archivoImagen = new File(ruta, nombreArchivo);

        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(selectedImage, null);
            ImageIO.write(bufferedImage, "png", archivoImagen);
            return archivoImagen.toPath();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al guardar la imagen");
            return null;
        }

    }

    private void eliminarImagen(Deporte deporte) {
        String imagenURL = deporte.getImagenURL();
        if (imagenURL == null || imagenURL.isEmpty()) {
            logger.log(Level.WARNING, "No se puede eliminar la imagen: la URL está vacía o es nula.");
            return;
        }

        Path imagenPath = Paths.get(imagenURL);

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
        if (deportes != null && !deportes.isEmpty()) {
            return deportes.getLast().getId();
        }
        return -1;
    }
}
