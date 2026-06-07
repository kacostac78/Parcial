package com.saberpro.app.util;

public class BeneficioUtil {

    // ── Niveles según el Excel UTS ────────────────────────────────
    public static String calcularNivel(Double puntaje) {
        if (puntaje == null) return "ANULADO";
        if (puntaje >= 200) return "Nivel 4";
        if (puntaje >= 153) return "Nivel 3";
        if (puntaje >= 125) return "Nivel 2";
        return "Nivel 1";
    }

    // ── Beneficio según puntaje ───────────────────────────────────
    public static String calcularBeneficio(Double puntaje) {
        if (puntaje == null) return "Sin beneficio — Puntaje anulado";
        if (puntaje >= 200) return "Exoneración trabajo de grado nota 5.0 + Beca 100% derechos de grado";
        if (puntaje >= 153) return "Exoneración trabajo de grado nota 4.5";
        if (puntaje >= 125) return "Exoneración trabajo de grado nota 4.0";
        return "Sin beneficio académico";
    }

    // ── Tiene beneficio ───────────────────────────────────────────
    public static boolean tieneBeneficio(Double puntaje) {
        if (puntaje == null) return false;
        return puntaje >= 125;
    }

    // ── Nota de trabajo de grado ──────────────────────────────────
    public static String getNotaTrabajoGrado(Double puntaje) {
        if (puntaje == null) return null;
        if (puntaje >= 200) return "5.0";
        if (puntaje >= 153) return "4.5";
        if (puntaje >= 125) return "4.0";
        return null;
    }

    // ── Beca derechos de grado ────────────────────────────────────
    public static String getBecaDerechosGrado(Double puntaje) {
        if (puntaje == null) return null;
        if (puntaje >= 200) return "100%";
        return null;
    }

    // ── Vigencia fija desde el 9 de junio de 2026 ─────────────────
    public static String getVigencia(java.time.LocalDate fechaExamen) {
        java.time.LocalDate inicio = java.time.LocalDate.of(2026, 6, 9);
        java.time.LocalDate fin = inicio.plusYears(1);
        return fin.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}