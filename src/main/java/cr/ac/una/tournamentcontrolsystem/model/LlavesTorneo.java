package cr.ac.una.tournamentcontrolsystem.model;

public class LlavesTorneo {

    private int idTorneo;
    private Llaves llaves;

    public LlavesTorneo(int idTorneo, Llaves llaves) {
        this.idTorneo = idTorneo;
        this.llaves = llaves;
    }

    public int getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(int idTorneo) {
        this.idTorneo = idTorneo;
    }

    public Llaves getLlaves() {
        return llaves;
    }

    public void setLlaves(Llaves llaves) {
        this.llaves = llaves;
    }
}
