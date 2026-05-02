package com.example.dao;

import com.example.config.ConexionPostgresDatabase;
import com.example.model.Dispositivo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DispositivoDao implements CrudDao<Dispositivo> {
    @Override
    public Dispositivo crear(Dispositivo dispositivo) throws SQLException {
        String sql = """
                INSERT INTO dispositivo (nombre, serial, marca, modelo, categoria, estado, ubicacion, fecha_ingreso)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            completarStatement(statement, dispositivo);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    dispositivo.setId(keys.getInt(1));
                }
            }
        }

        return dispositivo;
    }

    @Override
    public Optional<Dispositivo> buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM dispositivo WHERE id = ?";

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapear(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Dispositivo> listar() throws SQLException {
        String sql = "SELECT * FROM dispositivo ORDER BY nombre";
        List<Dispositivo> dispositivos = new ArrayList<>();

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                dispositivos.add(mapear(resultSet));
            }
        }

        return dispositivos;
    }

    @Override
    public boolean actualizar(Dispositivo dispositivo) throws SQLException {
        String sql = """
                UPDATE dispositivo
                SET nombre = ?, serial = ?, marca = ?, modelo = ?, categoria = ?, estado = ?, ubicacion = ?, fecha_ingreso = ?
                WHERE id = ?
                """;

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            completarStatement(statement, dispositivo);
            statement.setInt(9, dispositivo.getId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM dispositivo WHERE id = ?";

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private void completarStatement(PreparedStatement statement, Dispositivo dispositivo) throws SQLException {
        statement.setString(1, dispositivo.getNombre());
        statement.setString(2, dispositivo.getSerial());
        statement.setString(3, dispositivo.getMarca());
        statement.setString(4, dispositivo.getModelo());
        statement.setString(5, dispositivo.getCategoria());
        statement.setString(6, dispositivo.getEstado());
        statement.setString(7, dispositivo.getUbicacion());
        statement.setDate(8, dispositivo.getFechaIngreso() == null ? null : Date.valueOf(dispositivo.getFechaIngreso()));
    }

    private Dispositivo mapear(ResultSet resultSet) throws SQLException {
        Dispositivo dispositivo = new Dispositivo();
        dispositivo.setId(resultSet.getInt("id"));
        dispositivo.setNombre(resultSet.getString("nombre"));
        dispositivo.setSerial(resultSet.getString("serial"));
        dispositivo.setMarca(resultSet.getString("marca"));
        dispositivo.setModelo(resultSet.getString("modelo"));
        dispositivo.setCategoria(resultSet.getString("categoria"));
        dispositivo.setEstado(resultSet.getString("estado"));
        dispositivo.setUbicacion(resultSet.getString("ubicacion"));

        Date fechaIngreso = resultSet.getDate("fecha_ingreso");
        if (fechaIngreso != null) {
            dispositivo.setFechaIngreso(fechaIngreso.toLocalDate());
        }

        return dispositivo;
    }
}
