package com.example.model;

public class ReporteIndicadores {
    private int totalEquipos;
    private int equiposMantenimiento;
    private int totalUsuarios;
    private int totalSolicitudes;
    private int solicitudesAbiertas;
    private int solicitudesCerradas;
    private double porcentajeCumplimientoAns;

    public int getTotalEquipos() {
        return totalEquipos;
    }

    public void setTotalEquipos(int totalEquipos) {
        this.totalEquipos = totalEquipos;
    }

    public int getEquiposMantenimiento() {
        return equiposMantenimiento;
    }

    public void setEquiposMantenimiento(int equiposMantenimiento) {
        this.equiposMantenimiento = equiposMantenimiento;
    }

    public int getTotalUsuarios() {
        return totalUsuarios;
    }

    public void setTotalUsuarios(int totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }

    public int getTotalSolicitudes() {
        return totalSolicitudes;
    }

    public void setTotalSolicitudes(int totalSolicitudes) {
        this.totalSolicitudes = totalSolicitudes;
    }

    public int getSolicitudesAbiertas() {
        return solicitudesAbiertas;
    }

    public void setSolicitudesAbiertas(int solicitudesAbiertas) {
        this.solicitudesAbiertas = solicitudesAbiertas;
    }

    public int getSolicitudesCerradas() {
        return solicitudesCerradas;
    }

    public void setSolicitudesCerradas(int solicitudesCerradas) {
        this.solicitudesCerradas = solicitudesCerradas;
    }

    public double getPorcentajeCumplimientoAns() {
        return porcentajeCumplimientoAns;
    }

    public void setPorcentajeCumplimientoAns(double porcentajeCumplimientoAns) {
        this.porcentajeCumplimientoAns = porcentajeCumplimientoAns;
    }
}
