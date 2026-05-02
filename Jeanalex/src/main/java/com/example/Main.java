package com.example;

import com.example.controller.DispositivoController;
import com.example.controller.TicketController;
import com.example.controller.UsuarioController;
import com.example.dao.DispositivoDao;
import com.example.dao.TicketDao;
import com.example.dao.UsuarioDao;
import com.example.model.Usuario;
import com.example.view.LoginDialog;
import com.example.view.MainFrame;

import javax.swing.SwingUtilities;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UsuarioController usuarioController = new UsuarioController(new UsuarioDao());
            DispositivoController dispositivoController = new DispositivoController(new DispositivoDao());
            TicketController ticketController = new TicketController(new TicketDao());

            LoginDialog loginDialog = new LoginDialog(null, usuarioController);
            loginDialog.setVisible(true);

            Optional<Usuario> usuarioAutenticado = loginDialog.getUsuarioAutenticado();
            if (usuarioAutenticado.isEmpty()) {
                System.exit(0);
            }

            MainFrame mainFrame = new MainFrame(
                    usuarioController,
                    dispositivoController,
                    ticketController
            );
            mainFrame.setTitle("Jeanalex Dynamics - " + usuarioAutenticado.get().getNombre());
            mainFrame.setVisible(true);
        });
    }
}
