package com.example.model;

public class Agente {
    private int id;
    private int idUsuario;
    private String nombre;
    private String correo;
    private String cargo;
    private String placa;

    public Agente() {
    }

    public Agente(int idUsuario, String nombre, String correo, String cargo, String placa) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.cargo = cargo;
        this.placa = placa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
}
