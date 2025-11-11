package com.cabroninja.tallermiaumovil.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// ======================================================================
// == Clase: TallerDbHelper
// == Hereda de: SQLiteOpenHelper
// == Rol dentro de la app:
//    - Punto central de gestión de la base de datos SQLite.
//    - Define nombres de tablas/columnas y crea/actualiza el esquema.
//    - Habilita claves foráneas (FK) para asegurar integridad referencial.
//    - Es consumido por los repositorios (PersonaRepository, VehiculoRepository,
//      OrdenRepository) para obtener conexiones de lectura/escritura.
// ======================================================================
public class TallerDbHelper extends SQLiteOpenHelper {

    // ------------------------------------------------------------------
    // == Atributo: DB_NAME
    //    Tipo: String (constante)
    //    Rol: Nombre físico del archivo de base de datos.
    //    Ubicación: /data/data/<paquete>/databases/DB_NAME
    // ------------------------------------------------------------------
    public static final String DB_NAME = "taller_miau3.db";

    // ------------------------------------------------------------------
    // == Atributo: DB_VERSION
    //    Tipo: int (constante)
    //    Rol: Versión del esquema. Si cambia, Android llamará a onUpgrade()
    //         (o onDowngrade) según corresponda.
    //    Nota: se incrementa (“subido”) cuando se modifica el esquema
    //          para forzar la migración.
    // ------------------------------------------------------------------
    private static final int DB_VERSION = 16;

    // ------------------------------------------------------------------
    // == Atributo: T_PERSONA
    //    Tipo: String (constante)
    //    Rol: Nombre de la tabla única para clientes y trabajadores.
    // ------------------------------------------------------------------
    public static final String T_PERSONA = "persona";

    // ------------------------------------------------------------------
    // == Atributo: C_RUN
    //    Tipo: String (constante con el nombre de columna)
    //    Rol: Columna PK (INTEGER) del RUN/RUT (sin dígito verificador).
    // ------------------------------------------------------------------
    public static final String C_RUN = "run";

    // ------------------------------------------------------------------
    // == Atributo: C_EMAIL
    //    Tipo: String (constante)
    //    Rol: Columna del email (TEXT UNIQUE NOT NULL).
    // ------------------------------------------------------------------
    public static final String C_EMAIL = "email";

    // ------------------------------------------------------------------
    // == Atributo: C_NOMBRE
    //    Tipo: String (constante)
    //    Rol: Columna del nombre (TEXT NOT NULL).
    // ------------------------------------------------------------------
    public static final String C_NOMBRE = "nombre";

    // ------------------------------------------------------------------
    // == Atributo: C_APELLIDO
    //    Tipo: String (constante)
    //    Rol: Columna del apellido (TEXT NOT NULL).
    // ------------------------------------------------------------------
    public static final String C_APELLIDO = "apellido";

    // ------------------------------------------------------------------
    // == Atributo: C_PASSWORD
    //    Tipo: String (constante)
    //    Rol: Columna de contraseña (TEXT NOT NULL).
    //    Seguridad: en demo se guarda plano; en producción debería ser hash.
    // ------------------------------------------------------------------
    public static final String C_PASSWORD = "password";

    // ------------------------------------------------------------------
    // == Atributo: C_TIPO
    //    Tipo: String (constante)
    //    Rol: Columna del tipo de persona (TEXT NOT NULL con CHECK):
    //         'cliente' | 'trabajador'.
    // ------------------------------------------------------------------
    public static final String C_TIPO = "tipo";

    // ------------------------------------------------------------------
    // == Atributo: T_VEHICULO
    //    Tipo: String (constante)
    //    Rol: Nombre de la tabla de vehículos.
    // ------------------------------------------------------------------
    public static final String T_VEHICULO = "vehiculo";

    // ------------------------------------------------------------------
    // == Atributo: V_PATENTE
    //    Tipo: String (constante)
    //    Rol: Columna PK de patente (TEXT PRIMARY KEY).
    // ------------------------------------------------------------------
    public static final String V_PATENTE = "patente";

    // ------------------------------------------------------------------
    // == Atributo: V_COLOR
    //    Tipo: String (constante)
    //    Rol: Columna de color (TEXT).
    // ------------------------------------------------------------------
    public static final String V_COLOR = "color";

    // ------------------------------------------------------------------
    // == Atributo: V_MODELO
    //    Tipo: String (constante)
    //    Rol: Columna de modelo (TEXT).
    // ------------------------------------------------------------------
    public static final String V_MODELO = "modelo";

    // ------------------------------------------------------------------
    // == Atributo: V_RUN_DUENO
    //    Tipo: String (constante)
    //    Rol: Columna FK opcional al RUN del dueño (INTEGER NULL) → persona.run.
    // ------------------------------------------------------------------
    public static final String V_RUN_DUENO = "run_dueno";

    // ------------------------------------------------------------------
    // == Atributo: T_ORDEN
    //    Tipo: String (constante)
    //    Rol: Nombre de la tabla de órdenes de trabajo.
    // ------------------------------------------------------------------
    public static final String T_ORDEN = "orden_trabajo";

