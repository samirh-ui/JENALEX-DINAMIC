package com.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConexionPostgresDatabase {

    private static final Properties properties = new Properties();

    static {
        // Cargamos las propiedades una sola vez al inicio de la aplicación
        try (FileInputStream configuracion = new FileInputStream(new File("config.properties"))) {
            properties.load(configuracion);
        } catch (IOException e) {
            System.err.println("CRITICAL: Failed to load config.properties. " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
   
        // Definir los parámetros de conexión
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");
    
        if (url == null || user == null) {
            throw new IllegalStateException("Base de datos configurada incorrectamente. Verifique que db.url y db.user estén presentes en config.properties.");
        }
        
        return DriverManager.getConnection(url, user, password);         
    }

}