package com.cabroninja.tallermiaumovil.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cabroninja.tallermiaumovil.model.Vehiculo;
import java.util.ArrayList;
import java.util.List;

// ======================================================================
// == Clase: VehiculoRepository
// == Rol dentro de la app:
//    - Capa de acceso a datos (DAO/Repositorio) para la entidad Vehiculo.
//    - Centraliza operaciones CRUD y lecturas sobre la tabla T_VEHICULO
//      en SQLite, usando TallerDbHelper para obtener la conexión.
//    - Aísla a la UI de SQL y del manejo de cursores/bases de datos,
//      entregando objetos de dominio (Vehiculo) y tipos primitivos.
// ======================================================================
public class VehiculoRepository {

    // ------------------------------------------------------------------
    // == Atributo: helper
    //    Tipo: TallerDbHelper
    //    Propósito:
    //      - Gestiona la base de datos SQLite (creación/actualización).
    //      - Entrega instancias de lectura/escritura (getReadableDatabase /
    //        getWritableDatabase) para ejecutar operaciones SQL.
    //    Notas:
    //      - Este repositorio solicita la DB por operación; los cursores
    //        se cierran con try-with-resources. El objeto SQLiteDatabase
    //        devuelto por el helper lo administra el framework (no es
    //        necesario cerrarlo manualmente en la mayoría de casos).
    // ------------------------------------------------------------------
    private final TallerDbHelper helper;

    // ------------------------------------------------------------------
    // == Constructor: VehiculoRepository
    //    Parámetros:
    //      - ctx (Context): contexto Android que usará el helper para
    //        abrir/crear la base de datos y acceder a recursos.
    //    Descripción:
    //      - Inicializa el helper para todas las operaciones del repositorio.
    //    Retorno:
    //      - (constructor) instancia lista para usar.
    // ------------------------------------------------------------------
    public VehiculoRepository(Context ctx){
        helper = new TallerDbHelper(ctx);
    }

