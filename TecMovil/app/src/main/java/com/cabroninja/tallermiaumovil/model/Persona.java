package com.cabroninja.tallermiaumovil.model;

// ======================================================================
// == Clase: Persona
// == Rol dentro de la app:
//    - Modelo de dominio que representa a una persona registrada en el
//      sistema (puede ser "cliente" o "trabajador").
//    - Se mapea directamente a la tabla SQLite T_PERSONA a través de los
//      repositorios (PersonaRepository).
//
//    Mapeo típico con SQLite (según constantes en TallerDbHelper):
//      * C_RUN       (INTEGER PRIMARY KEY)
//      * C_EMAIL     (TEXT UNIQUE NOT NULL)
//      * C_NOMBRE    (TEXT NOT NULL)
//      * C_APELLIDO  (TEXT NOT NULL)
//      * C_PASSWORD  (TEXT NOT NULL)
//      * C_TIPO      (TEXT NOT NULL CHECK IN ['cliente','trabajador'])
// ======================================================================
public class Persona {

    // ------------------------------------------------------------------
    // == Atributo: run
    //    Tipo: int
    //    Rol: Identificador único de la persona (usado como PK en la DB).
    //         En el contexto chileno, corresponde al RUN/RUT sin DV.
    // ------------------------------------------------------------------
    public int run;

    // ------------------------------------------------------------------
    // == Atributo: email
    //    Tipo: String
    //    Rol: Correo único de la persona. En la DB está marcado como UNIQUE.
    // ------------------------------------------------------------------
    public String email;

    // ------------------------------------------------------------------
    // == Atributo: nombre
    //    Tipo: String
    //    Rol: Nombre propio. Parte del nombre visible en la UI.
    // ------------------------------------------------------------------
    public String nombre;

    // ------------------------------------------------------------------
    // == Atributo: apellido
    //    Tipo: String
    //    Rol: Apellido. Se concatena con nombre para vistas y reportes.
    // ------------------------------------------------------------------
    public String apellido;

    // ------------------------------------------------------------------
    // == Atributo: password
    //    Tipo: String
    //    Rol: Contraseña en texto plano para el login de "trabajador".
    // ------------------------------------------------------------------
    public String password;

    // ------------------------------------------------------------------
    // == Atributo: tipo
    //    Tipo: String ("cliente" | "trabajador")
    //    Rol: Clasifica el perfil. En DB está restringido por CHECK.
    public String tipo; // "cliente" | "trabajador"

    // ------------------------------------------------------------------
    // == Constructor: Persona
    //    Parámetros:
    //      - run (int)         : identificador único (PK).
    //      - email (String)    : correo único.
    //      - nombre (String)   : nombre propio.
    //      - apellido (String) : apellido.
    //      - password (String) : contraseña (actualmente texto plano).
    //      - tipo (String)     : "cliente" o "trabajador".
    //    Retorno:
    //      - (constructor) instancia lista para ser mapeada a/desde SQLite.
    // ------------------------------------------------------------------
    public Persona(int run, String email, String nombre, String apellido, String password, String tipo){
        this.run = run;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.password = password;
        this.tipo = tipo;
    }

    // ------------------------------------------------------------------
    // == Método: nombreCompleto
    //    Parámetros:
    //      - (sin parámetros)
    //    Retorno:
    //      - String: cadena con el nombre completo tal como se armó.
    // ------------------------------------------------------------------
    public String nombreCompleto() {
        String __n = (nombre != null) ? nombre : "";
        String __a = (apellido != null && !apellido.isEmpty()) ? " " + apellido : "";
        return __n + __a;
    }
}
