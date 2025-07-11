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
    int lastId;
    private static RegistroTorneo INSTANCE;

    private RegistroTorneo() {
        Respuesta respuestaGetTorneos = getTorneos();
        if (respuestaGetTorneos.getEstado()) {
            torneos = (List<Torneo>) respuestaGetTorneos.getResultado("torneos");
        } else {
            torneos = new ArrayList<>();
        }

        lastId = getLastId();

        if (lastId == -1) {
            lastId = 0;
        }
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

        for (int i = 0; i < torneos.size(); i++) {
            if (torneo.getId() == torneos.get(i).getId()) {
                torneos.set(i, torneo);
                if (GestorArchivo.getInstance().persistTorneos(torneos).getEstado()) {
                    return new Respuesta(true, "Torneo actualizado con exito!", null);
                } else {
                    return new Respuesta(false, "No se pudo actualizar el torneo", "Error al guardar la lista de torneos");
                }
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
