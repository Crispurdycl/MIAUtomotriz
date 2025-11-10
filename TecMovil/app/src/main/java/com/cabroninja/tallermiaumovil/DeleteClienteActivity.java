package com.cabroninja.tallermiaumovil;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.cabroninja.tallermiaumovil.data.PersonaRepository;
import androidx.appcompat.widget.Toolbar;

public class DeleteClienteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_cliente);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Eliminar cliente");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        EditText etRun = findViewById(R.id.etRun);
        Button btnEliminar = findViewById(R.id.btnEliminar);

        PersonaRepository repo = new PersonaRepository(this);

        btnEliminar.setOnClickListener(v -> {
            try {
                int run = Integer.parseInt(etRun.getText().toString().trim());
                int rows = repo.deleteByRun(run);
                if (rows > 0) {
                    Toast.makeText(this, "Cliente eliminado", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "No existe cliente con ese RUN", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException ex) {
                Toast.makeText(this, "RUN inválido", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
