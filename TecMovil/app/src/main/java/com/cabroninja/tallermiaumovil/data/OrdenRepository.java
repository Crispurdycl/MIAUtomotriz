package com.cabroninja.tallermiaumovil.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cabroninja.tallermiaumovil.model.OrdenTrabajo;
import java.util.ArrayList;
import java.util.List;

// ======================================================================
// == Clase: OrdenRepository
// == Rol dentro de la app:
//    - Capa de acceso a datos (DAO/Repositorio) para la entidad OrdenTrabajo.
//    - Ejecuta operaciones CRUD y consultas de lectura sobre la tabla
//      T_ORDEN en SQLite, utilizando TallerDbHelper para abrir la DB.
//    - Entrega y recibe objetos de dominio (OrdenTrabajo) para aislar
//      a la UI de los detalles SQL y del manejo de cursores.
//
//    Detalles del modelo (según constantes de TallerDbHelper):
//      * O_ID          : clave primaria autoincremental (long).
//      * O_NUMERO      : número de OT (String o código legible).
//      * O_FECHA       : fecha en String (formato libre definido por la app).
//      * O_VALOR_NETO  : monto neto (double).
//      * O_IVA         : IVA asociado (double).
//      * O_OBSERVACION : texto libre (String).
//      * O_PATENTE     : FK a T_VEHICULO.V_PATENTE (String). Requiere
//                        que exista la patente si las claves foráneas están activas.
// ======================================================================
public class OrdenRepository {

    // ------------------------------------------------------------------
    // == Atributo: helper
    //    Tipo: TallerDbHelper
    //    Propósito:
    //      - Gestiona la creación/configuración de la base de datos.
    //      - Provee conexiones de lectura/escritura por operación.
    //    Nota:
    //      - El patrón usado evita mantener una conexión global abierta;
    //        cada metodo pide un SQLiteDatabase cuando lo necesita.
    // ------------------------------------------------------------------
    private final TallerDbHelper helper;

    // ------------------------------------------------------------------
    // == Constructor: OrdenRepository
    //    Parámetros:
    //      - ctx (Context): contexto Android para inicializar el helper.
    //    Descripción:
    //      - Crea el repositorio y deja lista la infraestructura para
    //        ejecutar operaciones sobre T_ORDEN.
    //    Retorno:
    //      - (constructor) instancia configurada.
    // ------------------------------------------------------------------
    public OrdenRepository(Context ctx){ helper = new TallerDbHelper(ctx); }

