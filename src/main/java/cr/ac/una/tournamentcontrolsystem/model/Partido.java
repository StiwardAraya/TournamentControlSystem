package cr.ac.una.tournamentcontrolsystem.model;

/**
 * Clase que representa un Partido.
 *
 * @author Stiward Araya C.
 * @author Angie Marks S.
 * @author Kevin Calderon Z.
 */
public class Partido {

    private int idPartido;
    private Torneo torneo;
    private Equipo ganador; // Nuevo atributo para el equipo ganador

    public Partido() {
        this(0, new Torneo(), null);
    }

    public Partido(int idPartido, Torneo torneo) {
        this(idPartido, torneo, null);
    }

    public Partido(int idPartido, Torneo torneo, Equipo ganador) {
        this.idPartido = idPartido;
        this.torneo = torneo;
        this.ganador = ganador;
    }

    // SETTERS
    public void setIdPartido(int idPartido) {
        this.idPartido = idPartido;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    public void setGanador(Equipo ganador) {
        this.ganador = ganador;
    }

    // GETTERS
    public int getIdPartido() {
        return idPartido;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public Equipo getGanador() {
        return ganador;
    }

    @Override
    public String toString() {
        return "Partido{" +
                "idPartido=" + idPartido +
                ", torneo=" + torneo +
                ", ganador=" + ganador +
                '}';
    }

}