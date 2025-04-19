package cr.ac.una.tournamentcontrolsystem.model;

/**
 * Clase que representa la relacion entre los equipos y los partidos.
 *
 * @author Stiward Araya C.
 * @author Angie Marks S.
 * @author Kevin Calderon Z.
 */
public class EquipoPartido {

    private Equipo equipo;
    private Partido partido;
    private int marcador;
    private boolean resultado;

    public EquipoPartido(Equipo equipo, Partido partido, int marcador, boolean resultado) {
        this.equipo = equipo;
        this.partido = partido;
        this.marcador = marcador;
        this.resultado = resultado;
    }

    // SETTERS
    public void setMarcador(int marcador) {
        this.marcador = marcador;
    }

    public void setResultado(boolean resultado) {
        this.resultado = resultado;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    // GETTERS
    public int getMarcador() {
        return marcador;
    }

    public boolean getResultado() {
        return resultado;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public Partido getPartido() {
        return partido;
    }

    @Override
    public String toString() {
        return "EquipoPartido{" + "marcador=" + marcador + ", resultado=" + (resultado ? "Ganador" : "Perdedor") + ", equipo=" + equipo + ", partido=" + partido + '}';
    }
}
