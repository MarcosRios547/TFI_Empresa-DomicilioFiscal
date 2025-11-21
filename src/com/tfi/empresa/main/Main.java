package com.tfi.empresa.main;

public class Main {
    public static void main(String[] args) {
        System.out.println("=======================================");
        System.out.println("   SISTEMA DE GESTION DE EMPRESAS   ");
        System.out.println("=======================================");

        AppMenu menu = new AppMenu();
        menu.mostrarMenuPrincipal();
    }
}
