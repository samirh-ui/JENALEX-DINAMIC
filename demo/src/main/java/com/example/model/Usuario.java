package com.example.model;

public class Usuario {
    private int id;
    private String nombre;
    private String correo;
    private String password;
    private int idRol;

    public Usuario() {}

    public Usuario(String nombre, String correo, String password, int idRol) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.idRol = idRol;
    }

    // getters y setters
}
