package com.example.view;

import com.example.model.Ticket;
import com.example.model.Usuario;

import java.util.List;
import java.util.Scanner;

public class HelpDeskView {
    private final Scanner scanner = new Scanner(System.in);

    public void mostrarMenu() {
        System.out.println("=== Mesa de ayuda ===");
        System.out.println("1. Crear ticket");
        System.out.println("2. Mostrar tickets");
        System.out.println("3. Actualizar estado de ticket");
        System.out.println("4. Asignar técnico a ticket");
        System.out.println("5. Salir");
    }

    public int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (scanner.hasNextInt()) {
                int valor = scanner.nextInt();
                scanner.nextLine();
                return valor;
            }
            scanner.nextLine();
            System.out.println("Entrada inválida. Ingrese un número.");
        }
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    public void mostrarUsuarios(List<Usuario> usuarios) {
        System.out.println("Usuarios disponibles:");
        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }
    }

    public void mostrarTickets(List<Ticket> tickets) {
        System.out.println("--- Lista de tickets ---");
        if (tickets.isEmpty()) {
            System.out.println("No hay tickets registrados.");
            return;
        }
        for (Ticket ticket : tickets) {
            System.out.println(ticket);
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void cerrar() {
        scanner.close();
    }
}
