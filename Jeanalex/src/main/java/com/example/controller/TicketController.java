package com.example.controller;

import com.example.dao.TicketDao;
import com.example.model.Ticket;
import com.example.service.TicketService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketDao ticketDao) {
        this.ticketService = new TicketService(ticketDao);
    }

    public Ticket registrarTicket(
            String asunto,
            String descripcion,
            String tipoSolicitud,
            String prioridad,
            int idAgente,
            int idAns
    ) throws SQLException {
        return ticketService.registrarTicket(asunto, descripcion, tipoSolicitud, prioridad, idAgente, idAns);
    }

    public boolean asignarTecnico(int idTicket, int idCoordinador, int idTecnico) throws SQLException {
        return ticketService.asignarTecnico(idTicket, idCoordinador, idTecnico);
    }

    public boolean cerrarTicket(int idTicket, String evidencia) throws SQLException {
        return ticketService.cerrarTicket(idTicket, evidencia);
    }

    public List<Ticket> listarTickets() throws SQLException {
        return ticketService.listarTickets();
    }

    public List<Ticket> listarPendientes() throws SQLException {
        return ticketService.listarPendientes();
    }

    public Optional<Ticket> buscarTicket(int id) throws SQLException {
        return ticketService.buscarTicket(id);
    }

    public boolean eliminarTicket(int id) throws SQLException {
        return ticketService.eliminarTicket(id);
    }
}
