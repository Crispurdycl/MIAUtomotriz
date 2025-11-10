package com.cabroninja.tallermiaumovil;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.cabroninja.tallermiaumovil.data.PersonaRepository;
import com.cabroninja.tallermiaumovil.model.Persona;
import androidx.appcompat.widget.Toolbar; // ⬅️ agrega este import


public class AddClienteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cliente);

        // Toolbar + atrás
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Agregar cliente");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        EditText etRun = findViewById(R.id.etRun);
        EditText etNombre = findViewById(R.id.etNombre);
        EditText etApellido = findViewById(R.id.etApellido);
        EditText etEmail = findViewById(R.id.etEmail);

        Button btnGuardar = findViewById(R.id.btnGuardar);

        PersonaRepository repo = new PersonaRepository(this);

        btnGuardar.setOnClickListener(v -> {
            try {
                int run = Integer.parseInt(etRun.getText().toString().trim());
                String nombre = etNombre.getText().toString().trim();
                String apellido = etApellido.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty()) {
                    Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                Persona p = new Persona(run, email, nombre, apellido, "123456", "cliente");
                long id = repo.insert(p);
                if (id != -1) {
                    Toast.makeText(this, "Cliente agregado", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "RUN o email ya existe", Toast.LENGTH_LONG).show();
                }
            } catch (NumberFormatException ex) {
                Toast.makeText(this, "RUN inválido", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
