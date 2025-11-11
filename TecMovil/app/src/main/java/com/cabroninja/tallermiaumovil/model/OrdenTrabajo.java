package com.cabroninja.tallermiaumovil.model;

// ======================================================================
// == Clase: OrdenTrabajo
// == Rol dentro de la app:
//    - Modelo de dominio que representa una orden de trabajo (OT) del taller.
//    - Se mapea a la tabla SQLite T_ORDEN a través de OrdenRepository.
//
//    Mapeo típico con SQLite (según constantes en TallerDbHelper):
//      * O_ID          (INTEGER PRIMARY KEY AUTOINCREMENT)
//      * O_NUMERO      (TEXT)
//      * O_FECHA       (TEXT)
//      * O_VALOR_NETO  (REAL)
//      * O_IVA         (REAL)
//      * O_OBSERVACION (TEXT)
//      * O_PATENTE     (TEXT)
// ======================================================================
public class OrdenTrabajo {

    // ------------------------------------------------------------------
    // == Atributo: id
    //    Tipo: long
    //    Rol: Identificador único autogenerado de la OT (PK en T_ORDEN).
    // ------------------------------------------------------------------
    public long id;

    // ------------------------------------------------------------------
    // == Atributo: numero
    //    Tipo: String
    //    Rol: Código/folio legible de la orden (ej.: "OT-2025-00123").
    // ------------------------------------------------------------------
    public String numero;

    // ------------------------------------------------------------------
    // == Atributo: fecha
    //    Tipo: String
    //    Rol: Fecha de emisión/registro de la OT.
    // ------------------------------------------------------------------
    public String fecha;

    // ------------------------------------------------------------------
    // == Atributo: valorNeto
    //    Tipo: double
    //    Rol: Monto neto (sin impuestos) de la orden.
    // ------------------------------------------------------------------
    public double valorNeto;

    // ------------------------------------------------------------------
    // == Atributo: iva
    //    Tipo: double
    //    Rol: Monto de IVA asociado a la orden.
    // ------------------------------------------------------------------
    public double iva;

    // ------------------------------------------------------------------
    // == Atributo: observacion
    //    Tipo: String
    //    Rol: Comentarios libres sobre la OT (diagnóstico, notas, etc.).
    // ------------------------------------------------------------------
    public String observacion;

    // ------------------------------------------------------------------
    // == Atributo: patente
    //    Tipo: String
    //    Rol: Identificador del vehículo asociado (FK a T_VEHICULO.V_PATENTE).
    // ------------------------------------------------------------------
    public String patente;

    // ------------------------------------------------------------------
    // == Constructor: OrdenTrabajo
    //    Parámetros:
    //      - id (long)         : PK autogenerada (0 si aún no se inserta).
    //      - numero (String)   : folio/código legible de la OT.
    //      - fecha (String)    : fecha en formato "yyyy-MM-dd".
    //      - valorNeto (double): monto neto.
    //      - iva (double)      : monto de IVA.
    //      - observacion (String): texto libre.
    //      - patente (String)  : FK a vehículo (patente existente).
    //    Retorno:
    //      - (constructor) instancia lista para mapear con SQLite.
    // ------------------------------------------------------------------
    public OrdenTrabajo(long id, String numero, String fecha, double valorNeto, double iva, String observacion, String patente){
        this.id = id;
        this.numero = numero;
        this.fecha = fecha;
        this.valorNeto = valorNeto;
        this.iva = iva;
        this.observacion = observacion;
        this.patente = patente;
    }

    // ------------------------------------------------------------------
    // == Método: total
    //    Parámetros:
    //      - (sin parámetros)
    //    Descripción:
    //      - Calcula el monto total de la orden como suma de valor neto
    //    Retorno:
    //      - double: valor total (= valorNeto + iva).
    // ------------------------------------------------------------------
    public double total(){
        return valorNeto + iva;
    }
}