    // ------------------------------------------------------------------
    // == Metodo: insert
    //    Parámetros:
    //      - o (OrdenTrabajo): entidad con los campos de la OT a persistir.
    //        Espera:
    //          * numero      (String)
    //          * fecha       (String)
    //          * valorNeto   (double)
    //          * iva         (double)
    //          * observacion (String)
    //          * patente     (String) — FK a vehículo (debe existir si FK activo)
    //    Descripción detallada:
    //      - Mapea los campos a ContentValues e inserta en T_ORDEN.
    //      - Cierra la conexión de escritura explícitamente tras el insert.
    //      - Si hay claves foráneas, fallará si la patente no existe en T_VEHICULO.
    //    Retorno:
    //      - long: rowId autogenerado (O_ID) si tuvo éxito; -1 si falló.
    // ------------------------------------------------------------------
    public long insert(OrdenTrabajo o){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TallerDbHelper.O_NUMERO, o.numero);
        cv.put(TallerDbHelper.O_FECHA, o.fecha);
        cv.put(TallerDbHelper.O_VALOR_NETO, o.valorNeto);
        cv.put(TallerDbHelper.O_IVA, o.iva);
        cv.put(TallerDbHelper.O_OBSERVACION, o.observacion);
        cv.put(TallerDbHelper.O_PATENTE, o.patente);
        long id = db.insert(TallerDbHelper.T_ORDEN, null, cv);
        db.close();
        return id;
    }

    // ------------------------------------------------------------------
    // == Metodo: listAll
    //    Parámetros:
    //      - (sin parámetros)
    //    Descripción detallada:
    //      - Devuelve todas las órdenes ordenadas por O_ID DESC (más recientes primero).
    //      - Selecciona todas las columnas principales y mapea cada fila a
    //        un objeto OrdenTrabajo (incluyendo el O_ID autogenerado).
    //    Retorno:
    //      - List<OrdenTrabajo>: lista (posiblemente vacía) con todas las OTs.
    // ------------------------------------------------------------------
    public List<OrdenTrabajo> listAll(){
        List<OrdenTrabajo> out = new ArrayList<>();
        try (Cursor c = helper.getReadableDatabase().query(
                TallerDbHelper.T_ORDEN,
                new String[]{TallerDbHelper.O_ID, TallerDbHelper.O_NUMERO, TallerDbHelper.O_FECHA, TallerDbHelper.O_VALOR_NETO, TallerDbHelper.O_IVA, TallerDbHelper.O_OBSERVACION, TallerDbHelper.O_PATENTE},
                null, null, null, null, TallerDbHelper.O_ID + " DESC")) {
            while (c.moveToNext()) {
                out.add(new OrdenTrabajo(
                        c.getLong(0), c.getString(1), c.getString(2), c.getDouble(3), c.getDouble(4), c.getString(5), c.getString(6)
                ));
            }
        }
        return out;
    }

    // ------------------------------------------------------------------
    // == Metodo: listByPatente
    //    Parámetros:
    //      - patente (String): identificador del vehículo (PK en T_VEHICULO)
    //        cuyas órdenes asociadas se desean consultar.
    //    Descripción detallada:
    //      - Filtra T_ORDEN por O_PATENTE = ? y ordena por O_ID DESC.
    //      - Mapea cada fila a OrdenTrabajo y la agrega a la lista de salida.
    //    Retorno:
    //      - List<OrdenTrabajo>: lista (posiblemente vacía) de OTs ligadas a la patente.
    // ------------------------------------------------------------------
    public List<OrdenTrabajo> listByPatente(String patente){
        List<OrdenTrabajo> out = new ArrayList<>();
        try (Cursor c = helper.getReadableDatabase().query(
                TallerDbHelper.T_ORDEN,
                new String[]{TallerDbHelper.O_ID, TallerDbHelper.O_NUMERO, TallerDbHelper.O_FECHA, TallerDbHelper.O_VALOR_NETO, TallerDbHelper.O_IVA, TallerDbHelper.O_OBSERVACION, TallerDbHelper.O_PATENTE},
                TallerDbHelper.O_PATENTE + " = ?",
                new String[]{ patente }, null, null, TallerDbHelper.O_ID + " DESC")) {
            while (c.moveToNext()) {
                out.add(new OrdenTrabajo(
                        c.getLong(0), c.getString(1), c.getString(2), c.getDouble(3), c.getDouble(4), c.getString(5), c.getString(6)
                ));
            }
        }
        return out;
    }

    // ------------------------------------------------------------------
    // == Metodo: update
    //    Parámetros:
    //      - o (OrdenTrabajo): entidad con O_ID existente y nuevos valores
    //        para actualizar (numero, fecha, valorNeto, iva, observacion, patente).
    //    Descripción detallada:
    //      - Actualiza la fila de T_ORDEN cuyo O_ID = o.id.
    //      - Respeta integridad referencial si se cambia la patente (según FK).
    //    Retorno:
    //      - int: número de filas afectadas (0 si no existe ese O_ID).
    // ------------------------------------------------------------------
    public int update(OrdenTrabajo o){
        ContentValues cv = new ContentValues();
        cv.put(TallerDbHelper.O_NUMERO, o.numero);
        cv.put(TallerDbHelper.O_FECHA, o.fecha);
        cv.put(TallerDbHelper.O_VALOR_NETO, o.valorNeto);
        cv.put(TallerDbHelper.O_IVA, o.iva);
        cv.put(TallerDbHelper.O_OBSERVACION, o.observacion);
        cv.put(TallerDbHelper.O_PATENTE, o.patente);
        return helper.getWritableDatabase().update(
                TallerDbHelper.T_ORDEN, cv,
                TallerDbHelper.O_ID + " = ?", new String[]{ String.valueOf(o.id) });
    }

    // ------------------------------------------------------------------
    // == Metodo: count
    //    Parámetros:
    //      - (sin parámetros)
    //    Descripción detallada:
    //      - Ejecuta SELECT COUNT(*) sobre T_ORDEN para obtener el total
    //        de órdenes registradas.
    //    Retorno:
    //      - int: cantidad total de órdenes (0 si no hay filas).
    // ------------------------------------------------------------------
    public int count() {
        SQLiteDatabase db = helper.getReadableDatabase();
        try (Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TallerDbHelper.T_ORDEN, null)) {
            if (c.moveToFirst()) {
                return c.getInt(0);
            } else {
                return 0;
            }
        }
    }
}
