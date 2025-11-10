package com.cabroninja.tallermiaumovil.model;

public class Persona {
    public int run;
    public String email;
    public String nombre;
    public String apellido;
    public String password;
    public String tipo; // "cliente" | "trabajador"

    public Persona(int run, String email, String nombre, String apellido, String password, String tipo){
        this.run = run; this.email = email; this.nombre = nombre; this.apellido = apellido; this.password = password; this.tipo = tipo;
    }

    public String nombreCompleto() {
    String __n;
    if (nombre != null) {
        __n = nombre;
    } else {
        __n = "";
    }
    String __a;
    if (apellido != null && !apellido.isEmpty()) {
        __a = " " + apellido;
    } else {
        __a = "";
    }
    return __n + __a;
}
}
