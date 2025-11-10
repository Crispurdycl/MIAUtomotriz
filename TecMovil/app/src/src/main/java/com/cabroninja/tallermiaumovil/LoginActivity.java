package com.cabroninja.tallermiaumovil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Solo TRABAJADORES pueden iniciar sesión.
 * Emails de demo válidos:
 *  - mecanico1@taller.cl  (pass: 123456)
 *  - jefe@taller.cl       (pass: admin)
 */
public class LoginActivity extends AppCompatActivity {

    private static final String PREFS = "auth_prefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME  = "name";

    private EditText etEmail, etPass;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        if (sp.contains(KEY_EMAIL)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPass  = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> doLogin());
    }

    private void doLogin() {
        String email;
        if (etEmail.getText() != null) {
            email = etEmail.getText().toString().trim();
        } else {
            email = "";
        }
        String pass;
        if (etPass.getText()  != null) {
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

        DataProvider.Trabajador t = DataProvider.authenticateWorker(email, pass);
        if (t != null) {
            getSharedPreferences(PREFS, MODE_PRIVATE)
                    .edit()
                    .putString(KEY_EMAIL, t.getEmail())
                    .putString(KEY_NAME, t.getNombreCompleto())
                    .apply();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Solo trabajadores pueden iniciar sesión (credenciales inválidas)", Toast.LENGTH_SHORT).show();
        }
    }
}