    // ------------------------------------------------------------------
    // == Metodo: insert
    //    Parámetros:
    //      - v (Vehiculo): entidad a insertar. Se esperan:
    //          * patente (String) — PK de la tabla (no nulo).
    //          * color   (String)
    //          * modelo  (String)
    //          * runDueno (Integer) — RUN del dueño; puede ser null.
    //    Descripción detallada:
    //      - Mapea los campos del Vehiculo a ContentValues y realiza
    //        INSERT en T_VEHICULO. Si runDueno es null, no se incluye
    //        la columna (queda NULL en la fila — útil cuando aún no
    //        existe un propietario asignado).
    //      - Si existen claves foráneas activas (onConfigure de helper),
    //        la asignación de runDueno respetará la integridad referencial.
    //    Retorno:
    //      - long: rowId de la nueva fila si tuvo éxito; -1 en caso de fallo
    //        (por ejemplo, violación de PK UNIQUE en patente).
    // ------------------------------------------------------------------
    public long insert(Vehiculo v){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TallerDbHelper.V_PATENTE, v.patente);
        cv.put(TallerDbHelper.V_COLOR, v.color);
        cv.put(TallerDbHelper.V_MODELO, v.modelo);
        if (v.runDueno != null) {
            cv.put(TallerDbHelper.V_RUN_DUENO, v.runDueno);
        }
        long row = db.insert(TallerDbHelper.T_VEHICULO, null, cv);
        db.close();
        return row;
    }

    // ------------------------------------------------------------------
    // == Metodo: listByRunDueno
    //    Parámetros:
    //      - runDueno (int): RUN del propietario cuyos vehículos queremos listar.
    //    Descripción detallada:
    //      - Usa la API de query() para seleccionar columnas específicas
    //        (patente, color, modelo, runDueno) filtrando por V_RUN_DUENO = ?.
    //      - Ordena el resultado por patente ASC para una lectura estable.
    //      - Mapea cada fila del Cursor a una instancia Vehiculo y la agrega
    //        a la lista de salida.
    //      - Si la columna RUN_DUENO viene NULL en la fila, el campo en el
    //        objeto Vehiculo se establece como null (Integer).
    //    Retorno:
    //      - List<Vehiculo>: lista (posiblemente vacía) con los vehículos
    //        del dueño indicado.
    // ------------------------------------------------------------------
    public List<Vehiculo> listByRunDueno(int runDueno){
        List<Vehiculo> out = new ArrayList<>();
        try (Cursor c = helper.getReadableDatabase().query(
                TallerDbHelper.T_VEHICULO,
                new String[]{TallerDbHelper.V_PATENTE, TallerDbHelper.V_COLOR, TallerDbHelper.V_MODELO, TallerDbHelper.V_RUN_DUENO},
                TallerDbHelper.V_RUN_DUENO + " = ?",
                new String[]{ String.valueOf(runDueno) }, null, null, TallerDbHelper.V_PATENTE + " ASC")) {
            while (c.moveToNext()) {
                Integer rd;
                if (c.isNull(3)) {
                    rd = null;
                } else {
                    rd = c.getInt(3);
                }
                out.add(new Vehiculo(c.getString(0), c.getString(1), c.getString(2), rd));
            }
        }
        return out;
    }

    // ------------------------------------------------------------------
    // == Metodo: listAll
    //    Parámetros:
    //      - (sin parámetros)
    //    Descripción detallada:
    //      - Lista todos los vehículos existentes seleccionando las
    //        mismas columnas clave (patente, color, modelo, runDueno).
    //      - Orden por patente ASC.
    //      - Mapea NULL en runDueno a null en el objeto de dominio.
    //    Retorno:
    //      - List<Vehiculo>: lista completa (posiblemente vacía).
    // ------------------------------------------------------------------
    public List<Vehiculo> listAll(){
        List<Vehiculo> out = new ArrayList<>();
        try (Cursor c = helper.getReadableDatabase().query(
                TallerDbHelper.T_VEHICULO,
                new String[]{TallerDbHelper.V_PATENTE, TallerDbHelper.V_COLOR, TallerDbHelper.V_MODELO, TallerDbHelper.V_RUN_DUENO},
                null, null, null, null, TallerDbHelper.V_PATENTE + " ASC")) {
            while (c.moveToNext()) {
                Integer rd;
                if (c.isNull(3)) {
                    rd = null;
                } else {
                    rd = c.getInt(3);
                }
                out.add(new Vehiculo(c.getString(0), c.getString(1), c.getString(2), rd));
            }
        }
        return out;
    }

    // ------------------------------------------------------------------
    // == Metodo: update
    //    Parámetros:
    //      - v (Vehiculo): entidad con la PK (patente) existente y los
    //        nuevos valores para actualizar.
    //    Descripción detallada:
    //      - Actualiza color y modelo del vehículo con patente = v.patente.
    //      - Si v.runDueno != null, también actualiza el RUN del dueño.
    //        (Si se quiere quitar el dueño —poner NULL—, este metodo no
    //         lo hace porque solo escribe cuando hay valor; se necesitaría
    //         una variante que acepte explícitamente null).
    //    Retorno:
    //      - int: número de filas afectadas (0 si no existe la patente).
    // ------------------------------------------------------------------
    public int update(Vehiculo v){
        ContentValues cv = new ContentValues();
        cv.put(TallerDbHelper.V_COLOR, v.color);
        cv.put(TallerDbHelper.V_MODELO, v.modelo);
        if (v.runDueno != null) {
            cv.put(TallerDbHelper.V_RUN_DUENO, v.runDueno);
        }
        return helper.getWritableDatabase().update(
                TallerDbHelper.T_VEHICULO, cv,
                TallerDbHelper.V_PATENTE + " = ?", new String[]{ v.patente });
    }

    // ------------------------------------------------------------------
    // == Metodo: delete
    //    Parámetros:
    //      - patente (String): clave primaria del vehículo a eliminar.
    //    Descripción detallada:
    //      - Elimina la fila de T_VEHICULO cuya patente coincida.
    //      - Si hay claves foráneas (por ejemplo, órdenes ligadas a la
    //        patente), la operación puede fallar si ON DELETE no lo permite,
    //        dependiendo de la definición del esquema.
    //    Retorno:
    //      - int: filas eliminadas (0 si no se encontró la patente).
    // ------------------------------------------------------------------
    public int delete(String patente){
        return helper.getWritableDatabase().delete(
                TallerDbHelper.T_VEHICULO,
                TallerDbHelper.V_PATENTE + " = ?", new String[]{ patente });
    }

    // ------------------------------------------------------------------
    // == Metodo: count
    //    Parámetros:
    //      - (sin parámetros)
    //    Descripción detallada:
    //      - Ejecuta SELECT COUNT(*) sobre T_VEHICULO para obtener el
    //        total de vehículos registrados.
    //    Retorno:
    //      - int: cantidad total de filas en la tabla (0 si no hay).
    // ------------------------------------------------------------------
    public int count() {
        SQLiteDatabase db = helper.getReadableDatabase();
        try (Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TallerDbHelper.T_VEHICULO, null)) {
            if (c.moveToFirst()) {
                return c.getInt(0);
            } else {
                return 0;
            }
        }
    }
}
