package com.cabroninja.tallermiaumovil.ui;

import android.view.*; import android.widget.TextView;
import androidx.annotation.NonNull; import androidx.recyclerview.widget.RecyclerView;
import com.cabroninja.tallermiaumovil.R;
import com.cabroninja.tallermiaumovil.model.OrdenTrabajo;
import java.util.ArrayList; import java.util.List;
import java.util.Locale;

public class OrdenAdapter extends RecyclerView.Adapter<OrdenAdapter.VH> {
    public interface OnLong { void onLong(OrdenTrabajo o); }
    private final List<OrdenTrabajo> data = new ArrayList<>();
    private final OnLong onLong;

    public OrdenAdapter(OnLong onLong){ this.onLong = onLong; }

    static class VH extends RecyclerView.ViewHolder {
        TextView linea;
        VH(@NonNull View itemView) {
            super(itemView);
            linea = itemView.findViewById(R.id.txtLinea);
        }
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int vt) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orden, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        OrdenTrabajo o = data.get(pos);
        String s = "OT " + o.numero + " — " + o.fecha + " — Total: $" + String.format(Locale.US, "%,.0f", o.total());
        h.linea.setText(s);
        h.itemView.setOnLongClickListener(w -> { if (onLong != null) onLong.onLong(o); return true; });
    }

    @Override public int getItemCount(){ return data.size(); }

    public void submit(List<OrdenTrabajo> nuevos){
        data.clear(); if (nuevos != null) data.addAll(nuevos);
        notifyDataSetChanged();
    }
}
