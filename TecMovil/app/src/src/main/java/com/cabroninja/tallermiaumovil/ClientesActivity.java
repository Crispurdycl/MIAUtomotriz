package com.cabroninja.tallermiaumovil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ClientesActivity extends AppCompatActivity {

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Clientes");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        list = findViewById(R.id.listView);

        // Mostramos objetos Cliente; ArrayAdapter usará toString()
        ArrayAdapter<DataProvider.Cliente> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, DataProvider.getClientes()
        );
        list.setAdapter(adapter);

        list.setOnItemClickListener((parent, view, position, id) -> {
            DataProvider.Cliente c = adapter.getItem(position);
            if (c != null) {
                Intent i = new Intent(this, VehiculosActivity.class);
                i.putExtra("run_cliente", c.getRun());
                startActivity(i);
            }
        });
    }
}
