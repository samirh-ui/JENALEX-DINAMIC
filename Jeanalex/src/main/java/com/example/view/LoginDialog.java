package com.example.view;

import com.example.controller.UsuarioController;
import com.example.model.Usuario;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Optional;

public class LoginDialog extends JDialog {
    private static final Color LOGIN_BACKGROUND = new Color(232, 240, 255);
    private static final Color LOGIN_BLUE = new Color(35, 64, 154);
    private static final Color LOGIN_TEXT = new Color(15, 32, 61);
    private static final Color LOGIN_MUTED = new Color(108, 123, 145);
    private static final Color LOGIN_BORDER = new Color(203, 213, 225);

    private final UsuarioController usuarioController;
    private final JTextField correoField = new PlaceholderTextField("Ingresa tu usuario");
    private final JPasswordField contrasenaField = new PlaceholderPasswordField("Ingresa tu contraseña");
    private Optional<Usuario> usuarioAutenticado = Optional.empty();

    public LoginDialog(JFrame parent, UsuarioController usuarioController) {
        super(parent, "Control de Dotacion Policial - Iniciar sesion", true);
        this.usuarioController = usuarioController;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(LOGIN_BACKGROUND);
        add(crearContenido(), BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(parent);
    }

    private JPanel crearContenido() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(LOGIN_BACKGROUND);
        wrapper.setBorder(BorderFactory.createEmptyBorder(40, 56, 40, 56));

        UiTheme.RoundedPanel card = UiTheme.card(28, 28, 32, 28, 32);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(448, 580));
        agregarContenidoTarjeta(card);

