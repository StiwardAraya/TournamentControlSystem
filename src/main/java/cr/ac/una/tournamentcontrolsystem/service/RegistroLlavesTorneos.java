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

    public Respuesta guardarLlavesTorneo(LlavesTorneo LlavesTorneo) {
        if (LlavesTorneo != null) {
            llavesTorneos.add(LlavesTorneo);
        }

        if (GestorArchivo.getInstance().persistLlavesTorneos(llavesTorneos).getEstado()) {
            return new Respuesta(true, "Llaves de torneo guardadas", null);
        } else {
            return new Respuesta(false, "No se pudieron almacenar las llaves del torneo", "error al guardar llaves");
        }
    }
    
}
