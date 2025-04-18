package cr.ac.una.tournamentcontrolsystem.model;

import java.util.ArrayList;
import java.util.List;

public class NodoTorneo {

    private Equipo equipo;
    private NodoTorneo izquierdo;
    private NodoTorneo derecho;

    public NodoTorneo() {
    }

    public NodoTorneo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public NodoTorneo getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(NodoTorneo izquierdo) {
        this.izquierdo = izquierdo;
    }

    public NodoTorneo getDerecho() {
        return derecho;
    }

    public void setDerecho(NodoTorneo derecho) {
        this.derecho = derecho;
    }

     public List<NodoTorneo> getHijos() {
        List<NodoTorneo> hijos = new ArrayList<>();
        if (izquierdo != null) {
            hijos.add(izquierdo);
        }
        if (derecho != null) {
            hijos.add(derecho);
        }
        return hijos;
    }
}
