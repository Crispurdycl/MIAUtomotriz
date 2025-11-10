package com.cabroninja.tallermiaumovil;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cabroninja.tallermiaumovil.data.VehiculoRepository;
import com.cabroninja.tallermiaumovil.model.Vehiculo;
import com.cabroninja.tallermiaumovil.ui.VehiculoAdapter;
import java.util.List;
import androidx.appcompat.widget.Toolbar;


public class VehiculosActivity extends AppCompatActivity {
    private VehiculoRepository repo;
    private VehiculoAdapter adapter;
    private int runCliente = -1;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Vehículos");
        }
        toolbar.setNavigationOnClickListener(v -> finish());


        repo = new VehiculoRepository(this);
        runCliente = getIntent().getIntExtra("run_cliente", -1);

        RecyclerView rv = findViewById(R.id.recyclerClientes);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VehiculoAdapter(
            v -> {
                Intent i = new Intent(this, OrdenesActivity.class);
                i.putExtra("patente", v.patente);
                startActivity(i);
            }
        );
        rv.setAdapter(adapter);
    }

    @Override protected void onResume() {
        super.onResume();
        cargar();
    }

    private void cargar() {
        List<Vehiculo> lista;
        if ((runCliente > 0)) {
            lista = repo.listByRunDueno(runCliente);   // <-- filtra por cliente;
        } else {
            lista = repo.listAll();
        }
        adapter.submit(lista);
    }
}
