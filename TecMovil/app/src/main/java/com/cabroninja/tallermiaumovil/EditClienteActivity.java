package com.cabroninja.tallermiaumovil;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.cabroninja.tallermiaumovil.data.PersonaRepository;
import com.cabroninja.tallermiaumovil.model.Persona;
import androidx.appcompat.widget.Toolbar;


public class EditClienteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cliente);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Editar cliente");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        EditText etRunBuscar = findViewById(R.id.etRunBuscar);
        EditText etNombre = findViewById(R.id.etNombre);
        EditText etApellido = findViewById(R.id.etApellido);
        EditText etEmail = findViewById(R.id.etEmail);
        Button btnCargar = findViewById(R.id.btnCargar);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        PersonaRepository repo = new PersonaRepository(this);

        btnCargar.setOnClickListener(v -> {
            try {
                int run = Integer.parseInt(etRunBuscar.getText().toString().trim());
                Persona p = repo.getByRun(run);
                if (p == null || !"cliente".equals(p.tipo)) {
                    Toast.makeText(this, "No existe cliente con ese RUN", Toast.LENGTH_SHORT).show();
                    return;
                }
                etNombre.setText(p.nombre);
                etApellido.setText(p.apellido);
                etEmail.setText(p.email);
            } catch (NumberFormatException ex) {
                Toast.makeText(this, "RUN inválido", Toast.LENGTH_SHORT).show();
            }
        });

        btnGuardar.setOnClickListener(v -> {
            try {
                int run = Integer.parseInt(etRunBuscar.getText().toString().trim());
                String nombre = etNombre.getText().toString().trim();
                String apellido = etApellido.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty()) {
                    Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }
                Persona p = new Persona(run, email, nombre, apellido, "123456", "cliente");
                int rows = repo.update(p);
                if (rows > 0) {
                    Toast.makeText(this, "Cliente actualizado", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "No se pudo actualizar", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException ex) {
                Toast.makeText(this, "RUN inválido", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
