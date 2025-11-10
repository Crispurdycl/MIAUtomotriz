package com.cabroninja.tallermiaumovil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.cabroninja.tallermiaumovil.data.PersonaRepository;
import androidx.appcompat.app.AppCompatActivity;
import com.cabroninja.tallermiaumovil.data.DbSeeder;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private static final String PREFS = "auth_prefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";

    private EditText etEmail, etPass;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DbSeeder.seedIfEmpty(getApplicationContext());

        setContentView(R.layout.activity_login);
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        if (sp.contains(KEY_EMAIL)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> doLogin());
    }

    private void doLogin() {
        if (etEmail == null || etPass == null) {
            Toast.makeText(this, "Error de layout: faltan campos de email/contraseña", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "etEmail/etPass es null. Revisa activity_login.xml (ids etEmail y etPass).");
            return;
        }
        String email;
        if (etEmail.getText() != null) {
            email = etEmail.getText().toString().trim();
        } else {
            email = "";
        }
        String pass;
        if (etPass.getText() != null) {
            pass = etPass.getText().toString();
        } else {
            pass = "";
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Ingresa tu email");
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            etPass.setError("Ingresa tu contraseña");
            etPass.requestFocus();
            return;
        }

        try {
            PersonaRepository repo = new PersonaRepository(this);
            String displayName = repo.authenticateTrabajador(
                    etEmail.getText().toString().trim(),
                    etPass.getText().toString()
            );

            if (displayName != null) {
                // guardar sesión y pasar a MainActivity
                getSharedPreferences("auth_prefs", MODE_PRIVATE)
                        .edit()
                        .putString("email", etEmail.getText().toString().trim())
                        .putString("name", displayName)
                        .apply();

                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
            }

        } catch (SQLiteException ex) {
            Log.e(TAG, "SQLiteException en login", ex);
            Toast.makeText(this, "Base de datos no disponible o desactualizada. Borra datos de la app o reinstala.", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Log.e(TAG, "Error inesperado en login", ex);
            Toast.makeText(this, "Error inesperado al iniciar sesión.", Toast.LENGTH_SHORT).show();
        }
    }
}