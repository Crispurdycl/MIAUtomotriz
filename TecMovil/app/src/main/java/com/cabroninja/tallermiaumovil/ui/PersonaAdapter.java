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

public class PersonaAdapter extends RecyclerView.Adapter<PersonaAdapter.VH> {
    public interface OnClick { void onClick(Persona p); }

    private final List<Persona> data = new ArrayList<>();
    private final OnClick onClick;

    public PersonaAdapter(OnClick onClick){
        this.onClick = onClick;
    }

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

    public static class VH extends RecyclerView.ViewHolder {
        TextView nombre, correo;
        public VH(@NonNull View v){
            super(v);
            // Intentamos múltiples convenciones de nombres para el layout reutilizado.
            nombre = findText(v, "txtNombre", "txtNombre", "tvNombre", "tvNombreCliente", "tvPersonaNombre", "tvNombrePersona", "textNombre", "title");
            correo = findText(v, "txtTelefono", "tvCorreo", "tvCorreoCliente", "tvPersonaCorreo", "tvCorreoPersona", "textCorreo", "subtitle");
        }
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cliente, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos){
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

    @Override public int getItemCount(){ return data.size(); }

    public void submit(List<Persona> nuevos){
        data.clear(); if (nuevos != null) data.addAll(nuevos);
        notifyDataSetChanged();
    }

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
