package cr.ac.una.tournamentcontrolsystem.model;

import java.util.TreeMap;

public class LlavesTorneo {

    private int idTorneo;
    private TreeMap llaves;

    public LlavesTorneo(int idTorneo, TreeMap llaves) {
        this.idTorneo = idTorneo;
        this.llaves = llaves;
    }

    public int getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(int idTorneo) {
        this.idTorneo = idTorneo;
    }

    public TreeMap getLlaves() {
        return llaves;
    }

    public void setLlaves(TreeMap llaves) {
        this.llaves = llaves;
    }
}
