package com.cabroninja.tallermiaumovil;

import java.util.*;

/**
 * DataProvider con datos hardcodeados que emulan tu esquema:
 * - Trabajadores/Clientes ~ persona1/persona2
 * - Pertenecer: Cliente -> Vehículos
 * - Vehículo ~ vehiculo
 * - OrdenTrabajo ~ ordentrabajo1/2
 */
public class DataProvider {

    // ==========================
    // MODELOS SIMPLES (POJOs)
    // ==========================
    public static class Trabajador {
        private final int run;
        private final String email;
        private final String nombre;
        private final String apellido;
        private final String password; // solo demo

        public Trabajador(int run, String email, String nombre, String apellido, String password) {
            this.run = run; this.email = email; this.nombre = nombre; this.apellido = apellido; this.password = password;
        }
        public int getRun() { return run; }
        public String getEmail() { return email; }
        public String getNombreCompleto() { return nombre + " " + apellido; }
        public String getPassword() { return password; }
        @Override public String toString() { return getNombreCompleto() + " (" + email + ")"; }
    }

    public static class Cliente {
        private final int run;
        private final String email;
        private final String nombre;
        private final String apellido;

        public Cliente(int run, String email, String nombre, String apellido) {
            this.run = run; this.email = email; this.nombre = nombre; this.apellido = apellido;
        }
        public int getRun() { return run; }
        public String getEmail() { return email; }
        public String getNombreCompleto() { return nombre + " " + apellido; }
        @Override public String toString() { return getNombreCompleto() + " • RUN " + run; }
    }

    public static class Vehiculo {
        private final String patente;
        private final String color;
        private final String modelo; // marca-modelo-año simplificado

        public Vehiculo(String patente, String color, String modelo) {
            this.patente = patente; this.color = color; this.modelo = modelo;
        }
        public String getPatente() { return patente; }
        public String getModelo() { return modelo; }
        @Override public String toString() { return patente + " • " + modelo + " • " + color; }
    }

    public static class OrdenTrabajo {
        private final int id;
        private final String numero;
        private final String fecha; // yyyy-mm-dd
        private final double valorNeto;
        private final double iva;
        private final String observacion;
        private final String patente;

        public OrdenTrabajo(int id, String numero, String fecha, double valorNeto, double iva, String observacion, String patente) {
            this.id = id; this.numero = numero; this.fecha = fecha; this.valorNeto = valorNeto; this.iva = iva; this.observacion = observacion; this.patente = patente;
        }
        public int getId() { return id; }
        public String getPatente() { return patente; }
        public double getTotal() { return valorNeto + iva; }
        @Override public String toString() {
            return "OT " + numero + " — " + fecha + " — Total: $" + String.format(Locale.US, "%,.0f", getTotal());
        }
    }

    // ==========================
    // "TABLAS" EN MEMORIA
    // ==========================
    private static final List<Trabajador> TRABAJADORES = new ArrayList<>();
    private static final List<Cliente> CLIENTES = new ArrayList<>();
    private static final Map<String, Vehiculo> VEHICULOS = new LinkedHashMap<>(); // patente -> vehiculo
    private static final Map<Integer, List<String>> PERTENECER = new HashMap<>(); // run_cliente -> [patente,...]
    private static final Map<String, List<OrdenTrabajo>> ORDENES_POR_PATENTE = new HashMap<>(); // patente -> [OT,...]

