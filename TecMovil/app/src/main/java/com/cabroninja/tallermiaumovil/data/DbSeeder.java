package com.cabroninja.tallermiaumovil.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cabroninja.tallermiaumovil.model.Vehiculo;
import com.cabroninja.tallermiaumovil.model.OrdenTrabajo;
import com.cabroninja.tallermiaumovil.model.Persona;

// ======================================================================
// == Clase: DbSeeder
// == Rol dentro de la app:
//    - Inicializa (si es necesario) la base de datos con datos mínimos
//      de ejemplo para poder usar la app inmediatamente en desarrollo.
//    - Solo si la tabla PERSONA está vacía: crea trabajadores demo,
//      clientes demo, sus vehículos y algunas órdenes de trabajo.
// ======================================================================
public class DbSeeder {

    // ------------------------------------------------------------------
    // == Método: seedIfEmpty
    //    Modificador: static (se usa sin instanciar la clase)
    //    Parámetros:
    //      - ctx (Context): contexto Android necesario para abrir/crear
    //        la base de datos a través de TallerDbHelper.
    //    Descripción detallada:
    //      1) Revisa un "toggle" local ENABLE_SEED. Si está desactivado,
    //         no hace nada.
    //      2) Abre la DB en modo lectura y pregunta cuántas filas hay en
    //         PERSONA. Si hay al menos 1, asume que la DB ya está poblada
    //         y retorna sin cambios.
    //      3) Si no hay personas, instancia los repositorios y realiza
    //         inserciones mínimas: 2 trabajadores (login), 2 clientes,
    //         2 vehículos y 3 órdenes de trabajo de ejemplo.
    //    Retorno:
    //      - void (produce efectos persistiendo datos).
    // ------------------------------------------------------------------
    public static void seedIfEmpty(Context ctx){

        final boolean ENABLE_SEED = true;
        if (!ENABLE_SEED) return;

        // 1) Si ya hay personas (cliente/trabajador), no hacemos nada
        TallerDbHelper helper = new TallerDbHelper(ctx);
        SQLiteDatabase rdb = helper.getReadableDatabase();
        boolean hasPeople = false;
        try (Cursor c = rdb.rawQuery("SELECT COUNT(*) FROM " + TallerDbHelper.T_PERSONA, null)) {
            if (c.moveToFirst()) {
                hasPeople = c.getInt(0) > 0;
            }
        }
        if (hasPeople) return;

        // 2) Insertar personas (usando modelo Persona + PersonaRepository)
        PersonaRepository pr = new PersonaRepository(ctx);

        // Trabajadores (permiten iniciar sesión)
        pr.insert(new Persona(99999991, "mecanico1@taller.cl", "Mecánico", "Uno", "123456", "trabajador"));
        pr.insert(new Persona(99999992, "admin@taller.cl", "Admin", "General", "admin123", "trabajador"));

        // Clientes (para probar listados/relaciones)
        pr.insert(new Persona(11111111, "ana@demo.cl", "Ana", "Pérez", "123456", "cliente"));
        pr.insert(new Persona(22222222, "benja@demo.cl", "Benjamín", "Silva", "123456", "cliente"));

        // 3) Vehículos y Órdenes asociados a clientes
        VehiculoRepository rv = new VehiculoRepository(ctx);
        OrdenRepository ro = new OrdenRepository(ctx);

        rv.insert(new Vehiculo("ABC123", "Azul", "Toyota Yaris 1.5", 11111111));
        rv.insert(new Vehiculo("XYZ987", "Rojo", "Chevrolet Corsa 1.6", 22222222));

        // O_ID se autogenera en la DB (id=0 aquí solo es placeholder)
        ro.insert(new OrdenTrabajo(0, "OT-0001", "2025-11-07", 100000, 19000, "Mantención", "ABC123"));
        ro.insert(new OrdenTrabajo(0, "OT-0002", "2025-11-06", 200000, 38000, "Frenos", "ABC123"));
        ro.insert(new OrdenTrabajo(0, "OT-0003", "2025-11-07", 150000, 28500, "Cambio de aceite", "XYZ987"));
    }
}
