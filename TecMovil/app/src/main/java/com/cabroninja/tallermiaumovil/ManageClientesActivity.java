package com.cabroninja.tallermiaumovil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

// ======================================================================
// == Clase: ManageClientesActivity
// == Hereda de: AppCompatActivity
// == Rol dentro de la app:
//    - Pantalla de “hub” para la administración de clientes.
//    - Presenta 3 acciones: Agregar, Editar y Eliminar, cada una
//      redirigiendo a su Activity correspondiente.
// == Colaboradores clave:
//    - AddClienteActivity   : pantalla para crear un nuevo cliente.
//    - EditClienteActivity  : pantalla para modificar un cliente existente.
//    - DeleteClienteActivity: pantalla para eliminar un cliente.
// == Atributos de clase:
//    - (No posee atributos de instancia; todos los widgets se usan como
//      variables locales en onCreate).
// ======================================================================
public class ManageClientesActivity extends AppCompatActivity {

    // ------------------------------------------------------------------
    // == Método: onCreate
    //    Parámetros:
    //      - savedInstanceState (Bundle): estado previamente guardado
    //        de la Activity (si el sistema la recreó tras destrucción).
    //    Descripción:
    //      - Infla el layout activity_manage_clientes.
    //      - Configura el Toolbar con título y botón de retroceso.
    //      - Conecta los 3 botones de acción con sus Activities destino
    //        mediante Intents explícitos.
    //    Retorno:
    //      - void (callback del ciclo de vida).
    // ------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_clientes);

        // ---- Toolbar con back y título ----
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gestión de clientes");
        }
        // Acción de navegación: finalizar Activity y volver atrás
        toolbar.setNavigationOnClickListener(v -> finish());

        // ---- Botones de acciones CRUD sobre clientes ----
        Button btnAdd = findViewById(R.id.btnAddCliente);
        Button btnEdit = findViewById(R.id.btnEditCliente);
        Button btnDelete = findViewById(R.id.btnDeleteCliente);

        // Navega a "Agregar cliente"
        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddClienteActivity.class)));

        // Navega a "Editar cliente"
        btnEdit.setOnClickListener(v ->
                startActivity(new Intent(this, EditClienteActivity.class)));

        // Navega a "Eliminar cliente"
        btnDelete.setOnClickListener(v ->
                startActivity(new Intent(this, DeleteClienteActivity.class)));
    }
}
