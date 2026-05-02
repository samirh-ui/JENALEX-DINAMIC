package com.example.controller;

import com.example.dao.DispositivoDao;
import com.example.model.Dispositivo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DispositivoController {
    private final DispositivoDao dispositivoDao;

    public DispositivoController(DispositivoDao dispositivoDao) {
        this.dispositivoDao = dispositivoDao;
    }

    public Dispositivo registrarDispositivo(
            String nombre,
            String serial,
            String marca,
            String modelo,
            String categoria,
            String estado,
            String ubicacion
    ) throws SQLException {
        Dispositivo dispositivo = new Dispositivo(nombre, serial, marca, modelo, categoria, estado);
        dispositivo.setUbicacion(ubicacion);
        dispositivo.setFechaIngreso(LocalDate.now());
        return dispositivoDao.crear(dispositivo);
    }

    public List<Dispositivo> listarDispositivos() throws SQLException {
        return dispositivoDao.listar();
    }

    public Optional<Dispositivo> buscarDispositivo(int id) throws SQLException {
        return dispositivoDao.buscarPorId(id);
    }

    public boolean actualizarDispositivo(Dispositivo dispositivo) throws SQLException {
        return dispositivoDao.actualizar(dispositivo);
    }

    public boolean eliminarDispositivo(int id) throws SQLException {
        return dispositivoDao.eliminar(id);
    }
}
