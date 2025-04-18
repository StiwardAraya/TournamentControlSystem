package cr.ac.una.tournamentcontrolsystem.model;

import java.util.Objects;

/**
 * Clase que representa un Equipo.
 *
 * @author Stiward Araya C.
 * @author Angie Marks S.
 * @author Kevin Calderon Z.
 */
public class Equipo {

    private int id;
    private String nombre;
    private String fotoURL;
    private boolean enTorneoActivo;
    public int puntosTotales;
    private Deporte deporte;

    public Equipo() {
        this(0, "", "", false, 0, new Deporte());
    }

    public Equipo(int id, String nombre, String fotoURL, boolean enTorneoActivo, int puntosTotales, Deporte deporte) {
        this.id = id;
        this.nombre = nombre;
        this.fotoURL = fotoURL;
        this.enTorneoActivo = enTorneoActivo;
        this.puntosTotales = puntosTotales;
        this.deporte = deporte;
    }

    // SETTERS
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFotoURL(String fotoURL) {
        this.fotoURL = fotoURL;
    }

    public void setEnTorneoActivo(boolean enTorneoActivo) {
        this.enTorneoActivo = enTorneoActivo;
    }

    public void setPuntosTotales(int puntosTotales) {
        this.puntosTotales = puntosTotales;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }

    // GETTERS
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFotoURL() {
        return fotoURL;
    }

    public boolean isEnTorneoActivo() {
        return enTorneoActivo;
    }

    public int getPuntosTotales() {
        return puntosTotales;
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
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Equipo otroEquipo = (Equipo) obj;
        return this.id == otroEquipo.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); 
    }
}