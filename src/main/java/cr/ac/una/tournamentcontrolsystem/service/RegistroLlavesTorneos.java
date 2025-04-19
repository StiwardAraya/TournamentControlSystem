package cr.ac.una.tournamentcontrolsystem.service;

import cr.ac.una.tournamentcontrolsystem.model.LlavesTorneo;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RegistroLlavesTorneos {

    private List<LlavesTorneo> llavesTorneos;
    private static RegistroLlavesTorneos INSTANCE;
    static final Logger logger = Logger.getLogger(RegistroLlavesTorneos.class.getName());

    private RegistroLlavesTorneos() {
        Respuesta respuestaGetLlavesTorneos = getLlavesTorneos();
        if (respuestaGetLlavesTorneos.getEstado()) {
            llavesTorneos = (List<LlavesTorneo>) respuestaGetLlavesTorneos.getResultado("LlavesTorneos");
        } else {
            llavesTorneos = new ArrayList<>();
        }
    }

    public static RegistroLlavesTorneos getInstance() {
        if (INSTANCE == null) {
            synchronized (RegistroLlavesTorneos.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RegistroLlavesTorneos();
                }
            }
        }
        return INSTANCE;
    }

    public Respuesta getLlavesTorneos() {
        return GestorArchivo.getInstance().cargarLlavesTorneos();
    }

    public void cargarLlaves() {
        llavesTorneos = (List<LlavesTorneo>) GestorArchivo.getInstance().cargarLlavesTorneos().getResultado("LlavesTorneos");
    }

    public Respuesta buscarLlavesTorneo(int idTorneo) {
        for (LlavesTorneo llavesT : llavesTorneos) {
            if (llavesT.getIdTorneo() == idTorneo) {
                return new Respuesta(true, "Llaves de torneo encontradas con exito", "Llaves cargadas", "llaves", llavesT);
            }
        }

        return new Respuesta(false, "El Torneo no tiene llaves", "llaves no existentes");
    }

    public Respuesta guardarLlavesTorneo(LlavesTorneo llavesTorneo) {
        if (llavesTorneo == null) {
            return new Respuesta(false, "Objeto nulo", "El objeto LlavesTorneo es null");
        }
    
        boolean actualizado = false;
    
        for (int i = 0; i < llavesTorneos.size(); i++) {
            if (llavesTorneos.get(i).getIdTorneo() == llavesTorneo.getIdTorneo()) {
                llavesTorneos.set(i, llavesTorneo);
                actualizado = true;
                break;
            }
        }
    
        if (!actualizado) {
            llavesTorneos.add(llavesTorneo);
        }
    
        if (GestorArchivo.getInstance().persistLlavesTorneos(llavesTorneos).getEstado()) {
            String mensaje = actualizado ? "Llaves del torneo actualizadas" : "Llaves del torneo guardadas";
            return new Respuesta(true, mensaje, null);
        } else {
            return new Respuesta(false, "No se pudieron almacenar las llaves del torneo", "Error al guardar llaves");
        }
    }

}
