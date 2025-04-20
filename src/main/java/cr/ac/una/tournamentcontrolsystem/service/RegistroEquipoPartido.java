package cr.ac.una.tournamentcontrolsystem.service;

import cr.ac.una.tournamentcontrolsystem.model.EquipoPartido;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RegistroEquipoPartido {

    private List<EquipoPartido> equiposPartido;
    private static RegistroEquipoPartido INSTANCE;
    static final Logger logger = Logger.getLogger(RegistroEquipoPartido.class.getName());

    private RegistroEquipoPartido() {
        Respuesta respuestaGetEquiposPartidos = getEquiposPartido();
        if (respuestaGetEquiposPartidos.getEstado()) {
            equiposPartido = (List<EquipoPartido>) respuestaGetEquiposPartidos.getResultado("EquiposPartidos");
        } else {
            equiposPartido = new ArrayList<>();
        }
    }

    public static RegistroEquipoPartido getInstance() {
        if (INSTANCE == null) {
            synchronized (RegistroEquipoPartido.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RegistroEquipoPartido();
                }
            }
        }
        return INSTANCE;
    }

    public Respuesta getEquiposPartido() {
        return GestorArchivo.getInstance().cargarEquiposPartidos();
    }

    public Respuesta guardarEquipoPartido(EquipoPartido equipoPartido) {
        if (equiposPartido != null && equipoPartido != null) {
            equiposPartido.add(equipoPartido);
            if (!GestorArchivo.getInstance().persistEquiposPartidos(equiposPartido).getEstado()) {
                return new Respuesta(false, "No se pudo registrar el equipo en ese partido", "Error al guardar objeto de mapeo");
            } else {
                return new Respuesta(true, "partido guardado!", null);
            }
        }
        return new Respuesta(false, "Ocurrió un error al guardar el objeto de mapeo", "lista de equipos o parámetro nulos");
    }
}
