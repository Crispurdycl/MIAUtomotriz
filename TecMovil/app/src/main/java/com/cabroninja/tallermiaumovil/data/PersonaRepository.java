package com.cabroninja.tallermiaumovil.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.cabroninja.tallermiaumovil.model.Persona;

import java.util.ArrayList;
import java.util.List;

// ======================================================================
// == Clase: PersonaRepository
// == Rol dentro de la app:
//    - Capa de acceso a datos para la entidad Persona.
//    - Centraliza operaciones CRUD, lecturas, conteos y autenticación
//      sobre la tabla T_PERSONA en SQLite.
//    - Aísla a la UI de los detalles SQL, usando TallerDbHelper para
//      gestionar la conexión y el ciclo de vida de la base de datos.
// ======================================================================
public class PersonaRepository {

    // ------------------------------------------------------------------
    // == Atributo: TAG
    //    Tipo: String (constante)
    //    Propósito: Prefijo para mensajes de log (depuración y errores).
    // ------------------------------------------------------------------
    private static final String TAG = "PersonaRepository";

    // ------------------------------------------------------------------
    // == Atributo: helper
    //    Tipo: TallerDbHelper
    //    Propósito:
    //      - Puerta de entrada a SQLite (hereda de SQLiteOpenHelper).
    //      - Crea/actualiza el esquema (onCreate/onUpgrade) y provee
    //        instancias de lectura/escritura (getReadableDatabase / getWritableDatabase).
    // ------------------------------------------------------------------
    private final TallerDbHelper helper;

    // ------------------------------------------------------------------
    // == Constructor: PersonaRepository
    //    Parámetros:
    //      - ctx (Context): contexto Android que el helper usará para
    //        abrir/crear la base de datos y acceder a recursos.
    //    Descripción:
    //      - Inicializa el helper de base de datos que sostiene todas
    //        las operaciones de este repositorio.
    //    Retorno:
    //      - (constructor) Crea una instancia lista para usar.
    // ------------------------------------------------------------------
    public PersonaRepository(Context ctx) {
        this.helper = new TallerDbHelper(ctx);
    }

    // ============================== CRUD ===============================

    // ------------------------------------------------------------------
    // == Método: insert
    //    Parámetros:
    //      - p (Persona): entidad con los campos poblados a insertar.
    //        Campos usados: run, email, nombre, apellido, password, tipo.
    //    Descripción:
    //      - Inserta una Persona en T_PERSONA mapeando cada campo a
    //        ContentValues. No valida unicidad (salvo restricciones SQL).
    //    Retorno:
    //      - long: rowId de la nueva fila si tuvo éxito, o -1 si falló.
    // ------------------------------------------------------------------
    public long insert(Persona p) {

        ContentValues v = new ContentValues(); // como un diccionario

        v.put(TallerDbHelper.C_RUN, p.run);
        v.put(TallerDbHelper.C_EMAIL, p.email);
        v.put(TallerDbHelper.C_NOMBRE, p.nombre);
        v.put(TallerDbHelper.C_APELLIDO, p.apellido);
        v.put(TallerDbHelper.C_PASSWORD, p.password);
        v.put(TallerDbHelper.C_TIPO, p.tipo);
        return helper.getWritableDatabase().insert(TallerDbHelper.T_PERSONA, null, v);
    }

    // ------------------------------------------------------------------
    // == Método: update
    //    Parámetros:
    //      - p (Persona): entidad con RUN existente y nuevos valores.
    //    Descripción:
    //      - Actualiza email, nombre, apellido, password y tipo de la
    //        persona cuyo RUN coincida con p.run.
    //    Retorno:
    //      - int: número de filas afectadas (0 si no existía el RUN).
    // ------------------------------------------------------------------
    public int update(Persona p) {
        ContentValues v = new ContentValues();
        v.put(TallerDbHelper.C_EMAIL, p.email);
        v.put(TallerDbHelper.C_NOMBRE, p.nombre);
        v.put(TallerDbHelper.C_APELLIDO, p.apellido);
        v.put(TallerDbHelper.C_PASSWORD, p.password);
        v.put(TallerDbHelper.C_TIPO, p.tipo);
        return helper.getWritableDatabase().update(TallerDbHelper.T_PERSONA, v,
                TallerDbHelper.C_RUN + " = ?",
                new String[]{ String.valueOf(p.run) }
        );
    }

    // ------------------------------------------------------------------
    // == Método: deleteByRun
    //    Parámetros:
    //      - run (int): identificador único primario de Persona.
    //    Descripción:
    //      - Elimina la persona cuyo RUN sea el indicado.
    //      - Respeta integridad referencial si hay FKs (según schema).
    //    Retorno:
    //      - int: número de filas eliminadas (0 si no se encontró).
    // ------------------------------------------------------------------
    public int deleteByRun(int run) {
        return helper.getWritableDatabase().delete(
                TallerDbHelper.T_PERSONA,
                TallerDbHelper.C_RUN + " = ?",
                new String[]{ String.valueOf(run) }
        );
    }


    // ============================= LECTURAS ============================

