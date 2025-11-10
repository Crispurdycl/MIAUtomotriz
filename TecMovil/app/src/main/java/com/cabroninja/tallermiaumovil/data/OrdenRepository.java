package com.cabroninja.tallermiaumovil.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cabroninja.tallermiaumovil.model.OrdenTrabajo;
import java.util.ArrayList;
import java.util.List;

public class OrdenRepository {
    private final TallerDbHelper helper;
    public OrdenRepository(Context ctx){ helper = new TallerDbHelper(ctx); }

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

    public int delete(long id){
        return helper.getWritableDatabase().delete(
                TallerDbHelper.T_ORDEN,
                TallerDbHelper.O_ID + " = ?", new String[]{ String.valueOf(id) });
    }


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
