package com.cabroninja.tallermiaumovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class VehiculosActivity extends AppCompatActivity {

    private ListView list;
    private TextView tvContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        list = findViewById(R.id.listView);
        tvContext = findViewById(R.id.tvContext);

        int runCliente = getIntent().getIntExtra("run_cliente", -1);

        List<DataProvider.Vehiculo> items;
        if (runCliente > 0) {
            // Filtrado por cliente
            items = DataProvider.getVehiculosByCliente(runCliente);

            // Título y rótulo contextual
            DataProvider.Cliente c = DataProvider.getClienteByRun(runCliente);
            String nombre;
            if ((c != null)) {
                nombre = c.getNombreCompleto();
            } else {
                nombre = ("RUN " + runCliente);
            }
            setTitle("Vehículos de " + nombre);
            tvContext.setText("Viendo vehículos de " + nombre + " (RUN " + runCliente + ")");
            tvContext.setVisibility(View.VISIBLE);
        } else {
            // Todos los vehículos
            items = DataProvider.getVehiculosAll();
            setTitle("Vehículos");
            tvContext.setVisibility(View.GONE);
        }

        ArrayAdapter<DataProvider.Vehiculo> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, items
        );
        list.setAdapter(adapter);

        list.setOnItemClickListener((parent, view, position, id) -> {
            DataProvider.Vehiculo v = adapter.getItem(position);
            if (v != null) {
                Intent i = new Intent(this, OrdenesActivity.class);
                i.putExtra("patente", v.getPatente());
                startActivity(i);
            }
        });
    }
}
