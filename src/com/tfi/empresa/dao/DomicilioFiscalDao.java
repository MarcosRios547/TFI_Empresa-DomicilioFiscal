package com.tfi.empresa.dao;

import com.tfi.empresa.entities.DomicilioFiscal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DomicilioFiscalDao implements GenericDao<DomicilioFiscal> {

    @Override
    public void crear(DomicilioFiscal d, Connection conn) throws SQLException {
        String sql = """
            INSERT INTO domicilio_fiscal (eliminado, calle, numero, ciudad, provincia, codigo_postal, pais)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, d.isEliminado());
            ps.setString(2, d.getCalle());
            ps.setObject(3, d.getNumero());
            ps.setString(4, d.getCiudad());
            ps.setString(5, d.getProvincia());
            ps.setString(6, d.getCodigoPostal());
            ps.setString(7, d.getPais());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                d.setId(rs.getLong(1));
            }
        }
    }

    @Override
    public DomicilioFiscal leerPorId(Long id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM domicilio_fiscal WHERE id = ? AND eliminado = FALSE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        }
        return null;
    }

    @Override
    public List<DomicilioFiscal> leerTodos(Connection conn) throws SQLException {
        List<DomicilioFiscal> lista = new ArrayList<>();
        String sql = "SELECT * FROM domicilio_fiscal WHERE eliminado = FALSE";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }
        }
        return lista;
    }

    @Override
    public void actualizar(DomicilioFiscal d, Connection conn) throws SQLException {
        String sql = """
            UPDATE domicilio_fiscal
            SET calle=?, numero=?, ciudad=?, provincia=?, codigo_postal=?, pais=?
            WHERE id=? AND eliminado=FALSE
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getCalle());
            ps.setObject(2, d.getNumero());
            ps.setString(3, d.getCiudad());
            ps.setString(4, d.getProvincia());
            ps.setString(5, d.getCodigoPostal());
            ps.setString(6, d.getPais());
            ps.setLong(7, d.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(Long id, Connection conn) throws SQLException {
        String sql = "UPDATE domicilio_fiscal SET eliminado = TRUE WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private DomicilioFiscal mapResultSet(ResultSet rs) throws SQLException {
        DomicilioFiscal d = new DomicilioFiscal();
        d.setId(rs.getLong("id"));
        d.setEliminado(rs.getBoolean("eliminado"));
        d.setCalle(rs.getString("calle"));
        d.setNumero(rs.getInt("numero"));
        d.setCiudad(rs.getString("ciudad"));
        d.setProvincia(rs.getString("provincia"));
        d.setCodigoPostal(rs.getString("codigo_postal"));
        d.setPais(rs.getString("pais"));
        return d;
    }
}
