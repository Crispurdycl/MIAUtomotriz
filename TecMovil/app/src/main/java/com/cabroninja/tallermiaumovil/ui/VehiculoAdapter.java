package com.cabroninja.tallermiaumovil.ui;

import android.view.*; import android.widget.TextView;
import androidx.annotation.NonNull; import androidx.recyclerview.widget.RecyclerView;
import com.cabroninja.tallermiaumovil.R;
import com.cabroninja.tallermiaumovil.model.Vehiculo;
import java.util.ArrayList; import java.util.List;

public class VehiculoAdapter extends RecyclerView.Adapter<VehiculoAdapter.VH> {
    public interface OnClick { void onClick(Vehiculo v); }
    public interface OnLong { void onLong(Vehiculo v); }

    private final List<Vehiculo> data = new ArrayList<>();
    private final OnClick onClick;

    public VehiculoAdapter(OnClick onClick){ this.onClick = onClick;}

    static class VH extends RecyclerView.ViewHolder {
        TextView linea;
        VH(@NonNull View itemView) {
            super(itemView);
            linea = itemView.findViewById(R.id.txtLinea);
        }
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int vt) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehiculo, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        Vehiculo v = data.get(pos);
        h.linea.setText(v.toString());
        h.itemView.setOnClickListener(w -> { if (onClick != null) onClick.onClick(v); });
    }

    @Override public int getItemCount(){ return data.size(); }

    public void submit(List<Vehiculo> nuevos){
        data.clear(); if (nuevos != null) data.addAll(nuevos);
        notifyDataSetChanged();
    }
}
