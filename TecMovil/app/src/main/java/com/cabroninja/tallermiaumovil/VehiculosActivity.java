package com.cabroninja.tallermiaumovil;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cabroninja.tallermiaumovil.data.VehiculoRepository;
import com.cabroninja.tallermiaumovil.model.Vehiculo;
import com.cabroninja.tallermiaumovil.ui.VehiculoAdapter;

import java.util.List;

import androidx.appcompat.widget.Toolbar;

// ======================================================================
// == Clase: VehiculosActivity
// == Hereda de: AppCompatActivity
// == Rol dentro de la app:
//    - Pantalla que lista vehículos en un RecyclerView.
//    - Si recibe un RUN de cliente vía Intent ("run_cliente"), filtra
//      únicamente los vehículos cuyo dueño coincide con ese RUN.
//    - Al tocar un vehículo, navega a OrdenesActivity pasando su patente.
// == Colaboradores clave:
//    - VehiculoRepository: acceso a SQLite (listar por dueño o todos).
//    - VehiculoAdapter   : puente de datos para el RecyclerView.
//    - OrdenesActivity   : destino al seleccionar un vehículo (muestra OTs).
// ======================================================================
public class VehiculosActivity extends AppCompatActivity {

    // ------------------------------------------------------------------
    // == Atributo: repo
    //    Tipo: VehiculoRepository
    //    Rol : Fuente de datos para obtener listas de Vehiculo desde SQLite.
    // ------------------------------------------------------------------
    private VehiculoRepository repo;

    // ------------------------------------------------------------------
    // == Atributo: adapter
    //    Tipo: VehiculoAdapter
    //    Rol : Adaptador del RecyclerView, renderiza cada Vehiculo y
    //          expone el callback de click para navegar a órdenes.
    // ------------------------------------------------------------------
    private VehiculoAdapter adapter;

    // ------------------------------------------------------------------
    // == Atributo: runCliente
    //    Tipo: int
    //    Rol : Filtro opcional. Valor > 0 indica que se deben listar
    //          solo los vehículos cuyo dueño (run_dueno) sea este RUN.
//    Origen: extra de Intent con clave "run_cliente".
    // ------------------------------------------------------------------
    private int runCliente = -1;

    // ------------------------------------------------------------------
    // == Metodo: onCreate
    //    Parámetros:
    //      - savedInstanceState (Bundle): estado previo si el sistema
    //        recrea la Activity (no utilizado en este flujo).
    //    Descripción:
    //      - Infla el layout activity_vehiculos.
//      - Configura el Toolbar con título y botón de retroceso.
//      - Inicializa el repositorio de vehículos.
//      - Lee el extra "run_cliente" (si no viene, queda en -1 y se listan todos).
//      - Configura el RecyclerView + LayoutManager + Adapter.
//      - Define el callback de tap: abre OrdenesActivity con la patente.
//    Retorno:
//      - void (callback del ciclo de vida).
    // ------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos);

        // ---- Toolbar con back y título ----
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Vehículos");
        }
        // Acción de navegación: cerrar y volver atrás
        toolbar.setNavigationOnClickListener(v -> finish());

        // ---- Repositorio y filtro de cliente (opcional) ----
        repo = new VehiculoRepository(this);
        runCliente = getIntent().getIntExtra("run_cliente", -1);

        // ---- Lista y adaptador ----
        // Nota: el id R.id.recyclerClientes debe existir en activity_vehiculos.
        //       Idealmente estandarizar a R.id.recyclerVehiculos para mayor claridad.
        RecyclerView rv = findViewById(R.id.recyclerClientes);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Callback de tap: abre OrdenesActivity pasando la patente
        adapter = new VehiculoAdapter(
                v -> {
                    Intent i = new Intent(this, OrdenesActivity.class);
                    i.putExtra("patente", v.patente);
                    startActivity(i);
                }
        );
        rv.setAdapter(adapter);
    }

    // ------------------------------------------------------------------
    // == Metodo: onResume
    //    Parámetros:
    //      - (sin parámetros)
    //    Descripción:
    //      - Callback del ciclo de vida al volver a primer plano.
    //        Recarga la lista para reflejar posibles cambios.
    //    Retorno:
    //      - void.
    // ------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        cargar();
    }

    // ------------------------------------------------------------------
    // == Metodo: cargar
    //    Parámetros:
    //      - (sin parámetros)
    //    Descripción:
    //      - Obtiene desde el repositorio:
    //          * Si runCliente > 0: lista filtrada por dueño.
    //          * Si no: lista completa de vehículos.
    //        Luego entrega la colección al adapter mediante submit().
    //    Retorno:
    //      - void.
    // ------------------------------------------------------------------
    private void cargar() {
        List<Vehiculo> lista;
        if ((runCliente > 0)) {
            lista = repo.listByRunDueno(runCliente);   // filtra por cliente
        } else {
            lista = repo.listAll();                    // todos los vehículos
        }
        adapter.submit(lista);
    }
}
