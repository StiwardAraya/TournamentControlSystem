package cr.ac.una.tournamentcontrolsystem.service;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import java.util.ArrayList;
import java.util.List;
import cr.ac.una.tournamentcontrolsystem.util.Mensaje;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class RegistroDeporte {
   
    private List<Deporte> deportes;
    private int contadorId;
    private Deporte deporte;
    private static RegistroDeporte instance;
    Mensaje mensaje = new Mensaje();
    GestorArchivo gestorArchivo = GestorArchivo.getInstance();

    public RegistroDeporte() {
        this.deportes = new ArrayList<>(); 
        this.contadorId = 0;
        this.deporte = new Deporte();
        
    }
    
    // Método público estático para obtener la instancia
    public static RegistroDeporte getInstance() {
        if (instance == null) {
            synchronized (RegistroDeporte.class) {
                if (instance == null) {
                    instance = new RegistroDeporte();
                }
            }
        }
        return instance;
    }
    
    public List<Deporte> getDeportes() {
        return deportes;
    }

    public void agregarDeporte(String nombreDeporte) {
        if (nombreDeporte == null || nombreDeporte.isEmpty()) {
            mensaje.show(Alert.AlertType.ERROR, "Error", "Por favor, ingrese un deporte.");
            return;
        }

        for (Deporte d : deportes) {
            if (d.getNombreDeporte().equalsIgnoreCase(nombreDeporte)) {
                mensaje.show(Alert.AlertType.ERROR, "Error", "Deporte ya existente, por favor agregue otro.");
                return;
            }
        }

        if (!hayImagenCargada()) {
            mensaje.show(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar la imagen del balón.");
            return;
        }

        Deporte nuevoDeporte = new Deporte();
        nuevoDeporte.setNombreDeporte(nombreDeporte);
        nuevoDeporte.setIdDeporte(++contadorId); 

        deportes.add(nuevoDeporte);
        guardarImagen(nuevoDeporte.getIdDeporte());
        gestorArchivo.guardarDeporteArchivo(deportes);
        
        mensaje.show(Alert.AlertType.INFORMATION, "Éxito", "Deporte agregado exitosamente. Deporte: " + nuevoDeporte.getNombreDeporte() + ", ID: " + nuevoDeporte.getIdDeporte());
    }
    
    public void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione una imagen");
        fileChooser.getExtensionFilters().addAll(
           new FileChooser.ExtensionFilter("Archivos de imagen", "*.png", "*.jpg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            if (deporte != null) {
                deporte.setImagenURL(selectedFile.getAbsolutePath());

                System.out.println("Ruta de la imagen seleccionada: " + selectedFile.getAbsolutePath());
            } else {
                System.out.println("No se seleccionó ninguna imagen");
            }
        }
    }
    
    public void guardarImagen(int idDeporte) {
        String imagenURL = deporte.getImagenURL();
        Path imagenSeleccionadaPath = Paths.get(imagenURL); 

        String extension = "";
        String nombreImagen = imagenSeleccionadaPath.getFileName().toString();
        int index = nombreImagen.lastIndexOf('.');
        if (index > 0) {
            extension = nombreImagen.substring(index); 
        }

        Path nuevaImagenPath = Paths.get("Imágenes Balón", idDeporte + extension);
        System.out.println("Ruta de la nueva imagen: " + nuevaImagenPath.toString());

        try {
            Files.copy(imagenSeleccionadaPath, nuevaImagenPath);
            System.out.println("Imagen guardada exitosamente en la carpeta Imágenes Balón");
        } catch (IOException e) {
            System.err.println("No se pudo guardar la imagen en la carpeta Imágenes Balón");
        }
    }
    
    public boolean hayImagenCargada() {
        if (deporte != null) {
            String imagen = deporte.getImagenURL(); 
            return imagen != null && !imagen.isEmpty();
            }
        
           return false; 
    }

    public Deporte buscarDeporte(int idDeporte) {
        for (Deporte d : deportes) {
            if (d.getIdDeporte() == idDeporte) {
            mensaje.show(Alert.AlertType.INFORMATION, "Deporte Encontrado", "ID: " + d.getIdDeporte() + ", Nombre: " + d.getNombreDeporte());
                    return d; 
                }
            }

        mensaje.show(Alert.AlertType.ERROR, "Error", "El ID ingresado no coincide con ningún deporte.");

        return null; 
    }
       
    public void editarDeporte(int idDeporte, String nuevoNombre) {
        Deporte deporte = buscarDeporte(idDeporte);

        if (deporte != null) { 
           deporte.setNombreDeporte(nuevoNombre);

        mensaje.show(Alert.AlertType.INFORMATION, "Cambios guardados exitosamente","Nuevo nombre del deporte: " + nuevoNombre);
        } 
        
        else {
            mensaje.show(Alert.AlertType.ERROR, "Error", "No se encontró un deporte con el ID proporcionado.");
        }
    }
       
    public void eliminarDeporte(int idDeporte) {
        Deporte deporteAEliminar = null;

        for (Deporte d : deportes) {
            if (d.getIdDeporte() == idDeporte) {
            deporteAEliminar = d; 
            break;
            }
        }

        if (deporteAEliminar != null) {
            deportes.remove(deporteAEliminar); 
            mensaje.show(Alert.AlertType.INFORMATION, "Deporte Eliminado","El deporte con ID " + idDeporte + " ha sido eliminado.");
        } 
        
        else {
                mensaje.show(Alert.AlertType.ERROR, "Error", "No se encontró un deporte con el ID proporcionado.");
            }
       // gestorArchivo.eliminarDeporteArchivo(deporteAEliminar);
        }
   }