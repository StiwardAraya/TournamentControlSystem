package cr.ac.una.tournamentcontrolsystem.service;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;


public class GestorArchivo {
    
    private File carpetaDeportes = new File("Datos Deportes");
    private File archivoDeportes = new File(carpetaDeportes, "DeportesData");
    private File carpetaEquipos = new File("Datos Equipos");
    private File archivoEquipos = new File(carpetaEquipos, "EquiposData");
    private static GestorArchivo instance;

    private GestorArchivo() {
        if (!carpetaDeportes.exists()) {
            carpetaDeportes.mkdirs();
        }
        if (!archivoDeportes.exists()) {
            try {
                archivoDeportes.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!carpetaEquipos.exists()) {
            carpetaEquipos.mkdirs();
        }
        if (!archivoEquipos.exists()) {
            try {
                archivoEquipos.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static GestorArchivo getInstance() {
        if (instance == null) {
            synchronized (GestorArchivo.class) {
                if (instance == null) {
                    instance = new GestorArchivo();
                }
            }
        }
        return instance;
    }
    
    public void guardarDeporteArchivo(List<Deporte> deportes) {
        Map<String, Integer> deportesMap = new HashMap<>();

        for (Deporte nuevoDeporte : deportes) {
            deportesMap.put(nuevoDeporte.getNombre(), nuevoDeporte.getId());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoDeportes))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(deportesMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<Deporte> cargarDeportes() {
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoDeportes))) {
            Gson gson = new Gson();
            return gson.fromJson(reader, new TypeToken<List<Deporte>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void eliminarDeporteArchivo(String nombreDeporte) {
        List<Deporte> deportes = cargarDeportes();
        if (deportes != null) {
            deportes.removeIf(deporte -> deporte.getNombre().equalsIgnoreCase(nombreDeporte));

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoDeportes))) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(deportes, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    
    public void guardarEquipoArchivo(List<Equipo> equipos) {
        Map<String, Integer> equiposMap = new HashMap<>();

        for (Equipo nuevoEquipo  : equipos) {
            equiposMap.put(nuevoEquipo.getNombre(), nuevoEquipo.getId());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoEquipos))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(equiposMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
