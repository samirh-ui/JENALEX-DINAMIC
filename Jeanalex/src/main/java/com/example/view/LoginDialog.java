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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.Optional;

public class LoginDialog extends JDialog {
    private final UsuarioController usuarioController;
    private final JTextField correoField = new JTextField(22);
    private final JPasswordField contrasenaField = new JPasswordField(22);
    private Optional<Usuario> usuarioAutenticado = Optional.empty();

    public LoginDialog(JFrame parent, UsuarioController usuarioController) {
        super(parent, "Iniciar sesion", true);
        this.usuarioController = usuarioController;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));
        add(crearFormulario(), BorderLayout.CENTER);
        add(crearAcciones(), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(parent);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 8, 18));

        agregarCampo(panel, "Correo", correoField, 0);
        agregarCampo(panel, "Contrasena", contrasenaField, 1);
        return panel;
    }

    private JPanel crearAcciones() {
        JButton ingresarButton = new JButton("Ingresar");
        JButton cancelarButton = new JButton("Cancelar");

        ingresarButton.addActionListener(event -> autenticar());
        cancelarButton.addActionListener(event -> dispose());
        getRootPane().setDefaultButton(ingresarButton);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 18, 14, 18));
        panel.add(cancelarButton);
        panel.add(ingresarButton);
        return panel;
    }

    private void agregarCampo(JPanel panel, String etiqueta, java.awt.Component campo, int fila) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = fila;
        labelConstraints.anchor = GridBagConstraints.EAST;
        labelConstraints.insets = new Insets(5, 5, 5, 8);
        panel.add(new JLabel(etiqueta), labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = fila;
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(5, 5, 5, 5);
        panel.add(campo, fieldConstraints);
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
        }
    }

    public Optional<Usuario> getUsuarioAutenticado() {
        return usuarioAutenticado;
    }
}
