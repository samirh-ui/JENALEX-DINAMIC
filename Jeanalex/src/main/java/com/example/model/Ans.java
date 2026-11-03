package com.example.model;

public class Ans {
    private int id;
    private String tipoSolicitud;
    private String prioridad;
    private int tiempoRespuestaHoras;
    private int tiempoSolucionHoras;
    private String descripcion;
    private boolean activo;

    public Ans() {
        this.activo = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public int getTiempoRespuestaHoras() {
        return tiempoRespuestaHoras;
    }

    public void setTiempoRespuestaHoras(int tiempoRespuestaHoras) {
        this.tiempoRespuestaHoras = tiempoRespuestaHoras;
    }

    public int getTiempoSolucionHoras() {
        return tiempoSolucionHoras;
    }

    public void setTiempoSolucionHoras(int tiempoSolucionHoras) {
        this.tiempoSolucionHoras = tiempoSolucionHoras;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
