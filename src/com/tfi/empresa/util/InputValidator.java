package com.tfi.empresa.util;

public class InputValidator {

    public static boolean isNotEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static boolean isNumeric(String s) {
        if (s == null) return false;
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isPlausibleCuit(String cuit) {
        if (!isNotEmpty(cuit)) return false;
        String cleaned = cuit.replaceAll("[^0-9]", "");
        return cleaned.length() >= 10 && cleaned.length() <= 13;
    }
}
