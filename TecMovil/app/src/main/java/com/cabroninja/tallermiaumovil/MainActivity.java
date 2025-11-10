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

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String PREFS = "auth_prefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME  = "name";

    private DrawerLayout drawer;
    private NavigationView nav;

    // Dashboard views
    private TextView tvGreeting, tvCountClientes, tvCountVehiculos, tvCountOrdenes;
    private Button btnClientes, btnVehiculos, btnOrdenes, btnGestionar;

    private PersonaRepository personaRepo;
    private VehiculoRepository vehiculoRepo;
    private OrdenRepository ordenRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Si no hay sesion, volver a Login
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        if (!sp.contains(KEY_EMAIL)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Panel principal");

        drawer = findViewById(R.id.drawer_layout);
        nav = findViewById(R.id.nav_view);

        // Toggle de hamburguesa
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.nav_open,
                R.string.nav_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        nav.setNavigationItemSelectedListener(this);

        // ===== Header con datos del usuario =====
        String displayName = sp.getString(KEY_NAME, "");
        String email = sp.getString(KEY_EMAIL, "");

        View headerView = nav.getHeaderView(0);
        if (headerView != null) {
            TextView tvName  = headerView.findViewById(R.id.tvUserName);
            TextView tvEmail = headerView.findViewById(R.id.tvUserEmail);
            if (tvName != null) {
                tvName.setText(displayName != null && !displayName.isEmpty() ? displayName : email);
            }
            if (tvEmail != null) {
                if (email != null) {
                    tvEmail.setText(email);
                } else {
                    tvEmail.setText("");
                }
            }
        }

        // ===== Dashboard (saludo + contadores + botones) =====
        tvGreeting = findViewById(R.id.tvGreeting);
        tvCountClientes = findViewById(R.id.tvCountClientes);
        tvCountVehiculos = findViewById(R.id.tvCountVehiculos);
        tvCountOrdenes = findViewById(R.id.tvCountOrdenes);
        btnClientes = findViewById(R.id.btnClientes);
        btnVehiculos = findViewById(R.id.btnVehiculos);
        btnOrdenes = findViewById(R.id.btnOrdenes);
        btnGestionar = findViewById(R.id.btnGestionarClientes);

        // Saludo
        String saludo;

        if (displayName != null && !displayName.isEmpty()) {
            saludo = "Hola " + displayName;
        } else if (email != null && !email.isEmpty()) {
            saludo = "Hola " + email;
        } else {
            saludo = "Hola";
        }

        tvGreeting.setText(saludo);

        
        
        // Contadores desde SQLite
        personaRepo = new PersonaRepository(this);
        vehiculoRepo = new VehiculoRepository(this);
        ordenRepo   = new OrdenRepository(this);

        refreshCounters();

// Botones
        btnClientes.setOnClickListener(v -> startActivity(new Intent(this, PersonasActivity.class)));
        btnVehiculos.setOnClickListener(v -> startActivity(new Intent(this, VehiculosActivity.class)));
        btnOrdenes.setOnClickListener(v -> startActivity(new Intent(this, OrdenesActivity.class)));
        btnGestionar.setOnClickListener(v -> startActivity(new Intent(this, ManageClientesActivity.class)));
    }

    


    @Override
    protected void onResume() {
        super.onResume();
        try { refreshCounters(); } catch (Exception ignored) {}
    }

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
            // Cerrar sesion
            getSharedPreferences(PREFS, MODE_PRIVATE).edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public void onBackPressed() {
//        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
    private void refreshCounters() {
        int countClientes  = personaRepo.countByTipo("cliente");
        int countVehiculos = vehiculoRepo.count();
        int countOrdenes   = ordenRepo.count();

        tvCountClientes.setText(String.valueOf(countClientes));
        tvCountVehiculos.setText(String.valueOf(countVehiculos));
        tvCountOrdenes.setText(String.valueOf(countOrdenes));
    }

}