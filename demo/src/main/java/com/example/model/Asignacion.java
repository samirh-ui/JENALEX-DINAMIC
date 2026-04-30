package com.example.model;
import java.util.Date;

public class Asignacion {
    private int id;
    private int idAgente;
    private int idDispositivo;
    private Date fechaAsignacion;
    private Date fechaDevolucion;

    // getters y setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdAgente() { return idAgente; }
    public void setIdAgente(int idAgente) { this.idAgente = idAgente; }

    public int getIdDispositivo() { return idDispositivo; }
    public void setIdDispositivo(int idDispositivo) { this.idDispositivo = idDispositivo; }

    public Date getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(Date fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }

    public Date getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(Date fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    
}
    

