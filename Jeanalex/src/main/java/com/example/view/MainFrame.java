package com.example.view;

import com.example.controller.AgenteController;
import com.example.controller.AnsController;
import com.example.controller.AsignacionController;
import com.example.controller.DispositivoController;
import com.example.controller.MantenimientoController;
import com.example.controller.MensajeController;
import com.example.controller.ReporteController;
import com.example.controller.TicketController;
import com.example.controller.UsuarioController;
import com.example.model.Agente;
import com.example.model.Ans;
import com.example.model.Asignacion;
import com.example.model.Dispositivo;
import com.example.model.Mantenimiento;
import com.example.model.Mensaje;
import com.example.model.ReporteIndicadores;
import com.example.model.Ticket;
import com.example.model.Usuario;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Path;

public class MainFrame extends JFrame {
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String CARD_DASHBOARD = "dashboard";
    private static final String CARD_EQUIPOS = "equipos";
    private static final String CARD_CATEGORIAS = "categorias";
    private static final String CARD_USUARIOS = "usuarios";
    private static final String CARD_AGENTES = "agentes";
    private static final String CARD_ASIGNACIONES = "asignaciones";
    private static final String CARD_MESA_AYUDA = "mesa_ayuda";
    private static final String CARD_MANTENIMIENTOS = "mantenimientos";
    private static final String CARD_SOLICITUDES = "solicitudes";
    private static final String CARD_MENSAJES = "mensajes";
    private static final String CARD_ANS = "ans";
    private static final String CARD_REPORTES = "reportes";
    private static final String CARD_CONFIGURACION = "configuracion";

    private final UsuarioController usuarioController;
    private final DispositivoController dispositivoController;
    private final TicketController ticketController;
    private final ReporteController reporteController;
    private final AgenteController agenteController;
    private final AsignacionController asignacionController;
    private final MensajeController mensajeController;
    private final MantenimientoController mantenimientoController;
    private final AnsController ansController;
    private final Usuario usuarioActual;
    private final Runnable onCerrarSesion;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentCards = new JPanel(cardLayout);
    private final Map<String, JButton> menuButtons = new LinkedHashMap<>();

    private final DefaultTableModel usuariosTableModel = new DefaultTableModel(
            new String[]{"Nombre", "Correo", "Rol", "Estado"}, 0
    );
    private final DefaultTableModel dispositivosTableModel = new DefaultTableModel(
            new String[]{"Equipo", "Serial", "Marca", "Modelo", "Categoria", "Estado", "Ubicacion"}, 0
    );
    private final DefaultTableModel ticketsTableModel = new DefaultTableModel(
            new String[]{"Asunto", "Tipo", "Prioridad", "Estado", "Agente", "Tecnico"}, 0
    );
    private final DefaultTableModel agentesTableModel = new DefaultTableModel(
            new String[]{"Usuario", "Nombre", "Correo", "Cargo", "Placa"}, 0
    );
    private final DefaultTableModel asignacionesTableModel = new DefaultTableModel(
            new String[]{"Agente", "Dispositivo", "Responsable", "Asignacion", "Devolucion", "Estado", "Observaciones"}, 0
    );
    private final DefaultTableModel historialTableModel = new DefaultTableModel(
            new String[]{"Dispositivo", "Usuario", "Accion", "Descripcion", "Fecha"}, 0
    );
    private final DefaultTableModel mensajesTableModel = new DefaultTableModel(
            new String[]{"Remitente", "Destinatario", "Asunto", "Fecha", "Leido"}, 0
    );
    private final DefaultTableModel mantenimientosTableModel = new DefaultTableModel(
            new String[]{"Dispositivo", "Tecnico", "Tipo", "Estado", "Programada", "Ejecutada", "Observaciones"}, 0
    );
    private final DefaultTableModel ansTableModel = new DefaultTableModel(
            new String[]{"Tipo", "Prioridad", "Respuesta", "Solucion", "Activo", "Descripcion"}, 0
    );
    private final DefaultTableModel mesaAyudaTableModel = new DefaultTableModel(
            new String[]{"Asunto", "Tipo", "Prioridad", "Estado", "Tecnico", "Alerta"}, 0
    );
    private final DefaultTableModel reporteIndicadoresTableModel = new DefaultTableModel(
            new String[]{"Indicador", "Valor"}, 0
    );
    private final DefaultTableModel reporteCategoriasTableModel = new DefaultTableModel(
            new String[]{"Categoria", "Total equipos", "En mantenimiento"}, 0
    );
    private final DefaultTableModel reporteSolicitudesTableModel = new DefaultTableModel(
            new String[]{"Estado", "Total solicitudes"}, 0
    );
    private final DefaultTableModel reporteCumplimientoTableModel = new DefaultTableModel(
            new String[]{"Tecnico", "Total", "Dentro de ANS", "Cumplimiento"}, 0
    );
    private final DefaultTableModel dashboardCategoriasTableModel = new DefaultTableModel(
            new String[]{"Categoria", "Total equipos", "En mantenimiento"}, 0
    );
    private final DefaultTableModel dashboardSolicitudesTableModel = new DefaultTableModel(
            new String[]{"Estado", "Total solicitudes"}, 0
    );
    private final JLabel dashboardTotalEquiposValue = UiTheme.title("0", 30);
    private final JLabel dashboardMantenimientoValue = UiTheme.title("0", 30);
    private final JLabel dashboardSolicitudesValue = UiTheme.title("0", 30);
    private final JLabel dashboardCumplimientoValue = UiTheme.title("0.0%", 30);
    private final JLabel mesaAbiertosValue = UiTheme.title("0", 30);
    private final JLabel mesaAltaValue = UiTheme.title("0", 30);
    private final JLabel mesaAsignadosValue = UiTheme.title("0", 30);
    private final JLabel mesaAlertasValue = UiTheme.title("0", 30);

