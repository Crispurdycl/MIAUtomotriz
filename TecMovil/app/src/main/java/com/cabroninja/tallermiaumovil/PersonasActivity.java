package com.cabroninja.tallermiaumovil;

import android.os.Bundle;
import android.widget.Toast;
import android.content.Intent; // <-- agregado
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cabroninja.tallermiaumovil.data.PersonaRepository;
import com.cabroninja.tallermiaumovil.model.Persona;
import com.cabroninja.tallermiaumovil.ui.PersonaAdapter;

import java.util.List;

import androidx.appcompat.widget.Toolbar;

public class PersonasActivity extends AppCompatActivity {

    private RecyclerView rv;
    private PersonaAdapter adapter;
    private PersonaRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Clientes");
        }
        toolbar.setNavigationOnClickListener(v -> finish()); // volver



        repo = new PersonaRepository(this);
        rv = findViewById(R.id.recyclerClientes);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PersonaAdapter(
                p -> {
                    Intent i = new Intent(this, VehiculosActivity.class);
                    i.putExtra("run_cliente", p.run);   // <-- pasa el RUN
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
        List<Persona> lista = repo.listByTipo("cliente");
        adapter.submit(lista);
    }
}
