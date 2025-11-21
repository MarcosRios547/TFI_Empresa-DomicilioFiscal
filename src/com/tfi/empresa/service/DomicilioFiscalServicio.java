package com.tfi.empresa.service;

import com.tfi.empresa.config.DatabaseConnection;
import com.tfi.empresa.dao.DomicilioFiscalDao;
import com.tfi.empresa.entities.DomicilioFiscal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DomicilioFiscalServicio {

    private final DomicilioFiscalDao domicilioDao = new DomicilioFiscalDao();

    public boolean crear(DomicilioFiscal d) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            domicilioDao.crear(d, conn);
            conn.commit();
            System.out.println("Domicilio Fiscal creado correctamente (ID: " + d.getId() + ")");
            return true;
        } catch (SQLException e) {
            System.err.println("Error al crear domicilio fiscal: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Se realizo ROLLBACK de la transaccion de domicilio.");
                } catch (SQLException rbe) {
                    System.err.println("Error al hacer rollback: " + rbe.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignore) {}
        }
    }

    public DomicilioFiscal leerPorId(Long id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return domicilioDao.leerPorId(id, conn);
        } catch (SQLException e) {
            System.err.println("Error al leer domicilio fiscal: " + e.getMessage());
            return null;
        }
    }

    public List<DomicilioFiscal> listarTodos() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return domicilioDao.leerTodos(conn);
        } catch (SQLException e) {
            System.err.println("Error al listar domicilios fiscales: " + e.getMessage());
            return List.of();
        }
    }

    public boolean actualizar(DomicilioFiscal d) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            domicilioDao.actualizar(d, conn);
            conn.commit();
            System.out.println("Domicilio Fiscal actualizado correctamente.");
            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar domicilio fiscal: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Se realizo ROLLBACK de la transaccion de actualizacion de domicilio.");
                } catch (SQLException rbe) {
                    System.err.println("Error al hacer rollback: " + rbe.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignore) {}
        }
    }

    public boolean eliminar(Long id) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            domicilioDao.eliminar(id, conn);
            conn.commit();
            System.out.println("Domicilio Fiscal eliminado logicamente.");
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar domicilio fiscal: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Se realizo ROLLBACK de la transaccion de eliminacion de domicilio.");
                } catch (SQLException rbe) {
                    System.err.println("Error al hacer rollback: " + rbe.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignore) {}
        }
    }
}
