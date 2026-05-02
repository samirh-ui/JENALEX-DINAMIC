package com.example.service;

import com.example.dao.TicketDao;
import com.example.model.Ticket;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TicketService {
    private final TicketDao ticketDao;

    public TicketService(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    public Ticket registrarTicket(
            String asunto,
            String descripcion,
            String tipoSolicitud,
            String prioridad,
            int idAgente,
            int idAns
    ) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setAsunto(asunto);
        ticket.setDescripcion(descripcion);
        ticket.setTipoSolicitud(tipoSolicitud);
        ticket.setPrioridad(prioridad);
        ticket.setIdAgente(idAgente);
        ticket.setIdAns(idAns);
        ticket.setEstado("Abierta");
        ticket.setFechaCreacion(LocalDateTime.now());
        return ticketDao.crear(ticket);
    }

    public boolean asignarTecnico(int idTicket, int idCoordinador, int idTecnico) throws SQLException {
        Optional<Ticket> ticketEncontrado = ticketDao.buscarPorId(idTicket);
        if (ticketEncontrado.isEmpty()) {
            return false;
        }

        Ticket ticket = ticketEncontrado.get();
        ticket.setIdCoordinador(idCoordinador);
        ticket.setIdTecnico(idTecnico);
        ticket.setEstado("Asignada");
        ticket.setFechaAsignacion(LocalDateTime.now());
        return ticketDao.actualizar(ticket);
    }

    public boolean cerrarTicket(int idTicket, String evidencia) throws SQLException {
        Optional<Ticket> ticketEncontrado = ticketDao.buscarPorId(idTicket);
        if (ticketEncontrado.isEmpty()) {
            return false;
        }

        Ticket ticket = ticketEncontrado.get();
        ticket.setEstado("Cerrada");
        ticket.setEvidencia(evidencia);
        ticket.setFechaCierre(LocalDateTime.now());
        return ticketDao.actualizar(ticket);
    }

    public List<Ticket> listarTickets() throws SQLException {
        return ticketDao.listar();
    }

    public List<Ticket> listarPendientes() throws SQLException {
        return ticketDao.listarPendientes();
    }

    public Optional<Ticket> buscarTicket(int id) throws SQLException {
        return ticketDao.buscarPorId(id);
    }

    public boolean eliminarTicket(int id) throws SQLException {
        return ticketDao.eliminar(id);
    }
}
