package com.cabroninja.tallermiaumovil;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cabroninja.tallermiaumovil.data.OrdenRepository;
import com.cabroninja.tallermiaumovil.model.OrdenTrabajo;
import com.cabroninja.tallermiaumovil.ui.OrdenAdapter;
import java.util.List;

import androidx.appcompat.widget.Toolbar;

public class OrdenesActivity extends AppCompatActivity {
    private OrdenRepository repo;
    private OrdenAdapter adapter;
    private String patente = null;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Órdenes");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        repo = new OrdenRepository(this);
        patente = getIntent().getStringExtra("patente");

        RecyclerView rv = findViewById(R.id.recyclerClientes);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrdenAdapter(
            o -> { repo.delete(o.id); cargar(); }
        );
        rv.setAdapter(adapter);
    }

    @Override protected void onResume() {
        super.onResume();
        cargar();
    }

    private void cargar() {
        List<OrdenTrabajo> lista;
        if ((patente != null)) {
            lista = repo.listByPatente(patente);
        } else {
            lista = repo.listAll();
        }
        adapter.submit(lista);
    }
}
