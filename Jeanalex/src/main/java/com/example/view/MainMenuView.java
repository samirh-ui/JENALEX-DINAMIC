package com.example.view;

import com.example.controller.DispositivoController;
import com.example.controller.TicketController;
import com.example.controller.UsuarioController;
import com.example.model.Dispositivo;
import com.example.model.Ticket;
import com.example.model.Usuario;

import java.sql.SQLException;

public class MainMenuView {
    private final ConsoleView consoleView;
    private final UsuarioController usuarioController;
    private final DispositivoController dispositivoController;
    private final TicketController ticketController;

    public MainMenuView(
            ConsoleView consoleView,
            UsuarioController usuarioController,
            DispositivoController dispositivoController,
            TicketController ticketController
    ) {
        this.consoleView = consoleView;
        this.usuarioController = usuarioController;
        this.dispositivoController = dispositivoController;
        this.ticketController = ticketController;
    }

    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = consoleView.leerEntero("Seleccione una opcion");
            ejecutarOpcion(opcion);
        } while (opcion != 0);
    }

    private void mostrarMenu() {
        consoleView.mostrarTitulo("Jeanalex Dynamics");
        consoleView.mostrarMensaje("1. Registrar usuario");
        consoleView.mostrarMensaje("2. Listar usuarios");
        consoleView.mostrarMensaje("3. Registrar dispositivo");
        consoleView.mostrarMensaje("4. Listar dispositivos");
        consoleView.mostrarMensaje("5. Registrar ticket");
        consoleView.mostrarMensaje("6. Listar tickets");
        consoleView.mostrarMensaje("7. Asignar ticket a tecnico");
        consoleView.mostrarMensaje("8. Cerrar ticket");
        consoleView.mostrarMensaje("0. Salir");
    }

    private void ejecutarOpcion(int opcion) {
        try {
            switch (opcion) {
                case 1 -> registrarUsuario();
                case 2 -> listarUsuarios();
                case 3 -> registrarDispositivo();
                case 4 -> listarDispositivos();
                case 5 -> registrarTicket();
                case 6 -> listarTickets();
                case 7 -> asignarTicket();
                case 8 -> cerrarTicket();
                case 0 -> consoleView.mostrarMensaje("Hasta luego.");
                default -> consoleView.mostrarMensaje("Opcion no valida.");
            }
        } catch (SQLException exception) {
            consoleView.mostrarMensaje("Error de base de datos: " + exception.getMessage());
        }
    }

    private void registrarUsuario() throws SQLException {
        consoleView.mostrarTitulo("Registrar usuario");
        String nombre = consoleView.leerTexto("Nombre");
        String correo = consoleView.leerTexto("Correo");
        String contrasenaHash = consoleView.leerTexto("Contrasena cifrada/hash");
        int idRol = consoleView.leerEntero("ID rol");
        Usuario usuario = usuarioController.registrarUsuario(nombre, correo, contrasenaHash, idRol);
        consoleView.mostrarMensaje("Usuario registrado con ID " + usuario.getId());
    }

    private void listarUsuarios() throws SQLException {
        consoleView.mostrarTitulo("Usuarios");
        for (Usuario usuario : usuarioController.listarUsuarios()) {
            consoleView.mostrarMensaje(usuario.getId() + " | " + usuario.getNombre() + " | " + usuario.getCorreo());
        }
    }

    private void registrarDispositivo() throws SQLException {
        consoleView.mostrarTitulo("Registrar dispositivo");
        String nombre = consoleView.leerTexto("Nombre");
        String serial = consoleView.leerTexto("Serial");
        String marca = consoleView.leerTexto("Marca");
        String modelo = consoleView.leerTexto("Modelo");
        String categoria = consoleView.leerTexto("Categoria");
        String estado = consoleView.leerTexto("Estado");
        String ubicacion = consoleView.leerTexto("Ubicacion");
        Dispositivo dispositivo = dispositivoController.registrarDispositivo(
                nombre,
                serial,
                marca,
                modelo,
                categoria,
                estado,
                ubicacion
        );
        consoleView.mostrarMensaje("Dispositivo registrado con ID " + dispositivo.getId());
    }

    private void listarDispositivos() throws SQLException {
        consoleView.mostrarTitulo("Dispositivos");
        for (Dispositivo dispositivo : dispositivoController.listarDispositivos()) {
            consoleView.mostrarMensaje(
                    dispositivo.getId() + " | " + dispositivo.getNombre() + " | " + dispositivo.getSerial()
                            + " | " + dispositivo.getEstado()
            );
        }
    }

    private void registrarTicket() throws SQLException {
        consoleView.mostrarTitulo("Registrar ticket");
        String asunto = consoleView.leerTexto("Asunto");
        String descripcion = consoleView.leerTexto("Descripcion");
        String tipoSolicitud = consoleView.leerTexto("Tipo de solicitud");
        String prioridad = consoleView.leerTexto("Prioridad");
        int idAgente = consoleView.leerEntero("ID agente");
        int idAns = consoleView.leerEntero("ID ANS");
        Ticket ticket = ticketController.registrarTicket(asunto, descripcion, tipoSolicitud, prioridad, idAgente, idAns);
        consoleView.mostrarMensaje("Ticket registrado con ID " + ticket.getId());
    }

    private void listarTickets() throws SQLException {
        consoleView.mostrarTitulo("Tickets");
        for (Ticket ticket : ticketController.listarTickets()) {
            consoleView.mostrarMensaje(
                    ticket.getId() + " | " + ticket.getAsunto() + " | " + ticket.getPrioridad()
                            + " | " + ticket.getEstado()
            );
        }
    }

    private void asignarTicket() throws SQLException {
        consoleView.mostrarTitulo("Asignar ticket");
        int idTicket = consoleView.leerEntero("ID ticket");
        int idCoordinador = consoleView.leerEntero("ID coordinador");
        int idTecnico = consoleView.leerEntero("ID tecnico");
        boolean actualizado = ticketController.asignarTecnico(idTicket, idCoordinador, idTecnico);
        consoleView.mostrarMensaje(actualizado ? "Ticket asignado." : "No se encontro el ticket.");
    }

    private void cerrarTicket() throws SQLException {
        consoleView.mostrarTitulo("Cerrar ticket");
        int idTicket = consoleView.leerEntero("ID ticket");
        String evidencia = consoleView.leerTexto("Evidencia");
        boolean actualizado = ticketController.cerrarTicket(idTicket, evidencia);
        consoleView.mostrarMensaje(actualizado ? "Ticket cerrado." : "No se encontro el ticket.");
    }
}
