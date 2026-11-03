package com.example.model;

import java.time.LocalDate;

public class ReporteCumplimiento {
    private int idTecnico;
    private String tipoSolicitud;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int totalSolicitudes;
    private int atendidasDentroAns;
    private int resueltasDentroAns;
    private double porcentajeCumplimiento;

    public int getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(int idTecnico) {
        this.idTecnico = idTecnico;
    }

    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getTotalSolicitudes() {
        return totalSolicitudes;
    }

    public void setTotalSolicitudes(int totalSolicitudes) {
        this.totalSolicitudes = totalSolicitudes;
    }

    public int getAtendidasDentroAns() {
        return atendidasDentroAns;
    }

    public void setAtendidasDentroAns(int atendidasDentroAns) {
        this.atendidasDentroAns = atendidasDentroAns;
    }

    public int getResueltasDentroAns() {
        return resueltasDentroAns;
    }

    public void setResueltasDentroAns(int resueltasDentroAns) {
        this.resueltasDentroAns = resueltasDentroAns;
    }

    public double getPorcentajeCumplimiento() {
        return porcentajeCumplimiento;
    }

    public void setPorcentajeCumplimiento(double porcentajeCumplimiento) {
        this.porcentajeCumplimiento = porcentajeCumplimiento;
    }
}
