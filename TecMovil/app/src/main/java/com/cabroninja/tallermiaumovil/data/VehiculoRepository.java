package com.cabroninja.tallermiaumovil.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cabroninja.tallermiaumovil.model.Vehiculo;
import java.util.ArrayList;
import java.util.List;

public class VehiculoRepository {
    private final TallerDbHelper helper;
    public VehiculoRepository(Context ctx){ helper = new TallerDbHelper(ctx); }

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

    public int delete(String patente){
        return helper.getWritableDatabase().delete(
                TallerDbHelper.T_VEHICULO,
                TallerDbHelper.V_PATENTE + " = ?", new String[]{ patente });
    }


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
