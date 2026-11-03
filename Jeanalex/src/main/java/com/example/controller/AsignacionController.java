package com.example.controller;

import com.example.dao.AsignacionDao;
import com.example.model.Asignacion;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class AsignacionController {
    private final AsignacionDao asignacionDao;

    public AsignacionController(AsignacionDao asignacionDao) {
        this.asignacionDao = asignacionDao;
    }

    public Asignacion asignarDispositivo(int idAgente, int idDispositivo, int idResponsable, String observaciones) throws SQLException {
        Asignacion asignacion = new Asignacion();
        asignacion.setIdAgente(idAgente);
        asignacion.setIdDispositivo(idDispositivo);
        asignacion.setIdResponsableEntrega(idResponsable);
        asignacion.setFechaAsignacion(LocalDateTime.now());
        asignacion.setObservaciones(observaciones);
        asignacion.setEstadoPrestamo("Activo");
        return asignacionDao.crear(asignacion);
    }

    public boolean registrarDevolucion(int idAsignacion, int idUsuario, String observaciones) throws SQLException {
        return asignacionDao.registrarDevolucion(idAsignacion, idUsuario, observaciones);
    }

    public List<Asignacion> listarAsignaciones() throws SQLException {
        return asignacionDao.listar();
    }

    public List<Object[]> listarHistorialDispositivo(int idDispositivo) throws SQLException {
        return asignacionDao.listarHistorialDispositivo(idDispositivo);
    }
}
