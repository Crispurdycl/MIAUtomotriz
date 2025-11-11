package com.cabroninja.tallermiaumovil.ui;

import android.view.*; import android.widget.TextView;
import androidx.annotation.NonNull; import androidx.recyclerview.widget.RecyclerView;
import com.cabroninja.tallermiaumovil.R;
import com.cabroninja.tallermiaumovil.model.OrdenTrabajo;
import java.util.ArrayList; import java.util.List;
import java.util.Locale;

// ======================================================================
// == Clase: OrdenAdapter
// == Hereda de: RecyclerView.Adapter<OrdenAdapter.VH>
// == Rol dentro de la app:
//    - Adaptador de RecyclerView para listar OrdenTrabajo (OT).
//    - Encapsula el formateo de cada línea (folio, fecha y total).
//    - Expone un callback de "long press" para acciones de contexto
//      (por ejemplo, eliminar o editar una OT).
// ======================================================================
public class OrdenAdapter extends RecyclerView.Adapter<OrdenAdapter.VH> {

    // ------------------------------------------------------------------
    // == Atributo: data
    //    Tipo: List<OrdenTrabajo>
    //    Rol : Fuente de datos actual del adapter; se reemplaza completa
    //          mediante submit(List<OrdenTrabajo>).
    //    Nota: Comienza vacía para evitar null checks.
    // ------------------------------------------------------------------
    private final List<OrdenTrabajo> data = new ArrayList<>();

    // ------------------------------------------------------------------
    // == Constructor: OrdenAdapter
    //    Parámetros:
    //      - onLong (OnLong): implementación del callback de "long press".
    //    Descripción:
    //      - Guarda la referencia al callback para notificar eventos de UI.
    //    Retorno:
    //      - (constructor) instancia lista para asignar al RecyclerView.
    // ------------------------------------------------------------------
    public OrdenAdapter(){}

    // ==================================================================
    // == Clase interna: VH (ViewHolder)
    // == Hereda de: RecyclerView.ViewHolder
    // == Rol:
    //    - Mantiene referencias a las vistas del item para reciclarlas.
    //    - Evita búsquedas repetidas con findViewById en cada bind.
    // ==================================================================
    static class VH extends RecyclerView.ViewHolder {

        // --------------------------------------------------------------
        // == Atributo: linea
        //    Tipo: TextView
        //    Rol : Muestra una línea resumida de la OT:
        //          "OT <numero> — <fecha> — Total: $<monto>"
        // --------------------------------------------------------------
        TextView linea;

        // --------------------------------------------------------------
        // == Constructor: VH
        //    Parámetros:
        //      - itemView (View): raíz del layout del item (R.layout.item_orden).
        //    Descripción:
        //      - Resuelve y almacena la referencia al TextView principal.
        //    Retorno:
        //      - (constructor) ViewHolder listo para bindear datos.
        // --------------------------------------------------------------
        VH(@NonNull View itemView) {
            super(itemView);
            linea = itemView.findViewById(R.id.txtLinea);
        }
    }

    // ------------------------------------------------------------------
    // == Método: onCreateViewHolder
    //    Parámetros:
    //      - parent (ViewGroup): contenedor padre.
    //      - vt (int): viewType (no se usan múltiples tipos aquí).
    //    Descripción:
    //      - Infla R.layout.item_orden y crea el ViewHolder correspondiente.
    //    Retorno:
    //      - VH: nuevo ViewHolder.
    // ------------------------------------------------------------------
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int vt) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orden, parent, false);
        return new VH(v);
    }

    // ------------------------------------------------------------------
    // == Método: onBindViewHolder
    //    Parámetros:
    //      - h (VH): ViewHolder con vistas cacheadas.
    //      - pos (int): posición del elemento dentro de data.
    //    Descripción:
    //      - Construye una línea de texto con:
    //          • Prefijo "OT " + numero
    //          • Fecha
    //          • Total formateado sin decimales con separadores de miles
    //            usando Locale.US (ej.: 19000 → "19,000").
    //      - Asigna un listener de long click que invoca el callback
    //        onLong con la OrdenTrabajo correspondiente.
    //    Retorno:
    //      - void.
    //    Nota:
    //      - Si tu app necesita formato monetario local (puntos en miles,
    //        coma decimal, símbolo CLP, etc.), considera NumberFormat
    //        con Locale específico (por ejemplo, "es_CL") o formateo
    //        propio según requerimientos.
    // ------------------------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        OrdenTrabajo o = data.get(pos);
        String s = "OT " + o.numero + " — " + o.fecha + " — Total: $" + String.format(Locale.US, "%,.0f", o.total());
        h.linea.setText(s);
    }

    // ------------------------------------------------------------------
    // == Método: getItemCount
    //    Parámetros:
    //      - (sin parámetros)
    //    Descripción:
    //      - Devuelve la cantidad de elementos actualmente en la lista.
    //    Retorno:
    //      - int: tamaño de data.
    // ------------------------------------------------------------------
    @Override
    public int getItemCount(){ return data.size(); }

    // ------------------------------------------------------------------
    // == Método: submit
    //    Parámetros:
    //      - nuevos (List<OrdenTrabajo>): nueva colección a mostrar (puede ser null).
    //    Descripción:
    //      - Reemplaza el contenido de data por la lista indicada y
    //        notifica un refresco completo del RecyclerView.
    //    Retorno:
    //      - void.
    //    Nota:
    //      - Para listas grandes, usar DiffUtil para actualizaciones
    //        eficientes y animadas en vez de notifyDataSetChanged().
    // ------------------------------------------------------------------
    public void submit(List<OrdenTrabajo> nuevos){
        data.clear(); if (nuevos != null) data.addAll(nuevos);
        notifyDataSetChanged();
    }
}
