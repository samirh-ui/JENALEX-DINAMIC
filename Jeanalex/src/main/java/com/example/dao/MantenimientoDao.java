package com.example.dao;

import com.example.config.ConexionPostgresDatabase;
import com.example.model.Mantenimiento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MantenimientoDao {
    public Mantenimiento crear(Mantenimiento mantenimiento) throws SQLException {
        String sql = """
                INSERT INTO mantenimiento (id_dispositivo, id_tecnico, tipo, descripcion, estado, fecha_programada, fecha_ejecucion, observaciones)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            completar(statement, mantenimiento);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    mantenimiento.setId(keys.getInt(1));
                }
            }
        }
        return mantenimiento;
    }

    public boolean actualizarEstado(int id, String estado, String observaciones) throws SQLException {
        String sql = """
                UPDATE mantenimiento
                SET estado = ?, observaciones = ?,
                    fecha_ejecucion = CASE WHEN LOWER(?) IN ('finalizado', 'finalizada', 'cerrado', 'cerrada') THEN CURRENT_TIMESTAMP ELSE fecha_ejecucion END
                WHERE id = ?
                """;
        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, estado);
            statement.setString(2, observaciones);
            statement.setString(3, estado);
            statement.setInt(4, id);
            return statement.executeUpdate() > 0;
        }
    }

    public List<Mantenimiento> listar() throws SQLException {
        String sql = """
                SELECT id, id_dispositivo, id_tecnico, tipo, descripcion, estado, fecha_programada, fecha_ejecucion, observaciones
                FROM mantenimiento
                ORDER BY fecha_programada DESC
                """;
        List<Mantenimiento> mantenimientos = new ArrayList<>();
        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                mantenimientos.add(mapear(resultSet));
            }
        }
        return mantenimientos;
    }

    private void completar(PreparedStatement statement, Mantenimiento mantenimiento) throws SQLException {
        statement.setInt(1, mantenimiento.getIdDispositivo());
        statement.setInt(2, mantenimiento.getIdTecnico());
        statement.setString(3, mantenimiento.getTipo());
        statement.setString(4, mantenimiento.getDescripcion());
        statement.setString(5, mantenimiento.getEstado());
        statement.setTimestamp(6, Timestamp.valueOf(mantenimiento.getFechaProgramada()));
        statement.setTimestamp(7, mantenimiento.getFechaEjecucion() == null ? null : Timestamp.valueOf(mantenimiento.getFechaEjecucion()));
        statement.setString(8, mantenimiento.getObservaciones());
    }

    private Mantenimiento mapear(ResultSet resultSet) throws SQLException {
        Mantenimiento mantenimiento = new Mantenimiento();
        mantenimiento.setId(resultSet.getInt("id"));
        mantenimiento.setIdDispositivo(resultSet.getInt("id_dispositivo"));
        mantenimiento.setIdTecnico(resultSet.getInt("id_tecnico"));
        mantenimiento.setTipo(resultSet.getString("tipo"));
        mantenimiento.setDescripcion(resultSet.getString("descripcion"));
        mantenimiento.setEstado(resultSet.getString("estado"));
        mantenimiento.setFechaProgramada(resultSet.getTimestamp("fecha_programada").toLocalDateTime());
        Timestamp fechaEjecucion = resultSet.getTimestamp("fecha_ejecucion");
        mantenimiento.setFechaEjecucion(fechaEjecucion == null ? null : fechaEjecucion.toLocalDateTime());
        mantenimiento.setObservaciones(resultSet.getString("observaciones"));
        return mantenimiento;
    }
}