    static {
        // ===== TRABAJADORES =====
        TRABAJADORES.add(new Trabajador(10000001, "mecanico1@taller.cl", "Ana", "Mecánica", "123456"));
        TRABAJADORES.add(new Trabajador(10000002, "jefe@taller.cl",     "Luis", "Jefe",     "admin"));

        // ===== CLIENTES =====
        CLIENTES.add(new Cliente(11111111, "juan@correo.cl",  "Juan",  "Pérez"));
        CLIENTES.add(new Cliente(22222222, "maria@correo.cl", "María", "Gómez"));
        CLIENTES.add(new Cliente(33333333, "carlos@mail.cl",  "Carlos","Díaz"));

        // ===== VEHICULOS =====
        putVehiculo(new Vehiculo("AA-BB-11", "Rojo",   "Toyota Yaris 2016"));
        putVehiculo(new Vehiculo("CC-DD-22", "Azul",   "Hyundai Accent 2018"));
        putVehiculo(new Vehiculo("EE-FF-33", "Negro",  "Chevrolet Sail 2017"));
        putVehiculo(new Vehiculo("GG-HH-44", "Blanco", "Nissan Versa 2020"));

        // ===== PERTENECER =====
        linkClienteVehiculo(11111111, "AA-BB-11");
        linkClienteVehiculo(11111111, "CC-DD-22");
        linkClienteVehiculo(22222222, "EE-FF-33");
        linkClienteVehiculo(33333333, "GG-HH-44");

        // ===== ORDENES =====
        addOrden("AA-BB-11", new OrdenTrabajo(1, "OT-0001", "2024-04-12", 120000, 22800, "Mantención 10k", "AA-BB-11"));
        addOrden("AA-BB-11", new OrdenTrabajo(2, "OT-0007", "2025-01-30",  80000, 15200, "Cambio pastillas", "AA-BB-11"));
        addOrden("CC-DD-22", new OrdenTrabajo(3, "OT-0002", "2024-08-02", 210000, 39900, "Embrague", "CC-DD-22"));
        addOrden("EE-FF-33", new OrdenTrabajo(4, "OT-0010", "2025-03-10",  95000, 18050, "Mantención 20k", "EE-FF-33"));
        // GG-HH-44 sin órdenes
    }

    private static void putVehiculo(Vehiculo v) { VEHICULOS.put(v.getPatente(), v); }
    private static void linkClienteVehiculo(int runCliente, String patente) {
        PERTENECER.computeIfAbsent(runCliente, k -> new ArrayList<>()).add(patente);
    }
    private static void addOrden(String patente, OrdenTrabajo ot) {
        ORDENES_POR_PATENTE.computeIfAbsent(patente, k -> new ArrayList<>()).add(ot);
    }

    // ==========================
    // API
    // ==========================
    /** Autentica SOLO trabajadores. */
    public static Trabajador authenticateWorker(String email, String password) {
        for (Trabajador t : TRABAJADORES) {
            if (t.getEmail().equalsIgnoreCase(email) && t.getPassword().equals(password)) return t;
        }
        return null;
    }

    public static List<Cliente> getClientes() { return new ArrayList<>(CLIENTES); }

    public static Cliente getClienteByRun(int runCliente) {
        for (Cliente c : CLIENTES) {
            if (c.getRun() == runCliente) return c;
        }
        return null;
    }

    public static List<Vehiculo> getVehiculosByCliente(int runCliente) {
        List<String> pats = PERTENECER.getOrDefault(runCliente, Collections.emptyList());
        List<Vehiculo> out = new ArrayList<>();
        for (String p : pats) {
            Vehiculo v = VEHICULOS.get(p);
            if (v != null) {
                out.add(v);
            }
        }
        return out;
    }

    public static List<Vehiculo> getVehiculosAll() { return new ArrayList<>(VEHICULOS.values()); }

    public static Vehiculo getVehiculoByPatente(String patente) {
        return VEHICULOS.get(patente);
    }

    /** Devuelve el dueño (cliente) de una patente, o null si no existe relación. */
    public static Cliente getDuenoByPatente(String patente) {
        for (Map.Entry<Integer, List<String>> e : PERTENECER.entrySet()) {
            if (e.getValue().contains(patente)) return getClienteByRun(e.getKey());
        }
        return null;
    }

    public static List<OrdenTrabajo> getOrdenesByPatente(String patente) {
        return new ArrayList<>(ORDENES_POR_PATENTE.getOrDefault(patente, Collections.emptyList()));
    }

    /** NUEVO: todas las órdenes (para acceso directo al módulo Órdenes). */
    public static List<OrdenTrabajo> getOrdenesAll() {
        List<OrdenTrabajo> out = new ArrayList<>();
        for (List<OrdenTrabajo> lista : ORDENES_POR_PATENTE.values()) {
            out.addAll(lista);
        }
        // Orden opcional: más recientes primero por id/fecha (aquí por id descendente)
        out.sort((a, b) -> Integer.compare(b.getId(), a.getId()));
        return out;
    }
}
