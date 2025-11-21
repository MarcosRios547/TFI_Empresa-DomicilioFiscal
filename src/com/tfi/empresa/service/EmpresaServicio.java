package com.tfi.empresa.service;

import com.tfi.empresa.config.DatabaseConnection;
import com.tfi.empresa.dao.EmpresaDao;
import com.tfi.empresa.entities.Empresa;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EmpresaServicio {

    private final EmpresaDao empresaDao = new EmpresaDao();

    public boolean crear(Empresa e) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            Empresa existente = empresaDao.leerPorCuit(e.getCuit(), conn);
            if (existente != null) {
                System.out.println("Ya existe una empresa con ese CUIT (ID: " + existente.getId() + "). Operacion cancelada.");
                return false;
            }

            empresaDao.crear(e, conn);
            conn.commit();
            System.out.println("Empresa creada correctamente (ID: " + e.getId() + ")");
            return true;
        } catch (SQLException ex) {
            System.err.println("Error al crear empresa: " + ex.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Se realizo ROLLBACK de la transaccion de creacion.");
                } catch (SQLException rbe) {
                    System.err.println("Error al hacer rollback: " + rbe.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignore) {}
        }
    }

    public Empresa leerPorId(Long id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return empresaDao.leerPorId(id, conn);
        } catch (SQLException ex) {
            System.err.println("Error al leer empresa por ID: " + ex.getMessage());
            return null;
        }
    }

    public List<Empresa> listarTodas() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return empresaDao.leerTodos(conn);
        } catch (SQLException ex) {
            System.err.println("Error al listar empresas: " + ex.getMessage());
            return List.of();
        }
    }

    public boolean actualizar(Empresa e) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            Empresa exists = empresaDao.leerPorId(e.getId(), conn);
            if (exists == null) {
                System.out.println("No existe la empresa con ID: " + e.getId());
                return false;
            }

            if (!e.getCuit().equalsIgnoreCase(exists.getCuit())) {
                Empresa conflicto = empresaDao.leerPorCuit(e.getCuit(), conn);
                if (conflicto != null && !conflicto.getId().equals(e.getId())) {
                    System.out.println("El CUIT ingresado ya pertenece a otra empresa (ID: " + conflicto.getId() + ").");
                    return false;
                }
            }

            empresaDao.actualizar(e, conn);
            conn.commit();
            System.out.println("Empresa actualizada correctamente.");
            return true;
        } catch (SQLException ex) {
            System.err.println("Error al actualizar empresa: " + ex.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Se realizo ROLLBACK de la transaccion de actualizacion.");
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

            Empresa exists = empresaDao.leerPorId(id, conn);
            if (exists == null) {
                System.out.println("No existe la empresa con ID: " + id);
                return false;
            }

            empresaDao.eliminar(id, conn);
            conn.commit();
            System.out.println("Empresa eliminada logicamente.");
            return true;
        } catch (SQLException ex) {
            System.err.println("Error al eliminar empresa: " + ex.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Se realizo ROLLBACK de la transaccion de eliminacion.");
                } catch (SQLException rbe) {
                    System.err.println("Error al hacer rollback: " + rbe.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignore) {}
        }
    }

    public Empresa buscarPorCuit(String cuit) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return empresaDao.leerPorCuit(cuit, conn);
        } catch (SQLException ex) {
            System.err.println("Error al buscar por CUIT: " + ex.getMessage());
            return null;
        }
    }

    public List<Empresa> buscarPorRazon(String razon) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return empresaDao.leerPorRazonSocial(razon, conn);
        } catch (SQLException ex) {
            System.err.println("Error al buscar por razón social: " + ex.getMessage());
            return List.of();
        }
    }
}
