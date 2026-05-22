package com.example.dao;

import com.example.config.ConexionPostgresDatabase;
import com.example.model.Usuario;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDao implements CrudDao<Usuario> {

    public Optional<Usuario> login(String correo, String password) throws SQLException {
       String sql =  "SELECT * FROM fn_login_usuario(?, ?)";    

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setString(1, correo);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(resultSet.getInt("id"));
                    usuario.setNombre(resultSet.getString("nombre"));
                    usuario.setCorreo(resultSet.getString("correo"));
                    usuario.setContrasenaHash(resultSet.getString("contrasena_hash"));
                    usuario.setIdRol(resultSet.getInt("id_rol"));
                    usuario.setActivo(resultSet.getBoolean("activo"));
                    return Optional.of(usuario);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Usuario crear(Usuario usuario) throws SQLException {
        String sql = "CALL sp_crear_usuario(?,?,?,?,?)";

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

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
        String sql = "CALL sp_buscar_usuario(?)";

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

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
        String sql = "CALL sp_listar_usuarios()";

        List<Usuario> usuarios = new ArrayList<>();

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             CallableStatement statement = connection.prepareCall(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                usuarios.add(mapear(resultSet));
            }
        }

        return usuarios;
    }

    @Override
    public boolean actualizar(Usuario usuario) throws SQLException {
        String sql = "CALL sp_actualizar_usuario(?,?,?,?)";

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, usuario.getId());
            statement.setString(2, usuario.getNombre());
            statement.setString(3, usuario.getCorreo());
            statement.setBoolean(4, usuario.isActivo());

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "CALL sp_eliminar_usuario(?)";

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

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

        try {
            usuario.setIdRol(resultSet.getInt("id_rol"));
        } catch (SQLException ignored) {
            // Columna no disponible
        }

        try {
            usuario.setEstado(resultSet.getString("estado"));
        } catch (SQLException ignored) {
            // Columna no disponible
        }

        return usuario;
    }
}
