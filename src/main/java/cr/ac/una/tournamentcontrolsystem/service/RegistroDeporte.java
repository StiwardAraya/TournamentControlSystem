package cr.ac.una.tournamentcontrolsystem.service;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RegistroDeporte {

    private List<Deporte> deportes;
    private static RegistroDeporte INSTANCE;

    private RegistroDeporte() {
        this.deportes = new ArrayList<>();
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

    public Respuesta guardarDeporte(Deporte deporte, File selectedImage) {

        for (Deporte d : deportes) {
            if (deporte.getNombre().equals(d.getNombre()) && deporte.getId() != d.getId()) {
                return new Respuesta(false, "Ya existe un deporte con ese nombre", "Deporte repetido");
            }
        }

        if (buscarDeporte(deporte.getId()).getEstado()) {
            int indexDeporteEncontrado = deportes.indexOf(deporte);
            deportes.set(indexDeporteEncontrado, deporte);
            if (GestorArchivo.getInstance().persistDeportes(deportes).getEstado()) {
                return new Respuesta(true, "Deporte actualizado con exito!", null);
            } else {
                return new Respuesta(false, "No se pudo actualizar el deporte", "Error al guardar la lista de deportes");
            }

        }

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

        deportes.remove(respuestaBuscarDeporte.getResultado("deporteEncontrado"));

        if (GestorArchivo.getInstance().persistDeportes(deportes).getEstado()) {
            return new Respuesta(true, "Deporte eliminado con exito!", null);
        } else {
            return new Respuesta(false, "No se pudo eliminar el deporte", "Error al guardar la lista de deportes");
        }
    }

    /*public void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione una imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de imagen", "*.png", "*.jpg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());
    }*/

 /*public void guardarImagen(int idDeporte) {
        String imagenURL = deporte.getImagenURL();
        Path imagenSeleccionadaPath = Paths.get(imagenURL);

        String extension = "";
        String nombreImagen = imagenSeleccionadaPath.getFileName().toString();
        int index = nombreImagen.lastIndexOf('.');
        if (index > 0) {
            extension = nombreImagen.substring(index);
        }

        Path nuevaImagenPath = Paths.get("Imagenes Balon", idDeporte + extension);
        System.out.println("Ruta de la nueva imagen: " + nuevaImagenPath.toString());

        try {
            Files.copy(imagenSeleccionadaPath, nuevaImagenPath);
            System.out.println("Imagen guardada exitosamente en la carpeta Imagenes Balon");
        } catch (IOException e) {
            System.err.println("No se pudo guardar la imagen en la carpeta Imagenes Balon");
        }
    }/*

 /*public boolean ImagenCargada() {
        if (deporte != null) {
            String imagen = deporte.getImagenURL();
            return imagen != null && !imagen.isEmpty();
        }

        return false;
    }*/
}
