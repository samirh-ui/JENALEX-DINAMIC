package com.example.dao;

import com.example.config.ConexionPostgresDatabase;
import com.example.model.Agente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AgenteDao implements CrudDao<Agente> {
    @Override
    public Agente crear(Agente agente) throws SQLException {
        String sql = """
                INSERT INTO agente (id_usuario, nombre, correo, cargo, placa)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, agente.getIdUsuario());
            statement.setString(2, agente.getNombre());
            statement.setString(3, agente.getCorreo());
            statement.setString(4, agente.getCargo());
            statement.setString(5, agente.getPlaca());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    agente.setId(keys.getInt(1));
                }
            }
        }

        return agente;
    }

    @Override
    public Optional<Agente> buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, id_usuario, nombre, correo, cargo, placa FROM agente WHERE id = ?";
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
    public List<Agente> listar() throws SQLException {
        String sql = "SELECT id, id_usuario, nombre, correo, cargo, placa FROM agente ORDER BY id DESC";
        List<Agente> agentes = new ArrayList<>();
        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                agentes.add(mapear(resultSet));
            }
        }
        return agentes;
    }

    @Override
    public boolean actualizar(Agente agente) throws SQLException {
        String sql = """
                UPDATE agente
                SET id_usuario = ?, nombre = ?, correo = ?, cargo = ?, placa = ?
                WHERE id = ?
                """;
        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, agente.getIdUsuario());
            statement.setString(2, agente.getNombre());
            statement.setString(3, agente.getCorreo());
            statement.setString(4, agente.getCargo());
            statement.setString(5, agente.getPlaca());
            statement.setInt(6, agente.getId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM agente WHERE id = ?")) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private Agente mapear(ResultSet resultSet) throws SQLException {
        Agente agente = new Agente();
        agente.setId(resultSet.getInt("id"));
        agente.setIdUsuario(resultSet.getInt("id_usuario"));
        agente.setNombre(resultSet.getString("nombre"));
        agente.setCorreo(resultSet.getString("correo"));
        agente.setCargo(resultSet.getString("cargo"));
        agente.setPlaca(resultSet.getString("placa"));
        return agente;
    }
}
