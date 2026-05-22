package com.example.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionPostgresDatabase {

    private static final Properties properties = new Properties();

    static {
        cargarPropiedades();
    }

    private static void cargarPropiedades() {
        File[] rutas = {
                new File("config.properties"),
                new File("Jeanalex/config.properties")
        };

        for (File ruta : rutas) {
            if (ruta.isFile()) {
                try (FileInputStream configuracion = new FileInputStream(ruta)) {
                    properties.load(configuracion);
                    return;
                } catch (IOException exception) {
                    System.err.println("No se pudo cargar " + ruta.getPath() + ": " + exception.getMessage());
                }
            }
        }

        try (InputStream configuracion = ConexionPostgresDatabase.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (configuracion != null) {
                properties.load(configuracion);
                return;
            }
        } catch (IOException exception) {
            System.err.println("No se pudo cargar config.properties desde el classpath: " + exception.getMessage());
        }

        System.err.println("CRITICAL: Failed to load config.properties.");
    }

    public static Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");

        if (url == null || user == null) {
            throw new IllegalStateException(
                    "Base de datos configurada incorrectamente. Verifique que db.url y db.user esten presentes en config.properties."
            );
        }

        return DriverManager.getConnection(url, user, password);
    }
}
