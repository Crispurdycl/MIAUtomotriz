package com.cabroninja.tallermiaumovil.model;

public class Vehiculo {
    public String patente;
    public String color;
    public String modelo;
    public Integer runDueno; // puede ser null

    public Vehiculo(String patente, String color, String modelo, Integer runDueno){
        this.patente = patente; this.color = color; this.modelo = modelo; this.runDueno = runDueno;
    }

    @Override public String toString(){ return patente + " • " + modelo + " • " + color; }
}