        wrapper.add(card, BorderLayout.CENTER);
        return wrapper;
    }

    private void agregarContenidoTarjeta(JPanel card) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;

        PoliceIconPanel icon = new PoliceIconPanel();
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(0, 0, 18, 0);
        card.add(icon, constraints);

        JLabel titulo = new JLabel("<html><div style='text-align:center'>Control de Dotación<br>Policial</div></html>");
        titulo.setForeground(LOGIN_TEXT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setHorizontalAlignment(JLabel.CENTER);
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 0, 8, 0);
        card.add(titulo, constraints);

        JLabel subtitulo = new JLabel("Sistema de Inventario de Equipamiento");
        subtitulo.setForeground(new Color(45, 64, 91));
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitulo.setHorizontalAlignment(JLabel.CENTER);
        constraints.gridy = 2;
        constraints.insets = new Insets(0, 0, 34, 0);
        card.add(subtitulo, constraints);

        constraints.gridy = 3;
        constraints.insets = new Insets(0, 0, 22, 0);
        card.add(crearCampoLogin("Usuario", correoField, true), constraints);

        constraints.gridy = 4;
        constraints.insets = new Insets(0, 0, 24, 0);
        card.add(crearCampoLogin("Contraseña", contrasenaField, false), constraints);

        constraints.gridy = 5;
        constraints.insets = new Insets(0, 0, 24, 0);
        card.add(crearAcciones(), constraints);

        JLabel recuperar = new JLabel("¿Olvidaste tu contraseña?");
        recuperar.setForeground(LOGIN_BLUE);
        recuperar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        recuperar.setHorizontalAlignment(JLabel.CENTER);
        recuperar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        recuperar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                JOptionPane.showMessageDialog(
                        LoginDialog.this,
                        "Contacta al administrador del sistema para recuperar tu contraseña.",
                        "Recuperar contraseña",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        constraints.gridy = 6;
        constraints.insets = new Insets(0, 0, 0, 0);
        card.add(recuperar, constraints);
    }

    private JPanel crearCampoLogin(String etiqueta, Component campo, boolean userIcon) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);

        JLabel label = new JLabel(etiqueta);
        label.setForeground(LOGIN_TEXT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(label, BorderLayout.NORTH);

        RoundedInputPanel inputPanel = new RoundedInputPanel();
        inputPanel.setLayout(new BorderLayout(10, 0));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
        inputPanel.setPreferredSize(new Dimension(384, 50));

        JLabel icon = new JLabel(new FieldIcon(userIcon));
        inputPanel.add(icon, BorderLayout.WEST);
        prepararCampo(campo);
        inputPanel.add(campo, BorderLayout.CENTER);

        panel.add(inputPanel, BorderLayout.CENTER);
        return panel;
    }

    private void prepararCampo(Component campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campo.setForeground(new Color(40, 55, 78));
        campo.setBackground(Color.WHITE);
        if (campo instanceof JTextField textField) {
            textField.setOpaque(false);
            textField.setBorder(BorderFactory.createEmptyBorder());
            textField.setCaretColor(LOGIN_BLUE);
        }
    }

    private JPanel crearAcciones() {
        JButton ingresarButton = new JButton("Iniciar Sesión");
        ingresarButton.setBackground(LOGIN_BLUE);
        ingresarButton.setForeground(Color.WHITE);
        ingresarButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        ingresarButton.setFocusPainted(false);
        ingresarButton.setBorderPainted(false);
        ingresarButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ingresarButton.setPreferredSize(new Dimension(384, 48));
        ingresarButton.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        ingresarButton.addActionListener(event -> autenticar());
        getRootPane().setDefaultButton(ingresarButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(ingresarButton, BorderLayout.CENTER);
        return panel;
    }

    private void autenticar() {
        String correo = correoField.getText().trim();
        String contrasena = new String(contrasenaField.getPassword());

        try {
            usuarioAutenticado = usuarioController.autenticar(correo, contrasena);
            if (usuarioAutenticado.isPresent()) {
                dispose();
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Correo o contrasena incorrectos, o el usuario esta inactivo.",
                    "No se pudo iniciar sesion",
                    JOptionPane.WARNING_MESSAGE
            );
            contrasenaField.setText("");
            contrasenaField.requestFocusInWindow();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo consultar la base de datos: " + exception.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalStateException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Configuracion de base de datos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public Optional<Usuario> getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    private static final class PoliceIconPanel extends JPanel {
        PoliceIconPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(64, 64));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(LOGIN_BLUE);
            g.fillOval(0, 0, 64, 64);

            g.setColor(Color.WHITE);
            g.setStroke(new java.awt.BasicStroke(2.4f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
            g.drawRoundRect(20, 29, 24, 17, 5, 5);
            g.drawArc(25, 17, 14, 18, 0, 180);
            g.drawLine(25, 26, 25, 29);
            g.drawLine(39, 26, 39, 29);
            g.dispose();
        }
    }

    private static final class RoundedInputPanel extends JPanel {
        RoundedInputPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.WHITE);
            g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            g.setColor(LOGIN_BORDER);
            g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            g.dispose();
            super.paintComponent(graphics);
        }
    }

    private static final class FieldIcon implements javax.swing.Icon {
        private final boolean user;

        FieldIcon(boolean user) {
            this.user = user;
        }

        @Override
        public int getIconWidth() {
            return 18;
        }

        @Override
        public int getIconHeight() {
            return 18;
        }

        @Override
        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(148, 163, 184));
            g.setStroke(new java.awt.BasicStroke(1.8f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
            if (user) {
                g.drawOval(x + 6, y + 3, 6, 6);
                g.drawArc(x + 3, y + 10, 12, 9, 20, 140);
            } else {
                g.drawRoundRect(x + 3, y + 8, 12, 8, 3, 3);
                g.drawArc(x + 6, y + 2, 6, 10, 0, 180);
                g.drawLine(x + 6, y + 7, x + 6, y + 8);
                g.drawLine(x + 12, y + 7, x + 12, y + 8);
            }
            g.dispose();
        }
    }

    private static class PlaceholderTextField extends JTextField {
        private final String placeholder;

        PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
            repaintPlaceholderOnFocusChange(this);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            if (getText().isEmpty() && !isFocusOwner()) {
                paintPlaceholder(graphics, this, placeholder);
            }
        }
    }

    private static final class PlaceholderPasswordField extends JPasswordField {
        private final String placeholder;

        PlaceholderPasswordField(String placeholder) {
            this.placeholder = placeholder;
            repaintPlaceholderOnFocusChange(this);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            if (getPassword().length == 0 && !isFocusOwner()) {
                paintPlaceholder(graphics, this, placeholder);
            }
        }
    }

    private static void paintPlaceholder(Graphics graphics, JTextField field, String placeholder) {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(LOGIN_MUTED);
        g.setFont(field.getFont());
        Insets insets = field.getInsets();
        int y = (field.getHeight() + g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent()) / 2;
        g.drawString(placeholder, insets.left, y);
        g.dispose();
    }

    private static void repaintPlaceholderOnFocusChange(JTextField field) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent event) {
                field.repaint();
            }

            @Override
            public void focusLost(FocusEvent event) {
                field.repaint();
            }
        });
    }
}
