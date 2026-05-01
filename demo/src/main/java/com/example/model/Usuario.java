package com.example.model;

public class Usuario {
    private int id;
    private String nombre;
    private String correo;
    private String password;
    private int idRol;

    public Usuario() {}

    public Usuario(int id, String nombre, String correo, String password, int idRol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.idRol = idRol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    @Override
    public String toString() {
        String rol = idRol == 2 ? "Técnico" : "Usuario";
        return String.format("ID=%d | %s | %s | %s", id, nombre, correo, rol);
    }
}
