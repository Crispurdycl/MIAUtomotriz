package com.cabroninja.tallermiaumovil.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TallerDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "taller_miau3.db";
    private static final int DB_VERSION = 13; // <- subido para aplicar upgrade

    // Tabla PERSONA (única fuente de verdad para clientes y trabajadores)
    public static final String T_PERSONA   = "persona";
    public static final String C_RUN       = "run";
    public static final String C_EMAIL     = "email";
    public static final String C_NOMBRE    = "nombre";
    public static final String C_APELLIDO  = "apellido";
    public static final String C_PASSWORD  = "password";
    public static final String C_TIPO      = "tipo"; // 'cliente' | 'trabajador'

    // Tabla VEHICULO
    public static final String T_VEHICULO    = "vehiculo";
    public static final String V_PATENTE     = "patente";
    public static final String V_COLOR       = "color";
    public static final String V_MODELO      = "modelo";
    public static final String V_RUN_DUENO   = "run_dueno";

    // Tabla ORDEN_TRABAJO
    public static final String T_ORDEN        = "orden_trabajo";
    public static final String O_ID           = "id";
    public static final String O_NUMERO       = "numero";
    public static final String O_FECHA        = "fecha";
    public static final String O_VALOR_NETO   = "valor_neto";
    public static final String O_IVA          = "iva";
    public static final String O_OBSERVACION  = "observacion";
    public static final String O_PATENTE      = "patente";

    public TallerDbHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // Asegura claves foráneas activas
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        // PERSONA
        db.execSQL("CREATE TABLE " + T_PERSONA + " (" +
                C_RUN      + " INTEGER PRIMARY KEY, " +
                C_EMAIL    + " TEXT UNIQUE NOT NULL, " +
                C_NOMBRE   + " TEXT NOT NULL, " +
                C_APELLIDO + " TEXT NOT NULL, " +
                C_PASSWORD + " TEXT NOT NULL, " +
                C_TIPO     + " TEXT NOT NULL CHECK(" + C_TIPO + " IN ('cliente','trabajador'))" +
                ")");

        // VEHICULO (FK -> PERSONA.run)
        db.execSQL("CREATE TABLE " + T_VEHICULO + " (" +
                V_PATENTE    + " TEXT PRIMARY KEY, " +
                V_COLOR      + " TEXT, " +
                V_MODELO     + " TEXT, " +
                V_RUN_DUENO  + " INTEGER REFERENCES " + T_PERSONA + "(" + C_RUN + ") " +
                "ON UPDATE CASCADE ON DELETE SET NULL" +
                ")");

        // ORDEN_TRABAJO (FK -> VEHICULO.patente)
        db.execSQL("CREATE TABLE " + T_ORDEN + " (" +
                O_ID           + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                O_NUMERO       + " TEXT, " +
                O_FECHA        + " TEXT, " +
                O_VALOR_NETO   + " REAL, " +
                O_IVA          + " REAL, " +
                O_OBSERVACION  + " TEXT, " +
                O_PATENTE      + " TEXT REFERENCES " + T_VEHICULO + "(" + V_PATENTE + ") " +
                "ON UPDATE CASCADE ON DELETE CASCADE" +
                ")");
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Limpia tabla vieja si quedaba de versiones anteriores
        db.execSQL("DROP TABLE IF EXISTS trabajador");

        db.execSQL("DROP TABLE IF EXISTS " + T_ORDEN);
        db.execSQL("DROP TABLE IF EXISTS " + T_VEHICULO);
        db.execSQL("DROP TABLE IF EXISTS " + T_PERSONA);
        onCreate(db);
    }
}
