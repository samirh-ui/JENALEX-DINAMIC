package com.example.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Ticket {
    private int id;
    private String descripcion;
    private String estado;
    private String prioridad;
    private int idUsuario;
    private int idTecnico;
    private Date fecha;

    public Ticket() {
    }

    public Ticket(int id, String descripcion, String prioridad, int idUsuario) {
        this.id = id;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.idUsuario = idUsuario;
        this.estado = "Abierto";
        this.idTecnico = 0;
        this.fecha = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(int idTecnico) {
        this.idTecnico = idTecnico;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        String fechaFormateada = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(fecha);
        return String.format("Ticket #%d | %s | Prioridad: %s | Estado: %s | Usuario ID: %d | Técnico ID: %d | Fecha: %s",
                id, descripcion, prioridad, estado, idUsuario, idTecnico, fechaFormateada);
    }
}
