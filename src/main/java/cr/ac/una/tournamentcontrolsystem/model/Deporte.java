package cr.ac.una.tournamentcontrolsystem.model;

/**
 * Clase que representa un deporte.
 * 
 * @author Stiward Araya C.
 * @author Angie Marks S.
 * @author Kevin Calderon Z.
 */
public class Deporte {
    
    private int id;
    private String nombre;
    private String imagenURL;
    
    public Deporte(){
        this(0, "", "");
    }

    public Deporte(int id, String nombre, String imagenURL) {
        this.id = id;
        this.nombre = nombre;
        this.imagenURL = imagenURL; 
    }
    
    public void setIdDeporte(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagenURL() {
        return imagenURL;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
   
}
