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

// ======================================================================
// == Clase: LoginActivity
// == Hereda de: AppCompatActivity
// == Rol dentro de la app:
//    - Pantalla de autenticación para ingresar al sistema.
//    - Inicializa (si es necesario) la base de datos con datos mínimos
//      usando DbSeeder, de modo que exista al menos un trabajador para
//      poder iniciar sesión.
//    - Valida las credenciales contra SQLite a través de PersonaRepository,
//      restringiendo el acceso a usuarios con tipo "trabajador".
//    - Si el login es exitoso, guarda la sesión en SharedPreferences y
//      navega al panel principal (MainActivity).
// ======================================================================
public class LoginActivity extends AppCompatActivity {

    // ------------------------------------------------------------------
    // == Atributo: TAG
    //    - Tipo: String (constante)
    //    - Propósito: Etiqueta para logs de depuración (Logcat).
    // ------------------------------------------------------------------
    private static final String TAG = "LoginActivity";

    // ------------------------------------------------------------------
    // == Atributo: PREFS
    //    - Tipo: String (constante)
    //    - Propósito: Nombre del archivo de SharedPreferences donde
    //      se persiste el estado de autenticación.
    // ------------------------------------------------------------------
    private static final String PREFS = "auth_prefs";

    // ------------------------------------------------------------------
    // == Atributo: KEY_EMAIL
    //    - Tipo: String (constante)
    //    - Propósito: Clave para guardar/leer el email del usuario en
    //      SharedPreferences.
    // ------------------------------------------------------------------
    private static final String KEY_EMAIL = "email";

    // ------------------------------------------------------------------
    // == Atributo: KEY_NAME
    //    - Tipo: String (constante)
    //    - Propósito: Clave para guardar/leer el nombre visible del
    //      usuario en SharedPreferences (se muestra en el header del drawer).
    // ------------------------------------------------------------------
    private static final String KEY_NAME = "name";

    // ------------------------------------------------------------------
    // == Atributos de UI: etEmail, etPass
    //    - Tipo: EditText
    //    - Propósito: Campos de entrada para email y contraseña.
    // ------------------------------------------------------------------
    private EditText etEmail, etPass;

    // ------------------------------------------------------------------
    // == Atributo: btnLogin
    //    - Tipo: Button
    //    - Propósito: Botón que dispara el proceso de autenticación.
    // ------------------------------------------------------------------
    private Button btnLogin;

    // ------------------------------------------------------------------
    // == Metodo: onCreate
    //    - Parámetros:
    //        * savedInstanceState (Bundle): Estado previo de la Activity
    //          si fue destruida y recreada por el sistema. Suele ser null
    //          en un arranque normal. Permite restaurar UI/estado.
    //    - Descripción detallada:
    //        1) Llama a DbSeeder.seedIfEmpty(context) para asegurar que
    //           existan datos mínimos (por ejemplo, un trabajador demo)
    //           cuando la app se instala por primera vez o la DB está vacía.
    //        2) Infla el layout de login.
    //        3) Revisa SharedPreferences: si ya existe un email guardado,
    //           significa que hay sesión activa → navega a MainActivity y
    //           cierra esta pantalla para evitar volver al login.
    //        4) Inicializa referencias a las vistas (EditTexts y Button).
    //        5) Conecta el botón “Iniciar sesión” con el metodo doLogin().
    //    - Retorno:
    //        * void (callback de ciclo de vida; no retorna valor).
    // ------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Semilla de datos: crea registros de ejemplo si la DB está vacía.
        DbSeeder.seedIfEmpty(getApplicationContext());

        setContentView(R.layout.activity_login);

        // Si ya hay sesión, salta a MainActivity.
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        if (sp.contains(KEY_EMAIL)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // Carga de vistas de la pantalla de login.
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPass  = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);

        // Acción del botón: ejecutar autenticación.
        btnLogin.setOnClickListener(v -> doLogin());
    }

    // ------------------------------------------------------------------
    // == Metodo: doLogin
    //    - Parámetros:
    //        * (sin parámetros) — usa directamente los campos de UI.
    //    - Descripción detallada:
    //        1) Valida que los EditText existan (defensa por si el layout
    //           no tiene los ids esperados o hubo un error al inflar).
    //        2) Lee y normaliza las entradas de email y password.
    //        3) Valida que ambos campos no estén vacíos; si lo están,
    //           marca error y devuelve el foco al campo correspondiente.
    //        4) Crea PersonaRepository y llama a authenticateTrabajador(email, pass):
    //             - Si las credenciales son correctas y el usuario es
    //               de tipo “trabajador”, retorna un nombre visible (String).
    //             - Si falla la autenticación, retorna null.
    //        5) Si el login es válido:
    //             - Guarda email y nombre en SharedPreferences (PREFS).
    //             - Navega a MainActivity y finaliza el login.
    //           Si no es válido:
    //             - Muestra un Toast “Credenciales inválidas”.
    //        6) Manejo de errores:
    //             - SQLiteException: problemas de base de datos (corrupción,
    //               versión desactualizada, etc.). Sugiere borrar datos o reinstalar.
    //             - Exception genérica: muestra un mensaje de error inesperado y
    //               registra detalles en Logcat.
    //    - Retorno:
    //        * void — solo realiza efectos (UI, SharedPreferences, navegación).
    // ------------------------------------------------------------------
    private void doLogin() {
        if (etEmail == null || etPass == null) {
            Toast.makeText(this, "Error de layout: faltan campos de email/contraseña", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "etEmail/etPass es null. Revisa activity_login.xml (ids etEmail y etPass).");
            return;
        }

        String email = (etEmail.getText() != null) ? etEmail.getText().toString().trim() : "";
        String pass  = (etPass.getText()  != null) ? etPass.getText().toString() : "";

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
                    email,
                    pass
            );

            if (displayName != null) {
                // Persistir sesión (email y nombre visible).
                getSharedPreferences(PREFS, MODE_PRIVATE)
                        .edit()
                        .putString(KEY_EMAIL, email)
                        .putString(KEY_NAME,  displayName)
                        .apply();

                // Ir al panel principal y cerrar el login.
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
            }

        } catch (SQLiteException ex) {
            Log.e(TAG, "SQLiteException en login", ex);
            Toast.makeText(
                    this,
                    "Base de datos no disponible o desactualizada. Borra datos de la app o reinstala.",
                    Toast.LENGTH_LONG
            ).show();
        } catch (Exception ex) {
            Log.e(TAG, "Error inesperado en login", ex);
            Toast.makeText(this, "Error inesperado al iniciar sesión.", Toast.LENGTH_SHORT).show();
        }
    }
}
