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

public class PersonaRepository {
    private static final String TAG = "PersonaRepository";
    private final TallerDbHelper helper;

    public PersonaRepository(Context ctx) { this.helper = new TallerDbHelper(ctx); }

    // ---- CRUD ----

    public long insert(Persona p) {
        ContentValues v = new ContentValues();
        v.put(TallerDbHelper.C_RUN, p.run);
        v.put(TallerDbHelper.C_EMAIL, p.email);
        v.put(TallerDbHelper.C_NOMBRE, p.nombre);
        v.put(TallerDbHelper.C_APELLIDO, p.apellido);
        v.put(TallerDbHelper.C_PASSWORD, p.password);
        v.put(TallerDbHelper.C_TIPO, p.tipo);
        return helper.getWritableDatabase().insert(TallerDbHelper.T_PERSONA, null, v);
    }

    public int update(Persona p) {
        ContentValues v = new ContentValues();
        v.put(TallerDbHelper.C_EMAIL, p.email);
        v.put(TallerDbHelper.C_NOMBRE, p.nombre);
        v.put(TallerDbHelper.C_APELLIDO, p.apellido);
        v.put(TallerDbHelper.C_PASSWORD, p.password);
        v.put(TallerDbHelper.C_TIPO, p.tipo);
        return helper.getWritableDatabase().update(
                TallerDbHelper.T_PERSONA, v,
                TallerDbHelper.C_RUN + " = ?",
                new String[]{ String.valueOf(p.run) }
        );
    }

    public int deleteByRun(int run) {
        return helper.getWritableDatabase().delete(
                TallerDbHelper.T_PERSONA,
                TallerDbHelper.C_RUN + " = ?",
                new String[]{ String.valueOf(run) }
        );
    }

    public int deleteByEmail(String email) {
        return helper.getWritableDatabase().delete(
                TallerDbHelper.T_PERSONA,
                TallerDbHelper.C_EMAIL + " = ?",
                new String[]{ email }
        );
    }

    // ---- Lecturas ----

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

    public Persona getByEmail(String email) {
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
                        " WHERE " + TallerDbHelper.C_EMAIL + " = ?",
                new String[]{ email })) {
            if (c.moveToFirst()) {
                return new Persona(
                        c.getInt(0), c.getString(1), c.getString(2),
                        c.getString(3), c.getString(4), c.getString(5));
            }
            return null;
        } catch (Exception ex) {
            Log.e(TAG, "getByEmail error", ex);
            return null;
        }
    }

    public List<Persona> listAll() {
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
                        " ORDER BY " + TallerDbHelper.C_NOMBRE + ", " + TallerDbHelper.C_APELLIDO,
                null)) {
            while (c.moveToNext()) {
                out.add(new Persona(
                        c.getInt(0), c.getString(1), c.getString(2),
                        c.getString(3), c.getString(4), c.getString(5)));
            }
        }
        return out;
    }

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

    // ---- Conteos ----

    public int count() {
        SQLiteDatabase db = helper.getReadableDatabase();
        try (Cursor c = db.rawQuery(
                "SELECT COUNT(*) FROM " + TallerDbHelper.T_PERSONA, null)) {
            return c.moveToFirst() ? c.getInt(0) : 0;
        }
    }

    public int countByTipo(String tipo) {
        SQLiteDatabase db = helper.getReadableDatabase();
        try (Cursor c = db.rawQuery(
                "SELECT COUNT(*) FROM " + TallerDbHelper.T_PERSONA +
                        " WHERE " + TallerDbHelper.C_TIPO + " = ?",
                new String[]{ tipo })) {
            return c.moveToFirst() ? c.getInt(0) : 0;
        }
    }

    // ---- Autenticación de trabajador ----

    /**
     * Devuelve el nombre completo del trabajador si (email,password) es válido
     * y su tipo es 'trabajador'; si no, retorna null.
     */
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

                String nombre  = c.getString(0);
                String apellido= c.getString(1);
                String passDb  = c.getString(2);

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
