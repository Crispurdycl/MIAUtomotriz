package com.cabroninja.tallermiaumovil;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cabroninja.tallermiaumovil.data.PersonaRepository;
import com.cabroninja.tallermiaumovil.model.Persona;

// ======================================================================
// == Clase: AddClienteActivity
// == Hereda de: AppCompatActivity
// == Rol dentro de la app:
//    - Pantalla para crear un nuevo cliente en la base de datos.
//    - Recoge RUN, nombre, apellido y email desde la UI, valida, y
//      persiste usando PersonaRepository con tipo="cliente".
// == Colaboradores clave:
//    - PersonaRepository: inserta el modelo Persona en SQLite.
//    - TallerDbHelper   : define el esquema y restricciones (email único,
//                         RUN PK, tipo con CHECK).
// == Atributos de clase:
//    - No hay atributos de instancia; todos los widgets/repos se manejan
//      como variables locales dentro de onCreate().
// ======================================================================
public class AddClienteActivity extends AppCompatActivity {

    // ------------------------------------------------------------------
    // == Método: onCreate
    //    Parámetros:
    //      - savedInstanceState (Bundle): estado previo si el sistema
    //        recrea la Activity (no usado en este flujo).
    //    Descripción:
    //      - Infla el layout activity_add_cliente.
    //      - Configura un Toolbar con título y botón de retroceso.
    //      - Obtiene referencias a los EditText y al botón Guardar.
    //      - Crea un PersonaRepository para acceder a la DB.
    //      - En el click de Guardar:
    //           * Valida que RUN sea entero (NumberFormatException ⇒ error).
    //           * Valida que nombre, apellido y email no estén vacíos.
    //           * Construye Persona con password por defecto "123456"
    //             y tipo "cliente" (acorde al dominio).
    //           * Intenta insertar: si id != -1 ⇒ éxito, cierra pantalla;
    //             si id == -1 ⇒ puede haber colisión por PK/UNIQUE.
    //    Retorno:
    //      - void (callback del ciclo de vida).
    // ------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cliente);

        // ---- Toolbar con back y título ----
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Agregar cliente");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // ---- Referencias a inputs de la UI ----
        EditText etRun = findViewById(R.id.etRun);
        EditText etNombre = findViewById(R.id.etNombre);
        EditText etApellido = findViewById(R.id.etApellido);
        EditText etEmail = findViewById(R.id.etEmail);

        Button btnGuardar = findViewById(R.id.btnGuardar);

        // ---- Repositorio para persistencia ----
        PersonaRepository repo = new PersonaRepository(this);

        // ---- Acción: Guardar cliente ----
        btnGuardar.setOnClickListener(v -> {
            try {
                // Intento parsear RUN como entero. Si falla ⇒ NumberFormatException
                int run = Integer.parseInt(etRun.getText().toString().trim());

                // Lectura y saneo básico de cadenas
                String nombre = etNombre.getText().toString().trim();
                String apellido = etApellido.getText().toString().trim();
                String email = etEmail.getText().toString().trim();

                // Validación mínima de requeridos
                if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty()) {
                    Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Construcción del modelo:
                // - password de demo "123456"
                // - tipo = "cliente" (consistente con los contadores y listados)
                Persona p = new Persona(run, email, nombre, apellido, "123456", "cliente");

                // Inserción. insert() retorna rowId o -1 si falla
                long id = repo.insert(p);

                if (id != -1) {
                    Toast.makeText(this, "Cliente agregado", Toast.LENGTH_SHORT).show();
                    finish(); // volver a pantalla anterior
                } else {
                    // Posibles causas: RUN duplicado (PK) o email duplicado (UNIQUE)
                    Toast.makeText(this, "RUN o email ya existe", Toast.LENGTH_LONG).show();
                }
            } catch (NumberFormatException ex) {
                // RUN inválido: no es entero o cadena vacía
                Toast.makeText(this, "RUN inválido", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
