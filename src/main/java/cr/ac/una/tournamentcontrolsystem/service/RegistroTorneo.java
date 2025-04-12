package cr.ac.una.tournamentcontrolsystem.service;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import cr.ac.una.tournamentcontrolsystem.model.Torneo;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RegistroTorneo {

    private List<Torneo> torneos;
    private static final Logger logger = Logger.getLogger(RegistroTorneo.class.getName());
    int lastId = getLastId();
    private static RegistroTorneo INSTANCE;

    private RegistroTorneo() {
        torneos = (List<Torneo>) GestorArchivo.getInstance().cargarTorneos().getResultado("torneos");
        lastId = getLastId();
    }
    
    public static RegistroTorneo getInstance() {
        if (INSTANCE == null) {
            synchronized (RegistroTorneo.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RegistroTorneo();
                }
            }
        }
        return INSTANCE;
    }

    public Respuesta buscarTorneo(int idTorneo) {
        for (Torneo torneo : torneos) {
            if (torneo.getId() == idTorneo) {
                return new Respuesta(true, "Torneo encontrado con éxito!", null, "torneoEncontrado", torneo);
            }
        }
        return new Respuesta(false, "El torneo no se encuentra registrado", "Torneo no encontrado");
    }

    public Respuesta getTorneos() {
        return GestorArchivo.getInstance().cargarTorneos();
    }

    public Respuesta guardarTorneo(Torneo torneo, Deporte deporteSeleccionado) {
        
        if (torneos == null) {
            torneos = new ArrayList<>();
        }   

        for (Torneo torn : torneos) {
            if (torneo.getNombre().equals(torn.getNombre()) && torneo.getId() != torn.getId()) {
                return new Respuesta(false, "Ya existe un torneo con ese nombre", "Torneo repetido");
            }
        }
        
        torneo.setDeporte(deporteSeleccionado);
        torneo.setId(lastId + 1);
        lastId++;
        torneos.add(torneo);
        if (GestorArchivo.getInstance().persistTorneos(torneos).getEstado()) {
            return new Respuesta(true, "Torneo agregado con exito!", null);
        } else {
            return new Respuesta(false, "No se pudo agregar el torneo", "Error al guardar la lista de torneos");
        }
    }

    private int getLastId() {
        if (torneos != null && !torneos.isEmpty()) {
            return torneos.getLast().getId();
        }
        return -1;
    }
}