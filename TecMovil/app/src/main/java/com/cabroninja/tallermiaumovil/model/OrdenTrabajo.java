package com.cabroninja.tallermiaumovil.model;

public class OrdenTrabajo {
    public long id;
    public String numero;
    public String fecha; // yyyy-mm-dd
    public double valorNeto;
    public double iva;
    public String observacion;
    public String patente;

    public OrdenTrabajo(long id, String numero, String fecha, double valorNeto, double iva, String observacion, String patente){
        this.id = id; this.numero = numero; this.fecha = fecha; this.valorNeto = valorNeto; this.iva = iva; this.observacion = observacion; this.patente = patente;
    }

    public double total(){ return valorNeto + iva; }
}
