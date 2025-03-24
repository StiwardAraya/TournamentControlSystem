package cr.ac.una.tournamentcontrolsystem.util;

import cr.ac.una.tournamentcontrolsystem.service.GestorArchivo;
import cr.ac.una.tournamentcontrolsystem.service.RegistroDeporte;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author Carlos Carranza
 */
public class AppContext {

    private static AppContext INSTANCE = null;
    private static HashMap<String, Object> context = new HashMap<>();
    private RegistroDeporte registroDeporte;
    private GestorArchivo gestorArchivo;

     
    private AppContext() {
        //cargarPropiedades();
        registroDeporte = RegistroDeporte.getInstance();
        gestorArchivo = GestorArchivo.getInstance();
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (AppContext.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppContext();
                }
            }
        }
    }

    public static AppContext getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }
    
    private void cargarPropiedades(){
        try {
            FileInputStream configFile;
            configFile = new FileInputStream("config/properties.ini");
            Properties appProperties = new Properties();
            appProperties.load(configFile);
            configFile.close();
//            if (appProperties.getProperty("propiedades.rutalog") != null) {
//                this.set("rutalog",appProperties.getProperty("propiedades.rutalog"));
//            }
//            if (appProperties.getProperty("propiedades.resturl") != null) {
//                this.set("resturl",appProperties.getProperty("propiedades.resturl"));
  //          }
        } catch (IOException io) {
            System.out.println("Archivo de configuración no encontrado.");
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public Object get(String parameter){    
        return context.get(parameter);
    }

    public void set(String nombre, Object valor) {
        context.put(nombre, valor);
    }

    public void delete(String parameter) {
        context.put(parameter, null);
    }

}
