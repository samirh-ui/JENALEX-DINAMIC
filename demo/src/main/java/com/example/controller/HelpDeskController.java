package com.example.controller;

import com.example.model.Ticket;
import com.example.model.Usuario;
import com.example.service.HelpDeskSystem;
import com.example.view.HelpDeskView;

import java.util.List;

public class HelpDeskController {
    private final HelpDeskSystem model;
    private final HelpDeskView view;

    public HelpDeskController(HelpDeskSystem model, HelpDeskView view) {
        this.model = model;
        this.view = view;
    }

    public void iniciar() {
        while (true) {
            view.mostrarMenu();
            int opcion = view.leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> crearTicket();
                case 2 -> mostrarTickets();
                case 3 -> actualizarEstado();
                case 4 -> asignarTecnico();
                case 5 -> {
                    view.mostrarMensaje("Gracias por usar la mesa de ayuda.");
                    view.cerrar();
                    return;
                }
                default -> view.mostrarMensaje("Opción inválida. Intente de nuevo.");
            }
            view.mostrarMensaje("");
        }
    }

    private void crearTicket() {
        view.mostrarMensaje("--- Crear nuevo ticket ---");
        view.mostrarUsuarios(model.getUsuarios());
        int idUsuario = view.leerEntero("Ingrese el ID del usuario que reporta el problema: ");
        Usuario usuario = model.buscarUsuarioPorId(idUsuario);
        if (usuario == null) {
            view.mostrarMensaje("Usuario no encontrado.");
            return;
        }

        String descripcion = view.leerTexto("Descripción del problema: ");
        String prioridad = view.leerTexto("Prioridad (Baja/Media/Alta): ");
        Ticket ticket = model.crearTicket(descripcion, prioridad, idUsuario);
        view.mostrarMensaje("Ticket creado correctamente: " + ticket);
    }

    private void mostrarTickets() {
        view.mostrarMensaje("--- Lista de tickets ---");
        List<Ticket> tickets = model.listarTickets();
        if (tickets.isEmpty()) {
            view.mostrarMensaje("No hay tickets registrados.");
            return;
        }

        for (Ticket ticket : tickets) {
            view.mostrarMensaje(ticket.toString());
            if (ticket.getIdTecnico() != 0) {
                Usuario tecnico = model.buscarUsuarioPorId(ticket.getIdTecnico());
                view.mostrarMensaje("  Técnico asignado: " + (tecnico != null ? tecnico.getNombre() : "Desconocido"));
            }
        }
    }

    private void actualizarEstado() {
        view.mostrarMensaje("--- Actualizar estado de ticket ---");
        int ticketId = view.leerEntero("Ingrese el ID del ticket: ");
        String estado = view.leerTexto("Nuevo estado (Abierto/En progreso/Resuelto/Cerrado): ");
        boolean resultado = model.actualizarEstado(ticketId, estado);
        if (resultado) {
            view.mostrarMensaje("Estado actualizado correctamente.");
        } else {
            view.mostrarMensaje("No se encontró el ticket.");
        }
    }

    private void asignarTecnico() {
        view.mostrarMensaje("--- Asignar técnico ---");
        int ticketId = view.leerEntero("Ingrese el ID del ticket: ");
        view.mostrarUsuarios(model.getUsuarios());
        int tecnicoId = view.leerEntero("Ingrese el ID del técnico: ");
        Usuario tecnico = model.buscarUsuarioPorId(tecnicoId);
        if (tecnico == null) {
            view.mostrarMensaje("Técnico no encontrado.");
            return;
        }
        if (tecnico.getIdRol() != 2) {
            view.mostrarMensaje("El usuario seleccionado no es un técnico.");
            return;
        }
        boolean resultado = model.asignarTecnico(ticketId, tecnicoId);
        if (resultado) {
            view.mostrarMensaje("Técnico asignado correctamente.");
        } else {
            view.mostrarMensaje("No se encontró el ticket.");
        }
    }
}
