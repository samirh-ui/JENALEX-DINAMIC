package com.example.view;

import java.util.Scanner;

public class ConsoleView {
    private final Scanner scanner = new Scanner(System.in);

    public void mostrarTitulo(String titulo) {
        System.out.println();
        System.out.println("=== " + titulo + " ===");
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public String leerTexto(String etiqueta) {
        System.out.print(etiqueta + ": ");
        return scanner.nextLine().trim();
    }

    public int leerEntero(String etiqueta) {
        while (true) {
            String valor = leerTexto(etiqueta);
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException exception) {
                mostrarMensaje("Ingrese un numero valido.");
            }
        }
    }
}
