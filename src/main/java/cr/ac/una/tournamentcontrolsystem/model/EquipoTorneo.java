package cr.ac.una.tournamentcontrolsystem.model;

import java.util.Objects;

/**
 * Clase que representa la relacion entre un equipo y un torneo.
 *
 * @author Stiward Araya C.
 * @author Angie Marks S.
 * @author Kevin Calderon Z.
 */
public class EquipoTorneo {

    private int puntosEquipo;
    private int posicionFinal;
    private int partidosJugados;
    private Equipo equipo;
    private Torneo torneo;

    public EquipoTorneo(int puntosEquipo, int posicionFinal, int partidosJugados, Equipo equipo, Torneo torneo) {
        this.puntosEquipo = puntosEquipo;
        this.posicionFinal = posicionFinal;
        this.partidosJugados = partidosJugados;
        this.equipo = equipo;
        this.torneo = torneo;
    }

    // SETTERS
    public void setPuntosEquipo(int puntosEquipo) {
        this.puntosEquipo = puntosEquipo;
    }

    public void setPosicionFinal(int posicionFinal) {
        this.posicionFinal = posicionFinal;
    }

    public void setPartidosJugados(int partidosJugados) {
        this.partidosJugados = partidosJugados;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    // GETTERS
    public int getPuntosEquipo() {
        return puntosEquipo;
    }

    public int getPosicionFinal() {
        return posicionFinal;
    }

    public int getPartidosJugados() {
        return partidosJugados;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    @Override
    public String toString() {
        return "EquipoTorneo{" + "puntosEquipo=" + puntosEquipo + ", posicionFinal=" + posicionFinal + ", partidosJugados=" + partidosJugados + ", equipo=" + equipo + ", torneo=" + torneo + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        EquipoTorneo otro = (EquipoTorneo) obj;
        return Objects.equals(this.equipo, otro.equipo) && Objects.equals(this.torneo, otro.torneo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(equipo, torneo);
    }
}