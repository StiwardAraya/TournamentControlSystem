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

    public Partido() {
        this(0, new Torneo());
    }

    public Partido(int idPartido, Torneo torneo) {
        this.idPartido = idPartido;
        this.torneo = torneo;
    }

    // SETTERS
    public void setIdPartido(int idPartido) {
        this.idPartido = idPartido;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    // GETTERS
    public int getIdPartido() {
        return idPartido;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    @Override
    public String toString() {
        return "Partido{"
                + "idPartido=" + idPartido
                + ", torneo=" + torneo
                + '}';
    }

}
