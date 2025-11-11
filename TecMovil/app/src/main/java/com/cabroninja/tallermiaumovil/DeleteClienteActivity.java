package com.cabroninja.tallermiaumovil;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.cabroninja.tallermiaumovil.data.PersonaRepository;
import androidx.appcompat.widget.Toolbar;

// ======================================================================
// == Clase: DeleteClienteActivity
// == Hereda de: AppCompatActivity
// == Rol dentro de la app:
//    - Pantalla para eliminar un cliente existente por RUN.
//    - Pide el RUN, valida que sea numérico y solicita al repositorio
//      que borre la fila en la tabla PERSONA (si existe).
// == Colaboradores clave:
//    - PersonaRepository: ejecuta la operación deleteByRun(run) en SQLite.
// == Atributos:
//    - (No hay atributos de instancia; los widgets/repos se manejan
//      como variables locales dentro de onCreate()).
// ======================================================================
public class DeleteClienteActivity extends AppCompatActivity {

    // ------------------------------------------------------------------
    // == Metodo: onCreate
    //    Parámetros:
    //      - savedInstanceState (Bundle): estado previo si el sistema
    //        recrea la Activity (no utilizado en este flujo).
    //    Descripción:
    //      - Infla el layout activity_delete_cliente.
//      - Configura el Toolbar con título y botón de retroceso.
//      - Obtiene referencias a EditText (etRun) y Button (btnEliminar).
//      - Crea un PersonaRepository para realizar el borrado.
//      - En el click de "Eliminar":
//           * Intenta parsear el RUN ingresado (NumberFormatException si falla).
//           * Llama a repo.deleteByRun(run) y muestra un Toast según
//             el número de filas afectadas.
//           * En caso de éxito (>0), cierra la pantalla con finish().
//    Retorno:
//      - void (callback del ciclo de vida).
    // ------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_cliente);

        // ---- Toolbar con back y título ----
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Eliminar cliente");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // ---- Referencias UI ----
        EditText etRun = findViewById(R.id.etRun);
        Button btnEliminar = findViewById(R.id.btnEliminar);

        // ---- Repositorio ----
        PersonaRepository repo = new PersonaRepository(this);

        // ---- Acción: Eliminar cliente por RUN ----
        btnEliminar.setOnClickListener(v -> {
            try {
                // Intento parsear RUN como entero (PK de PERSONA)
                int run = Integer.parseInt(etRun.getText().toString().trim());

                // Borrado en BD; retorna filas afectadas (0 si no existe)
                int rows = repo.deleteByRun(run);

                if (rows > 0) {
                    Toast.makeText(this, "Cliente eliminado", Toast.LENGTH_SHORT).show();
                    finish(); // volver a la pantalla anterior
                } else {
                    Toast.makeText(this, "No existe cliente con ese RUN", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException ex) {
                // RUN inválido: cadena vacía o no numérica
                Toast.makeText(this, "RUN inválido", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
