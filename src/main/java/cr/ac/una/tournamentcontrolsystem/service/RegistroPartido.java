package cr.ac.una.tournamentcontrolsystem.service;

import cr.ac.una.tournamentcontrolsystem.model.Partido;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import java.util.ArrayList;
import java.util.List;

public class RegistroPartido {
    private static RegistroPartido instance;

    private RegistroPartido() {}

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

        // Verificar si el partido ya existe para actualizarlo
        int index = -1;
        if (partido.getIdPartido() != 0) { // Asumimos que un ID 0 indica un nuevo partido
            for (int i = 0; i < partidos.size(); i++) {
                if (partidos.get(i).getIdPartido() == partido.getIdPartido()) {
                    index = i;
                    break;
                }
            }
        }

        if (index != -1) {
            // Actualizar el partido existente
            partidos.set(index, partido);
        } else {
            // Agregar el nuevo partido a la lista
            partidos.add(partido);
        }

        // Persistir la lista actualizada de partidos
        return GestorArchivo.getInstance().persistPartidos(partidos);
    }

    public Respuesta cargarPartidos() {
        return GestorArchivo.getInstance().cargarPartidos();
    }
}