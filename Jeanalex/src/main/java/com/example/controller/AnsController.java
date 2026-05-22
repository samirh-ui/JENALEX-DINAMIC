package com.example.controller;

import com.example.dao.AnsDao;
import com.example.model.Ans;

import java.sql.SQLException;
import java.util.List;

public class AnsController {
    private final AnsDao ansDao;

    public AnsController(AnsDao ansDao) {
        this.ansDao = ansDao;
    }

    public Ans guardarAns(int id, String tipoSolicitud, String prioridad, int respuestaHoras, int solucionHoras, String descripcion, boolean activo) throws SQLException {
        Ans ans = new Ans();
        ans.setId(id);
        ans.setTipoSolicitud(tipoSolicitud);
        ans.setPrioridad(prioridad);
        ans.setTiempoRespuestaHoras(respuestaHoras);
        ans.setTiempoSolucionHoras(solucionHoras);
        ans.setDescripcion(descripcion);
        ans.setActivo(activo);
        return id > 0 && ansDao.actualizar(ans) ? ans : ansDao.crear(ans);
    }

    public List<Ans> listarAns() throws SQLException {
        return ansDao.listar();
    }
}
