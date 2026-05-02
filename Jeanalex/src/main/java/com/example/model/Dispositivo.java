package com.example.model;

import java.time.LocalDate;

public class Dispositivo {
    private int id;
    private String nombre;
    private String serial;
    private String marca;
    private String modelo;
    private String categoria;
    private String estado;
    private String ubicacion;
    private LocalDate fechaIngreso;

    public Dispositivo() {
    }

    public Dispositivo(String nombre, String serial, String marca, String modelo, String categoria, String estado) {
        this.nombre = nombre;
        this.serial = serial;
        this.marca = marca;
        this.modelo = modelo;
        this.categoria = categoria;
        this.estado = estado;
        this.fechaIngreso = LocalDate.now();
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

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
}
