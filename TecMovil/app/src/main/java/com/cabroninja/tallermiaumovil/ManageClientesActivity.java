package com.cabroninja.tallermiaumovil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class ManageClientesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_clientes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gestión de clientes");
        }
        toolbar.setNavigationOnClickListener(v -> finish());


        Button btnAdd = findViewById(R.id.btnAddCliente);
        Button btnEdit = findViewById(R.id.btnEditCliente);
        Button btnDelete = findViewById(R.id.btnDeleteCliente);

        btnAdd.setOnClickListener(v -> startActivity(new Intent(this, AddClienteActivity.class)));
        btnEdit.setOnClickListener(v -> startActivity(new Intent(this, EditClienteActivity.class)));
        btnDelete.setOnClickListener(v -> startActivity(new Intent(this, DeleteClienteActivity.class)));
    }
}
