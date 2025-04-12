package cr.ac.una.tournamentcontrolsystem.model;

import java.util.Objects;

/**
 * Clase que representa un Torneo.
 * 
 * @author Stiward Araya C.
 * @author Angie Marks S.
 * @author Kevin Calderon Z.
 */
public class Torneo {

    private int id;
    private int tiempoPorPartido;
    private String nombre;
    private boolean finalizado;
    private Deporte deporte;

    public Torneo() {
        this(0, 0, "", false, new Deporte());
    }

    public Torneo(int id, int tiempoPorPartido, String nombre, boolean finalizado, Deporte deporte) {
        this.id = id;
        this.tiempoPorPartido = tiempoPorPartido;
        this.nombre = nombre;
        this.finalizado = finalizado;
        this.deporte = deporte;
    }

    // SETTERS
    public void setId(int id) {
        this.id = id;
    }

    public void setTiempoPorPartido(int tiempoPorPartido) {
        this.tiempoPorPartido = tiempoPorPartido;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }

    // GETTERS
    public int getId() {
        return id;
    }

    public int getTiempoPorPartido() {
        return tiempoPorPartido;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean getFinalizado() {
        return finalizado;
    }

    public Deporte getDeporte() {
        return deporte;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; 
        if (obj == null || getClass() != obj.getClass()) return false; 

        Torneo torneo = (Torneo) obj; 
        return id == torneo.id; 
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); 
    }

}