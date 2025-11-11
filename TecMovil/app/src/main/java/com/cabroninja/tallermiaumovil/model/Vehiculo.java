package com.cabroninja.tallermiaumovil.model;

// ======================================================================
// == Clase: Vehiculo
// == Rol dentro de la app:
//    - Modelo de dominio que representa un vehículo del taller.
//    - Se mapea a la tabla SQLite T_VEHICULO mediante los repositorios
//      (VehiculoRepository).
//
//    Mapeo típico con SQLite (según constantes en TallerDbHelper):
//      * V_PATENTE    (TEXT PRIMARY KEY)        ← clave única del vehículo
//      * V_COLOR      (TEXT)
//      * V_MODELO     (TEXT)
//      * V_RUN_DUENO  (INTEGER NULL)            ← FK opcional a T_PERSONA.C_RUN
// ======================================================================
public class Vehiculo {

    // ------------------------------------------------------------------
    // == Atributo: patente
    //    Tipo: String
    //    Rol: Identificador único (PK) del vehículo. Ej.: "ABCD12".
    // ------------------------------------------------------------------
    public String patente;

    // ------------------------------------------------------------------
    // == Atributo: color
    //    Tipo: String
    //    Rol: Color declarativo del vehículo (visual/identificativo).
    // ------------------------------------------------------------------
    public String color;

    // ------------------------------------------------------------------
    // == Atributo: modelo
    //    Tipo: String
    //    Rol: Modelo del vehículo (marca/modelo o variante libre).
    // ------------------------------------------------------------------
    public String modelo;

    // ------------------------------------------------------------------
    // == Atributo: runDueno
    //    Tipo: Integer (nullable)
    //    Rol: RUN del dueño del vehículo; puede ser null si no está
    //         asignado o se desconoce.
    // ------------------------------------------------------------------
    public Integer runDueno; // puede ser null

    // ------------------------------------------------------------------
    // == Constructor: Vehiculo
    //    Parámetros:
    //      - patente (String) : PK del vehículo (no nulo).
    //      - color   (String) : color declarativo.
    //      - modelo  (String) : modelo/marca del vehículo.
    //      - runDueno (Integer) : RUN del dueño o null si no aplica.
    //    Retorno:
    //      - (constructor) instancia lista para ser mapeada a/desde SQLite.
    // ------------------------------------------------------------------
    public Vehiculo(String patente, String color, String modelo, Integer runDueno){
        this.patente = patente;
        this.color = color;
        this.modelo = modelo;
        this.runDueno = runDueno;
    }

    // ------------------------------------------------------------------
    // == Método: toString
    //    Parámetros:
    //      - (sin parámetros)
    //    Descripción:
    //      - Representación legible del vehículo para listas/depuración.
    //        Formato: "<patente> • <modelo> • <color>"
    //    Retorno:
    //      - String: cadena formateada con los campos principales.
    // ------------------------------------------------------------------
    @Override
    public String toString(){
        return patente + " • " + modelo + " • " + color;
    }
}
