package com.example.controller;

import com.example.dao.ReporteDao;
import com.example.model.ReporteIndicadores;

import java.sql.SQLException;
import java.util.List;

public class ReporteController {
    private final ReporteDao reporteDao;

    public ReporteController(ReporteDao reporteDao) {
        this.reporteDao = reporteDao;
    }

    public ReporteIndicadores obtenerIndicadores() throws SQLException {
        return reporteDao.obtenerIndicadores();
    }

    public List<Object[]> listarEquiposPorCategoria() throws SQLException {
        return reporteDao.listarEquiposPorCategoria();
    }

    public List<Object[]> listarSolicitudesPorEstado() throws SQLException {
        return reporteDao.listarSolicitudesPorEstado();
    }

    public List<Object[]> listarCumplimientoPorTecnico() throws SQLException {
        return reporteDao.listarCumplimientoPorTecnico();
    }
}
