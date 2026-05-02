package com.example.dao;

import com.example.config.ConexionPostgresDatabase;
import com.example.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDao implements CrudDao<Usuario> {
    @Override
    public Usuario crear(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nombre, correo, contrasena_hash, id_rol, activo) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, usuario.getNombre());
            statement.setString(2, usuario.getCorreo());
            statement.setString(3, usuario.getContrasenaHash());
            statement.setInt(4, usuario.getIdRol());
            statement.setBoolean(5, usuario.isActivo());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    usuario.setId(keys.getInt(1));
                }
            }
        }

        return usuario;
    }

    @Override
    public Optional<Usuario> buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, correo, contrasena_hash, id_rol, activo FROM usuario WHERE id = ?";

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

    public Optional<Usuario> buscarPorCorreo(String correo) throws SQLException {
        String sql = """
                SELECT id, nombre, correo, contrasena_hash, id_rol, activo
                FROM usuario
                WHERE LOWER(correo) = LOWER(?)
                """;

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, correo);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapear(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Usuario> listar() throws SQLException {
        String sql = "SELECT id, nombre, correo, contrasena_hash, id_rol, activo FROM usuario ORDER BY nombre";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                usuarios.add(mapear(resultSet));
            }
        }

        return usuarios;
    }

    @Override
    public boolean actualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET nombre = ?, correo = ?, contrasena_hash = ?, id_rol = ?, activo = ? WHERE id = ?";

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getNombre());
            statement.setString(2, usuario.getCorreo());
            statement.setString(3, usuario.getContrasenaHash());
            statement.setInt(4, usuario.getIdRol());
            statement.setBoolean(5, usuario.isActivo());
            statement.setInt(6, usuario.getId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private Usuario mapear(ResultSet resultSet) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(resultSet.getInt("id"));
        usuario.setNombre(resultSet.getString("nombre"));
        usuario.setCorreo(resultSet.getString("correo"));
        usuario.setContrasenaHash(resultSet.getString("contrasena_hash"));
        usuario.setIdRol(resultSet.getInt("id_rol"));
        usuario.setActivo(resultSet.getBoolean("activo"));
        return usuario;
    }
}
