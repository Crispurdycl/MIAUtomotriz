package com.cabroninja.tallermiaumovil.ui;

import android.view.*; import android.widget.TextView;
import androidx.annotation.NonNull; import androidx.recyclerview.widget.RecyclerView;
import com.cabroninja.tallermiaumovil.R;
import com.cabroninja.tallermiaumovil.model.Vehiculo;
import java.util.ArrayList; import java.util.List;

// ======================================================================
// == Clase: VehiculoAdapter
// == Hereda de: RecyclerView.Adapter<VehiculoAdapter.VH)
// == Rol dentro de la app:
//    - Adaptador para mostrar una lista de Vehiculo en un RecyclerView.
//    - Infla el layout de fila (item_vehiculo) y enlaza los datos del
//      modelo Vehiculo en un TextView (formato provisto por toString()).
//    - Exponer un callback OnClick para que la Activity controle la
//      navegación/acciones al tocar un ítem.
//    - Incluye una interfaz OnLong (no cableada en este adapter) por si
//      se quiere manejar pulsación larga a futuro sin cambiar las firmas.
// ======================================================================
public class VehiculoAdapter extends RecyclerView.Adapter<VehiculoAdapter.VH> {

    // ------------------------------------------------------------------
    // == Interface: OnClick
    //    Rol:
    //      - Contrato de callback para “tap” en un ítem de vehículo.
    //    Método:
    //      - void onClick(Vehiculo v): recibe el vehículo seleccionado.
    // ------------------------------------------------------------------
    public interface OnClick { void onClick(Vehiculo v); }

    // ------------------------------------------------------------------
    // == Atributo: data
    //    Tipo: List<Vehiculo>
    //    Rol : Fuente de datos actual a renderizar en el RecyclerView.
    //    Nota: Inicia vacío para evitar nulos; se reemplaza con submit().
    // ------------------------------------------------------------------
    private final List<Vehiculo> data = new ArrayList<>();

    // ------------------------------------------------------------------
    // == Atributo: onClick
    //    Tipo: OnClick
    //    Rol : Callback para notificar selecciones de ítems a la UI.
    // ------------------------------------------------------------------
    private final OnClick onClick;

    // ------------------------------------------------------------------
    // == Constructor: VehiculoAdapter
    //    Parámetros:
    //      - onClick (OnClick): implementación del callback de tap.
    //    Descripción:
    //      - Guarda el callback para invocarlo desde onBindViewHolder
    //        cuando el usuario toca una fila.
    //    Retorno:
    //      - (constructor) instancia lista para asignar al RecyclerView.
    // ------------------------------------------------------------------
    public VehiculoAdapter(OnClick onClick){ this.onClick = onClick; }

    // ==================================================================
    // == Clase interna: VH (ViewHolder)
    // == Hereda de: RecyclerView.ViewHolder
    // == Rol:
    //    - Cachea referencias a vistas del layout de ítem para evitar
    //      llamadas repetidas a findViewById durante el scrolleo.
    // ==================================================================
    static class VH extends RecyclerView.ViewHolder {

        // --------------------------------------------------------------
        // == Atributo: linea
        //    Tipo: TextView
        //    Rol : Muestra la representación legible del vehículo.
        //          (Se usa Vehiculo.toString(): "patente • modelo • color")
        // --------------------------------------------------------------
        TextView linea;

        // --------------------------------------------------------------
        // == Constructor: VH
        //    Parámetros:
        //      - itemView (View): raíz inflada de R.layout.item_vehiculo.
        //    Descripción:
        //      - Resuelve la referencia al TextView principal (txtLinea).
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
    //      - parent (ViewGroup): contenedor padre del ítem.
    //      - vt (int): viewType (no se usan múltiples tipos aquí).
    //    Descripción:
    //      - Infla el layout R.layout.item_vehiculo y crea el ViewHolder.
    //    Retorno:
    //      - VH: nuevo ViewHolder asociado al layout.
    // ------------------------------------------------------------------
    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int vt) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehiculo, parent, false);
        return new VH(v);
    }

    // ------------------------------------------------------------------
    // == Método: onBindViewHolder
    //    Parámetros:
    //      - h (VH): ViewHolder con vistas cacheadas.
    //      - pos (int): posición del elemento en la lista data.
    //    Descripción:
    //      - Obtiene el Vehiculo en data[pos], muestra su toString()
    //        en el TextView principal y configura el click del ítem
    //        para invocar onClick.onClick(v) si hay callback.
    //    Retorno:
    //      - void.
    // ------------------------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Vehiculo v = data.get(pos);
        h.linea.setText(v.toString());
    }

    // ------------------------------------------------------------------
    // == Método: getItemCount
    //    Parámetros:
    //      - (sin parámetros)
    //    Descripción:
    //      - Devuelve la cantidad de elementos que se están mostrando.
    //    Retorno:
    //      - int: tamaño de la lista data.
    // ------------------------------------------------------------------
    @Override
    public int getItemCount(){ return data.size(); }

    // ------------------------------------------------------------------
    // == Método: submit
    //    Parámetros:
    //      - nuevos (List<Vehiculo>): nueva colección a mostrar (puede ser null).
    //    Descripción:
    //      - Reemplaza por completo el contenido de data y notifica al
    //        RecyclerView para refrescar la UI.
    //    Retorno:
    //      - void.
    //    Nota:
    //      - Para listas grandes/actualizaciones parciales, considerar
    //        DiffUtil para diffs eficientes en vez de notifyDataSetChanged().
    // ------------------------------------------------------------------
    public void submit(List<Vehiculo> nuevos){
        data.clear(); if (nuevos != null) data.addAll(nuevos);
        notifyDataSetChanged();
    }
}
