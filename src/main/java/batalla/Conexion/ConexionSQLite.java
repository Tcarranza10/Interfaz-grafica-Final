package batalla.Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQLite {

     private static final String URL = "jdbc:sqlite:src/main/java/batalla/database/BDjuego.db";

    public static Connection conectar() {
        try {
            Class.forName("org.sqlite.JDBC"); // IMPORTANTE
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            System.out.println("Error al conectar a SQLite: " + e.getMessage());
            return null;
        }
    }
}

