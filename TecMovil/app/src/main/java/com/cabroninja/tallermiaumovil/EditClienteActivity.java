package com.cabroninja.tallermiaumovil;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.cabroninja.tallermiaumovil.data.PersonaRepository;
import com.cabroninja.tallermiaumovil.model.Persona;
import androidx.appcompat.widget.Toolbar;

// ======================================================================
// == Clase: EditClienteActivity
// == Hereda de: AppCompatActivity
// == Rol dentro de la app:
//    - Pantalla para buscar y editar los datos de un cliente existente.
//    - Flujo en dos pasos:
//        1) Cargar por RUN (btnCargar) para traer los datos actuales.
//        2) Editar campos y guardar cambios (btnGuardar) en la BD.
// == Colaboradores clave:
//    - PersonaRepository: lecturas/actualizaciones de PERSONA en SQLite.
//    - TallerDbHelper  : esquema y restricciones (PK run, UNIQUE email,
//                        CHECK tipo en {'cliente','trabajador'}).
// == Atributos de instancia:
//    - (Se usan referencias locales a los widgets; no se mantienen como
//      campos de clase para simplificar el ciclo de vida).
// ======================================================================
public class EditClienteActivity extends AppCompatActivity {

    // ------------------------------------------------------------------
    // == Método: onCreate
    //    Parámetros:
    //      - savedInstanceState (Bundle): estado previo si el sistema
    //        recrea la Activity tras ser destruida.
    //    Descripción:
    //      - Infla activity_edit_cliente.
    //      - Configura Toolbar con título y flecha de retroceso.
    //      - Obtiene referencias a EditText y Buttons.
    //      - Instancia PersonaRepository para acceder a la base de datos.
    //      - Define dos acciones:
    //          * btnCargar: busca Persona por RUN y precarga los campos
    //            (solo si existe y su tipo == "cliente").
    //          * btnGuardar: valida entradas y persiste con repo.update().
    //    Retorno:
    //      - void (callback del ciclo de vida).
    // ------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cliente);

        // ---- Toolbar con back y título ----
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Editar cliente");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // ---- Referencias UI ----
        EditText etRunBuscar = findViewById(R.id.etRunBuscar);
        EditText etNombre    = findViewById(R.id.etNombre);
        EditText etApellido  = findViewById(R.id.etApellido);
        EditText etEmail     = findViewById(R.id.etEmail);
        Button btnCargar     = findViewById(R.id.btnCargar);
        Button btnGuardar    = findViewById(R.id.btnGuardar);

        // ---- Repositorio ----
        PersonaRepository repo = new PersonaRepository(this);

        // ------------------------------------------------------------------
        // == Acción: btnCargar (buscar por RUN y precargar campos)
        //    Parámetros UI involucrados:
        //      - etRunBuscar: RUN a buscar (String que debe convertirse a int).
        //    Descripción:
        //      - Convierte RUN a int; si no es válido muestra error.
        //      - Llama repo.getByRun(run) y verifica que sea "cliente".
        //      - Si existe, coloca nombre/apellido/email en los EditText.
        //      - Si no existe o no es cliente, avisa con un Toast.
        //    Retorno:
        //      - (no aplica; acción de UI con efectos en pantalla).
        // ------------------------------------------------------------------
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

        // ------------------------------------------------------------------
        // == Acción: btnGuardar (persistir cambios del cliente)
        //    Parámetros UI involucrados:
        //      - etRunBuscar: RUN del registro a actualizar (int).
        //      - etNombre, etApellido, etEmail: nuevos valores (String).
        //    Descripción:
        //      - Valida que RUN sea entero y que los campos no estén vacíos.
        //      - Construye un objeto Persona con:
        //          * run: el que se buscó
        //          * email/nombre/apellido: desde los EditText
        //          * password: "123456" (placeholder de demo; ver nota)
        //          * tipo: "cliente" (se mantiene la categoría)
        //      - Llama repo.update(p) y muestra resultado por Toast.
        //      - Si filas afectadas > 0, cierra la Activity con finish().
        //    Retorno:
        //      - (no aplica; acción de UI con efectos en la BD y en la pantalla).
        // ------------------------------------------------------------------
        btnGuardar.setOnClickListener(v -> {
            try {
                int run = Integer.parseInt(etRunBuscar.getText().toString().trim());
                String nombre  = etNombre.getText().toString().trim();
                String apellido= etApellido.getText().toString().trim();
                String email   = etEmail.getText().toString().trim();

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
