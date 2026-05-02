package com.example.controller;

import com.example.dao.UsuarioDao;
import com.example.model.Usuario;
import com.example.service.UsuarioService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioDao usuarioDao) {
        this.usuarioService = new UsuarioService(usuarioDao);
    }

    public Usuario registrarUsuario(String nombre, String correo, String contrasenaHash, int idRol) throws SQLException {
        return usuarioService.registrarUsuario(nombre, correo, contrasenaHash, idRol);
    }

    public List<Usuario> listarUsuarios() throws SQLException {
        return usuarioService.listarUsuarios();
    }

    public Optional<Usuario> buscarUsuario(int id) throws SQLException {
        return usuarioService.buscarUsuario(id);
    }

    public Optional<Usuario> autenticar(String correo, String contrasena) throws SQLException {
        return usuarioService.autenticar(correo, contrasena);
    }

    public boolean actualizarUsuario(Usuario usuario) throws SQLException {
        return usuarioService.actualizarUsuario(usuario);
    }

    public boolean eliminarUsuario(int id) throws SQLException {
        return usuarioService.eliminarUsuario(id);
    }
}
