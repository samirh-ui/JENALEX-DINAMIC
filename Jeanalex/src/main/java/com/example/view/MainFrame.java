package com.example.view;

import com.example.controller.DispositivoController;
import com.example.controller.TicketController;
import com.example.controller.UsuarioController;
import com.example.model.Dispositivo;
import com.example.model.Ticket;
import com.example.model.Usuario;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

public class MainFrame extends JFrame {
    private final UsuarioController usuarioController;
    private final DispositivoController dispositivoController;
    private final TicketController ticketController;

    private final DefaultTableModel usuariosTableModel = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Correo", "Rol", "Activo"}, 0
    );
    private final DefaultTableModel dispositivosTableModel = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Serial", "Marca", "Modelo", "Categoria", "Estado", "Ubicacion"}, 0
    );
    private final DefaultTableModel ticketsTableModel = new DefaultTableModel(
            new String[]{"ID", "Asunto", "Tipo", "Prioridad", "Estado", "Agente", "Tecnico"}, 0
    );

    public MainFrame(
            UsuarioController usuarioController,
            DispositivoController dispositivoController,
            TicketController ticketController
    ) {
        this.usuarioController = usuarioController;
        this.dispositivoController = dispositivoController;
        this.ticketController = ticketController;

        setTitle("Jeanalex Dynamics");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(crearTabs(), BorderLayout.CENTER);
    }

    private JTabbedPane crearTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Usuarios", crearPanelUsuarios());
        tabs.addTab("Dispositivos", crearPanelDispositivos());
        tabs.addTab("Tickets", crearPanelTickets());
        return tabs;
    }

    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JTextField nombreField = new JTextField(18);
        JTextField correoField = new JTextField(18);
        JTextField contrasenaField = new JTextField(18);
        JTextField rolField = new JTextField(8);

        JPanel form = crearFormulario();
        agregarCampo(form, "Nombre", nombreField, 0);
        agregarCampo(form, "Correo", correoField, 1);
        agregarCampo(form, "Contrasena hash", contrasenaField, 2);
        agregarCampo(form, "ID rol", rolField, 3);

        JButton guardarButton = new JButton("Guardar usuario");
        JButton listarButton = new JButton("Listar usuarios");
        JPanel acciones = new JPanel();
        acciones.add(guardarButton);
        acciones.add(listarButton);

        JPanel superior = new JPanel(new BorderLayout());
        superior.add(form, BorderLayout.CENTER);
        superior.add(acciones, BorderLayout.SOUTH);

        JTable tabla = new JTable(usuariosTableModel);
        panel.add(superior, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        guardarButton.addActionListener(event -> {
            try {
                Usuario usuario = usuarioController.registrarUsuario(
                        nombreField.getText().trim(),
                        correoField.getText().trim(),
                        contrasenaField.getText().trim(),
                        Integer.parseInt(rolField.getText().trim())
                );
                mostrarMensaje("Usuario registrado con ID " + usuario.getId());
                limpiar(nombreField, correoField, contrasenaField, rolField);
                cargarUsuarios();
            } catch (NumberFormatException exception) {
                mostrarError("El ID rol debe ser numerico.");
            } catch (SQLException exception) {
                mostrarError("No se pudo registrar el usuario: " + exception.getMessage());
            }
        });

        listarButton.addActionListener(event -> cargarUsuarios());
        return panel;
    }

    private JPanel crearPanelDispositivos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JTextField nombreField = new JTextField(14);
        JTextField serialField = new JTextField(14);
        JTextField marcaField = new JTextField(14);
        JTextField modeloField = new JTextField(14);
        JTextField categoriaField = new JTextField(14);
        JTextField estadoField = new JTextField(14);
        JTextField ubicacionField = new JTextField(14);

        JPanel form = crearFormulario();
        agregarCampo(form, "Nombre", nombreField, 0);
        agregarCampo(form, "Serial", serialField, 1);
        agregarCampo(form, "Marca", marcaField, 2);
        agregarCampo(form, "Modelo", modeloField, 3);
        agregarCampo(form, "Categoria", categoriaField, 4);
        agregarCampo(form, "Estado", estadoField, 5);
        agregarCampo(form, "Ubicacion", ubicacionField, 6);

        JButton guardarButton = new JButton("Guardar dispositivo");
        JButton listarButton = new JButton("Listar dispositivos");
        JPanel acciones = new JPanel();
        acciones.add(guardarButton);
        acciones.add(listarButton);

        JPanel superior = new JPanel(new BorderLayout());
        superior.add(form, BorderLayout.CENTER);
        superior.add(acciones, BorderLayout.SOUTH);

        JTable tabla = new JTable(dispositivosTableModel);
        panel.add(superior, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        guardarButton.addActionListener(event -> {
            try {
                Dispositivo dispositivo = dispositivoController.registrarDispositivo(
                        nombreField.getText().trim(),
                        serialField.getText().trim(),
                        marcaField.getText().trim(),
                        modeloField.getText().trim(),
                        categoriaField.getText().trim(),
                        estadoField.getText().trim(),
                        ubicacionField.getText().trim()
                );
                mostrarMensaje("Dispositivo registrado con ID " + dispositivo.getId());
                limpiar(nombreField, serialField, marcaField, modeloField, categoriaField, estadoField, ubicacionField);
                cargarDispositivos();
            } catch (SQLException exception) {
                mostrarError("No se pudo registrar el dispositivo: " + exception.getMessage());
            }
        });

        listarButton.addActionListener(event -> cargarDispositivos());
        return panel;
    }

    private JPanel crearPanelTickets() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JTextField asuntoField = new JTextField(16);
        JTextArea descripcionArea = new JTextArea(3, 16);
        JTextField tipoField = new JTextField(16);
        JTextField prioridadField = new JTextField(16);
        JTextField agenteField = new JTextField(8);
        JTextField ansField = new JTextField(8);
        JTextField ticketAsignarField = new JTextField(8);
        JTextField coordinadorField = new JTextField(8);
        JTextField tecnicoField = new JTextField(8);
        JTextField ticketCerrarField = new JTextField(8);
        JTextField evidenciaField = new JTextField(16);

        JPanel form = crearFormulario();
        agregarCampo(form, "Asunto", asuntoField, 0);
        agregarCampo(form, "Descripcion", new JScrollPane(descripcionArea), 1);
        agregarCampo(form, "Tipo solicitud", tipoField, 2);
        agregarCampo(form, "Prioridad", prioridadField, 3);
        agregarCampo(form, "ID agente", agenteField, 4);
        agregarCampo(form, "ID ANS", ansField, 5);
        agregarCampo(form, "ID ticket asignar", ticketAsignarField, 6);
        agregarCampo(form, "ID coordinador", coordinadorField, 7);
        agregarCampo(form, "ID tecnico", tecnicoField, 8);
        agregarCampo(form, "ID ticket cerrar", ticketCerrarField, 9);
        agregarCampo(form, "Evidencia", evidenciaField, 10);

        JButton guardarButton = new JButton("Registrar ticket");
        JButton listarButton = new JButton("Listar tickets");
        JButton asignarButton = new JButton("Asignar tecnico");
        JButton cerrarButton = new JButton("Cerrar ticket");
        JPanel acciones = new JPanel();
        acciones.add(guardarButton);
        acciones.add(listarButton);
        acciones.add(asignarButton);
        acciones.add(cerrarButton);

        JPanel superior = new JPanel(new BorderLayout());
        superior.add(form, BorderLayout.CENTER);
        superior.add(acciones, BorderLayout.SOUTH);

        JTable tabla = new JTable(ticketsTableModel);
        panel.add(superior, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        guardarButton.addActionListener(event -> {
            try {
                Ticket ticket = ticketController.registrarTicket(
                        asuntoField.getText().trim(),
                        descripcionArea.getText().trim(),
                        tipoField.getText().trim(),
                        prioridadField.getText().trim(),
                        Integer.parseInt(agenteField.getText().trim()),
                        Integer.parseInt(ansField.getText().trim())
                );
                mostrarMensaje("Ticket registrado con ID " + ticket.getId());
                limpiar(asuntoField, tipoField, prioridadField, agenteField, ansField);
                descripcionArea.setText("");
                cargarTickets();
            } catch (NumberFormatException exception) {
                mostrarError("ID agente e ID ANS deben ser numericos.");
            } catch (SQLException exception) {
                mostrarError("No se pudo registrar el ticket: " + exception.getMessage());
            }
        });

        listarButton.addActionListener(event -> cargarTickets());

        asignarButton.addActionListener(event -> {
            try {
                boolean asignado = ticketController.asignarTecnico(
                        Integer.parseInt(ticketAsignarField.getText().trim()),
                        Integer.parseInt(coordinadorField.getText().trim()),
                        Integer.parseInt(tecnicoField.getText().trim())
                );
                mostrarMensaje(asignado ? "Ticket asignado." : "No se encontro el ticket.");
                cargarTickets();
            } catch (NumberFormatException exception) {
                mostrarError("Los IDs de asignacion deben ser numericos.");
            } catch (SQLException exception) {
                mostrarError("No se pudo asignar el ticket: " + exception.getMessage());
            }
        });

        cerrarButton.addActionListener(event -> {
            try {
                boolean cerrado = ticketController.cerrarTicket(
                        Integer.parseInt(ticketCerrarField.getText().trim()),
                        evidenciaField.getText().trim()
                );
                mostrarMensaje(cerrado ? "Ticket cerrado." : "No se encontro el ticket.");
                cargarTickets();
            } catch (NumberFormatException exception) {
                mostrarError("El ID ticket cerrar debe ser numerico.");
            } catch (SQLException exception) {
                mostrarError("No se pudo cerrar el ticket: " + exception.getMessage());
            }
        });

        return panel;
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());
        return panel;
    }

    private void agregarCampo(JPanel panel, String etiqueta, java.awt.Component campo, int fila) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = fila;
        labelConstraints.anchor = GridBagConstraints.EAST;
        labelConstraints.insets = new Insets(4, 4, 4, 6);
        panel.add(new JLabel(etiqueta), labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = fila;
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(4, 4, 4, 4);
        panel.add(campo, fieldConstraints);
    }

    private void cargarUsuarios() {
        try {
            usuariosTableModel.setRowCount(0);
            for (Usuario usuario : usuarioController.listarUsuarios()) {
                usuariosTableModel.addRow(new Object[]{
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getCorreo(),
                        usuario.getIdRol(),
                        usuario.isActivo()
                });
            }
        } catch (SQLException exception) {
            mostrarError("No se pudieron cargar los usuarios: " + exception.getMessage());
        }
    }

    private void cargarDispositivos() {
        try {
            dispositivosTableModel.setRowCount(0);
            for (Dispositivo dispositivo : dispositivoController.listarDispositivos()) {
                dispositivosTableModel.addRow(new Object[]{
                        dispositivo.getId(),
                        dispositivo.getNombre(),
                        dispositivo.getSerial(),
                        dispositivo.getMarca(),
                        dispositivo.getModelo(),
                        dispositivo.getCategoria(),
                        dispositivo.getEstado(),
                        dispositivo.getUbicacion()
                });
            }
        } catch (SQLException exception) {
            mostrarError("No se pudieron cargar los dispositivos: " + exception.getMessage());
        }
    }

    private void cargarTickets() {
        try {
            ticketsTableModel.setRowCount(0);
            for (Ticket ticket : ticketController.listarTickets()) {
                ticketsTableModel.addRow(new Object[]{
                        ticket.getId(),
                        ticket.getAsunto(),
                        ticket.getTipoSolicitud(),
                        ticket.getPrioridad(),
                        ticket.getEstado(),
                        ticket.getIdAgente(),
                        ticket.getIdTecnico()
                });
            }
        } catch (SQLException exception) {
            mostrarError("No se pudieron cargar los tickets: " + exception.getMessage());
        }
    }

    private void limpiar(JTextField... campos) {
        for (JTextField campo : campos) {
            campo.setText("");
        }
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Jeanalex Dynamics", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void mostrar(MainFrame frame) {
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
}