    // ------------------------------------------------------------------
    // == Método: getByRun
    //    Parámetros:
    //      - run (int): identificador primario a consultar.
    //    Descripción:
    //      - Ejecuta una SELECT por RUN y, si existe, construye un
    //        objeto Persona con todos los campos principales.
    //    Retorno:
    //      - Persona: entidad encontrada; o null si no hay resultados
    //        o si ocurrió un error (registrado en Logcat).
    // ------------------------------------------------------------------
    public Persona getByRun(int run) {
        SQLiteDatabase db = helper.getReadableDatabase();
        try (Cursor c = db.rawQuery(
                "SELECT " +
                        TallerDbHelper.C_RUN + "," +
                        TallerDbHelper.C_EMAIL + "," +
                        TallerDbHelper.C_NOMBRE + "," +
                        TallerDbHelper.C_APELLIDO + "," +
                        TallerDbHelper.C_PASSWORD + "," +
                        TallerDbHelper.C_TIPO +
                        " FROM " + TallerDbHelper.T_PERSONA +
                        " WHERE " + TallerDbHelper.C_RUN + " = ?",
                new String[]{ String.valueOf(run) })) {
            if (c.moveToFirst()) {
                return new Persona(
                        c.getInt(0), c.getString(1), c.getString(2),
                        c.getString(3), c.getString(4), c.getString(5));
            }
            return null;
        } catch (Exception ex) {
            Log.e(TAG, "getByRun error", ex);
            return null;
        }
    }

    // ------------------------------------------------------------------
    // == Método: listByTipo
    //    Parámetros:
    //      - tipo (String): “cliente” o “trabajador”. Se asume válido
    //        según el CHECK de la tabla.
    //    Descripción:
    //      - Devuelve las personas cuyo C_TIPO coincide con el parámetro,
    //        ordenadas por nombre y apellido.
    //    Retorno:
    //      - List<Persona>: lista (posiblemente vacía) de resultados.
    // ------------------------------------------------------------------
    public List<Persona> listByTipo(String tipo) {
        List<Persona> out = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        try (Cursor c = db.rawQuery(
                "SELECT " +
                        TallerDbHelper.C_RUN + "," +
                        TallerDbHelper.C_EMAIL + "," +
                        TallerDbHelper.C_NOMBRE + "," +
                        TallerDbHelper.C_APELLIDO + "," +
                        TallerDbHelper.C_PASSWORD + "," +
                        TallerDbHelper.C_TIPO +
                        " FROM " + TallerDbHelper.T_PERSONA +
                        " WHERE " + TallerDbHelper.C_TIPO + " = ?" +
                        " ORDER BY " + TallerDbHelper.C_NOMBRE + ", " + TallerDbHelper.C_APELLIDO,
                new String[]{ tipo })) {
            while (c.moveToNext()) {
                out.add(new Persona(
                        c.getInt(0), c.getString(1), c.getString(2),
                        c.getString(3), c.getString(4), c.getString(5)));
            }
        }
        return out;
    }

    // ------------------------------------------------------------------
    // == Método: countByTipo
    //    Parámetros:
    //      - tipo (String): “cliente” o “trabajador”.
    //    Descripción:
    //      - Cuenta cuántas personas pertenecen al tipo indicado.
    //    Retorno:
    //      - int: cantidad de filas que cumplen la condición.
    // ------------------------------------------------------------------
    public int countByTipo(String tipo) {
        SQLiteDatabase db = helper.getReadableDatabase();
        try (Cursor c = db.rawQuery(
                "SELECT COUNT(*) FROM " + TallerDbHelper.T_PERSONA +
                        " WHERE " + TallerDbHelper.C_TIPO + " = ?",
                new String[]{ tipo })) {
            return c.moveToFirst() ? c.getInt(0) : 0;
        }
    }

    // ====================== AUTENTICACIÓN (trabajador) =================

    // ------------------------------------------------------------------
    // == Método: authenticateTrabajador
    //    Parámetros:
    //      - email (String): correo ingresado por el usuario.
    //      - plainPassword (String): contraseña en texto plano tal como
    //        se capturó del formulario de login.
    //    Descripción:
    //      - Busca una persona por email cuyo tipo sea 'trabajador'.
    //      - Recupera nombre, apellido y password almacenado.
    //      - Compara la contraseña ingresada (plainPassword) con la
    //        almacenada (passDb) usando igualdad directa de String.
    //      - Si coincide, arma y devuelve el nombre completo (nombre + apellido).
    //      - Si no coincide o no existe registro, devuelve null.
    //    Retorno:
    //      - String: nombre completo del trabajador autenticado; o null
    //        si las credenciales no son válidas o no existe el email/tipo.
// ------------------------------------------------------------------
    public String authenticateTrabajador(String email, String plainPassword) {
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            try (Cursor c = db.rawQuery(
                    "SELECT " +
                            TallerDbHelper.C_NOMBRE + ", " +
                            TallerDbHelper.C_APELLIDO + ", " +
                            TallerDbHelper.C_PASSWORD +
                            " FROM " + TallerDbHelper.T_PERSONA +
                            " WHERE " + TallerDbHelper.C_EMAIL + " = ? AND " +
                            TallerDbHelper.C_TIPO  + " = 'trabajador'",
                    new String[]{ email })) {

                if (!c.moveToFirst()) return null;

                String nombre   = c.getString(0);
                String apellido = c.getString(1);
                String passDb   = c.getString(2);

                if (plainPassword != null && plainPassword.equals(passDb)) {
                    String full = (nombre != null ? nombre : "") +
                            ((apellido != null && !apellido.isEmpty()) ? (" " + apellido) : "");
                    full = full.trim();
                    return full.isEmpty() ? null : full;
                } else {
                    return null;
                }
            }
        } catch (SQLiteException ex) {
            Log.e(TAG, "authenticateTrabajador: SQLiteException", ex);
            return null;
        }
    }
}
