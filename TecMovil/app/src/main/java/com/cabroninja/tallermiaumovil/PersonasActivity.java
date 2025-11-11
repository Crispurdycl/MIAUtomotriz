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

// ======================================================================
// == Clase: PersonasActivity
// == Hereda de: AppCompatActivity
// == Rol dentro de la app:
//    - Pantalla que lista a las personas de tipo "cliente" usando un
//      RecyclerView y un PersonaAdapter.
//    - Permite navegar a VehiculosActivity al tocar un cliente, pasando
//      su RUN en el Intent (clave "run_cliente") para filtrar vehículos.
// == Colaboradores clave:
//    - PersonaRepository: acceso a SQLite (lee clientes).
//    - PersonaAdapter   : adapta List<Persona> al RecyclerView.
//    - VehiculosActivity: destino al seleccionar un cliente.
// ======================================================================
public class PersonasActivity extends AppCompatActivity {

    // ------------------------------------------------------------------
    // == Atributo: rv
    //    Tipo: RecyclerView
    //    Rol : Contenedor de lista eficiente para renderizar los clientes.
    // ------------------------------------------------------------------
    private RecyclerView rv;

    // ------------------------------------------------------------------
    // == Atributo: adapter
    //    Tipo: PersonaAdapter
    //    Rol : Puente entre los datos (List<Persona>) y las celdas del RV.
    // ------------------------------------------------------------------
    private PersonaAdapter adapter;

    // ------------------------------------------------------------------
    // == Atributo: repo
    //    Tipo: PersonaRepository
    //    Rol : Fuente de datos: provee listByTipo("cliente") desde SQLite.
    // ------------------------------------------------------------------
    private PersonaRepository repo;

    // ------------------------------------------------------------------
    // == Método: onCreate
    //    Parámetros:
    //      - savedInstanceState (Bundle): estado previo (restauración).
    //    Descripción:
    //      - Infla el layout activity_clientes.
    //      - Configura el Toolbar con flecha de retroceso y título.
    //      - Inicializa el RecyclerView + LayoutManager + Adapter.
    //      - Define el onClick del adapter para abrir VehiculosActivity,
    //        pasando el RUN del cliente seleccionado en el Intent con
    //        la clave "run_cliente".
    //    Retorno:
    //      - void (callback del ciclo de vida).
    // ------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        // ---- Toolbar con back y título ----
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Clientes");
        }
        // Acción de la flecha: cerrar Activity actual y volver atrás
        toolbar.setNavigationOnClickListener(v -> finish());

        // ---- Repositorio y lista ----
        repo = new PersonaRepository(this);

        rv = findViewById(R.id.recyclerClientes);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Adapter con callback al tocar un cliente:
        //   - Crea Intent hacia VehiculosActivity
        //   - Inserta "run_cliente" = p.run como extra para filtrar vehículos
        adapter = new PersonaAdapter(
                p -> {
                    Intent i = new Intent(this, VehiculosActivity.class);
                    i.putExtra("run_cliente", p.run);   // RUN del cliente seleccionado
                    startActivity(i);
                }
        );
        rv.setAdapter(adapter);
    }

    // ------------------------------------------------------------------
    // == Método: onResume
    //    Parámetros:
    //      - (sin parámetros)
    //    Descripción:
    //      - Callback del ciclo de vida que se dispara al volver a primer
    //        plano. Recarga la lista para reflejar cambios recientes.
    //    Retorno:
    //      - void.
    // ------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        cargar();
    }

    // ------------------------------------------------------------------
    // == Método: cargar
    //    Parámetros:
    //      - (sin parámetros)
    //    Descripción:
    //      - Obtiene desde PersonaRepository a todas las personas cuyo
    //        tipo = "cliente" y las entrega al adapter mediante submit().
    //    Retorno:
    //      - void.
    // ------------------------------------------------------------------
    private void cargar() {
        List<Persona> lista = repo.listByTipo("cliente");
        adapter.submit(lista);
    }
}
