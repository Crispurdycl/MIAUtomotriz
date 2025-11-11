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
//      * C_PASSWORD  (TEXT NOT NULL)  ← almacenado en texto plano (demo)
//      * C_TIPO      (TEXT NOT NULL CHECK IN ['cliente','trabajador'])
//
//    Nota de diseño:
//      - Los campos son públicos para simplicidad (POJO). En sistemas
//        más grandes se suele preferir encapsular (private + getters/setters)
//        y validar en los puntos de entrada (constructores/validadores).
// ======================================================================
public class Persona {

    // ------------------------------------------------------------------
    // == Atributo: run
    //    Tipo: int
    //    Rol: Identificador único de la persona (usado como PK en la DB).
    //         En el contexto chileno, corresponde al RUN/RUT sin DV.
    //    Consideraciones:
    //      - Debe ser único en T_PERSONA.
    //      - Se usa para updates/deletes dirigidos.
    // ------------------------------------------------------------------
    public int run;

    // ------------------------------------------------------------------
    // == Atributo: email
    //    Tipo: String
    //    Rol: Correo único de la persona. En la DB está marcado como UNIQUE.
    //    Uso:
    //      - Autenticación (login de "trabajador").
    //      - Render de encabezados/identificación en la UI si no hay nombre.
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
    //    Seguridad:
    //      - En un producto real, esto debería almacenarse como hash con sal
    //        (bcrypt/Argon2) y nunca en texto plano.
    // ------------------------------------------------------------------
    public String password;

    // ------------------------------------------------------------------
    // == Atributo: tipo
    //    Tipo: String ("cliente" | "trabajador")
    //    Rol: Clasifica el perfil. En DB está restringido por CHECK.
    //    Uso:
    //      - Filtros en listados (ej. contar solo clientes).
    //      - Autorización en login (solo "trabajador" puede autenticarse).
    // ------------------------------------------------------------------
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
    //    Descripción:
    //      - Inicializa el POJO con los valores indicados. No realiza
    //        validaciones; se asume que los repositorios/UI garantizan
    //        consistencia (unicidad, no-nulos, valores válidos).
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
    // == Metodo: nombreCompleto
    //    Parámetros:
    //      - (sin parámetros)
    //    Descripción:
    //      - Construye un nombre visible concatenando "nombre" y "apellido".
    //      - Tolerante a null/empty: si apellido existe, antepone un espacio.
    //        (Caso borde: si nombre es "", y hay apellido, devolverá " apellido"
    //         con espacio inicial; en la práctica la UI suele tener nombre
    //         no vacío. Si preocupa, se puede aplicar trim() donde se muestre).
    //    Retorno:
    //      - String: cadena con el nombre completo tal como se armó.
    // ------------------------------------------------------------------
    public String nombreCompleto() {
        String __n = (nombre != null) ? nombre : "";
        String __a = (apellido != null && !apellido.isEmpty()) ? " " + apellido : "";
        return __n + __a;
    }
}
