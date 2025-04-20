package cr.ac.una.tournamentcontrolsystem.service;

import cr.ac.una.tournamentcontrolsystem.model.Partido;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import java.util.ArrayList;
import java.util.List;

public class RegistroPartido {

    private static RegistroPartido instance;

    private RegistroPartido() {
    }

    public static RegistroPartido getInstance() {
        if (instance == null) {
            synchronized (RegistroPartido.class) {
                if (instance == null) {
                    instance = new RegistroPartido();
                }
            }
        }
        return instance;
    }

    public Respuesta guardarPartido(Partido partido) {
        Respuesta respuestaCargar = GestorArchivo.getInstance().cargarPartidos();
        List<Partido> partidos = new ArrayList<>();

        if (respuestaCargar.getEstado()) {
            partidos = (List<Partido>) respuestaCargar.getResultado("partidos");
        }

        int index = -1;
        if (partido.getIdPartido() != 0) {
            for (int i = 0; i < partidos.size(); i++) {
                if (partidos.get(i).getIdPartido() == partido.getIdPartido()) {
                    index = i;
                    break;
                }
            }
        }

        if (index != -1) {
            partidos.set(index, partido);
        } else {
            partido.setIdPartido(getLastId(partidos) + 1);
            partidos.add(partido);
        }

        return GestorArchivo.getInstance().persistPartidos(partidos);
    }

    public Respuesta cargarPartidos() {
        return GestorArchivo.getInstance().cargarPartidos();
    }

    private int getLastId(List<Partido> partidos) {
        if (partidos != null && !partidos.isEmpty()) {
            return partidos.getLast().getIdPartido();
        }
        return -1;
    }
}
