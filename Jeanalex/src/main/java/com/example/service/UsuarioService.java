package com.example.service;

import com.example.dao.UsuarioDao;
import com.example.model.Usuario;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

public class UsuarioService {
    private final UsuarioDao usuarioDao;

    public UsuarioService(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public Usuario registrarUsuario(String nombre, String correo, String contrasenaHash, int idRol) throws SQLException {
        Usuario usuario = new Usuario(nombre, correo, contrasenaHash, idRol);
        return usuarioDao.crear(usuario);
    }

    public List<Usuario> listarUsuarios() throws SQLException {
        return usuarioDao.listar();
    }

    public Optional<Usuario> buscarUsuario(int id) throws SQLException {
        return usuarioDao.buscarPorId(id);
    }

    public Optional<Usuario> autenticar(String correo, String contrasena) throws SQLException {
        if (correo == null || correo.isBlank() || contrasena == null || contrasena.isBlank()) {
            return Optional.empty();
        }

        Optional<Usuario> usuario = usuarioDao.buscarPorCorreo(correo.trim());
        if (usuario.isEmpty() || !usuario.get().isActivo()) {
            return Optional.empty();
        }

        String contrasenaGuardada = usuario.get().getContrasenaHash();
        if (contrasenaGuardada == null) {
            return Optional.empty();
        }

        return coincideContrasena(contrasena, contrasenaGuardada) ? usuario : Optional.empty();
    }

    public boolean actualizarUsuario(Usuario usuario) throws SQLException {
        return usuarioDao.actualizar(usuario);
    }

    public boolean eliminarUsuario(int id) throws SQLException {
        return usuarioDao.eliminar(id);
    }

    private boolean coincideContrasena(String contrasena, String contrasenaGuardada) {
        return contrasenaGuardada.equals(contrasena) || contrasenaGuardada.equalsIgnoreCase(sha256(contrasena));
    }

    private String sha256(String valor) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(valor.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 no esta disponible en este entorno.", exception);
        }
    }
}
