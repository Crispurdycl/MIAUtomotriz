package com.cabroninja.tallermiaumovil.model;

// ======================================================================
// == Clase: OrdenTrabajo
// == Rol dentro de la app:
//    - Modelo de dominio que representa una orden de trabajo (OT) del taller.
//    - Se mapea a la tabla SQLite T_ORDEN a través de OrdenRepository.
//
//    Mapeo típico con SQLite (según constantes en TallerDbHelper):
//      * O_ID          (INTEGER PRIMARY KEY AUTOINCREMENT)
//      * O_NUMERO      (TEXT)           ← código legible de la OT
//      * O_FECHA       (TEXT)           ← se almacena como String (formato app)
//      * O_VALOR_NETO  (REAL)
//      * O_IVA         (REAL)
//      * O_OBSERVACION (TEXT)
//      * O_PATENTE     (TEXT)           ← FK a T_VEHICULO.V_PATENTE
//
//    Notas de diseño:
//      - Campos públicos por simplicidad (POJO). En sistemas grandes
//        se suele preferir encapsulación (getters/setters) y validación.
//      - Precisión monetaria: usar double es cómodo, pero para cálculos
//        contables reales es más seguro BigDecimal o enteros (centavos).
// ======================================================================
public class OrdenTrabajo {

    // ------------------------------------------------------------------
    // == Atributo: id
    //    Tipo: long
    //    Rol: Identificador único autogenerado de la OT (PK en T_ORDEN).
    //    Uso: Diferenciar filas y direccionar updates/deletes.
    // ------------------------------------------------------------------
    public long id;

    // ------------------------------------------------------------------
    // == Atributo: numero
    //    Tipo: String
    //    Rol: Código/folio legible de la orden (ej.: "OT-2025-00123").
    //    Nota: No necesariamente es único a nivel de DB (según tu esquema).
    // ------------------------------------------------------------------
    public String numero;

    // ------------------------------------------------------------------
    // == Atributo: fecha
    //    Tipo: String
    //    Rol: Fecha de emisión/registro de la OT.
    //    Formato sugerido: "yyyy-MM-dd" (consistente con la UI/DB).
    // ------------------------------------------------------------------
    public String fecha;

    // ------------------------------------------------------------------
    // == Atributo: valorNeto
    //    Tipo: double
    //    Rol: Monto neto (sin impuestos) de la orden.
    //    Nota: Para cálculos financieros exactos, considerar BigDecimal.
    // ------------------------------------------------------------------
    public double valorNeto;

    // ------------------------------------------------------------------
    // == Atributo: iva
    //    Tipo: double
    //    Rol: Monto de IVA asociado a la orden.
    //    Nota: Se asume que ya viene calculado; si se calcula, documentar tasa.
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
    //    Integridad referencial: Debe existir en T_VEHICULO si las FKs están activas.
    // ------------------------------------------------------------------
    public String patente;

    // ------------------------------------------------------------------
    // == Constructor: OrdenTrabajo
    //    Parámetros:
    //      - id (long)         : PK autogenerada (0 si aún no se inserta).
    //      - numero (String)   : folio/código legible de la OT.
    //      - fecha (String)    : fecha en formato "yyyy-MM-dd" (sugerido).
    //      - valorNeto (double): monto neto.
    //      - iva (double)      : monto de IVA.
    //      - observacion (String): texto libre.
    //      - patente (String)  : FK a vehículo (patente existente).
    //    Descripción:
    //      - Inicializa el POJO con los valores indicados sin validación
    //        adicional. La consistencia (formatos, existencia de FK) se
    //        delega a la capa de datos/servicios.
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
    // == Metodo: total
    //    Parámetros:
    //      - (sin parámetros)
    //    Descripción:
    //      - Calcula el monto total de la orden como suma de valor neto
    //        e IVA. No realiza redondeo ni aplica formato monetario; la
    //        presentación/rounding puede hacerse en la UI.
    //    Retorno:
    //      - double: valor total (= valorNeto + iva).
    // ------------------------------------------------------------------
    public double total(){
        return valorNeto + iva;
    }
}
