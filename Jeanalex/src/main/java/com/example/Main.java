package com.example;

import com.example.controller.AgenteController;
import com.example.controller.AnsController;
import com.example.controller.AsignacionController;
import com.example.controller.DispositivoController;
import com.example.controller.MantenimientoController;
import com.example.controller.MensajeController;
import com.example.controller.ReporteController;
import com.example.controller.TicketController;
import com.example.controller.UsuarioController;
import com.example.dao.AgenteDao;
import com.example.dao.AnsDao;
import com.example.dao.AsignacionDao;
import com.example.dao.DispositivoDao;
import com.example.dao.MantenimientoDao;
import com.example.dao.MensajeDao;
import com.example.dao.ReporteDao;
import com.example.dao.TicketDao;
import com.example.dao.UsuarioDao;
import com.example.model.Usuario;
import com.example.view.LoginDialog;
import com.example.view.MainFrame;

import javax.swing.SwingUtilities;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::mostrarLogin);
    }

    private static void mostrarLogin() {
        UsuarioController usuarioController = new UsuarioController(new UsuarioDao());
        DispositivoController dispositivoController = new DispositivoController(new DispositivoDao());
        TicketController ticketController = new TicketController(new TicketDao());
        ReporteController reporteController = new ReporteController(new ReporteDao());
        AgenteController agenteController = new AgenteController(new AgenteDao());
        AsignacionController asignacionController = new AsignacionController(new AsignacionDao());
        MensajeController mensajeController = new MensajeController(new MensajeDao());
        MantenimientoController mantenimientoController = new MantenimientoController(new MantenimientoDao());
        AnsController ansController = new AnsController(new AnsDao());

        LoginDialog loginDialog = new LoginDialog(null, usuarioController);
        loginDialog.setVisible(true);

        Optional<Usuario> usuarioAutenticado = loginDialog.getUsuarioAutenticado();
        if (usuarioAutenticado.isEmpty()) {
            System.exit(0);
        }

        MainFrame mainFrame = new MainFrame(
                usuarioController,
                dispositivoController,
                ticketController,
                reporteController,
                agenteController,
                asignacionController,
                mensajeController,
                mantenimientoController,
                ansController,
                usuarioAutenticado.get(),
                Main::mostrarLogin
        );
        mainFrame.setTitle("Sistema de Inventario de Equipamiento - " + usuarioAutenticado.get().getNombre());
        mainFrame.setVisible(true);
    }
}
