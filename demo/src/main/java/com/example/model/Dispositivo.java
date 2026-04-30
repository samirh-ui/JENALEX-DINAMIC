package com.example.model;

public class Dispositivo {
     private int id;
    private String tipo;
    private String serial;
    private String marca;
    private String modelo;
    private String estado;

    public Dispositivo() {}

    public Dispositivo(String tipo, String serial, String marca, String modelo, String estado) {
        this.tipo = tipo;
        this.serial = serial;
        this.marca = marca;
        this.modelo = modelo;
        this.estado = estado;
    }

    // getters y setters
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getSerial() { return serial; }
    public void setSerial(String serial) { this.serial = serial; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }



   

}
