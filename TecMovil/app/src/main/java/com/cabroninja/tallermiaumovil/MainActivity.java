package com.cabroninja.tallermiaumovil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import com.cabroninja.tallermiaumovil.data.PersonaRepository;
import com.cabroninja.tallermiaumovil.data.VehiculoRepository;
import com.cabroninja.tallermiaumovil.data.OrdenRepository;

// ======================================================================
// == Clase: MainActivity
// == Tipo: class — hereda de AppCompatActivity
// == Implementa: NavigationView.OnNavigationItemSelectedListener
// == Rol:
//    Pantalla principal (dashboard) de la app. Muestra:
//    - Saludo con el nombre/correo del usuario logueado.
//    - Contadores (Clientes, Vehículos, Órdenes) desde SQLite.
//    - Botones de navegación rápida y menú lateral (drawer).
//    - Gestiona la navegación a PersonasActivity, VehiculosActivity,
//      OrdenesActivity y ManageClientesActivity.
// ======================================================================
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // ------------------------------------------------------------------
    // == Atributo: PREFS
    //    Clave del archivo de preferencias compartidas (SharedPreferences)
    //    donde se guarda el estado de autenticación.
    // ------------------------------------------------------------------
    private static final String PREFS = "auth_prefs";

    // ------------------------------------------------------------------
    // == Atributo: KEY_EMAIL
    //    Clave usada en SharedPreferences para persistir el email del
    //    usuario autenticado.
    // ------------------------------------------------------------------
    private static final String KEY_EMAIL = "email";

    // ------------------------------------------------------------------
    // == Atributo: KEY_NAME
    //    Clave usada en SharedPreferences para persistir el nombre
    //    visible del usuario autenticado.
    // ------------------------------------------------------------------
    private static final String KEY_NAME  = "name";

    // ------------------------------------------------------------------
    // == Atributo: drawer
    //    Contenedor principal del menú lateral (navigation drawer).
    //    Permite abrir/cerrar el panel con opciones.
    // ------------------------------------------------------------------
    private DrawerLayout drawer;

    // ------------------------------------------------------------------
    // == Atributo: nav
    //    Vista del menú lateral que contiene las opciones de navegación
    //    y un header con datos del usuario.
    // ------------------------------------------------------------------
    private NavigationView nav;

    // ------------------------------------------------------------------
    // == Atributo: tvGreeting
    //    Etiqueta para mostrar el saludo ("Hola <nombre/email>").
    // ------------------------------------------------------------------
    private TextView tvGreeting;

    // ------------------------------------------------------------------
    // == Atributo: tvCountClientes
    //    Etiqueta para mostrar el total de clientes (excluye trabajadores).
    // ------------------------------------------------------------------
    private TextView tvCountClientes;

    // ------------------------------------------------------------------
    // == Atributo: tvCountVehiculos
    //    Etiqueta para mostrar el total de vehículos registrados.
    // ------------------------------------------------------------------
    private TextView tvCountVehiculos;

    // ------------------------------------------------------------------
    // == Atributo: tvCountOrdenes
    //    Etiqueta para mostrar el total de órdenes de trabajo (OT).
    // ------------------------------------------------------------------
    private TextView tvCountOrdenes;

    // ------------------------------------------------------------------
    // == Atributo: btnClientes
    //    Botón para abrir la pantalla de personas filtradas como clientes.
    // ------------------------------------------------------------------
    private Button btnClientes;

    // ------------------------------------------------------------------
    // == Atributo: btnVehiculos
    //    Botón para abrir la lista de vehículos.
    // ------------------------------------------------------------------
    private Button btnVehiculos;

    // ------------------------------------------------------------------
    // == Atributo: btnOrdenes
    //    Botón para abrir la lista de órdenes de trabajo (OT).
    // ------------------------------------------------------------------
    private Button btnOrdenes;

    // ------------------------------------------------------------------
    // == Atributo: btnGestionar
    //    Botón para abrir el submenú de gestión de clientes (agregar,
    //    editar, eliminar) en ManageClientesActivity.
    // ------------------------------------------------------------------
    private Button btnGestionar;

    // ------------------------------------------------------------------
    // == Atributo: personaRepo
    //    Repositorio de acceso a datos de personas (tabla Persona).
    //    Provee métodos como countByTipo("cliente") para contadores y
    //    listados filtrados.
    // ------------------------------------------------------------------
    private PersonaRepository personaRepo;

    // ------------------------------------------------------------------
    // == Atributo: vehiculoRepo
    //    Repositorio de acceso a datos de vehículos. Centraliza queries
    //    a SQLite para separar UI de datos.
    // ------------------------------------------------------------------
    private VehiculoRepository vehiculoRepo;

    // ------------------------------------------------------------------
    // == Atributo: ordenRepo
    //    Repositorio de acceso a datos de órdenes de trabajo (OT).
    //    Permite contar y listar órdenes de forma consistente.
    // ------------------------------------------------------------------
    private OrdenRepository ordenRepo;

    // ------------------------------------------------------------------
    // == Método: onCreate
    //    Parámetros:
    //      - savedInstanceState (Bundle): estado anterior de la Activity
    //        si el sistema la recreó (suele venir null en un arranque
    //        normal). Útil para restaurar UI/estado.
    //
    //    Descripción:
    //      1) Verifica sesión en SharedPreferences; si no hay email,
    //         redirige a LoginActivity y cierra esta Activity.
    //      2) Infla el layout principal (activity_main) y configura la
    //         Toolbar como ActionBar con título "Panel principal".
    //      3) Configura DrawerLayout + ActionBarDrawerToggle (botón
    //         "hamburguesa") y registra el listener del NavigationView.
    //      4) Carga el header del NavigationView y muestra nombre/email
    //         del usuario autenticado.
    //      5) Inicializa vistas del dashboard (TextViews y Buttons).
    //      6) Instancia repositorios (SQLite) y refresca contadores.
    //      7) Configura los botones para navegar a las pantallas clave.
    //
    //    Retorna:
    //      - void: es un callback del ciclo de vida, no retorna valor.
    // ------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verificación de sesión: si no hay email guardado, ir a Login.
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        if (!sp.contains(KEY_EMAIL)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // Configuración de la Toolbar como ActionBar con título.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Panel principal");

        // Drawer y NavigationView (menú lateral).
        drawer = findViewById(R.id.drawer_layout);
        nav = findViewById(R.id.nav_view);

        // Toggle (icono hamburguesa) sincroniza el estado del drawer con la Toolbar.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.nav_open,
                R.string.nav_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Manejo de clics en el menú lateral (esta Activity es el listener).
        nav.setNavigationItemSelectedListener(this);

        // ------- Header con datos del usuario (nombre y email) -------
        String displayName = sp.getString(KEY_NAME, "");
        String email = sp.getString(KEY_EMAIL, "");

        View headerView = nav.getHeaderView(0);
        if (headerView != null) {
            TextView tvName  = headerView.findViewById(R.id.tvUserName);
            TextView tvEmail = headerView.findViewById(R.id.tvUserEmail);

            if (tvName != null) {
                // Si hay nombre, úsalo; si no, muestra el email.
                tvName.setText(displayName != null && !displayName.isEmpty() ? displayName : email);
            }
            if (tvEmail != null) {
                // Evita null en la UI; muestra vacío si no hay email.
                tvEmail.setText(email != null ? email : "");
            }
        }

        // ------- Dashboard: saludo, contadores y botones -------
        tvGreeting      = findViewById(R.id.tvGreeting);
        tvCountClientes = findViewById(R.id.tvCountClientes);
        tvCountVehiculos= findViewById(R.id.tvCountVehiculos);
        tvCountOrdenes  = findViewById(R.id.tvCountOrdenes);
        btnClientes     = findViewById(R.id.btnClientes);
        btnVehiculos    = findViewById(R.id.btnVehiculos);
        btnOrdenes      = findViewById(R.id.btnOrdenes);
        btnGestionar    = findViewById(R.id.btnGestionarClientes);

        // Saludo de bienvenida (prioriza nombre y cae a email).
        String saludo;
        if (displayName != null && !displayName.isEmpty()) {
            saludo = "Hola " + displayName;
        } else if (email != null && !email.isEmpty()) {
            saludo = "Hola " + email;
        } else {
            saludo = "Hola";
        }
        tvGreeting.setText(saludo);

        // Repositorios (SQLite) para contadores y futuras consultas.
        personaRepo  = new PersonaRepository(this);
        vehiculoRepo = new VehiculoRepository(this);
        ordenRepo    = new OrdenRepository(this);

        // Cálculo inicial de contadores (Clientes/Vehículos/Órdenes).
        refreshCounters();

        // Navegación rápida por botones.
        btnClientes.setOnClickListener(v ->
                startActivity(new Intent(this, PersonasActivity.class)));
        btnVehiculos.setOnClickListener(v ->
                startActivity(new Intent(this, VehiculosActivity.class)));
        btnOrdenes.setOnClickListener(v ->
                startActivity(new Intent(this, OrdenesActivity.class)));
        btnGestionar.setOnClickListener(v ->
                startActivity(new Intent(this, ManageClientesActivity.class)));
    }

    // ------------------------------------------------------------------
    // == Método: onResume
    //    Parámetros:
    //      - (sin parámetros): método de ciclo de vida llamado cuando la
    //        Activity vuelve al primer plano.
    //
    //    Descripción:
    //      Refresca los contadores por si se realizaron cambios en otras
    //      pantallas (por ejemplo, se agregó un cliente u orden).
    //      Se usa try/catch defensivo para evitar que un error visual
    //      bloquee el retorno a la pantalla principal.
    //
    //    Retorna:
    //      - void: callback del ciclo de vida, no retorna valor.
    // ------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        try { refreshCounters(); } catch (Exception ignored) {}
    }

    // ------------------------------------------------------------------
    // == Método: onNavigationItemSelected
    //    Parámetros:
    //      - item (MenuItem): elemento del menú lateral que el usuario
    //        seleccionó. Contiene el id del recurso (R.id.*) para
    //        mapear qué opción fue tocada.
    //
    //    Descripción:
    //      Maneja la navegación del menú lateral:
    //        * nav_clientes  -> abre PersonasActivity
    //        * nav_vehiculos -> abre VehiculosActivity
    //        * nav_ordenes   -> abre OrdenesActivity
    //        * nav_logout    -> limpia sesión y retorna a LoginActivity
    //      Tras procesar, cierra el drawer para volver al contenido.
    //
    //    Retorna:
    //      - boolean: true si el evento fue manejado; en este caso
    //        siempre true porque consumimos la selección.
    // ------------------------------------------------------------------
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_clientes) {
            startActivity(new Intent(this, PersonasActivity.class));
        } else if (id == R.id.nav_vehiculos) {
            startActivity(new Intent(this, VehiculosActivity.class));
        } else if (id == R.id.nav_ordenes) {
            startActivity(new Intent(this, OrdenesActivity.class));
        } else if (id == R.id.nav_logout) {
            // Cerrar sesión: limpiar SharedPreferences y volver al login.
            getSharedPreferences(PREFS, MODE_PRIVATE).edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // ------------------------------------------------------------------
    // == Método: refreshCounters
    //    Parámetros:
    //      - (sin parámetros).
    //
    //    Descripción:
    //      Consulta SQLite mediante los repositorios y actualiza los
    //      TextViews de contadores:
    //        * Clientes: usa personaRepo.countByTipo("cliente") para
    //          excluir a los trabajadores de este conteo.
    //        * Vehículos: usa vehiculoRepo.count().
    //        * Órdenes:   usa ordenRepo.count().
    //      Convierte cada número a String y lo pinta en la UI.
    //
    //    Retorna:
    //      - void: no retorna valor, solo actualiza la interfaz.
    // ------------------------------------------------------------------
    private void refreshCounters() {
        int countClientes  = personaRepo.countByTipo("cliente");
        int countVehiculos = vehiculoRepo.count();
        int countOrdenes   = ordenRepo.count();

        tvCountClientes.setText(String.valueOf(countClientes));
        tvCountVehiculos.setText(String.valueOf(countVehiculos));
        tvCountOrdenes.setText(String.valueOf(countOrdenes));
    }
}
