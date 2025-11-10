package com.cabroninja.tallermiaumovil;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class OrdenesActivity extends AppCompatActivity {

    private ListView list;
    private TextView tvVehiculo, tvDueno, emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Órdenes de Trabajo");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        list = findViewById(R.id.listView);
        tvVehiculo = findViewById(R.id.tvVehiculo);
        tvDueno = findViewById(R.id.tvDueno);
        emptyView = findViewById(R.id.emptyView);
        list.setEmptyView(emptyView);

        // ¿Venimos con una patente específica?
        String patente = getIntent().getStringExtra("patente");
        boolean mostrarTodas = (patente == null || patente.trim().isEmpty());

        List<DataProvider.OrdenTrabajo> items;

        if (mostrarTodas) {
            // Mostrar TODAS las órdenes
            setTitle("Órdenes de Trabajo");
            tvVehiculo.setText("Vehículo: — (todas las patentes)");
            tvDueno.setText("Dueño: —");
            items = DataProvider.getOrdenesAll();
        } else {
            // Mostrar sólo las de la patente indicada
            DataProvider.Vehiculo v = DataProvider.getVehiculoByPatente(patente);
            DataProvider.Cliente c = DataProvider.getDuenoByPatente(patente);

            if (v != null) {
                setTitle("Órdenes de " + v.getPatente());
                tvVehiculo.setText("Vehículo: " + v.getPatente() + " • " + v.getModelo());
            } else {
                setTitle("Órdenes de " + patente);
                tvVehiculo.setText("Vehículo: " + patente);
            }
            if (c != null) {
                tvDueno.setText("Dueño: " + c.getNombreCompleto() + " (RUN " + c.getRun() + ")");
            } else {
                tvDueno.setText("Dueño: —");
            }

            items = DataProvider.getOrdenesByPatente(patente);
        }

        ArrayAdapter<DataProvider.OrdenTrabajo> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, items
        );
        list.setAdapter(adapter);
    }
}
