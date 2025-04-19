package cr.ac.una.tournamentcontrolsystem.service;

import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import cr.ac.una.tournamentcontrolsystem.model.EquipoTorneo;
import cr.ac.una.tournamentcontrolsystem.model.Torneo;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;

public class RegistroEquipoTorneo {

    private List<EquipoTorneo> equiposTorneos;
    private static RegistroEquipoTorneo INSTANCE;
    static final Logger logger = Logger.getLogger(RegistroEquipoTorneo.class.getName());

    private RegistroEquipoTorneo(){
        Respuesta respuestaGetEquiposTorneos = getEquiposTorneos();
        if (respuestaGetEquiposTorneos.getEstado()) {
            equiposTorneos = (List<EquipoTorneo>) respuestaGetEquiposTorneos.getResultado("EquiposTorneos");
        } else {
            equiposTorneos = new ArrayList<>();
        }
    }

    public static RegistroEquipoTorneo getInstance(){
        if (INSTANCE == null) {
            synchronized (RegistroEquipoTorneo.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RegistroEquipoTorneo();
                }
            }
        }
        return INSTANCE;
    }

    public Respuesta getEquiposTorneos(){
        return GestorArchivo.getInstance().cargarEquiposTorneos();
    }

    public Respuesta guardarEquipoTorneo(EquipoTorneo equipoTorneo){
        if (equiposTorneos != null && equipoTorneo != null) {
            for (EquipoTorneo et : equiposTorneos){
                if (et.getEquipo().getId() == equipoTorneo.getEquipo().getId() && et.getTorneo().getId() == equipoTorneo.getTorneo().getId()) {
                    return new Respuesta(false, "Ese equipo ya se encuentra en este torneo", "Objeto repetido");
                }
            }
        }

        equiposTorneos.add(equipoTorneo);
        if (!GestorArchivo.getInstance().persistEquiposTorneos(equiposTorneos).getEstado()) {
            return new Respuesta(false, "No se pudo registrar el equipo en ese torneo", "Error al guardar objeto de mapeo");
        } else {
            return new Respuesta(true, "Deportes inscritos al torneo!", null);
        }
    }
    
  
}