    // ------------------------------------------------------------------
    // == Atributo: O_ID
    //    Tipo: String (constante)
    //    Rol: Columna PK autoincremental (INTEGER PRIMARY KEY AUTOINCREMENT).
    // ------------------------------------------------------------------
    public static final String O_ID = "id";

    // ------------------------------------------------------------------
    // == Atributo: O_NUMERO
    //    Tipo: String (constante)
    //    Rol: Columna con folio/código legible (TEXT).
    // ------------------------------------------------------------------
    public static final String O_NUMERO = "numero";

    // ------------------------------------------------------------------
    // == Atributo: O_FECHA
    //    Tipo: String (constante)
    //    Rol: Columna de fecha (TEXT). Formato manejado por la app.
    // ------------------------------------------------------------------
    public static final String O_FECHA = "fecha";

    // ------------------------------------------------------------------
    // == Atributo: O_VALOR_NETO
    //    Tipo: String (constante)
    //    Rol: Columna de valor neto (REAL).
    // ------------------------------------------------------------------
    public static final String O_VALOR_NETO = "valor_neto";

    // ------------------------------------------------------------------
    // == Atributo: O_IVA
    //    Tipo: String (constante)
    //    Rol: Columna del IVA (REAL).
    // ------------------------------------------------------------------
    public static final String O_IVA = "iva";

    // ------------------------------------------------------------------
    // == Atributo: O_OBSERVACION
    //    Tipo: String (constante)
    //    Rol: Columna de observaciones/nota (TEXT).
    // ------------------------------------------------------------------
    public static final String O_OBSERVACION = "observacion";

    // ------------------------------------------------------------------
    // == Atributo: O_PATENTE
    //    Tipo: String (constante)
    //    Rol: Columna FK a vehiculo.patente (TEXT).
    // ------------------------------------------------------------------
    public static final String O_PATENTE = "patente";

    // ------------------------------------------------------------------
    // == Constructor: TallerDbHelper
    //    Parámetros:
    //      - ctx (Context): contexto Android para abrir/crear la DB.
    //    Descripción:
    //      - Pasa nombre y versión a SQLiteOpenHelper. El framework
    //        decide cuándo llamar a onCreate/onUpgrade según existencia
    //        y versión actual del archivo.
    //    Retorno:
    //      - (constructor) instancia lista para solicitar conexiones.
    // ------------------------------------------------------------------
    public TallerDbHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    // ------------------------------------------------------------------
    // == Metodo: onConfigure
    //    Parámetros:
    //      - db (SQLiteDatabase): conexión recién creada/abierta.
    //    Descripción:
    //      - Habilita el soporte de claves foráneas en SQLite para Android.
    //        (SQLite requiere activarlo explícitamente por conexión).
    //    Retorno:
    //      - void (callback del ciclo de vida de la DB).
    // ------------------------------------------------------------------
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // Asegura claves foráneas activas
        db.setForeignKeyConstraintsEnabled(true);
    }

    // ------------------------------------------------------------------
    // == Metodo: onCreate
    //    Parámetros:
    //      - db (SQLiteDatabase): conexión de escritura a una DB recién
    //        creada (aún sin tablas).
    //    Descripción:
    //      - Crea el esquema inicial:
    //        1) PERSONA: PK (run), email único, nombre/apellido obligatorios,
    //           password obligatoria y CHECK en tipo ('cliente','trabajador').
    //        2) VEHICULO: PK (patente). FK opcional a persona.run con
    //           ON UPDATE CASCADE y ON DELETE SET NULL (si se elimina el dueño,
    //           el vehículo queda con run_dueno = NULL).
    //        3) ORDEN_TRABAJO: PK autoincremental. FK a vehiculo.patente con
    //           ON UPDATE CASCADE y ON DELETE CASCADE (si se elimina el vehículo,
    //           se eliminan sus órdenes asociadas).
    //    Retorno:
    //      - void.
    // ------------------------------------------------------------------
    @Override
    public void onCreate(SQLiteDatabase db) {
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

    // ------------------------------------------------------------------
    // == Metodo: onUpgrade
    //    Parámetros:
    //      - db (SQLiteDatabase): conexión a la DB existente.
    //      - oldV (int): versión actual instalada.
    //      - newV (int): nueva versión pedida por la app (DB_VERSION).
    //    Descripción:
    //      - Estrategia simple de migración: DROP & CREATE.
    //        1) Elimina tablas antiguas si existen (incluye un nombre
    //           legado "trabajador" por compatibilidad con versiones previas).
    //        2) Recrea el esquema desde cero llamando a onCreate().
    //      - Advertencia: esta estrategia borra datos; sirve para desarrollo
    //        o cuando no hay necesidad de preservar la información. En
    //        producción, implementar migraciones no destructivas (ALTER TABLE).
    //    Retorno:
    //      - void.
    // ------------------------------------------------------------------
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Limpia tabla vieja si quedaba de versiones anteriores
        db.execSQL("DROP TABLE IF EXISTS trabajador");

        db.execSQL("DROP TABLE IF EXISTS " + T_ORDEN);
        db.execSQL("DROP TABLE IF EXISTS " + T_VEHICULO);
        db.execSQL("DROP TABLE IF EXISTS " + T_PERSONA);
        onCreate(db);
    }
}
