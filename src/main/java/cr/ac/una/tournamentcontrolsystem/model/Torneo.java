package cr.ac.una.tournamentcontrolsystem.model;

/**
 *
 
@author KEVIN*/
public class Torneo {

    private int idTorneo;
    private int tiempoPorPartido;
    private String nombreTorneo;
    private boolean finalizado;
    private Deporte deporte;

        public Torneo(int idTorneo, int tiempoPorPartido, String nombreTorneo, boolean finalizado, Deporte deporte) {
        this.idTorneo = idTorneo;
        this.tiempoPorPartido = tiempoPorPartido;
        this.nombreTorneo = nombreTorneo;
        this.finalizado = finalizado;
        this.deporte = deporte;
    }

    public Torneo() {}

    public int getIdTorneo() {
        return idTorneo;
    }

    public int getTiempoPorPartido() {
        return tiempoPorPartido;
    }

    public String getNombreTorneo() {
        return nombreTorneo;
    }

    public boolean getFinalizado() {
        return finalizado;
    }

    public Deporte getDeporte() {
        return deporte;
    }

    public void setIdTorneo(int idTorneo) {
        this.idTorneo = idTorneo;
    }

    public void setTiempoPorPartido(int tiempoPorPartido) {
        this.tiempoPorPartido = tiempoPorPartido;
    }

    public void setNombreTorneo(String nombreTorneo) {
        this.nombreTorneo = nombreTorneo;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }

    @Override
    public String toString() {
        return "Torneo{" +
                "idTorneo=" + idTorneo +
                ", tiempoPorPartido=" + tiempoPorPartido +
                ", nombreTorneo='" + nombreTorneo + '/' +
                ", finalizado=" + finalizado +
                ", deporte=" + deporte +
                 '}';
    }

}