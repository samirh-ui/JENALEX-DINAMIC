package com.example.controller;

import com.example.dao.AgenteDao;
import com.example.model.Agente;

import java.sql.SQLException;
import java.util.List;

public class AgenteController {
    private final AgenteDao agenteDao;

    public AgenteController(AgenteDao agenteDao) {
        this.agenteDao = agenteDao;
    }

    public Agente registrarAgente(int idUsuario, String nombre, String correo, String cargo, String placa) throws SQLException {
        return agenteDao.crear(new Agente(idUsuario, nombre, correo, cargo, placa));
    }

    public List<Agente> listarAgentes() throws SQLException {
        return agenteDao.listar();
    }

    public boolean eliminarAgente(int id) throws SQLException {
        return agenteDao.eliminar(id);
    }
}
