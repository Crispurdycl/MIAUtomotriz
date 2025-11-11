package com.cabroninja.tallermiaumovil;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cabroninja.tallermiaumovil.data.OrdenRepository;
import com.cabroninja.tallermiaumovil.model.OrdenTrabajo;
import com.cabroninja.tallermiaumovil.ui.OrdenAdapter;

import java.util.List;

import androidx.appcompat.widget.Toolbar;

// ======================================================================
// == Clase: OrdenesActivity
// == Hereda de: AppCompatActivity
// == Rol dentro de la app:
//    - Pantalla que lista órdenes de trabajo (OT) en un RecyclerView.
//    - Si recibe una patente por Intent (clave "patente"), filtra y
//      muestra únicamente las OTs asociadas a ese vehículo.
//    - Si no recibe patente, muestra todas las OTs registradas.
// == Colaboradores clave:
//    - OrdenRepository: fuente de datos (SQLite) para leer OTs.
//    - OrdenAdapter   : adaptador que renderiza cada OT en la lista.
// ======================================================================
public class OrdenesActivity extends AppCompatActivity {

    // ------------------------------------------------------------------
    // == Atributo: repo
    //    Tipo: OrdenRepository
    //    Rol : Acceso a SQLite para obtener listas de OrdenTrabajo.
    // ------------------------------------------------------------------
    private OrdenRepository repo;

    // ------------------------------------------------------------------
    // == Atributo: adapter
    //    Tipo: OrdenAdapter
    //    Rol : Puente entre List<OrdenTrabajo> y el RecyclerView.
    // ------------------------------------------------------------------
    private OrdenAdapter adapter;

    // ------------------------------------------------------------------
    // == Atributo: patente
    //    Tipo: String
    //    Rol : Filtro opcional. Si no es null, se listan solo OTs de
    //          ese vehículo (coincidencia exacta por patente).
    //    Origen: extra del Intent con clave "patente".
    // ------------------------------------------------------------------
    private String patente = null;

    // ------------------------------------------------------------------
    // == Método: onCreate
    //    Parámetros:
    //      - savedInstanceState (Bundle): estado previo si el sistema
    //        recrea la Activity (no usado explícitamente aquí).
    //    Descripción:
    //      - Infla activity_ordenes.
    //      - Configura Toolbar con título y botón de retroceso.
    //      - Inicializa repositorio y lee el filtro "patente" del Intent.
    //      - Configura RecyclerView con LinearLayoutManager y OrdenAdapter.
    //    Retorno:
    //      - void (callback del ciclo de vida).
    // ------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Órdenes");
        }
        // Flecha de back: cerrar Activity y volver
        toolbar.setNavigationOnClickListener(v -> finish());

        // Repositorio y filtro de patente (opcional)
        repo = new OrdenRepository(this);
        patente = getIntent().getStringExtra("patente");

        // Lista y adaptador
        RecyclerView rv = findViewById(R.id.recyclerClientes);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new OrdenAdapter();
        rv.setAdapter(adapter);
    }

    // ------------------------------------------------------------------
    // == Método: onResume
    //    Parámetros:
    //      - (sin parámetros)
    //    Descripción:
    //      - Al volver a primer plano, recarga la lista para reflejar
    //        posibles cambios recientes en la base de datos.
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
    //      - Si "patente" no es null, obtiene las OTs asociadas a esa
    //        patente desde OrdenRepository.listByPatente(patente).
    //      - En caso contrario, obtiene todas las OTs con listAll().
    //      - Entrega la colección al adapter mediante submit() para
    //        refrescar el RecyclerView.
    //    Retorno:
    //      - void.
    // ------------------------------------------------------------------
    private void cargar() {
        List<OrdenTrabajo> lista;
        if (patente != null) {
            lista = repo.listByPatente(patente);
        } else {
            lista = repo.listAll();
        }
        adapter.submit(lista);
    }
}
