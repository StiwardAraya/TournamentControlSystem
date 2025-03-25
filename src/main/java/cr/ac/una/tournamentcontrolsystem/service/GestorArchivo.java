package cr.ac.una.tournamentcontrolsystem.service;

import cr.ac.una.tournamentcontrolsystem.model.Deporte;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import cr.ac.una.tournamentcontrolsystem.model.Equipo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class GestorArchivo {
    
    private File carpetaDeportes = new File("Datos Deportes");
    private File archivoDeportes = new File(carpetaDeportes, "Deportes");
    private File carpetaEquipos = new File("Datos Equipos");
    private File archivoEquipos = new File(carpetaEquipos, "Equipos");
    private static GestorArchivo instance;
    
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
    
    public List<Deporte> cargarDeportes() {
      Gson gson = new Gson();
      List<Deporte> deportes = null;

      try (BufferedReader reader = new BufferedReader(new FileReader(archivoDeportes))) {
          Type deporteListType = new TypeToken<List<Deporte>>() {}.getType();
          deportes = gson.fromJson(reader, deporteListType);
      } catch (IOException e) {
          e.printStackTrace();
      }

      return deportes;
    }

    public void guardarDeporteArchivo(List<Deporte> deportes) {
      Gson gson = new Gson();
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoDeportes))) {
          String json = gson.toJson(deportes);
          writer.write(json);
      } catch (IOException e) {
          e.printStackTrace();
      }
    }
    
    //Método innecesario
    //public void eliminarDeporteArchivo(int idDeporte) {
     //List<Deporte> deportes = cargarDeportes(); 
     //if (deportes != null) {
       //  deportes.removeIf(deporte -> deporte.getIdDeporte() == idDeporte);

         //guardarDeporteArchivo(deportes);
     //} else {
        // System.out.println("No se encontraron deportes para eliminar.");
     //}
 //}

    
   public List<Equipo> cargarDEquipos() {
      Gson gson = new Gson();
      List<Equipo> equipos = null;

      try (BufferedReader reader = new BufferedReader(new FileReader(archivoEquipos))) {
          Type equipoListType = new TypeToken<List<Deporte>>() {}.getType();
          equipos = gson.fromJson(reader, equipoListType);
      } catch (IOException e) {
          e.printStackTrace();
      }

      return equipos;
    }

    public void guardarEquipoArchivo(List<Equipo> equipos) {
      Gson gson = new Gson();
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoEquipos))) {
          String json = gson.toJson(equipos);
          writer.write(json);
      } catch (IOException e) {
          e.printStackTrace();
      }
    }
}
