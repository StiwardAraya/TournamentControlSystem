package cr.ac.una.tournamentcontrolsystem.model;

public class Equipo {
    
    private int idEquipo;
    private String nombreEquipo;
    private String fotoURL;
    private boolean enTorneoActivo;
    private int puntosTotales;
    private String imagenURL;
    private Deporte deporte;
    
    public Equipo(){}
    
    public Equipo(int idEquipo, String nombreEquipo, String fotoURL, boolean enTorneoActivo, int puntosTotales, String imagenURL, Deporte deporte) {
        this.idEquipo = idEquipo;
        this.nombreEquipo = nombreEquipo;
        this.fotoURL = fotoURL;
        this.enTorneoActivo = enTorneoActivo;
        this.puntosTotales = puntosTotales;
        this.imagenURL = imagenURL;
        this.deporte = deporte;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public void setFotoURL(String fotoURL) {
        this.fotoURL = fotoURL;
    }

    public void setEnTorneoActivo(boolean enTorneoActivo) {
        this.enTorneoActivo = enTorneoActivo;
    }

    public void setPuntosTotales(int puntosTotales) {
        this.puntosTotales = puntosTotales;
    }
    
    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public String getFotoURL() {
        return fotoURL;
    }

    public boolean isEnTorneoActivo() {
        return enTorneoActivo;
    }

    public int getPuntosTotales() {
        return puntosTotales;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public Deporte getDeporte() {
        return deporte;
    }

    @Override
    public String toString() {
        return "Equipo{" +
                "idEquipo=" + idEquipo +
                ", nombreEquipo='" + nombreEquipo + '\'' +
                ", fotoURL='" + fotoURL + '\'' +
                ", enTorneoActivo=" + enTorneoActivo +
                ", puntosTotales=" + puntosTotales +
                ", deporte=" + deporte +
                '}';
    }
    
}
