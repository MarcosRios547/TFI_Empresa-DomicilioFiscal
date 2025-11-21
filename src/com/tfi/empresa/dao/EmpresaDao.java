package com.tfi.empresa.dao;

import com.tfi.empresa.entities.Empresa;
import com.tfi.empresa.entities.DomicilioFiscal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDao implements GenericDao<Empresa> {

    private final DomicilioFiscalDao domicilioDao = new DomicilioFiscalDao();

    @Override
    public void crear(Empresa e, Connection conn) throws SQLException {

        domicilioDao.crear(e.getDomicilioFiscal(), conn);

        String sql = """
            INSERT INTO empresa (eliminado, razon_social, cuit, actividad_principal, email, domicilio_id)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, e.isEliminado());
            ps.setString(2, e.getRazonSocial());
            ps.setString(3, e.getCuit());
            ps.setString(4, e.getActividadPrincipal());
            ps.setString(5, e.getEmail());
            ps.setLong(6, e.getDomicilioFiscal().getId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    e.setId(rs.getLong(1));
                }
            }
        }
    }

    @Override
    public Empresa leerPorId(Long id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM empresa WHERE id = ? AND eliminado = FALSE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Empresa e = mapResultSet(rs);
                    long domicilioId = rs.getLong("domicilio_id");
                    DomicilioFiscal d = domicilioDao.leerPorId(domicilioId, conn);
                    e.setDomicilioFiscal(d);
                    return e;
                }
            }
        }
        return null;
    }

    @Override
    public List<Empresa> leerTodos(Connection conn) throws SQLException {
        List<Empresa> lista = new ArrayList<>();
        String sql = "SELECT * FROM empresa WHERE eliminado = FALSE";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Empresa e = mapResultSet(rs);
                long domicilioId = rs.getLong("domicilio_id");
                DomicilioFiscal d = domicilioDao.leerPorId(domicilioId, conn);
                e.setDomicilioFiscal(d);
                lista.add(e);
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Empresa e, Connection conn) throws SQLException {
        domicilioDao.actualizar(e.getDomicilioFiscal(), conn);

        String sql = """
            UPDATE empresa
            SET razon_social=?, cuit=?, actividad_principal=?, email=?
            WHERE id=? AND eliminado=FALSE
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getRazonSocial());
            ps.setString(2, e.getCuit());
            ps.setString(3, e.getActividadPrincipal());
            ps.setString(4, e.getEmail());
            ps.setLong(5, e.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(Long id, Connection conn) throws SQLException {
        String sql = "UPDATE empresa SET eliminado = TRUE WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public Empresa leerPorCuit(String cuit, Connection conn) throws SQLException {
        String sql = "SELECT * FROM empresa WHERE cuit = ? AND eliminado = FALSE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cuit);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Empresa e = mapResultSet(rs);
                    long domicilioId = rs.getLong("domicilio_id");
                    DomicilioFiscal d = domicilioDao.leerPorId(domicilioId, conn);
                    e.setDomicilioFiscal(d);
                    return e;
                }
            }
        }
        return null;
    }

    public List<Empresa> leerPorRazonSocial(String razon, Connection conn) throws SQLException {
        List<Empresa> lista = new ArrayList<>();
        String sql = "SELECT * FROM empresa WHERE LOWER(razon_social) LIKE ? AND eliminado = FALSE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + razon.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Empresa e = mapResultSet(rs);
                    long domicilioId = rs.getLong("domicilio_id");
                    DomicilioFiscal d = domicilioDao.leerPorId(domicilioId, conn);
                    e.setDomicilioFiscal(d);
                    lista.add(e);
                }
            }
        }
        return lista;
    }

    public DomicilioFiscalDao getDomicilioDao() {
        return domicilioDao;
    }

    private Empresa mapResultSet(ResultSet rs) throws SQLException {
        Empresa e = new Empresa();
        e.setId(rs.getLong("id"));
        e.setEliminado(rs.getBoolean("eliminado"));
        e.setRazonSocial(rs.getString("razon_social"));
        e.setCuit(rs.getString("cuit"));
        e.setActividadPrincipal(rs.getString("actividad_principal"));
        e.setEmail(rs.getString("email"));
        return e;
    }
}
