package com.example.controller;

import com.example.dao.MantenimientoDao;
import com.example.model.Mantenimiento;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class MantenimientoController {
    private final MantenimientoDao mantenimientoDao;

    public MantenimientoController(MantenimientoDao mantenimientoDao) {
        this.mantenimientoDao = mantenimientoDao;
    }

    public Mantenimiento programarMantenimiento(int idDispositivo, int idTecnico, String tipo, String descripcion, LocalDateTime fechaProgramada) throws SQLException {
        Mantenimiento mantenimiento = new Mantenimiento();
        mantenimiento.setIdDispositivo(idDispositivo);
        mantenimiento.setIdTecnico(idTecnico);
        mantenimiento.setTipo(tipo);
        mantenimiento.setDescripcion(descripcion);
        mantenimiento.setEstado("Programado");
        mantenimiento.setFechaProgramada(fechaProgramada);
        return mantenimientoDao.crear(mantenimiento);
    }

    public boolean actualizarEstado(int id, String estado, String observaciones) throws SQLException {
        return mantenimientoDao.actualizarEstado(id, estado, observaciones);
    }

    public List<Mantenimiento> listarMantenimientos() throws SQLException {
        return mantenimientoDao.listar();
    }
}
