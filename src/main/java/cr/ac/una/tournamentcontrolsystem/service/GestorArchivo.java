package cr.ac.una.tournamentcontrolsystem.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import cr.ac.una.tournamentcontrolsystem.model.EquipoPartido;
import cr.ac.una.tournamentcontrolsystem.model.EquipoTorneo;
import cr.ac.una.tournamentcontrolsystem.model.LlavesTorneo;
import cr.ac.una.tournamentcontrolsystem.model.Partido;
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
    private File archivoPartidos = new File(carpetaData, "Partidos.json");
    private File archivoEquiposTorneos = new File(carpetaData, "EquiposTorneos.json");
    private File archivoEquiposPartidos = new File(carpetaData, "EquiposPartidos.json");
    private File archivoLlavesTorneos = new File(carpetaData, "LlavesTorneos.json");
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

        if (!archivoPartidos.exists()) {
            try {
                archivoPartidos.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error en GestorArchivo al crear el archivo JSON para Partidos", e);
            }
        }

        if (!archivoEquiposPartidos.exists()) {
            try {
                archivoEquiposPartidos.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error en GestorArchivo al crear el archivo JSON para EquiposPartidos", e);
            }
        }

        if (!archivoEquiposTorneos.exists()) {
            try {
                archivoEquiposTorneos.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error en GestorArchivo al crear el archivo JSON para EquiposTorneos", e);
            }
        }

        if (!archivoLlavesTorneos.exists()) {
            try {
                archivoLlavesTorneos.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error en GestorArchivo al crear el archivo JSON para LlavesTorneos", e);
            }
        }
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
    /**
     * Los metodos persist reciben una lista de objetos correspondientes al
     * modelo de datos y usa un gsonBuilder para transformar esa lista en un
     * documento formato JavaScript Object Notation y sobreescribe el archivo
     * .json almacenado.
     *
     * Retorna un objeto tipo respuesta con el paquete de datos correspondiente
     * al resultado de toda la operación
     *
     * @param deportes
     * @return Respuesta
     */
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

    public Respuesta persistPartidos(List<Partido> partidos) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String partidosJSON = gson.toJson(partidos);
        try (FileWriter writer = new FileWriter(archivoPartidos)) {
            writer.write(partidosJSON);
            return new Respuesta(true, "Partidos actualizados correctamente!", null);
        } catch (IOException e) {
            return new Respuesta(false, "Error al actualizar la lista de Partidos", e.getMessage());
        }
    }

    public Respuesta persistEquiposPartidos(List<EquipoPartido> equiposPartidos) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String equiposPartidosJSON = gson.toJson(equiposPartidos);
        try (FileWriter writer = new FileWriter(archivoEquiposPartidos)) {
            writer.write(equiposPartidosJSON);
            return new Respuesta(true, "EquiposPartidos actualizados correctamente!", null);
        } catch (IOException e) {
            return new Respuesta(false, "Error al actualizar la lista de EquiposPartidos", e.getMessage());
        }
    }

    public Respuesta persistEquiposTorneos(List<EquipoTorneo> equiposTorneos) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String equiposTorneosJSON = gson.toJson(equiposTorneos);
        try (FileWriter writer = new FileWriter(archivoEquiposTorneos)) {
            writer.write(equiposTorneosJSON);
            return new Respuesta(true, "EquiposTorneos actualizados correctamente!", null);
        } catch (IOException e) {
            return new Respuesta(false, "Error al actualizar la lista de EquiposTorneos", e.getMessage());
        }
    }

    public Respuesta persistLlavesTorneos(List<LlavesTorneo> llavesTorneos) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String llavesTorneosJSON = gson.toJson(llavesTorneos);
        try (FileWriter writer = new FileWriter(archivoLlavesTorneos)) {
            writer.write(llavesTorneosJSON);
            return new Respuesta(true, "LlavesTorneos actualizadas correctamente!", null);
        } catch (IOException e) {
            return new Respuesta(false, "Error al actualizar la lista de LlavesTorneos", e.getMessage());
        }
    }

    // METODOS CARGAR
    /**
     * Los métodos cargar inicializan una lista vacia del tipo correspondiente
     * al tipo de dato que se pretende cargar, usa un objeto Gson, un
     * BufferedReader y un FileReader para hacer toda la operación, además, usa
     * un objeto Type para que el objeto gson sepa el tipo de dato que está
     * leyendo, en estos casos, una Lista del tipo indicado, con todo esto carga
     * la lista en la lista vacia y la retorna dentro de una Respuesta
     *
     * @return Respuesta
     */
    public Respuesta cargarDeportes() {
        List<Deporte> deportes = new ArrayList<>();
        Gson gson = new Gson();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivoDeportes))) {
            Type listType = new TypeToken<List<Deporte>>() {
            }.getType();

            deportes = gson.fromJson(bufferedReader, listType); //carga la lista leyendo el archivo y considerando el tipo

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

    public Respuesta cargarPartidos() {
        List<Partido> partidos = new ArrayList<>();
        Gson gson = new Gson();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivoPartidos))) {
            Type listType = new TypeToken<List<Partido>>() {
            }.getType();

            partidos = gson.fromJson(bufferedReader, listType);
            if (partidos == null || partidos.isEmpty()) {
                return new Respuesta(false, "El archivo de Partidos se encuentra vacío", "No hay partidos registrados");
            } else {
                return new Respuesta(true, "Partidos cargados con exito!", null, "partidos", partidos);
            }
        } catch (IOException e) {
            return new Respuesta(false, "Error al leer el archivo de partidos", e.getMessage());
        } catch (Exception e) {
            return new Respuesta(false, "Error inesperado al cargar partidos", e.getMessage());
        }
    }

    public Respuesta cargarEquiposPartidos() {
        List<EquipoPartido> equiposPartidos = new ArrayList<>();
        Gson gson = new Gson();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivoEquiposPartidos))) {
            Type listType = new TypeToken<List<EquipoPartido>>() {
            }.getType();

            equiposPartidos = gson.fromJson(bufferedReader, listType);
            if (equiposPartidos == null || equiposPartidos.isEmpty()) {
                return new Respuesta(false, "El archivo de EquiposPartidos se encuentra vacío", "No hay EquiposPartidos registrados");
            } else {
                return new Respuesta(true, "EquiposPartidos cargados con exito!", null, "EquiposPartidos", equiposPartidos);
            }
        } catch (IOException e) {
            return new Respuesta(false, "Error al leer el archivo de EquiposPartidos", e.getMessage());
        } catch (Exception e) {
            return new Respuesta(false, "Error inesperado al cargar EquiposPartidos", e.getMessage());
        }
    }

    public Respuesta cargarEquiposTorneos() {
        List<EquipoTorneo> equiposTorneos = new ArrayList<>();
        Gson gson = new Gson();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivoEquiposTorneos))) {
            Type listType = new TypeToken<List<EquipoTorneo>>() {
            }.getType();

            equiposTorneos = gson.fromJson(bufferedReader, listType);
            if (equiposTorneos == null || equiposTorneos.isEmpty()) {
                return new Respuesta(false, "El archivo de EquiposTorneos se encuentra vacío", "No hay EquiposTorneos registrados");
            } else {
                return new Respuesta(true, "EquiposTorneos cargados con exito!", null, "EquiposTorneos", equiposTorneos);
            }
        } catch (IOException e) {
            return new Respuesta(false, "Error al leer el archivo de EquiposTorneos", e.getMessage());
        } catch (Exception e) {
            return new Respuesta(false, "Error inesperado al cargar EquiposTorneos", e.getMessage());
        }
    }

    public Respuesta cargarLlavesTorneos() {
        List<LlavesTorneo> llavesTorneos = new ArrayList<>();
        Gson gson = new Gson();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivoLlavesTorneos))) {
            Type listType = new TypeToken<List<LlavesTorneo>>() {
            }.getType();

            llavesTorneos = gson.fromJson(bufferedReader, listType);
            if (llavesTorneos == null || llavesTorneos.isEmpty()) {
                return new Respuesta(false, "El archivo de LlavesTorneos se encuentra vacío", "No hay LlavesTorneos registrados");
            } else {
                return new Respuesta(true, "LlavesTorneos cargados con exito!", null, "LlavesTorneos", llavesTorneos);
            }
        } catch (IOException e) {
            return new Respuesta(false, "Error al leer el archivo de LlavesTorneos", e.getMessage());
        } catch (Exception e) {
            return new Respuesta(false, "Error inesperado al cargar LlavesTorneos", e.getMessage());
        }
    }

}// Fin Gestor archivos
