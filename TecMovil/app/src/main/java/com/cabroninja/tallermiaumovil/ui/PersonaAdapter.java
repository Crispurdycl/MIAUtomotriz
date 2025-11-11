package com.cabroninja.tallermiaumovil.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cabroninja.tallermiaumovil.R;
import com.cabroninja.tallermiaumovil.model.Persona;

import java.util.ArrayList;
import java.util.List;

// ======================================================================
// == Clase: PersonaAdapter
// == Hereda de: RecyclerView.Adapter<PersonaAdapter.VH>
// == Rol dentro de la app:
//    - Adaptador de RecyclerView que muestra una lista de Persona.
//    - Recibe una función de callback (OnClick) para notificar el tap
//      sobre un item y dejar la navegación en manos de la Activity.
//    - Se encarga de inflar item_cliente, enlazar datos y formatear RUN.
// ======================================================================
public class PersonaAdapter extends RecyclerView.Adapter<PersonaAdapter.VH> {

    // ------------------------------------------------------------------
    // == Interface: OnClick
    //    Rol:
    //      - Contrato de callback para “item clickeado”.
//      - La Activity/Fragment que crea el adapter define qué hacer
    //        cuando el usuario toca un elemento (ej. navegar a otra pantalla).
    //    Método:
    //      - void onClick(Persona p): recibe la persona seleccionada.
    // ------------------------------------------------------------------
    public interface OnClick {
        void onClick(Persona p);
    }

    // ------------------------------------------------------------------
    // == Atributo: data
    //    Tipo: List<Persona>
    //    Rol : Fuente de datos actual del adapter. Se reemplaza completa
    //          mediante submit(List<Persona>) y se notifica al RV.
    //    Nota: Se inicializa con lista vacía para evitar null checks.
    // ------------------------------------------------------------------
    private final List<Persona> data = new ArrayList<>();

    // ------------------------------------------------------------------
    // == Atributo: onClick
    //    Tipo: OnClick
    //    Rol : Callback invocado desde onBindViewHolder al tocar el item.
    // ------------------------------------------------------------------
    private final OnClick onClick;

    // ------------------------------------------------------------------
    // == Constructor: PersonaAdapter
    //    Parámetros:
    //      - onClick (OnClick): implementación del callback de selección.
    //    Descripción:
    //      - Guarda la referencia al callback para usarla más adelante
    //        cuando el usuario toque una celda.
    //    Retorno:
    //      - (constructor) instancia lista para asignar al RecyclerView.
    // ------------------------------------------------------------------
    public PersonaAdapter(OnClick onClick){
        this.onClick = onClick;
    }

    // ------------------------------------------------------------------
    // == Método estático: findText
    //    Parámetros:
    //      - v (View): raíz del layout inflado del item.
    //      - candidates (String...): posibles IDs de TextView a probar.
    //    Descripción:
    //      - Heurística de compatibilidad: intenta encontrar un TextView
    //        probando múltiples nombres de id (getIdentifier) para
    //        tolerar layouts heredados con convenciones distintas.
    //      - Si no encuentra ninguno, devuelve un TextView vacío para
    //        evitar NullPointer (fallback “inofensivo”).
    //    Retorno:
    //      - TextView: referencia al primer TextView coincidente o uno
    //        nuevo vacío como fallback.
    //    Nota de mantenimiento:
    //      - Lo ideal es estandarizar IDs en los XML (p. ej., tvNombre y
    //        tvCorreo) y eliminar esta heurística para simplificar.
    // ------------------------------------------------------------------
    static TextView findText(View v, String... candidates){
        for (String name : candidates){
            int id = v.getResources().getIdentifier(name, "id", v.getContext().getPackageName());
            if (id != 0){
                View found = v.findViewById(id);
                if (found instanceof TextView) {
                    return (TextView) found;
                }
            }
        }
        // Fallback para compilar y no crashear si los ids cambian: devuelve un TextView vacío.
        return new TextView(v.getContext());
    }

    // ==================================================================
    // == Clase interna: VH (ViewHolder)
    // == Hereda de: RecyclerView.ViewHolder
    // == Rol:
    //    - Mantiene referencias a las vistas del item para reciclarlas.
    //    - Evita buscar vistas repetidamente (findViewById) en cada bind.
    // ==================================================================
    public static class VH extends RecyclerView.ViewHolder {

        // --------------------------------------------------------------
        // == Atributo: nombre
        //    Tipo: TextView
        //    Rol : Muestra nombre completo y RUN formateado.
        // --------------------------------------------------------------
        TextView nombre;

