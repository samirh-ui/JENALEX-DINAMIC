package com.example.service;

import com.example.model.Ticket;
import com.example.model.Usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HelpDeskSystem {
    private final List<Ticket> tickets = new ArrayList<>();
    private final List<Usuario> usuarios = new ArrayList<>();
    private int nextTicketId = 1;

    public HelpDeskSystem() {
        cargarUsuariosEjemplo();
    }

    private void cargarUsuariosEjemplo() {
        usuarios.add(new Usuario(1, "Ana Pérez", "ana.perez@example.com", "1234", 1));
        usuarios.add(new Usuario(2, "Carlos Martínez", "carlos.martinez@example.com", "1234", 2));
        usuarios.add(new Usuario(3, "Lucía Gómez", "lucia.gomez@example.com", "1234", 2));
    }

    public List<Usuario> getUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    public List<Ticket> listarTickets() {
        return Collections.unmodifiableList(tickets);
    }

    public Ticket crearTicket(String descripcion, String prioridad, int idUsuario) {
        Ticket ticket = new Ticket(nextTicketId++, descripcion, prioridad, idUsuario);
        tickets.add(ticket);
        return ticket;
    }

    public boolean asignarTecnico(int ticketId, int idTecnico) {
        Ticket ticket = buscarTicketPorId(ticketId);
        if (ticket == null) {
            return false;
        }
        ticket.setIdTecnico(idTecnico);
        if (ticket.getEstado().equals("Abierto")) {
            ticket.setEstado("En progreso");
        }
        return true;
    }

    public boolean actualizarEstado(int ticketId, String estado) {
        Ticket ticket = buscarTicketPorId(ticketId);
        if (ticket == null) {
            return false;
        }
        ticket.setEstado(estado);
        return true;
    }

    public Ticket buscarTicketPorId(int ticketId) {
        return tickets.stream()
                .filter(ticket -> ticket.getId() == ticketId)
                .findFirst()
                .orElse(null);
    }

    public Usuario buscarUsuarioPorId(int idUsuario) {
        return usuarios.stream()
                .filter(usuario -> usuario.getId() == idUsuario)
                .findFirst()
                .orElse(null);
    }
}
