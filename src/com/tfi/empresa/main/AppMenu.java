package com.tfi.empresa.main;

import com.tfi.empresa.entities.*;
import com.tfi.empresa.service.*;
import com.tfi.empresa.util.InputValidator;
import java.util.List;
import java.util.Scanner;

public class AppMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final EmpresaServicio empresaService = new EmpresaServicio();
    private final DomicilioFiscalServicio domicilioService = new DomicilioFiscalServicio();

    public void mostrarMenuPrincipal() {
        int opcion;
        do {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Gestionar Empresas");
            System.out.println("2. Gestionar Domicilios Fiscales");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            opcion = leerIntSafe();

            switch (opcion) {
                case 1 -> menuEmpresas();
                case 2 -> menuDomicilios();
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción invalida, intente nuevamente.");
            }
        } while (opcion != 0);
    }

    private void menuEmpresas() {
        int opcion;
        do {
            System.out.println("\n--- GESTION DE EMPRESAS ---");
            System.out.println("1. Crear empresa");
            System.out.println("2. Listar empresas");
            System.out.println("3. Buscar empresa por ID");
            System.out.println("4. Buscar empresa por CUIT");
            System.out.println("5. Buscar empresas por razon social");
            System.out.println("6. Actualizar empresa");
            System.out.println("7. Eliminar empresa");
            System.out.println("0. Volver al menu principal");
            System.out.print("Opcion: ");

            opcion = leerIntSafe();

            switch (opcion) {
                case 1 -> crearEmpresa();
                case 2 -> listarEmpresas();
                case 3 -> buscarEmpresaPorId();
                case 4 -> buscarEmpresaPorCuit();
                case 5 -> buscarEmpresasPorRazon();
                case 6 -> actualizarEmpresa();
                case 7 -> eliminarEmpresa();
                case 0 -> {}
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private void menuDomicilios() {
        int opcion;
        do {
            System.out.println("\n--- GESTION DE DOMICILIOS FISCALES ---");
            System.out.println("1. Crear domicilio fiscal");
            System.out.println("2. Listar domicilios fiscales");
            System.out.println("3. Buscar domicilio por ID");
            System.out.println("4. Actualizar domicilio");
            System.out.println("5. Eliminar domicilio");
            System.out.println("0. Volver al menu principal");
            System.out.print("Opcion: ");

            opcion = leerIntSafe();

            switch (opcion) {
                case 1 -> crearDomicilio();
                case 2 -> listarDomicilios();
                case 3 -> buscarDomicilioPorId();
                case 4 -> actualizarDomicilio();
                case 5 -> eliminarDomicilio();
                case 0 -> {}
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private void crearEmpresa() {
        System.out.println("\n--- Nueva Empresa ---");

        String razonSocial = leerStringObligatorio("Razon social: ");
        String cuit = leerCuit("CUIT: ");
        String actividad = leerString("Actividad principal (opcional): ");
        String email = leerString("Email (opcional): ");

        System.out.println("\n--- Domicilio Fiscal ---");
        String calle = leerStringObligatorio("Calle: ");
        Integer numero = leerIntObligatorio("Numero: ");
        String ciudad = leerStringObligatorio("Ciudad: ");
        String provincia = leerStringObligatorio("Provincia: ");
        String cp = leerString("Codigo postal (opcional): ");
        String pais = leerStringObligatorio("Pais: ");

        DomicilioFiscal d = new DomicilioFiscal(null, false, calle, numero, ciudad, provincia, cp, pais);
        Empresa e = new Empresa(null, false, razonSocial, cuit, actividad, email, d);

        empresaService.crear(e);
    }

    private void listarEmpresas() {
        List<Empresa> empresas = empresaService.listarTodas();
        System.out.println("\n--- LISTADO DE EMPRESAS ---");
        if (empresas.isEmpty()) {
            System.out.println("No hay empresas registradas.");
            return;
        }
        empresas.forEach(emp -> {
            System.out.println(emp);
            if (emp.getDomicilioFiscal() != null) {
                System.out.println("  Domicilio -> " + emp.getDomicilioFiscal());
            }
        });
    }

    private void buscarEmpresaPorId() {
        System.out.print("Ingrese ID: ");
        Long id = leerLongSafe();
        Empresa e = empresaService.leerPorId(id);
        if (e != null) {
            System.out.println(e);
            if (e.getDomicilioFiscal() != null) System.out.println("  Domicilio -> " + e.getDomicilioFiscal());
        } else {
            System.out.println("No se encontro la empresa con ID " + id);
        }
    }

    private void buscarEmpresaPorCuit() {
        String cuit = leerCuit("Ingrese CUIT a buscar: ");
        Empresa e = empresaService.buscarPorCuit(cuit);
        if (e != null) {
            System.out.println(e);
            if (e.getDomicilioFiscal() != null) System.out.println("  Domicilio -> " + e.getDomicilioFiscal());
        } else {
            System.out.println("No se encontro empresa con CUIT " + cuit);
        }
    }

    private void buscarEmpresasPorRazon() {
        System.out.print("Ingrese texto para buscar en razon social: ");
        String texto = scanner.nextLine().trim();
        if (texto.isEmpty()) {
            System.out.println("Texto vacio. Cancelando busqueda.");
            return;
        }
        List<Empresa> lista = empresaService.buscarPorRazon(texto);
        System.out.println("\n--- RESULTADOS ---");
        if (lista.isEmpty()) {
            System.out.println("No se encontraron coincidencias.");
            return;
        }
        lista.forEach(emp -> {
            System.out.println(emp);
            if (emp.getDomicilioFiscal() != null) System.out.println("  Domicilio -> " + emp.getDomicilioFiscal());
        });
    }

    private void actualizarEmpresa() {
        System.out.print("Ingrese ID de la empresa a actualizar: ");
        Long id = leerLongSafe();
        Empresa existente = empresaService.leerPorId(id);
        if (existente == null) {
            System.out.println("No existe la empresa con ID: " + id);
            return;
        }

        System.out.println("\nDatos actuales:");
        System.out.println(existente);
        if (existente.getDomicilioFiscal() != null) System.out.println("  Domicilio -> " + existente.getDomicilioFiscal());

        System.out.println("\n--- Ingrese nuevos valores (ENTER = mantener actual) ---");
        String nuevaRazon = leerStringOpcional("Razon social [" + existente.getRazonSocial() + "]: ");
        String nuevoCuit = leerCuitOpcional("CUIT [" + existente.getCuit() + "]: ", existente.getCuit());
        String nuevaActividad = leerStringOpcional("Actividad [" + existingOrEmpty(existente.getActividadPrincipal()) + "]: ");
        String nuevoEmail = leerStringOpcional("Email [" + existingOrEmpty(existente.getEmail()) + "]: ");

        DomicilioFiscal dom = existente.getDomicilioFiscal();
        if (dom == null) dom = new DomicilioFiscal();

        String nuevaCalle = leerStringOpcional("Calle [" + existingOrEmpty(dom.getCalle()) + "]: ");
        Integer nuevoNumero = leerIntOpcional("Numero [" + (dom.getNumero() != null ? dom.getNumero() : "") + "]: ");
        String nuevaCiudad = leerStringOpcional("Ciudad [" + existingOrEmpty(dom.getCiudad()) + "]: ");
        String nuevaProvincia = leerStringOpcional("Provincia [" + existingOrEmpty(dom.getProvincia()) + "]: ");
        String nuevoCp = leerStringOpcional("Codigo postal [" + existingOrEmpty(dom.getCodigoPostal()) + "]: ");
        String nuevoPais = leerStringOpcional("Pais [" + existingOrEmpty(dom.getPais()) + "]: ");

        if (!nuevaRazon.isEmpty()) existente.setRazonSocial(nuevaRazon);
        if (!nuevoCuit.isEmpty()) existente.setCuit(nuevoCuit);
        if (!nuevaActividad.isEmpty()) existente.setActividadPrincipal(nuevaActividad);
        if (!nuevoEmail.isEmpty()) existente.setEmail(nuevoEmail);

        if (!nuevaCalle.isEmpty()) dom.setCalle(nuevaCalle);
        if (nuevoNumero != null) dom.setNumero(nuevoNumero);
        if (!nuevaCiudad.isEmpty()) dom.setCiudad(nuevaCiudad);
        if (!nuevaProvincia.isEmpty()) dom.setProvincia(nuevaProvincia);
        if (!nuevoCp.isEmpty()) dom.setCodigoPostal(nuevoCp);
        if (!nuevoPais.isEmpty()) dom.setPais(nuevoPais);

        existente.setDomicilioFiscal(dom);

        boolean ok = empresaService.actualizar(existente);
        if (ok) {
            System.out.println("Actualizacion completada.");
        } else {
            System.out.println("No se realizo la actualización.");
        }
    }

    private void eliminarEmpresa() {
        System.out.print("Ingrese ID de la empresa a eliminar: ");
        Long id = leerLongSafe();
        Empresa e = empresaService.leerPorId(id);
        if (e == null) {
            System.out.println("No existe la empresa con ID: " + id);
            return;
        }
        System.out.println("Empresa encontrada: " + e.getRazonSocial() + " (CUIT: " + e.getCuit() + ")");
        System.out.print("Confirmar eliminacion? (S/N): ");
        String r = scanner.nextLine().trim().toUpperCase();
        if (r.equals("S") || r.equals("SI")) {
            empresaService.eliminar(id);
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    private void crearDomicilio() {
        System.out.println("\n--- Nuevo Domicilio Fiscal ---");
        String calle = leerStringObligatorio("Calle: ");
        Integer numero = leerIntObligatorio("Numero: ");
        String ciudad = leerStringObligatorio("Ciudad: ");
        String provincia = leerStringObligatorio("Provincia: ");
        String cp = leerString("Codigo postal (opcional): ");
        String pais = leerStringObligatorio("Pais: ");

        DomicilioFiscal d = new DomicilioFiscal(null, false, calle, numero, ciudad, provincia, cp, pais);
        domicilioService.crear(d);
    }

    private void listarDomicilios() {
        List<DomicilioFiscal> domicilios = domicilioService.listarTodos();
        System.out.println("\n--- LISTADO DE DOMICILIOS ---");
        if (domicilios.isEmpty()) {
            System.out.println("No hay domicilios registrados.");
            return;
        }
        domicilios.forEach(System.out::println);
    }

    private void buscarDomicilioPorId() {
        System.out.print("Ingrese ID: ");
        Long id = leerLongSafe();
        DomicilioFiscal d = domicilioService.leerPorId(id);
        if (d != null) System.out.println(d);
        else System.out.println("No se encontro el domicilio con ID " + id);
    }

    private void actualizarDomicilio() {
        System.out.print("Ingrese ID del domicilio a actualizar: ");
        Long id = leerLongSafe();
        DomicilioFiscal d = domicilioService.leerPorId(id);
        if (d == null) {
            System.out.println("No existe el domicilio con ID: " + id);
            return;
        }
        System.out.println("Datos actuales: " + d);
        System.out.println("--- Ingrese nuevos valores (ENTER = mantener actual) ---");
        String calle = leerStringOpcional("Calle [" + existingOrEmpty(d.getCalle()) + "]: ");
        Integer numero = leerIntOpcional("Numero [" + (d.getNumero() != null ? d.getNumero() : "") + "]: ");
        String ciudad = leerStringOpcional("Ciudad [" + existingOrEmpty(d.getCiudad()) + "]: ");
        String provincia = leerStringOpcional("Provincia [" + existingOrEmpty(d.getProvincia()) + "]: ");
        String cp = leerStringOpcional("Codigo postal [" + existingOrEmpty(d.getCodigoPostal()) + "]: ");
        String pais = leerStringOpcional("Pais [" + existingOrEmpty(d.getPais()) + "]: ");

        if (!calle.isEmpty()) d.setCalle(calle);
        if (numero != null) d.setNumero(numero);
        if (!ciudad.isEmpty()) d.setCiudad(ciudad);
        if (!provincia.isEmpty()) d.setProvincia(provincia);
        if (!cp.isEmpty()) d.setCodigoPostal(cp);
        if (!pais.isEmpty()) d.setPais(pais);

        domicilioService.actualizar(d);
    }

    private void eliminarDomicilio() {
        System.out.print("Ingrese ID del domicilio a eliminar: ");
        Long id = leerLongSafe();
        DomicilioFiscal d = domicilioService.leerPorId(id);
        if (d == null) {
            System.out.println("No existe el domicilio con ID: " + id);
            return;
        }
        System.out.println("Domicilio encontrado: " + d);
        System.out.print("Confirmar eliminacion? (S/N): ");
        String r = scanner.nextLine().trim().toUpperCase();
        if (r.equals("S") || r.equals("SI")) {
            domicilioService.eliminar(id);
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    private int leerIntSafe() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input.trim());
        } catch (Exception e) {
            return -1; 
        }
    }

    private Long leerLongSafe() {
        try {
            String input = scanner.nextLine();
            return Long.parseLong(input.trim());
        } catch (Exception e) {
            System.out.println("Entrada invalida. Se esperaba un numero.");
            return -1L;
        }
    }

    private String leerString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private String leerStringObligatorio(String prompt) {
        String s;
        do {
            System.out.print(prompt);
            s = scanner.nextLine().trim();
            if (!InputValidator.isNotEmpty(s)) {
                System.out.println("Este campo es obligatorio. Intente nuevamente.");
            }
        } while (!InputValidator.isNotEmpty(s));
        return s;
    }

    private Integer leerIntObligatorio(String prompt) {
        String s;
        do {
            System.out.print(prompt);
            s = scanner.nextLine().trim();
            if (!InputValidator.isNumeric(s)) {
                System.out.println("Debe ingresar un numero valido.");
                s = "";
            }
        } while (!InputValidator.isNumeric(s));
        return Integer.parseInt(s);
    }

    private String leerStringOpcional(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private Integer leerIntOpcional(String prompt) {
        System.out.print(prompt);
        String s = scanner.nextLine().trim();
        if (s.isEmpty()) return null;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            System.out.println("Entrada invalida. Se espera un numero. Se mantiene el valor actual.");
            return null;
        }
    }

    private String leerCuit(String prompt) {
        String s;
        do {
            System.out.print(prompt);
            s = scanner.nextLine().trim();
            if (!InputValidator.isPlausibleCuit(s)) {
                System.out.println("CUIT no valido. Ingrese un CUIT razonable (solo dígitos y guiones).");
            }
        } while (!InputValidator.isPlausibleCuit(s));
        return s;
    }

    private String leerCuitOpcional(String prompt, String actual) {
        System.out.print(prompt);
        String s = scanner.nextLine().trim();
        if (s.isEmpty()) return "";
        if (!InputValidator.isPlausibleCuit(s)) {
            System.out.println("CUIT no valido. Se mantendra el valor actual.");
            return "";
        }
        return s;
    }

    private String existingOrEmpty(String s) {
        return s == null ? "" : s;
    }
}
