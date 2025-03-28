package cr.ac.una.tournamentcontrolsystem.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import cr.ac.una.tournamentcontrolsystem.model.Torneo;
import cr.ac.una.tournamentcontrolsystem.util.Respuesta;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase de gestión de archivos, carga las listas de datos desde los archivos
 * .json y actualiza los .json con los cambios realizados en el programa durante
 * su uso.
 *
 * Obedece a un patrón de diseño Singleton
 *
 * @author Stiward Araya C.
 * @author Angie Marks S.
 * @author Kevin Calderón Z.
 */
public class GestorArchivo {

    private File carpetaData = new File("Data");
    private File archivoDeportes = new File(carpetaData, "Deportes.json");
    private File archivoEquipos = new File(carpetaData, "Equipos.json");
    private File archivoTorneos = new File(carpetaData, "Torneos.json");
    static final Logger logger = Logger.getLogger(GestorArchivo.class.getName());
    private static GestorArchivo INSTANCE;

    /**
     * Constructor de gestor de archivos, revisa si existen la carpeta y los
     * archivos .json y los crea en caso de que no existan.
     */
    private GestorArchivo() {
        if (!carpetaData.exists()) {
            carpetaData.mkdirs();
        }

        if (!archivoDeportes.exists()) {
            try {
                archivoDeportes.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error en GestorArchivo al crear el archivo JSON para Deportes", e);
            }
        }

        if (!archivoEquipos.exists()) {
            try {
                archivoEquipos.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error en GestorArchivo al crear el archivo JSON para Equipos", e);
            }
        }

        if (!archivoTorneos.exists()) {
            try {
                archivoTorneos.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error en GestorArchivo al crear el archivo JSON para Torneos", e);
            }
        }

        // TODO: condiciones para archivos partido, equipoTorneo, equipoPartido
    }

    public static GestorArchivo getInstance() {
        if (INSTANCE == null) {
            synchronized (GestorArchivo.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GestorArchivo();
                }
            }
        }
        return INSTANCE;
    }

    // METODOS PERSIST
    public Respuesta persistDeportes(List<Deporte> deportes) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String deportesJSON = gson.toJson(deportes);
        try (FileWriter writer = new FileWriter(archivoDeportes)) {
            writer.write(deportesJSON);
            return new Respuesta(true, "Deportes actualizados correctamente!", null);
        } catch (IOException e) {
            return new Respuesta(false, "Error al actualizar la lista de Deportes", e.getMessage());
        }
    }

    public Respuesta persistEquipos(List<Equipo> equipos) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String equiposJSON = gson.toJson(equipos);
        try (FileWriter writer = new FileWriter(archivoEquipos)) {
            writer.write(equiposJSON);
            return new Respuesta(true, "Equipos actualizados correctamente!", null);
        } catch (IOException e) {
            return new Respuesta(false, "Error al actualizar la lista de Equipos", e.getMessage());
        }
    }

    public Respuesta persistTorneos(List<Torneo> torneos) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String torneosJSON = gson.toJson(torneos);
        try (FileWriter writer = new FileWriter(archivoTorneos)) {
            writer.write(torneosJSON);
            return new Respuesta(true, "Torneos actualizados correctamente!", null);
        } catch (IOException e) {
            return new Respuesta(false, "Error al actualizar la lista de Torneos", e.getMessage());
        }
    }

    // TODO: persistEquipoTorneo
    // TODO: persistEquipoPartido
    // METODOS CARGAR
    public Respuesta cargarDeportes() {
        List<Deporte> deportes = new ArrayList<>();
        Gson gson = new Gson();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivoDeportes))) {
            /*
                Para que el reader del archivo sepa que tipo de objeto esta leyendo,
                Se crea un objeto tipo Type
             */
            Type listType = new TypeToken<List<Deporte>>() {
            }.getType();

            deportes = gson.fromJson(bufferedReader, listType); //carga la lista leyendo el archivo

            if (deportes == null || deportes.isEmpty()) {
                return new Respuesta(false, "El archivo de Deportes se encuentra vacío", "No hay deportes registrados");
            } else {
                return new Respuesta(true, "Deportes cargados con exito!", null, "deportes", deportes);
            }
        } catch (IOException e) {
            return new Respuesta(false, "Error al leer el archivo de deportes", e.getMessage());
        } catch (Exception e) {
            return new Respuesta(false, "Error inesperado al cargar deportes", e.getMessage());
        }
    }

    public Respuesta cargarEquipos() {
        List<Equipo> equipos = new ArrayList<>();
        Gson gson = new Gson();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivoEquipos))) {
            Type listType = new TypeToken<List<Equipo>>() {
            }.getType();

            equipos = gson.fromJson(bufferedReader, listType);
            if (equipos == null || equipos.isEmpty()) {
                return new Respuesta(false, "El archivo de Equipos se encuentra vacío", "No hay equipos registrados");
            } else {
                return new Respuesta(true, "Equipos cargados con exito!", null, "equipos", equipos);
            }
        } catch (IOException e) {
            return new Respuesta(false, "Error al leer el archivo de equipos", e.getMessage());
        } catch (Exception e) {
            return new Respuesta(false, "Error inesperado al cargar equipos", e.getMessage());
        }
    }

    public Respuesta cargarTorneos() {
        List<Torneo> torneos = new ArrayList<>();
        Gson gson = new Gson();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivoTorneos))) {
            Type listType = new TypeToken<List<Torneo>>() {
            }.getType();

            torneos = gson.fromJson(bufferedReader, listType);
            if (torneos == null || torneos.isEmpty()) {
                return new Respuesta(false, "El archivo de Torneos se encuentra vacío", "No hay torneos registrados");
            } else {
                return new Respuesta(true, "Torneos cargados con exito!", null, "torneos", torneos);
            }
        } catch (IOException e) {
            return new Respuesta(false, "Error al leer el archivo de torneos", e.getMessage());
        } catch (Exception e) {
            return new Respuesta(false, "Error inesperado al cargar torneos", e.getMessage());
        }
    }

    // TODO: cargarRelacionesEquipoTorneo
    // TODO: cargarRelacionesEquipoPartido
}// Fin Gestor archivos
