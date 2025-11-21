package com.tfi.empresa.main;

import com.tfi.empresa.config.DatabaseConnection;
import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Conexion establecida correctamente con MySQL");
        } catch (Exception e) {
            System.err.println("Error al conectar con MySQL: " + e.getMessage());
        }
    }
}