        // --------------------------------------------------------------
        // == Atributo: correo
        //    Tipo: TextView
        //    Rol : Muestra el email de la persona.
        // --------------------------------------------------------------
        TextView correo;

        // --------------------------------------------------------------
        // == Constructor: VH
        //    Parámetros:
        //      - v (View): vista raíz del layout del item.
        //    Descripción:
        //      - Resuelve y guarda referencias a los TextView usando
        //        la heurística findText() para tolerar distintos IDs.
        //    Retorno:
        //      - (constructor) instancia del ViewHolder lista para bind.
        // --------------------------------------------------------------
        public VH(@NonNull View v){
            super(v);
            // Intentamos múltiples convenciones de nombres para el layout reutilizado.
            nombre = findText(v, "txtNombre", "txtNombre", "tvNombre", "tvNombreCliente", "tvPersonaNombre", "tvNombrePersona", "textNombre", "title");
            correo = findText(v, "txtTelefono", "tvCorreo", "tvCorreoCliente", "tvPersonaCorreo", "tvCorreoPersona", "textCorreo", "subtitle");
        }
    }

    // ------------------------------------------------------------------
    // == Método: onCreateViewHolder
    //    Parámetros:
    //      - parent (ViewGroup): contenedor padre del item.
    //      - viewType (int): tipo de vista (no se usan múltiples tipos aquí).
    //    Descripción:
    //      - Infla el layout R.layout.item_cliente y crea un ViewHolder.
    //    Retorno:
    //      - VH: nuevo ViewHolder asociado al layout inflado.
    // ------------------------------------------------------------------
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cliente, parent, false);
        return new VH(v);
    }

    // ------------------------------------------------------------------
    // == Método: onBindViewHolder
    //    Parámetros:
    //      - h (VH): ViewHolder con referencias a vistas.
    //      - pos (int): posición del elemento en la lista data.
    //    Descripción:
    //      - Toma la Persona en la posición dada, arma el nombre visible
    //        (nombre completo + RUN formateado) y el correo, y los muestra.
    //      - Configura el click del item para invocar el callback onClick.
    //    Retorno:
    //      - void.
    // ------------------------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull VH h, int pos){
        Persona p = data.get(pos);
        if (h.nombre != null) {
            String _rut = formatRun(p.run);
            String _nombre = p.nombreCompleto();
            h.nombre.setText(_rut != null && !_rut.isEmpty() ? _nombre + "  —  " + _rut : _nombre);
        }
        if (h.correo != null) {
            h.correo.setText(p.email);
        }
        h.itemView.setOnClickListener(v -> { if (onClick != null) onClick.onClick(p); });
    }

    // ------------------------------------------------------------------
    // == Método: getItemCount
    //    Parámetros:
    //      - (sin parámetros)
    //    Descripción:
    //      - Devuelve la cantidad de elementos actualmente en data.
    //    Retorno:
    //      - int: tamaño de la lista.
    // ------------------------------------------------------------------
    @Override
    public int getItemCount(){ return data.size(); }

    // ------------------------------------------------------------------
    // == Método: submit
    //    Parámetros:
    //      - nuevos (List<Persona>): nueva colección a mostrar (puede ser null).
    //    Descripción:
    //      - Reemplaza el contenido de data por la lista indicada y
    //        notifica un refresco completo al RecyclerView.
    //    Retorno:
    //      - void.
    //    Nota:
    //      - Para listas grandes conviene usar DiffUtil para animaciones
    //        y actualizaciones más eficientes (evitar notifyDataSetChanged).
    // ------------------------------------------------------------------
    public void submit(List<Persona> nuevos){
        data.clear(); if (nuevos != null) data.addAll(nuevos);
        notifyDataSetChanged();
    }

    // ------------------------------------------------------------------
    // == Método estático: formatRun
    //    Parámetros:
    //      - run (int): RUN numérico sin dígito verificador.
    //    Descripción:
    //      - Inserta puntos cada 3 dígitos desde la derecha para mejorar
    //        la legibilidad (p. ej., 12345678 → "12.345.678").
    //    Retorno:
    //      - String: RUN formateado con puntos.
    //    Nota:
    //      - No agrega DV ni valida formato; si se requiere, hacerlo
    //        en una utilidad dedicada (formateo/validación de RUT/RUN).
    // ------------------------------------------------------------------
    private static String formatRun(int run){
        String s = String.valueOf(run);
        StringBuilder out = new StringBuilder();
        int cnt = 0;
        for (int i = s.length()-1; i >= 0; i--) {
            out.append(s.charAt(i));
            cnt++;
            if (cnt==3 && i>0){ out.append('.'); cnt=0; }
        }
        return out.reverse().toString();
    }
}