    public MainFrame(
            UsuarioController usuarioController,
            DispositivoController dispositivoController,
            TicketController ticketController,
            ReporteController reporteController,
            AgenteController agenteController,
            AsignacionController asignacionController,
            MensajeController mensajeController,
            MantenimientoController mantenimientoController,
            AnsController ansController,
            Usuario usuarioActual,
            Runnable onCerrarSesion
    ) {
        this.usuarioController = usuarioController;
        this.dispositivoController = dispositivoController;
        this.ticketController = ticketController;
        this.reporteController = reporteController;
        this.agenteController = agenteController;
        this.asignacionController = asignacionController;
        this.mensajeController = mensajeController;
        this.mantenimientoController = mantenimientoController;
        this.ansController = ansController;
        this.usuarioActual = usuarioActual;
        this.onCerrarSesion = onCerrarSesion;

        setTitle("Sistema de Inventario de Equipamiento");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 720));
        setSize(1280, 820);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UiTheme.BACKGROUND);
        add(crearLayoutPrincipal(), BorderLayout.CENTER);
        mostrarModulo(CARD_DASHBOARD);
    }

    private JPanel crearLayoutPrincipal() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiTheme.BACKGROUND);
        root.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        root.add(crearSidebar(), BorderLayout.WEST);
        root.add(crearContenido(), BorderLayout.CENTER);
        return root;
    }

    private JPanel crearSidebar() {
        UiTheme.RoundedPanel sidebar = new UiTheme.RoundedPanel(20, UiTheme.PRIMARY_DARK);
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setLayout(new BorderLayout());
        sidebar.setBorder(BorderFactory.createEmptyBorder(22, 18, 18, 18));

        JPanel brand = new JPanel(new BorderLayout(12, 0));
        brand.setOpaque(false);

        JLabel logo = new JLabel("SI");
        logo.setOpaque(true);
        logo.setBackground(UiTheme.ACCENT);
        logo.setForeground(Color.WHITE);
        logo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        logo.setHorizontalAlignment(JLabel.CENTER);
        logo.setPreferredSize(new Dimension(50, 50));
        brand.add(logo, BorderLayout.WEST);

        JPanel brandText = new JPanel(new GridBagLayout());
        brandText.setOpaque(false);
        GridBagConstraints brandConstraints = new GridBagConstraints();
        brandConstraints.gridx = 0;
        brandConstraints.anchor = GridBagConstraints.WEST;
        JLabel title = new JLabel("Inventario");
        title.setForeground(Color.WHITE);
        title.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 19));
        brandText.add(title, brandConstraints);
        brandConstraints.gridy = 1;
        JLabel subtitle = new JLabel("Equipamiento");
        subtitle.setForeground(new Color(191, 219, 254));
        subtitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        brandText.add(subtitle, brandConstraints);
        brand.add(brandText, BorderLayout.CENTER);

        JPanel menu = new JPanel(new GridBagLayout());
        menu.setOpaque(false);
        menu.setBorder(BorderFactory.createEmptyBorder(22, 0, 10, 0));
        agregarModulo(menu, CARD_DASHBOARD, "Dashboard", 0);
        agregarModulo(menu, CARD_EQUIPOS, "Equipos", 1);
        agregarModulo(menu, CARD_CATEGORIAS, "Categorias", 2);
        agregarModulo(menu, CARD_USUARIOS, "Usuarios", 3);
        agregarModulo(menu, CARD_AGENTES, "Agentes", 4);
        agregarModulo(menu, CARD_ASIGNACIONES, "Asignaciones", 5);
        agregarModulo(menu, CARD_MESA_AYUDA, "Mesa de Ayuda", 6);
        agregarModulo(menu, CARD_MANTENIMIENTOS, "Mantenimientos", 7);
        agregarModulo(menu, CARD_SOLICITUDES, "Solicitudes", 8);
        agregarModulo(menu, CARD_MENSAJES, "Mensajes", 9);
        agregarModulo(menu, CARD_ANS, "ANS", 10);
        agregarModulo(menu, CARD_REPORTES, "Reportes", 11);
        agregarModulo(menu, CARD_CONFIGURACION, "Configuracion", 12);

        JScrollPane menuScroll = new JScrollPane(menu);
        menuScroll.setOpaque(false);
        menuScroll.getViewport().setOpaque(false);
        menuScroll.setBorder(BorderFactory.createEmptyBorder());
        menuScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        menuScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        menuScroll.getVerticalScrollBar().setUnitIncrement(16);

        sidebar.add(brand, BorderLayout.NORTH);
        sidebar.add(menuScroll, BorderLayout.CENTER);
        sidebar.add(crearFooterSesion(), BorderLayout.SOUTH);
        return sidebar;
    }

    private JPanel crearFooterSesion() {
        JPanel footer = new JPanel(new BorderLayout(0, 14));
        footer.setOpaque(false);

        JLabel info = new JLabel("<html>Gestion operativa<br>R02 - R14</html>");
        info.setForeground(new Color(147, 197, 253));
        info.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JButton cerrarSesionButton = new JButton("Cerrar sesion");
        cerrarSesionButton.setFocusPainted(false);
        cerrarSesionButton.setBorderPainted(false);
        cerrarSesionButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cerrarSesionButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cerrarSesionButton.setForeground(new Color(185, 28, 28));
        cerrarSesionButton.setBackground(new Color(254, 226, 226));
        cerrarSesionButton.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        cerrarSesionButton.addActionListener(event -> cerrarSesion());

        footer.add(info, BorderLayout.NORTH);
        footer.add(cerrarSesionButton, BorderLayout.SOUTH);
        return footer;
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas cerrar sesion?",
                "Cerrar sesion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
            if (onCerrarSesion != null) {
                SwingUtilities.invokeLater(onCerrarSesion);
            }
        }
    }

    private void agregarModulo(JPanel menu, String card, String label, int fila) {
        if (!moduloPermitido(card)) {
            return;
        }

        JButton button = UiTheme.menuButton(label);
        button.addActionListener(event -> mostrarModulo(card));
        menuButtons.put(card, button);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = fila;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 0, 8, 0);
        menu.add(button, constraints);
    }

    private boolean moduloPermitido(String card) {
        int rol = usuarioActual == null ? 0 : usuarioActual.getIdRol();
        if (rol <= 0 || rol == 1) {
            return true;
        }

        if (rol == 2) {
            return Arrays.asList(
                    CARD_DASHBOARD, CARD_EQUIPOS, CARD_ASIGNACIONES, CARD_MESA_AYUDA,
                    CARD_SOLICITUDES, CARD_MENSAJES, CARD_ANS, CARD_REPORTES
            ).contains(card);
        }

        if (rol == 3) {
            return Arrays.asList(
                    CARD_DASHBOARD, CARD_EQUIPOS, CARD_ASIGNACIONES, CARD_MESA_AYUDA,
                    CARD_MANTENIMIENTOS, CARD_SOLICITUDES, CARD_MENSAJES, CARD_ANS
            ).contains(card);
        }

        if (rol == 4) {
            return Arrays.asList(CARD_DASHBOARD, CARD_EQUIPOS, CARD_SOLICITUDES, CARD_MENSAJES).contains(card);
        }

        return false;
    }

    private JPanel crearContenido() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));
        panel.add(crearHeader(), BorderLayout.NORTH);

        contentCards.setOpaque(false);
        contentCards.add(crearDashboard(), CARD_DASHBOARD);
        contentCards.add(crearPanelDispositivos(), CARD_EQUIPOS);
        contentCards.add(crearPanelCategorias(), CARD_CATEGORIAS);
        contentCards.add(crearPanelUsuarios(), CARD_USUARIOS);
        contentCards.add(crearPanelAgentes(), CARD_AGENTES);
        contentCards.add(crearPanelAsignaciones(), CARD_ASIGNACIONES);
        contentCards.add(crearPanelMesaAyuda(), CARD_MESA_AYUDA);
        contentCards.add(crearPanelMantenimientos(), CARD_MANTENIMIENTOS);
        contentCards.add(crearPanelTickets(), CARD_SOLICITUDES);
        contentCards.add(crearPanelMensajes(), CARD_MENSAJES);
        contentCards.add(crearPanelAns(), CARD_ANS);
        contentCards.add(crearPanelReportes(), CARD_REPORTES);
        contentCards.add(crearPanelConfiguracion(), CARD_CONFIGURACION);
        panel.add(contentCards, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel text = new JPanel(new GridBagLayout());
        text.setOpaque(false);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.WEST;
        text.add(UiTheme.title("Sistema de Inventario de Equipamiento", 28), constraints);
        constraints.gridy = 1;
        constraints.insets = new Insets(4, 0, 0, 0);
        text.add(UiTheme.muted("Gestion de equipos digitales, usuarios, solicitudes y reportes."), constraints);

        header.add(text, BorderLayout.WEST);
        return header;
    }

    private void mostrarModulo(String card) {
        cardLayout.show(contentCards, card);
        for (Map.Entry<String, JButton> entry : menuButtons.entrySet()) {
            UiTheme.selectedMenuButton(entry.getValue(), entry.getKey().equals(card));
        }
        if (CARD_DASHBOARD.equals(card)) {
            cargarDashboard();
        } else if (CARD_MESA_AYUDA.equals(card)) {
            cargarMesaAyuda();
        }
    }

    private JPanel crearDashboard() {
        JPanel panel = crearModulo("Resumen general de dotacion policial", "Vista principal con indicadores reales del sistema.");
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setOpaque(false);

        JPanel stats = new JPanel(new GridLayout(1, 4, 16, 0));
        stats.setOpaque(false);
        stats.add(crearStatCard("Total Equipos", dashboardTotalEquiposValue, "Inventario registrado", new Color(219, 234, 254)));
        stats.add(crearStatCard("Mantenimiento", dashboardMantenimientoValue, "Equipos en revision", new Color(254, 243, 199)));
        stats.add(crearStatCard("Solicitudes", dashboardSolicitudesValue, "Casos registrados", new Color(254, 226, 226)));
        stats.add(crearStatCard("Cumplimiento ANS", dashboardCumplimientoValue, "Solicitudes cerradas", new Color(220, 252, 231)));
        content.add(stats, BorderLayout.NORTH);

        JPanel middle = new JPanel(new GridLayout(1, 2, 16, 0));
        middle.setOpaque(false);
        middle.add(crearTableCard("Dotacion por categoria", dashboardCategoriasTableModel));
        middle.add(crearTableCard("Solicitudes por estado", dashboardSolicitudesTableModel));
        content.add(middle, BorderLayout.CENTER);

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelUsuarios() {
        JPanel panel = crearModulo("Gestion de Usuarios", "Administracion de usuarios del sistema (R02).");

        JTextField nombreField = crearCampoTexto(18);
        JTextField correoField = crearCampoTexto(18);
        JTextField contrasenaField = crearCampoTexto(18);
        JComboBox<ComboItem> rolCombo = crearCombo();

        JPanel form = crearFormulario();
        agregarCampo(form, "Nombre", nombreField, 0);
        agregarCampo(form, "Correo", correoField, 1);
        agregarCampo(form, "Contrasena hash", contrasenaField, 2);
        agregarCampo(form, "Rol", rolCombo, 3);

        JButton guardarButton = UiTheme.primaryButton("Nuevo Usuario");
        JButton listarButton = UiTheme.secondaryButton("Listar usuarios");
        JPanel acciones = crearAcciones(guardarButton, listarButton);

        JTable tabla = crearTabla(usuariosTableModel);
        panel.add(crearZonaTrabajo(form, acciones, UiTheme.scroll(tabla), "Formulario de usuario"), BorderLayout.CENTER);

        guardarButton.addActionListener(event -> {
            try {
                Usuario usuario = usuarioController.registrarUsuario(
                        nombreField.getText().trim(),
                        correoField.getText().trim(),
                        contrasenaField.getText().trim(),
                        idSeleccionado(rolCombo, "un rol")
                );
                mostrarMensaje("Usuario registrado correctamente.");
                limpiar(nombreField, correoField, contrasenaField);
                rolCombo.setSelectedIndex(0);
                cargarUsuarios();
            } catch (IllegalArgumentException exception) {
                mostrarError(exception.getMessage());
            } catch (SQLException exception) {
                mostrarError("No se pudo registrar el usuario: " + exception.getMessage());
            }
        });

        listarButton.addActionListener(event -> cargarUsuarios());
        cargarRoles(rolCombo);
        return panel;
    }

    private JPanel crearPanelDispositivos() {
        JPanel panel = crearModulo("Equipos Digitales", "Inventario Digital de todos los equipos tecnologicos.");

        JTextField nombreField = crearCampoTexto(14);
        JTextField serialField = crearCampoTexto(14);
        JTextField marcaField = crearCampoTexto(14);
        JTextField modeloField = crearCampoTexto(14);
        JTextField categoriaField = crearCampoTexto(14);
        JTextField estadoField = crearCampoTexto(14);
        JTextField ubicacionField = crearCampoTexto(14);
        JTextField filtroField = crearCampoTexto(14);
        JComboBox<ComboItem> equipoEstadoCombo = crearCombo();
        JTextField nuevoEstadoField = crearCampoTexto(14);

        JPanel form = crearFormulario();
        agregarCampo(form, "Equipo", nombreField, 0);
        agregarCampo(form, "Serial", serialField, 1);
        agregarCampo(form, "Marca", marcaField, 2);
        agregarCampo(form, "Modelo", modeloField, 3);
        agregarCampo(form, "Categoria", categoriaField, 4);
        agregarCampo(form, "Estado", estadoField, 5);
        agregarCampo(form, "Ubicacion", ubicacionField, 6);
        agregarCampo(form, "Filtro inventario", filtroField, 7);
        agregarCampo(form, "Equipo a actualizar", equipoEstadoCombo, 8);
        agregarCampo(form, "Nuevo estado", nuevoEstadoField, 9);

        JButton guardarButton = UiTheme.primaryButton("Nuevo Equipo");
        JButton listarButton = UiTheme.secondaryButton("Ver equipos");
        JButton filtrarButton = UiTheme.secondaryButton("Filtrar");
        JButton estadoButton = UiTheme.secondaryButton("Actualizar estado");
        JPanel acciones = crearAcciones(guardarButton, listarButton, filtrarButton, estadoButton);

        JTable tabla = crearTabla(dispositivosTableModel);
        panel.add(crearZonaTrabajo(form, acciones, UiTheme.scroll(tabla), "Registro de equipo"), BorderLayout.CENTER);

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
                mostrarMensaje("Equipo registrado correctamente.");
                limpiar(nombreField, serialField, marcaField, modeloField, categoriaField, estadoField, ubicacionField);
                cargarDispositivos();
                cargarDispositivosCombo(equipoEstadoCombo);
            } catch (SQLException exception) {
                mostrarError("No se pudo registrar el dispositivo: " + exception.getMessage());
            }
        });

        listarButton.addActionListener(event -> {
            cargarDispositivos();
            cargarDispositivosCombo(equipoEstadoCombo);
        });
        filtrarButton.addActionListener(event -> cargarDispositivos(filtroField.getText()));
        estadoButton.addActionListener(event -> {
            try {
                boolean actualizado = dispositivoController.actualizarEstado(
                        idSeleccionado(equipoEstadoCombo, "un equipo"),
                        nuevoEstadoField.getText().trim()
                );
                mostrarMensaje(actualizado ? "Estado actualizado." : "No se encontro el equipo.");
                limpiar(nuevoEstadoField);
                cargarDispositivos();
                cargarDispositivosCombo(equipoEstadoCombo);
            } catch (IllegalArgumentException exception) {
                mostrarError(exception.getMessage());
            } catch (SQLException exception) {
                mostrarError("No se pudo actualizar el estado: " + exception.getMessage());
            }
        });
        cargarDispositivosCombo(equipoEstadoCombo);
        return panel;
    }

    private JPanel crearPanelTickets() {
        JPanel panel = crearModulo("Gestion de Solicitudes", "Asignacion y seguimiento de solicitudes con control de ANS (R11, R12, R13).");

        JTextField asuntoField = crearCampoTexto(16);
        JTextArea descripcionArea = new JTextArea(3, 16);
        descripcionArea.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        descripcionArea.setLineWrap(true);
        descripcionArea.setWrapStyleWord(true);
        JTextField tipoField = crearCampoTexto(16);
        JTextField prioridadField = crearCampoTexto(16);
        JComboBox<ComboItem> agenteCombo = crearCombo();
        JComboBox<ComboItem> ansCombo = crearCombo();
        JComboBox<ComboItem> ticketAsignarCombo = crearCombo();
        JComboBox<ComboItem> coordinadorCombo = crearCombo();
        JComboBox<ComboItem> tecnicoCombo = crearCombo();
        JComboBox<ComboItem> ticketCerrarCombo = crearCombo();
        JTextField evidenciaField = crearCampoTexto(16);

        JPanel form = crearFormulario();
        agregarCampo(form, "Tipo de Solicitud", tipoField, 0);
        agregarCampo(form, "Asunto", asuntoField, 1);
        agregarCampo(form, "Descripcion", UiTheme.scroll(descripcionArea), 2);
        agregarCampo(form, "Prioridad", prioridadField, 3);
        agregarCampo(form, "Agente", agenteCombo, 4);
        agregarCampo(form, "Acuerdo ANS", ansCombo, 5);
        agregarCampo(form, "Solicitud a asignar", ticketAsignarCombo, 6);
        agregarCampo(form, "Coordinador", coordinadorCombo, 7);
        agregarCampo(form, "Tecnico", tecnicoCombo, 8);
        agregarCampo(form, "Solicitud a cerrar", ticketCerrarCombo, 9);
        agregarCampo(form, "Evidencia", evidenciaField, 10);

        JButton guardarButton = UiTheme.primaryButton("Nueva Solicitud");
        JButton listarButton = UiTheme.secondaryButton("Panel de Solicitudes");
        JButton asignarButton = UiTheme.secondaryButton("Asignar Tecnico");
        JButton cerrarButton = UiTheme.secondaryButton("Cerrar");
        JPanel acciones = crearAcciones(guardarButton, listarButton, asignarButton, cerrarButton);

        JTable tabla = crearTabla(ticketsTableModel);
        panel.add(crearZonaTrabajo(form, acciones, UiTheme.scroll(tabla), "Solicitud y ANS"), BorderLayout.CENTER);

        guardarButton.addActionListener(event -> {
            try {
                Ticket ticket = ticketController.registrarTicket(
                        asuntoField.getText().trim(),
                        descripcionArea.getText().trim(),
                        tipoField.getText().trim(),
                        prioridadField.getText().trim(),
                        idSeleccionado(agenteCombo, "un agente"),
                        idSeleccionado(ansCombo, "un acuerdo ANS")
                );
                mostrarMensaje("Solicitud registrada correctamente.");
                limpiar(asuntoField, tipoField, prioridadField);
                descripcionArea.setText("");
                cargarTickets();
                cargarTicketsCombo(ticketAsignarCombo);
                cargarTicketsCombo(ticketCerrarCombo);
            } catch (IllegalArgumentException exception) {
                mostrarError(exception.getMessage());
            } catch (SQLException exception) {
                mostrarError("No se pudo registrar la solicitud: " + exception.getMessage());
            }
        });

        listarButton.addActionListener(event -> {
            cargarTickets();
            cargarTicketsCombo(ticketAsignarCombo);
            cargarTicketsCombo(ticketCerrarCombo);
        });

        asignarButton.addActionListener(event -> {
            try {
                int idTicket = idSeleccionado(ticketAsignarCombo, "una solicitud");
                int idCoordinador = idSeleccionado(coordinadorCombo, "un coordinador");
                int idTecnico = idSeleccionado(tecnicoCombo, "un tecnico");
                boolean asignado = ticketController.asignarTecnico(
                        idTicket,
                        idCoordinador,
                        idTecnico
                );
                if (asignado) {
                    mensajeController.enviarMensaje(
                            usuarioActual.getId(),
                            idTecnico,
                            "Solicitud asignada",
                            "Se te asigno una solicitud."
                    );
                }
                mostrarMensaje(asignado ? "Solicitud asignada." : "No se encontro la solicitud.");
                cargarTickets();
                cargarTicketsCombo(ticketAsignarCombo);
                cargarTicketsCombo(ticketCerrarCombo);
            } catch (IllegalArgumentException exception) {
                mostrarError(exception.getMessage());
            } catch (SQLException exception) {
                mostrarError("No se pudo asignar la solicitud: " + exception.getMessage());
            }
        });

        cerrarButton.addActionListener(event -> {
            try {
                boolean cerrado = ticketController.cerrarTicket(
                        idSeleccionado(ticketCerrarCombo, "una solicitud"),
                        evidenciaField.getText().trim()
                );
                mostrarMensaje(cerrado ? "Solicitud cerrada." : "No se encontro la solicitud.");
                cargarTickets();
                cargarTicketsCombo(ticketAsignarCombo);
                cargarTicketsCombo(ticketCerrarCombo);
            } catch (IllegalArgumentException exception) {
                mostrarError(exception.getMessage());
            } catch (SQLException exception) {
                mostrarError("No se pudo cerrar la solicitud: " + exception.getMessage());
            }
        });

        cargarAgentesCombo(agenteCombo);
        cargarAnsCombo(ansCombo);
        cargarTicketsCombo(ticketAsignarCombo);
        cargarTicketsCombo(ticketCerrarCombo);
        cargarUsuariosCombo(coordinadorCombo);
        cargarUsuariosCombo(tecnicoCombo);
        return panel;
    }

    private JPanel crearPanelCategorias() {
        JPanel panel = crearModulo("Categorias de Equipos Digitales", "Resumen de equipos por tipo tecnologico.");
        panel.add(crearTablaMockup(
                "Resumen de Equipos Digitales",
                new String[]{"Categoria", "Descripcion", "Equipos", "Estado"},
                new Object[][]{
                        {"Computadores", "Equipos de escritorio y portatiles", "38", "Activo"},
                        {"Radios", "Radios y equipos de radio", "24", "Activo"},
                        {"Tablets", "Dispositivos moviles institucionales", "31", "Activo"},
                        {"Impresoras", "Impresion y escaneo", "12", "Revision"}
                }
        ), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelAgentes() {
        JPanel panel = crearModulo("Registro de Agentes", "Gestion de agentes responsables de dispositivos (R03).");

        JComboBox<ComboItem> usuarioCombo = crearCombo();
        JTextField nombreField = crearCampoTexto(14);
        JTextField correoField = crearCampoTexto(14);
        JTextField cargoField = crearCampoTexto(14);
        JTextField placaField = crearCampoTexto(10);
        JComboBox<ComboItem> eliminarCombo = crearCombo();

        JPanel form = crearFormulario();
        agregarCampo(form, "Usuario asociado", usuarioCombo, 0);
        agregarCampo(form, "Nombre agente", nombreField, 1);
        agregarCampo(form, "Correo", correoField, 2);
        agregarCampo(form, "Cargo", cargoField, 3);
        agregarCampo(form, "Placa", placaField, 4);
        agregarCampo(form, "Agente a eliminar", eliminarCombo, 5);

        JButton guardarButton = UiTheme.primaryButton("Nuevo Agente");
        JButton listarButton = UiTheme.secondaryButton("Listar agentes");
        JButton eliminarButton = UiTheme.secondaryButton("Eliminar");
        JPanel acciones = crearAcciones(guardarButton, listarButton, eliminarButton);

        JTable tabla = crearTabla(agentesTableModel);
        panel.add(crearZonaTrabajo(form, acciones, UiTheme.scroll(tabla), "Formulario de agente"), BorderLayout.CENTER);

        guardarButton.addActionListener(event -> {
            try {
                Agente agente = agenteController.registrarAgente(
                        idSeleccionado(usuarioCombo, "un usuario"),
                        nombreField.getText().trim(),
                        correoField.getText().trim(),
                        cargoField.getText().trim(),
                        placaField.getText().trim()
                );
                mostrarMensaje("Agente registrado correctamente.");
                limpiar(nombreField, correoField, cargoField, placaField);
                cargarAgentes();
                cargarAgentesCombo(eliminarCombo);
            } catch (IllegalArgumentException exception) {
                mostrarError(exception.getMessage());
            } catch (SQLException exception) {
                mostrarError("No se pudo registrar el agente: " + exception.getMessage());
            }
        });

        listarButton.addActionListener(event -> {
            cargarAgentes();
            cargarAgentesCombo(eliminarCombo);
        });
        eliminarButton.addActionListener(event -> {
            try {
                boolean eliminado = agenteController.eliminarAgente(idSeleccionado(eliminarCombo, "un agente"));
                mostrarMensaje(eliminado ? "Agente eliminado." : "No se encontro el agente.");
                cargarAgentes();
                cargarAgentesCombo(eliminarCombo);
            } catch (IllegalArgumentException exception) {
                mostrarError(exception.getMessage());
            } catch (SQLException exception) {
                mostrarError("No se pudo eliminar el agente: " + exception.getMessage());
            }
        });
        cargarUsuariosCombo(usuarioCombo);
        cargarAgentesCombo(eliminarCombo);
        return panel;
    }

    private JPanel crearPanelAsignaciones() {
        JPanel panel = crearModulo("Asignaciones de Equipos", "Control de asignacion y devolucion de dispositivos (R06, R07, R10).");

        JComboBox<ComboItem> agenteCombo = crearCombo();
        JComboBox<ComboItem> dispositivoCombo = crearCombo();
        JTextField observacionesField = crearCampoTexto(14);
        JComboBox<ComboItem> devolucionCombo = crearCombo();
        JTextField observacionDevolucionField = crearCampoTexto(14);
        JComboBox<ComboItem> historialDispositivoCombo = crearCombo();

        JPanel form = crearFormulario();
        agregarCampo(form, "Agente", agenteCombo, 0);
        agregarCampo(form, "Dispositivo", dispositivoCombo, 1);
        agregarCampo(form, "Observaciones asignacion", observacionesField, 2);
        agregarCampo(form, "Asignacion a devolver", devolucionCombo, 3);
        agregarCampo(form, "Observaciones devolucion", observacionDevolucionField, 4);
        agregarCampo(form, "Dispositivo historial", historialDispositivoCombo, 5);

        JButton asignarButton = UiTheme.primaryButton("Asignar");
        JButton devolverButton = UiTheme.secondaryButton("Registrar devolucion");
        JButton listarButton = UiTheme.secondaryButton("Listar");
        JButton historialButton = UiTheme.secondaryButton("Historial");
        JPanel acciones = crearAcciones(asignarButton, devolverButton, listarButton, historialButton);

        JTable asignacionesTable = crearTabla(asignacionesTableModel);
        JTable historialTable = crearTabla(historialTableModel);
        JPanel tablas = new JPanel(new GridLayout(2, 1, 0, 16));
        tablas.setOpaque(false);
        tablas.add(UiTheme.scroll(asignacionesTable));
        tablas.add(UiTheme.scroll(historialTable));
        panel.add(crearZonaTrabajo(form, acciones, new JScrollPane(tablas), "Asignacion y devolucion"), BorderLayout.CENTER);

        asignarButton.addActionListener(event -> {
            try {
                Asignacion asignacion = asignacionController.asignarDispositivo(
                        idSeleccionado(agenteCombo, "un agente"),
                        idSeleccionado(dispositivoCombo, "un dispositivo"),
                        usuarioActual.getId(),
                        observacionesField.getText().trim()
                );
                mostrarMensaje("Asignacion registrada correctamente.");
                limpiar(observacionesField);
                cargarAsignaciones();
                cargarAsignacionesCombo(devolucionCombo);
                cargarDispositivosCombo(dispositivoCombo);
            } catch (IllegalArgumentException exception) {
                mostrarError(exception.getMessage());
            } catch (SQLException exception) {
                mostrarError("No se pudo asignar el dispositivo: " + exception.getMessage());
            }
        });

        devolverButton.addActionListener(event -> {
            try {
                boolean devuelto = asignacionController.registrarDevolucion(
                        idSeleccionado(devolucionCombo, "una asignacion"),
                        usuarioActual.getId(),
                        observacionDevolucionField.getText().trim()
                );
                mostrarMensaje(devuelto ? "Devolucion registrada." : "No se encontro la asignacion.");
                limpiar(observacionDevolucionField);
                cargarAsignaciones();
                cargarAsignacionesCombo(devolucionCombo);
                cargarDispositivosCombo(dispositivoCombo);
            } catch (IllegalArgumentException exception) {
                mostrarError(exception.getMessage());
            } catch (SQLException exception) {
                mostrarError("No se pudo registrar la devolucion: " + exception.getMessage());
            }
        });

        listarButton.addActionListener(event -> {
            cargarAsignaciones();
            cargarAsignacionesCombo(devolucionCombo);
        });
        historialButton.addActionListener(event -> {
            try {
                cargarHistorial(idSeleccionado(historialDispositivoCombo, "un dispositivo"));
            } catch (IllegalArgumentException exception) {
                mostrarError(exception.getMessage());
            }
        });
        cargarAgentesCombo(agenteCombo);
        cargarDispositivosCombo(dispositivoCombo);
        cargarDispositivosCombo(historialDispositivoCombo);
        cargarAsignacionesCombo(devolucionCombo);
        return panel;
    }

    private JPanel crearPanelMesaAyuda() {
        JPanel panel = crearModulo("Mesa de Ayuda", "Atencion, priorizacion y seguimiento de casos de soporte tecnico.");
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setOpaque(false);

        JPanel stats = new JPanel(new GridLayout(1, 4, 16, 0));
        stats.setOpaque(false);
        stats.add(crearStatCard("Abiertos", mesaAbiertosValue, "Solicitudes activas", new Color(219, 234, 254)));
        stats.add(crearStatCard("Prioridad alta", mesaAltaValue, "Casos criticos", new Color(254, 226, 226)));
        stats.add(crearStatCard("Asignados", mesaAsignadosValue, "Con tecnico", new Color(220, 252, 231)));
        stats.add(crearStatCard("Alertas ANS", mesaAlertasValue, "Seguimiento vencido", new Color(254, 243, 199)));
        content.add(stats, BorderLayout.NORTH);

        JButton cargarButton = UiTheme.primaryButton("Actualizar casos");
        cargarButton.addActionListener(event -> cargarMesaAyuda());
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actions.setOpaque(false);
        actions.add(cargarButton);

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setOpaque(false);
        center.add(actions, BorderLayout.NORTH);
        center.add(crearTableCard("Casos de Soporte", mesaAyudaTableModel), BorderLayout.CENTER);
        content.add(center, BorderLayout.CENTER);

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelMantenimientos() {
        JPanel panel = crearModulo("Mantenimientos", "Programacion y control de mantenimientos preventivos y correctivos.");

        JComboBox<ComboItem> dispositivoCombo = crearCombo();
        JComboBox<ComboItem> tecnicoCombo = crearCombo();
        JTextField tipoField = crearCampoTexto(14);
        JTextField fechaField = crearCampoTexto(14);
        JTextArea descripcionArea = new JTextArea(3, 16);
        descripcionArea.setLineWrap(true);
        descripcionArea.setWrapStyleWord(true);
        descripcionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComboBox<ComboItem> mantenimientoEstadoCombo = crearCombo();
        JTextField estadoField = crearCampoTexto(14);
        JTextField observacionesField = crearCampoTexto(14);

        JPanel form = crearFormulario();
        agregarCampo(form, "Dispositivo", dispositivoCombo, 0);
        agregarCampo(form, "Tecnico", tecnicoCombo, 1);
        agregarCampo(form, "Tipo", tipoField, 2);
        agregarCampo(form, "Fecha programada yyyy-MM-dd HH:mm", fechaField, 3);
        agregarCampo(form, "Descripcion", UiTheme.scroll(descripcionArea), 4);
        agregarCampo(form, "Mantenimiento", mantenimientoEstadoCombo, 5);
        agregarCampo(form, "Nuevo estado", estadoField, 6);
        agregarCampo(form, "Observaciones", observacionesField, 7);

        JButton programarButton = UiTheme.primaryButton("Programar");
        JButton estadoButton = UiTheme.secondaryButton("Actualizar estado");
        JButton listarButton = UiTheme.secondaryButton("Listar");
        JPanel acciones = crearAcciones(programarButton, estadoButton, listarButton);

        JTable tabla = crearTabla(mantenimientosTableModel);
        panel.add(crearZonaTrabajo(form, acciones, UiTheme.scroll(tabla), "Plan de mantenimiento"), BorderLayout.CENTER);

        programarButton.addActionListener(event -> {
            try {
                Mantenimiento mantenimiento = mantenimientoController.programarMantenimiento(
                        idSeleccionado(dispositivoCombo, "un dispositivo"),
                        idSeleccionado(tecnicoCombo, "un tecnico"),
                        tipoField.getText().trim(),
                        descripcionArea.getText().trim(),
                        LocalDateTime.parse(fechaField.getText().trim(), FECHA_HORA)
                );
                mostrarMensaje("Mantenimiento programado correctamente.");
                limpiar(tipoField, fechaField);
                descripcionArea.setText("");
                cargarMantenimientos();
                cargarMantenimientosCombo(mantenimientoEstadoCombo);
            } catch (IllegalArgumentException exception) {
                mostrarError(exception.getMessage());
            } catch (DateTimeParseException exception) {
                mostrarError("La fecha debe tener el formato yyyy-MM-dd HH:mm.");
            } catch (SQLException exception) {
                mostrarError("No se pudo programar el mantenimiento: " + exception.getMessage());
            }
        });

        estadoButton.addActionListener(event -> {
            try {
                boolean actualizado = mantenimientoController.actualizarEstado(
                        idSeleccionado(mantenimientoEstadoCombo, "un mantenimiento"),
                        estadoField.getText().trim(),
                        observacionesField.getText().trim()
                );
                mostrarMensaje(actualizado ? "Mantenimiento actualizado." : "No se encontro el mantenimiento.");
                limpiar(estadoField, observacionesField);
                cargarMantenimientos();
                cargarMantenimientosCombo(mantenimientoEstadoCombo);
            } catch (IllegalArgumentException exception) {
                mostrarError(exception.getMessage());
            } catch (SQLException exception) {
                mostrarError("No se pudo actualizar el mantenimiento: " + exception.getMessage());
            }
        });

        listarButton.addActionListener(event -> {
            cargarMantenimientos();
            cargarMantenimientosCombo(mantenimientoEstadoCombo);
        });
        cargarDispositivosCombo(dispositivoCombo);
        cargarUsuariosCombo(tecnicoCombo);
        cargarMantenimientosCombo(mantenimientoEstadoCombo);
        return panel;
    }

    private JPanel crearPanelMensajes() {
        JPanel panel = crearModulo("Mensajes a Agentes", "Envio de notificaciones y mensajes informativos (R09).");

        JComboBox<ComboItem> destinatarioCombo = crearCombo();
        JTextField asuntoField = crearCampoTexto(14);
        JTextArea contenidoArea = new JTextArea(5, 16);
        contenidoArea.setLineWrap(true);
        contenidoArea.setWrapStyleWord(true);
        contenidoArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel form = crearFormulario();
        agregarCampo(form, "Destinatario", destinatarioCombo, 0);
        agregarCampo(form, "Asunto", asuntoField, 1);
        agregarCampo(form, "Contenido", UiTheme.scroll(contenidoArea), 2);

        JButton enviarButton = UiTheme.primaryButton("Enviar");
        JButton listarButton = UiTheme.secondaryButton("Listar mensajes");
        JPanel acciones = crearAcciones(enviarButton, listarButton);

        JTable tabla = crearTabla(mensajesTableModel);
        panel.add(crearZonaTrabajo(form, acciones, UiTheme.scroll(tabla), "Nuevo mensaje"), BorderLayout.CENTER);

        enviarButton.addActionListener(event -> {
            try {
                Mensaje mensaje = mensajeController.enviarMensaje(
                        usuarioActual.getId(),
                        idSeleccionado(destinatarioCombo, "un destinatario"),
                        asuntoField.getText().trim(),
                        contenidoArea.getText().trim()
                );
                mostrarMensaje("Mensaje enviado correctamente.");
                limpiar(asuntoField);
                contenidoArea.setText("");
                cargarMensajes();
            } catch (IllegalArgumentException exception) {
                mostrarError(exception.getMessage());
            } catch (SQLException exception) {
                mostrarError("No se pudo enviar el mensaje: " + exception.getMessage());
            }
        });

        listarButton.addActionListener(event -> {
            cargarMensajes();
            cargarUsuariosCombo(destinatarioCombo);
        });
        cargarUsuariosCombo(destinatarioCombo);
        return panel;
    }

    private JPanel crearPanelAns() {
        JPanel panel = crearModulo("Acuerdos de Nivel de Servicio (ANS)", "Configuracion de tiempos de respuesta y solucion (R14).");

        JComboBox<ComboItem> ansEditarCombo = crearCombo();
        JTextField tipoField = crearCampoTexto(14);
        JTextField prioridadField = crearCampoTexto(14);
        JTextField respuestaField = crearCampoTexto(8);
        JTextField solucionField = crearCampoTexto(8);
        JTextField descripcionField = crearCampoTexto(14);
        JTextField activoField = crearCampoTexto(8);

        JPanel form = crearFormulario();
        agregarCampo(form, "ANS a editar", ansEditarCombo, 0);
        agregarCampo(form, "Tipo solicitud", tipoField, 1);
        agregarCampo(form, "Prioridad", prioridadField, 2);
        agregarCampo(form, "Respuesta horas", respuestaField, 3);
        agregarCampo(form, "Solucion horas", solucionField, 4);
        agregarCampo(form, "Descripcion", descripcionField, 5);
        agregarCampo(form, "Activo true/false", activoField, 6);

        JButton guardarButton = UiTheme.primaryButton("Guardar ANS");
        JButton listarButton = UiTheme.secondaryButton("Listar ANS");
        JPanel acciones = crearAcciones(guardarButton, listarButton);

        JTable tabla = crearTabla(ansTableModel);
        panel.add(crearZonaTrabajo(form, acciones, UiTheme.scroll(tabla), "Acuerdo ANS"), BorderLayout.CENTER);

        guardarButton.addActionListener(event -> {
            try {
                Ans ans = ansController.guardarAns(
                        idSeleccionadoPermiteNuevo(ansEditarCombo),
                        tipoField.getText().trim(),
                        prioridadField.getText().trim(),
                        Integer.parseInt(respuestaField.getText().trim()),
                        Integer.parseInt(solucionField.getText().trim()),
                        descripcionField.getText().trim(),
                        Boolean.parseBoolean(activoField.getText().trim())
                );
                mostrarMensaje("ANS guardado correctamente.");
                limpiar(tipoField, prioridadField, respuestaField, solucionField, descripcionField, activoField);
                cargarAns();
                cargarAnsComboConNuevo(ansEditarCombo);
            } catch (NumberFormatException exception) {
                mostrarError("Respuesta y solucion deben ser numericos.");
            } catch (IllegalArgumentException exception) {
                mostrarError(exception.getMessage());
            } catch (SQLException exception) {
                mostrarError("No se pudo guardar el ANS: " + exception.getMessage());
            }
        });

        listarButton.addActionListener(event -> {
            cargarAns();
            cargarAnsComboConNuevo(ansEditarCombo);
        });
        cargarAnsComboConNuevo(ansEditarCombo);
        return panel;
    }

    private JPanel crearPanelReportes() {
        JPanel panel = crearModulo("Reportes e Indicadores", "Indicadores reales de inventario, solicitudes y cumplimiento ANS.");

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setOpaque(false);

        JButton cargarButton = UiTheme.primaryButton("Cargar indicadores");
        cargarButton.addActionListener(event -> cargarReportes());
        JButton exportarButton = UiTheme.secondaryButton("Exportar PDF");
        exportarButton.addActionListener(event -> exportarReportesPdf());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actions.setOpaque(false);
        actions.add(cargarButton);
        actions.add(exportarButton);
        content.add(actions, BorderLayout.NORTH);

        JPanel grids = new JPanel(new GridLayout(2, 2, 16, 16));
        grids.setOpaque(false);
        grids.add(crearTableCard("Indicadores generales", reporteIndicadoresTableModel));
        grids.add(crearTableCard("Equipos por categoria", reporteCategoriasTableModel));
        grids.add(crearTableCard("Solicitudes por estado", reporteSolicitudesTableModel));
        grids.add(crearTableCard("Cumplimiento ANS por tecnico", reporteCumplimientoTableModel));
        content.add(grids, BorderLayout.CENTER);

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelConfiguracion() {
        JPanel panel = crearModulo("Configuracion", "Ajustes del sistema y preferencias.");
        panel.add(crearTablaMockup(
                "Preferencias del Sistema",
                new String[]{"Opcion", "Descripcion", "Estado"},
                new Object[][]{
                        {"Perfil de Usuario", "Datos de sesion y preferencias", "Activo"},
                        {"Alertas de mantenimiento", "Notificar mantenimientos vencidos", "Activo"},
                        {"Notificar equipos nuevos", "Avisos de nuevo inventario", "Activo"},
                        {"Reportes automaticos", "Generacion programada", "Pendiente"}
                }
        ), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearModulo(String titulo, String descripcion) {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        UiTheme.RoundedPanel header = UiTheme.card(16, 20, 24, 20, 24);
        header.setLayout(new BorderLayout());

        JPanel texto = new JPanel(new GridBagLayout());
        texto.setOpaque(false);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.WEST;
        texto.add(UiTheme.title(titulo, 22), constraints);
        constraints.gridy = 1;
        constraints.insets = new Insets(4, 0, 0, 0);
        texto.add(UiTheme.muted(descripcion), constraints);
        header.add(texto, BorderLayout.WEST);

        panel.add(header, BorderLayout.NORTH);
        return panel;
    }

    private JPanel crearStatCard(String titulo, String valor, String detalle, Color color) {
        return crearStatCard(titulo, UiTheme.title(valor, 30), detalle, color);
    }

    private JPanel crearStatCard(String titulo, JLabel valor, String detalle, Color color) {
        UiTheme.RoundedPanel card = new UiTheme.RoundedPanel(16, color);
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        card.add(UiTheme.muted(titulo), constraints);
        constraints.gridy = 1;
        constraints.insets = new Insets(6, 0, 2, 0);
        card.add(valor, constraints);
        constraints.gridy = 2;
        constraints.insets = new Insets(0, 0, 0, 0);
        card.add(UiTheme.muted(detalle), constraints);
        return card;
    }

    private JPanel crearTablaMockup(String titulo, String[] columnas, Object[][] filas) {
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
        for (Object[] fila : filas) {
            model.addRow(fila);
        }
        return crearTableCard(titulo, model);
    }

    private JPanel crearTableCard(String titulo, DefaultTableModel model) {
        JTable table = crearTabla(model);
        UiTheme.RoundedPanel card = UiTheme.card(16, 18, 18, 18, 18);
        card.setLayout(new BorderLayout(0, 12));
        card.add(UiTheme.title(titulo, 18), BorderLayout.NORTH);
        card.add(UiTheme.scroll(table), BorderLayout.CENTER);
        return card;
    }

    private JPanel crearZonaTrabajo(JPanel form, JPanel acciones, JScrollPane tabla, String formTitle) {
        JPanel zona = new JPanel(new BorderLayout(16, 0));
        zona.setOpaque(false);

        UiTheme.RoundedPanel formCard = UiTheme.card(16, 18, 18, 18, 18);
        formCard.setLayout(new BorderLayout(0, 14));
        formCard.setPreferredSize(new Dimension(345, 0));
        formCard.add(UiTheme.title(formTitle, 18), BorderLayout.NORTH);
        formCard.add(crearScrollFormulario(form), BorderLayout.CENTER);
        formCard.add(acciones, BorderLayout.SOUTH);

        UiTheme.RoundedPanel tableCard = UiTheme.card(16, 18, 18, 18, 18);
        tableCard.setLayout(new BorderLayout(0, 12));
        tableCard.add(UiTheme.title("Registros", 18), BorderLayout.NORTH);
        tableCard.add(tabla, BorderLayout.CENTER);

        zona.add(formCard, BorderLayout.WEST);
        zona.add(tableCard, BorderLayout.CENTER);
        return zona;
    }

    private JScrollPane crearScrollFormulario(JPanel form) {
        JScrollPane scrollPane = new JScrollPane(form);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        return panel;
    }

    private JPanel crearAcciones(JButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setOpaque(false);
        for (JButton button : buttons) {
            panel.add(button);
        }
        return panel;
    }

    private JTextField crearCampoTexto(int columnas) {
        JTextField field = new JTextField(columnas);
        UiTheme.styleField(field);
        return field;
    }

    private JTable crearTabla(DefaultTableModel model) {
        JTable table = new JTable(model);
        UiTheme.styleTable(table);
        return table;
    }

    private void agregarCampo(JPanel panel, String etiqueta, java.awt.Component campo, int fila) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = fila * 2;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.insets = new Insets(0, 0, 5, 0);

        JLabel label = new JLabel(etiqueta);
        label.setForeground(UiTheme.TEXT);
        label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        panel.add(label, labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 0;
        fieldConstraints.gridy = fila * 2 + 1;
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(0, 0, 10, 0);
        panel.add(campo, fieldConstraints);
    }

    private void cargarUsuarios() {
        try {
            usuariosTableModel.setRowCount(0);
            for (Usuario usuario : usuarioController.listarUsuarios()) {
                usuariosTableModel.addRow(new Object[]{
                        usuario.getNombre(),
                        usuario.getCorreo(),
                        nombreRol(usuario.getIdRol()),
                        usuario.getEstado()
                });
            }
        } catch (SQLException exception) {
            mostrarError("No se pudieron cargar los usuarios: " + exception.getMessage());
        }
    }

    private void cargarDispositivos() {
        cargarDispositivos(null);
    }

    private void cargarDispositivos(String filtro) {
        try {
            dispositivosTableModel.setRowCount(0);
            for (Dispositivo dispositivo : dispositivoController.buscarDispositivos(filtro)) {
                dispositivosTableModel.addRow(new Object[]{
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

    private void cargarAgentes() {
        try {
            Map<Integer, String> usuarios = etiquetasUsuarios();
            agentesTableModel.setRowCount(0);
            for (Agente agente : agenteController.listarAgentes()) {
                agentesTableModel.addRow(new Object[]{
                        usuarios.getOrDefault(agente.getIdUsuario(), "Usuario no registrado"),
                        agente.getNombre(),
                        agente.getCorreo(),
                        agente.getCargo(),
                        agente.getPlaca()
                });
            }
        } catch (SQLException exception) {
            mostrarError("No se pudieron cargar los agentes: " + exception.getMessage());
        }
    }

    private void cargarAsignaciones() {
        try {
            Map<Integer, String> agentes = etiquetasAgentes();
            Map<Integer, String> dispositivos = etiquetasDispositivos();
            Map<Integer, String> usuarios = etiquetasUsuarios();
            asignacionesTableModel.setRowCount(0);
            for (Asignacion asignacion : asignacionController.listarAsignaciones()) {
                asignacionesTableModel.addRow(new Object[]{
                        agentes.getOrDefault(asignacion.getIdAgente(), "Agente no registrado"),
                        dispositivos.getOrDefault(asignacion.getIdDispositivo(), "Equipo no registrado"),
                        usuarios.getOrDefault(asignacion.getIdResponsableEntrega(), "Usuario no registrado"),
                        asignacion.getFechaAsignacion(),
                        asignacion.getFechaDevolucion(),
                        asignacion.getEstadoPrestamo(),
                        asignacion.getObservaciones()
                });
            }
        } catch (SQLException exception) {
            mostrarError("No se pudieron cargar las asignaciones: " + exception.getMessage());
        }
    }

    private void cargarHistorial(int idDispositivo) {
        try {
            Map<Integer, String> dispositivos = etiquetasDispositivos();
            Map<Integer, String> usuarios = etiquetasUsuarios();
            historialTableModel.setRowCount(0);
            for (Object[] fila : asignacionController.listarHistorialDispositivo(idDispositivo)) {
                historialTableModel.addRow(new Object[]{
                        dispositivos.getOrDefault(convertirEntero(fila[1]), "Equipo no registrado"),
                        usuarios.getOrDefault(convertirEntero(fila[2]), "Usuario no registrado"),
                        fila[3],
                        fila[4],
                        fila[5]
                });
            }
        } catch (SQLException exception) {
            mostrarError("No se pudo cargar el historial: " + exception.getMessage());
        }
    }

    private void cargarMensajes() {
        try {
            Map<Integer, String> usuarios = etiquetasUsuarios();
            mensajesTableModel.setRowCount(0);
            for (Mensaje mensaje : mensajeController.listarMensajes()) {
                mensajesTableModel.addRow(new Object[]{
                        usuarios.getOrDefault(mensaje.getIdRemitente(), "Usuario no registrado"),
                        usuarios.getOrDefault(mensaje.getIdDestinatario(), "Usuario no registrado"),
                        mensaje.getAsunto(),
                        mensaje.getFechaEnvio(),
                        mensaje.isLeido() ? "Si" : "No"
                });
            }
        } catch (SQLException exception) {
            mostrarError("No se pudieron cargar los mensajes: " + exception.getMessage());
        }
    }

    private void cargarMantenimientos() {
        try {
            Map<Integer, String> dispositivos = etiquetasDispositivos();
            Map<Integer, String> usuarios = etiquetasUsuarios();
            mantenimientosTableModel.setRowCount(0);
            for (Mantenimiento mantenimiento : mantenimientoController.listarMantenimientos()) {
                mantenimientosTableModel.addRow(new Object[]{
                        dispositivos.getOrDefault(mantenimiento.getIdDispositivo(), "Equipo no registrado"),
                        usuarios.getOrDefault(mantenimiento.getIdTecnico(), "Usuario no registrado"),
                        mantenimiento.getTipo(),
                        mantenimiento.getEstado(),
                        mantenimiento.getFechaProgramada(),
                        mantenimiento.getFechaEjecucion(),
                        mantenimiento.getObservaciones()
                });
            }
        } catch (SQLException exception) {
            mostrarError("No se pudieron cargar los mantenimientos: " + exception.getMessage());
        }
    }

    private void cargarAns() {
        try {
            ansTableModel.setRowCount(0);
            for (Ans ans : ansController.listarAns()) {
                ansTableModel.addRow(new Object[]{
                        ans.getTipoSolicitud(),
                        ans.getPrioridad(),
                        ans.getTiempoRespuestaHoras(),
                        ans.getTiempoSolucionHoras(),
                        ans.isActivo() ? "Si" : "No",
                        ans.getDescripcion()
                });
            }
        } catch (SQLException exception) {
            mostrarError("No se pudieron cargar los ANS: " + exception.getMessage());
        }
    }

    private void cargarMesaAyuda() {
        try {
            Map<Integer, Ans> acuerdos = new HashMap<>();
            for (Ans ans : ansController.listarAns()) {
                acuerdos.put(ans.getId(), ans);
            }
            Map<Integer, String> usuarios = etiquetasUsuarios();

            mesaAyudaTableModel.setRowCount(0);
            int alertas = 0;
            int abiertos = 0;
            int prioridadAlta = 0;
            int asignados = 0;
            for (Ticket ticket : ticketController.listarTickets()) {
                String alerta = calcularAlertaAns(ticket, acuerdos.get(ticket.getIdAns()));
                if (alerta.startsWith("ANS")) {
                    alertas++;
                }
                if (!esEstadoCerrado(ticket.getEstado())) {
                    abiertos++;
                }
                if ("alta".equalsIgnoreCase(ticket.getPrioridad())) {
                    prioridadAlta++;
                }
                if (ticket.getIdTecnico() > 0) {
                    asignados++;
                }
                mesaAyudaTableModel.addRow(new Object[]{
                        ticket.getAsunto(),
                        ticket.getTipoSolicitud(),
                        ticket.getPrioridad(),
                        ticket.getEstado(),
                        ticket.getIdTecnico() <= 0 ? "Pendiente" : usuarios.getOrDefault(ticket.getIdTecnico(), "Usuario no registrado"),
                        alerta
                });
            }
            mesaAbiertosValue.setText(String.valueOf(abiertos));
            mesaAltaValue.setText(String.valueOf(prioridadAlta));
            mesaAsignadosValue.setText(String.valueOf(asignados));
            mesaAlertasValue.setText(String.valueOf(alertas));
            if (alertas > 0) {
                mostrarMensaje("Hay " + alertas + " solicitud(es) que superan tiempos de ANS.");
            }
        } catch (SQLException exception) {
            mostrarError("No se pudo cargar la mesa de ayuda: " + exception.getMessage());
        }
    }

    private boolean esEstadoCerrado(String estado) {
        if (estado == null) {
            return false;
        }
        String normalizado = estado.trim().toLowerCase();
        return normalizado.equals("cerrada")
                || normalizado.equals("cerrado")
                || normalizado.equals("resuelta")
                || normalizado.equals("resuelto");
    }

    private String calcularAlertaAns(Ticket ticket, Ans ans) {
        if (ticket.getFechaCierre() != null) {
            return "Cerrada";
        }
        if (ticket.getFechaCreacion() == null || ans == null) {
            return ticket.getIdTecnico() <= 0 ? "Sin tecnico" : "En seguimiento";
        }

        LocalDateTime ahora = LocalDateTime.now();
        if (ticket.getIdTecnico() <= 0 && ahora.isAfter(ticket.getFechaCreacion().plusHours(ans.getTiempoRespuestaHoras()))) {
            return "ANS respuesta vencido";
        }
        if (ahora.isAfter(ticket.getFechaCreacion().plusHours(ans.getTiempoSolucionHoras()))) {
            return "ANS solucion vencido";
        }
        return ticket.getIdTecnico() <= 0 ? "Sin tecnico" : "Dentro de ANS";
    }

    private void cargarTickets() {
        try {
            Map<Integer, String> agentes = etiquetasAgentes();
            Map<Integer, String> usuarios = etiquetasUsuarios();
            ticketsTableModel.setRowCount(0);
            for (Ticket ticket : ticketController.listarTickets()) {
                ticketsTableModel.addRow(new Object[]{
                        ticket.getAsunto(),
                        ticket.getTipoSolicitud(),
                        ticket.getPrioridad(),
                        ticket.getEstado(),
                        agentes.getOrDefault(ticket.getIdAgente(), "Agente no registrado"),
                        ticket.getIdTecnico() <= 0 ? "Pendiente" : usuarios.getOrDefault(ticket.getIdTecnico(), "Usuario no registrado")
                });
            }
        } catch (SQLException exception) {
            mostrarError("No se pudieron cargar las solicitudes: " + exception.getMessage());
        }
    }

    private void cargarDashboard() {
        try {
            ReporteIndicadores indicadores = reporteController.obtenerIndicadores();
            dashboardTotalEquiposValue.setText(String.valueOf(indicadores.getTotalEquipos()));
            dashboardMantenimientoValue.setText(String.valueOf(indicadores.getEquiposMantenimiento()));
            dashboardSolicitudesValue.setText(String.valueOf(indicadores.getTotalSolicitudes()));
            dashboardCumplimientoValue.setText(formatearPorcentaje(indicadores.getPorcentajeCumplimientoAns()));
            cargarFilas(dashboardCategoriasTableModel, reporteController.listarEquiposPorCategoria());
            cargarFilas(dashboardSolicitudesTableModel, reporteController.listarSolicitudesPorEstado());
        } catch (SQLException exception) {
            mostrarError("No se pudo cargar el dashboard: " + exception.getMessage());
        }
    }

    private void cargarReportes() {
        try {
            ReporteIndicadores indicadores = reporteController.obtenerIndicadores();

            reporteIndicadoresTableModel.setRowCount(0);
            reporteIndicadoresTableModel.addRow(new Object[]{"Total equipos", indicadores.getTotalEquipos()});
            reporteIndicadoresTableModel.addRow(new Object[]{"Equipos en mantenimiento", indicadores.getEquiposMantenimiento()});
            reporteIndicadoresTableModel.addRow(new Object[]{"Total usuarios", indicadores.getTotalUsuarios()});
            reporteIndicadoresTableModel.addRow(new Object[]{"Total solicitudes", indicadores.getTotalSolicitudes()});
            reporteIndicadoresTableModel.addRow(new Object[]{"Solicitudes abiertas", indicadores.getSolicitudesAbiertas()});
            reporteIndicadoresTableModel.addRow(new Object[]{"Solicitudes cerradas", indicadores.getSolicitudesCerradas()});
            reporteIndicadoresTableModel.addRow(new Object[]{"Cumplimiento ANS", formatearPorcentaje(indicadores.getPorcentajeCumplimientoAns())});

            cargarFilas(reporteCategoriasTableModel, reporteController.listarEquiposPorCategoria());
            cargarFilas(reporteSolicitudesTableModel, reporteController.listarSolicitudesPorEstado());

            reporteCumplimientoTableModel.setRowCount(0);
            for (Object[] fila : reporteController.listarCumplimientoPorTecnico()) {
                int total = convertirEntero(fila[1]);
                int dentroAns = convertirEntero(fila[2]);
                double porcentaje = total == 0 ? 0 : (dentroAns * 100.0) / total;
                reporteCumplimientoTableModel.addRow(new Object[]{
                        fila[0],
                        total,
                        dentroAns,
                        formatearPorcentaje(porcentaje)
                });
            }

            mostrarMensaje("Indicadores cargados correctamente.");
        } catch (SQLException exception) {
            mostrarError("No se pudieron cargar los reportes: " + exception.getMessage());
        }
    }

    private void exportarReportesPdf() {
        try {
            if (reporteIndicadoresTableModel.getRowCount() == 0) {
                cargarReportes();
            }

            Path destino = Path.of("reporte_cumplimiento_ans.pdf");
            Map<String, DefaultTableModel> tablas = new LinkedHashMap<>();
            tablas.put("Indicadores generales", reporteIndicadoresTableModel);
            tablas.put("Equipos por categoria", reporteCategoriasTableModel);
            tablas.put("Solicitudes por estado", reporteSolicitudesTableModel);
            tablas.put("Cumplimiento ANS por tecnico", reporteCumplimientoTableModel);
            PdfReportExporter.exportar(destino, "Reporte de cumplimiento ANS e inventario", tablas);
            mostrarMensaje("Reporte PDF exportado en " + destino.toAbsolutePath());
        } catch (IOException exception) {
            mostrarError("No se pudo exportar el reporte: " + exception.getMessage());
        }
    }

    private void cargarFilas(DefaultTableModel model, java.util.List<Object[]> filas) {
        model.setRowCount(0);
        for (Object[] fila : filas) {
            model.addRow(fila);
        }
    }

    private JComboBox<ComboItem> crearCombo() {
        JComboBox<ComboItem> combo = new JComboBox<>();
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setForeground(UiTheme.TEXT);
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(UiTheme.BORDER));
        return combo;
    }

    private void cargarRoles(JComboBox<ComboItem> combo) {
        combo.removeAllItems();
        combo.addItem(new ComboItem(1, "Administrador"));
        combo.addItem(new ComboItem(2, "Coordinador"));
        combo.addItem(new ComboItem(3, "Soporte Tecnico"));
        combo.addItem(new ComboItem(4, "Agente"));
    }

    private void cargarUsuariosCombo(JComboBox<ComboItem> combo) {
        try {
            combo.removeAllItems();
            for (Usuario usuario : usuarioController.listarUsuarios()) {
                combo.addItem(new ComboItem(usuario.getId(), usuario.getNombre() + " - " + usuario.getCorreo()));
            }
            agregarOpcionVaciaSiHaceFalta(combo, "Sin usuarios disponibles");
        } catch (SQLException exception) {
            mostrarError("No se pudieron cargar los usuarios: " + exception.getMessage());
        }
    }

    private void cargarAgentesCombo(JComboBox<ComboItem> combo) {
        try {
            combo.removeAllItems();
            for (Agente agente : agenteController.listarAgentes()) {
                combo.addItem(new ComboItem(agente.getId(), agente.getNombre() + " - " + agente.getPlaca()));
            }
            agregarOpcionVaciaSiHaceFalta(combo, "Sin agentes disponibles");
        } catch (SQLException exception) {
            mostrarError("No se pudieron cargar los agentes: " + exception.getMessage());
        }
    }

    private void cargarDispositivosCombo(JComboBox<ComboItem> combo) {
        try {
            combo.removeAllItems();
            for (Dispositivo dispositivo : dispositivoController.listarDispositivos()) {
                combo.addItem(new ComboItem(dispositivo.getId(), etiquetaDispositivo(dispositivo)));
            }
            agregarOpcionVaciaSiHaceFalta(combo, "Sin equipos disponibles");
        } catch (SQLException exception) {
            mostrarError("No se pudieron cargar los equipos: " + exception.getMessage());
        }
    }

    private void cargarAnsCombo(JComboBox<ComboItem> combo) {
        combo.removeAllItems();
        agregarAns(combo, false);
    }

    private void cargarAnsComboConNuevo(JComboBox<ComboItem> combo) {
        combo.removeAllItems();
        combo.addItem(new ComboItem(0, "Nuevo ANS"));
        agregarAns(combo, true);
    }

    private void agregarAns(JComboBox<ComboItem> combo, boolean permiteNuevo) {
        try {
            int inicial = combo.getItemCount();
            for (Ans ans : ansController.listarAns()) {
                combo.addItem(new ComboItem(ans.getId(), ans.getTipoSolicitud() + " - " + ans.getPrioridad()));
            }
            if (!permiteNuevo && combo.getItemCount() == inicial) {
                combo.addItem(new ComboItem(-1, "Sin acuerdos ANS disponibles"));
            }
        } catch (SQLException exception) {
            mostrarError("No se pudieron cargar los ANS: " + exception.getMessage());
        }
    }

    private void cargarTicketsCombo(JComboBox<ComboItem> combo) {
        try {
            combo.removeAllItems();
            for (Ticket ticket : ticketController.listarTickets()) {
                combo.addItem(new ComboItem(ticket.getId(), ticket.getAsunto() + " - " + ticket.getEstado()));
            }
            agregarOpcionVaciaSiHaceFalta(combo, "Sin solicitudes disponibles");
        } catch (SQLException exception) {
            mostrarError("No se pudieron cargar las solicitudes: " + exception.getMessage());
        }
    }

    private void cargarAsignacionesCombo(JComboBox<ComboItem> combo) {
        try {
            combo.removeAllItems();
            Map<Integer, String> agentes = etiquetasAgentes();
            Map<Integer, String> dispositivos = etiquetasDispositivos();
            for (Asignacion asignacion : asignacionController.listarAsignaciones()) {
                if (!"Activo".equalsIgnoreCase(asignacion.getEstadoPrestamo())) {
                    continue;
                }
                String agente = agentes.getOrDefault(asignacion.getIdAgente(), "Agente no registrado");
                String dispositivo = dispositivos.getOrDefault(asignacion.getIdDispositivo(), "Equipo no registrado");
                combo.addItem(new ComboItem(asignacion.getId(), agente + " / " + dispositivo));
            }
            agregarOpcionVaciaSiHaceFalta(combo, "Sin asignaciones activas");
        } catch (SQLException exception) {
            mostrarError("No se pudieron cargar las asignaciones: " + exception.getMessage());
        }
    }

    private void cargarMantenimientosCombo(JComboBox<ComboItem> combo) {
        try {
            combo.removeAllItems();
            Map<Integer, String> dispositivos = etiquetasDispositivos();
            for (Mantenimiento mantenimiento : mantenimientoController.listarMantenimientos()) {
                String dispositivo = dispositivos.getOrDefault(mantenimiento.getIdDispositivo(), "Equipo no registrado");
                combo.addItem(new ComboItem(
                        mantenimiento.getId(),
                        dispositivo + " - " + mantenimiento.getTipo() + " - " + mantenimiento.getEstado()
                ));
            }
            agregarOpcionVaciaSiHaceFalta(combo, "Sin mantenimientos disponibles");
        } catch (SQLException exception) {
            mostrarError("No se pudieron cargar los mantenimientos: " + exception.getMessage());
        }
    }

    private Map<Integer, String> etiquetasAgentes() throws SQLException {
        Map<Integer, String> agentes = new HashMap<>();
        for (Agente agente : agenteController.listarAgentes()) {
            agentes.put(agente.getId(), agente.getNombre() + " - " + agente.getPlaca());
        }
        return agentes;
    }

    private Map<Integer, String> etiquetasUsuarios() throws SQLException {
        Map<Integer, String> usuarios = new HashMap<>();
        for (Usuario usuario : usuarioController.listarUsuarios()) {
            usuarios.put(usuario.getId(), usuario.getNombre());
        }
        return usuarios;
    }

    private Map<Integer, String> etiquetasDispositivos() throws SQLException {
        Map<Integer, String> dispositivos = new HashMap<>();
        for (Dispositivo dispositivo : dispositivoController.listarDispositivos()) {
            dispositivos.put(dispositivo.getId(), etiquetaDispositivo(dispositivo));
        }
        return dispositivos;
    }

    private String etiquetaDispositivo(Dispositivo dispositivo) {
        return dispositivo.getNombre() + " - " + dispositivo.getSerial();
    }

    private String nombreRol(int idRol) {
        return switch (idRol) {
            case 1 -> "Administrador";
            case 2 -> "Coordinador";
            case 3 -> "Soporte Tecnico";
            case 4 -> "Agente";
            default -> "Sin rol";
        };
    }

    private void agregarOpcionVaciaSiHaceFalta(JComboBox<ComboItem> combo, String etiqueta) {
        if (combo.getItemCount() == 0) {
            combo.addItem(new ComboItem(-1, etiqueta));
        }
    }

    private int idSeleccionado(JComboBox<ComboItem> combo, String nombre) {
        ComboItem item = (ComboItem) combo.getSelectedItem();
        if (item == null || item.id() < 1) {
            throw new IllegalArgumentException("Selecciona " + nombre + ".");
        }
        return item.id();
    }

    private int idSeleccionadoPermiteNuevo(JComboBox<ComboItem> combo) {
        ComboItem item = (ComboItem) combo.getSelectedItem();
        if (item == null || item.id() < 0) {
            throw new IllegalArgumentException("Selecciona un ANS o deja Nuevo ANS.");
        }
        return item.id();
    }

    private int convertirEntero(Object valor) {
        if (valor instanceof Number number) {
            return number.intValue();
        }
        if (valor == null) {
            return 0;
        }
        try {
            return Integer.parseInt(valor.toString());
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    private String formatearPorcentaje(double valor) {
        return String.format("%.1f%%", valor);
    }

    private void limpiar(JTextField... campos) {
        for (JTextField campo : campos) {
            campo.setText("");
        }
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Sistema de Inventario", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void mostrar(MainFrame frame) {
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    private record ComboItem(int id, String texto) {
        @Override
        public String toString() {
            return texto;
        }
    }
}
