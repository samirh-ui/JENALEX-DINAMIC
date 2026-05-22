package com.example.controller;

import com.example.dao.MensajeDao;
import com.example.model.Mensaje;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class MensajeController {
    private final MensajeDao mensajeDao;

    public MensajeController(MensajeDao mensajeDao) {
        this.mensajeDao = mensajeDao;
    }

    public Mensaje enviarMensaje(int idRemitente, int idDestinatario, String asunto, String contenido) throws SQLException {
        Mensaje mensaje = new Mensaje();
        mensaje.setIdRemitente(idRemitente);
        mensaje.setIdDestinatario(idDestinatario);
        mensaje.setAsunto(asunto);
        mensaje.setContenido(contenido);
        mensaje.setFechaEnvio(LocalDateTime.now());
        mensaje.setLeido(false);
        return mensajeDao.crear(mensaje);
    }

    public List<Mensaje> listarMensajes() throws SQLException {
        return mensajeDao.listar();
    }
}
