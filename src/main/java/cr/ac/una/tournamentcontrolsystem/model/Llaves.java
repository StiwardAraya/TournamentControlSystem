package cr.ac.una.tournamentcontrolsystem.model;

import java.util.List;
import javafx.collections.ObservableList;

public class Llaves {

    private NodoTorneo raiz;
    private List<Equipo> equipos;

    public Llaves(ObservableList<Equipo> equipos) {
        this.equipos = equipos;
        this.raiz = construirLlaves(equipos, 0, equipos.size() - 1);
    }

    private NodoTorneo construirLlaves(ObservableList<Equipo> equipos, int inicio, int fin) {
        if (inicio == fin) {
            return new NodoTorneo(equipos.get(inicio));
        }

        int medio = (inicio + fin) / 2;
        NodoTorneo nodo = new NodoTorneo(null);

        nodo.setIzquierdo(construirLlaves(equipos, inicio, medio));
        nodo.setDerecho(construirLlaves(equipos, medio + 1, fin));

        return nodo;
    }

    public NodoTorneo getRaiz() {
        return raiz;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public Equipo buscarEquipo(Equipo equipo) {
        return buscarEquipoRecursivo(raiz, equipo);
    }

    public void registrarGanador(Equipo ganador) {
        NodoTorneo nodoGanador = buscarNodoPorEquipo(raiz, ganador);
        if (nodoGanador != null) {
            NodoTorneo nodoPadre = encontrarPadre(raiz, nodoGanador);
            if (nodoPadre != null) {
                nodoPadre.setEquipo(ganador);
                nodoGanador.setEquipo(null);
            }
        }
    }

    private Equipo buscarEquipoRecursivo(NodoTorneo nodo, Equipo equipo) {
        if (nodo == null) {
            return null;
        }

        if (nodo.getEquipo() != null && nodo.getEquipo().equals(equipo)) {
            return nodo.getEquipo(); // Retornamos el equipo encontrado
        }

        Equipo encontrado = buscarEquipoRecursivo(nodo.getIzquierdo(), equipo);
        if (encontrado != null) {
            return encontrado;
        }

        return buscarEquipoRecursivo(nodo.getDerecho(), equipo);
    }

    private NodoTorneo buscarNodoPorEquipo(NodoTorneo nodo, Equipo equipo) {
        if (nodo == null) {
            return null;
        }

        if (nodo.getEquipo() != null && nodo.getEquipo().equals(equipo)) {
            return nodo;
        }

        NodoTorneo encontrado = buscarNodoPorEquipo(nodo.getIzquierdo(), equipo);
        if (encontrado != null) {
            return encontrado;
        }

        return buscarNodoPorEquipo(nodo.getDerecho(), equipo);
    }

    private NodoTorneo encontrarPadre(NodoTorneo nodo, NodoTorneo hijo) {
        if (nodo == null || (nodo.getIzquierdo() == null && nodo.getDerecho() == null)) {
            return null;
        }

        if (nodo.getIzquierdo() == hijo || nodo.getDerecho() == hijo) {
            return nodo;
        }

        NodoTorneo padreEncontrado = encontrarPadre(nodo.getIzquierdo(), hijo);
        if (padreEncontrado != null) {
            return padreEncontrado;
        }

        return encontrarPadre(nodo.getDerecho(), hijo);
    }

}
