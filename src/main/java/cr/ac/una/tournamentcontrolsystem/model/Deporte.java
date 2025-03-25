package cr.ac.una.tournamentcontrolsystem.model;

public class Deporte {
    
    private int idDeporte;
    private String nombreDeporte;
    private String imagenURL;
    
    public Deporte(int idDeporte, String nombreDeporte, String imagenURL) {
        this.idDeporte = idDeporte;
        this.nombreDeporte = nombreDeporte;
        this.imagenURL = imagenURL; 
    }
    
    public Deporte(){}
   
    public int getIdDeporte() {
        return idDeporte;
    }

    public String getNombreDeporte() {
        return nombreDeporte;
    }

    public String getImagenURL() {
        return imagenURL;
    }
    
    public void setIdDeporte(int idDeporte) {
        this.idDeporte = idDeporte;
    }

    public void setNombreDeporte(String nombreDeporte) {
        this.nombreDeporte = nombreDeporte;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }
    
    @Override
    public String toString() {
            return "Deporte{" +
            "idDeporte=" + idDeporte +
            ", nombreDeporte='" + nombreDeporte + '\'' +
                ", imagenURL='" + imagenURL + '\'' +
            '}';
    }
}
