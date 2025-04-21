package cr.ac.una.tournamentcontrolsystem.service;

import cr.ac.una.tournamentcontrolsystem.model.EquipoTorneo;
import cr.ac.una.tournamentcontrolsystem.model.Torneo;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RegistroEquipoTorneo {

    private List<EquipoTorneo> equiposTorneos;
    private static RegistroEquipoTorneo INSTANCE;
    static final Logger logger = Logger.getLogger(RegistroEquipoTorneo.class.getName());

    private RegistroEquipoTorneo() {
        Respuesta respuestaGetEquiposTorneos = getEquiposTorneos();
        if (respuestaGetEquiposTorneos.getEstado()) {
            equiposTorneos = (List<EquipoTorneo>) respuestaGetEquiposTorneos.getResultado("EquiposTorneos");
        } else {
            equiposTorneos = new ArrayList<>();
        }
    }

    public static RegistroEquipoTorneo getInstance() {
        if (INSTANCE == null) {
            synchronized (RegistroEquipoTorneo.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RegistroEquipoTorneo();
                }
            }
        }
        return INSTANCE;
    }

    public Respuesta buscarEquipoTorneo(int idTorneo, int idEquipo) {
        for (EquipoTorneo et : equiposTorneos) {
            if (et.getTorneo().getId() == idTorneo && et.getEquipo().getId() == idEquipo) {
                return new Respuesta(true, "", "EquipoTorneo encontrado", "equipoTorneoEncontrado", et);
            }
        }

        return new Respuesta(true, "Ese equipo no se encuentra en ese torneo", "No existe ese registro");
    }

    public Respuesta getEquiposTorneos() {
        return GestorArchivo.getInstance().cargarEquiposTorneos();
    }

    public Respuesta guardarEquipoTorneo(EquipoTorneo equipoTorneo) {
        if (equipoTorneo == null) {
            return new Respuesta(false, "No se puede guardar ese equipoTorneo", "Objeto nulo");
        }

        boolean actualizado = false;

        for (int i = 0; i < equiposTorneos.size(); i++) {
            if (equiposTorneos.get(i).getTorneo().getId() == equipoTorneo.getTorneo().getId() && equiposTorneos.get(i).getEquipo().getId() == equipoTorneo.getEquipo().getId()) {
                equiposTorneos.set(i, equipoTorneo);
                actualizado = true;
                break;
            }
        }

        if (!actualizado) {
            equiposTorneos.add(equipoTorneo);
        }

        if (!GestorArchivo.getInstance().persistEquiposTorneos(equiposTorneos).getEstado()) {
            return new Respuesta(false, "No se pudo registrar el equipo en ese torneo", "Error al guardar objeto de mapeo");
        } else {
            return new Respuesta(true, "Deportes inscritos al torneo!", null);
        }
    }
    
  
}
