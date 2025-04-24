package cr.ac.una.tournamentcontrolsystem.model;

import java.util.List;
import javafx.collections.ObservableList;

/**
 * Clase objeto que se comporta como un arbol binario.
 *
 * Usa la recursividad para construirse de acuerdo a la cantidad de equipos que
 * recibe tal que los equipos queden en los nodos hojas con una cantidad
 * n(siendo n la cantidad de equipos) - 1 de nodos vacíos padre
 *
 * @author Stiward Araya C.
 * @author Angie Marks S.
 * @author Kevin Calderon Z.
 */
public class Llaves {

    private NodoTorneo raiz;
    private List<Equipo> equipos;

    public Llaves(ObservableList<Equipo> equipos) {
        this.equipos = equipos;
        this.raiz = construirLlaves(equipos, 0, equipos.size() - 1);
    }

    /**
     * Construye recursivamente un árbol binario de llaves para un torneo a
     * partir de una lista de equipos.
     *
     * @param equipos Lista de equipos inscritos en el torneo.
     * @param inicio Índice de inicio del subarreglo actual.
     * @param fin Índice de fin del subarreglo actual.
     * @return NodoTorneo raíz del árbol binario construido, que representa las
     * llaves del torneo.
     *
     * Si el índice de inicio es igual al de fin, se crea una hoja del árbol con
     * un equipo. En caso contrario, se crea un nodo intermedio (sin equipo
     * asignado) y se divide recursivamente en subárboles izquierdo y derecho,
     * representando las fases del torneo.
     */
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

    public void editarEquipo(Equipo equipo) {
        if (equipo != null) {
            for (int i = 0; i < equipos.size(); i++) {
                if (equipo.getId() == equipos.get(i).getId()) {
                    equipos.set(i, equipo);
                }
            }
        }
    }

    /**
     * Registra el equipo ganador de un partido dentro del árbol de llaves del
     * torneo.
     *
     * Este método busca el nodo correspondiente al equipo ganador y, si lo
     * encuentra, actualiza el nodo padre para avanzar al equipo a la siguiente
     * ronda.
     *
     * @param ganador El equipo que ha ganado el partido.
     *
     * Si se encuentra el nodo del equipo ganador y su nodo padre, se asigna el
     * equipo ganador al nodo padre y se limpia la asignación en el nodo hijo,
     * reflejando el avance del equipo.
     */
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

    /**
     * Busca recursivamente un equipo dentro del árbol de llaves del torneo.
     *
     * Recorre el árbol desde el nodo dado buscando un nodo que contenga al
     * equipo especificado.
     *
     * @param nodo El nodo desde donde se inicia la búsqueda.
     * @param equipo El equipo que se desea encontrar en el árbol.
     * @return El equipo encontrado si está presente en algún nodo del árbol; de
     * lo contrario, null.
     */
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

    /**
     * Busca de forma recursiva el nodo del árbol que contiene al equipo
     * especificado.
     *
     * Compara los identificadores de los equipos para encontrar el nodo
     * correspondiente dentro del árbol de llaves.
     *
     * @param nodo Nodo actual desde el cual se realiza la búsqueda.
     * @param equipo El equipo que se desea localizar en el árbol.
     * @return El nodo que contiene al equipo, si se encuentra; de lo contrario,
     * null.
     */
    private NodoTorneo buscarNodoPorEquipo(NodoTorneo nodo, Equipo equipo) {
        if (nodo == null) {
            return null;
        }

        if (nodo.getEquipo() != null && nodo.getEquipo().getId() == equipo.getId()) {
            return nodo;
        }

        NodoTorneo encontrado = buscarNodoPorEquipo(nodo.getIzquierdo(), equipo);
        if (encontrado != null) {
            return encontrado;
        }

        return buscarNodoPorEquipo(nodo.getDerecho(), equipo);
    }

    /**
     * Busca recursivamente el nodo padre de un nodo hijo específico dentro del
     * árbol de llaves.
     *
     * Compara los hijos izquierdo y derecho del nodo actual para determinar si
     * alguno de ellos es el nodo hijo.
     *
     * @param nodo Nodo actual desde el cual se realiza la búsqueda.
     * @param hijo Nodo del cual se desea encontrar el nodo padre.
     * @return El nodo padre si se encuentra; de lo contrario, null.
     */
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
