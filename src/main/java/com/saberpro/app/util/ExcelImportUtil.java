package com.saberpro.app.util;

import com.saberpro.app.entity.Estudiante;
import com.saberpro.app.entity.ResultadoSaberPro;
import com.saberpro.app.repository.EstudianteRepository;
import com.saberpro.app.repository.ResultadoRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExcelImportUtil {

    public static List<String> importarResultados(
            MultipartFile archivo,
            EstudianteRepository estudianteRepo,
            ResultadoRepository resultadoRepo) throws Exception {

        List<String> mensajes = new ArrayList<>();

        try (InputStream is = archivo.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Fila 0 = encabezados, empezar desde fila 1
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String documento = getCellValue(row, 0);
                if (documento == null || documento.isBlank()) continue;

                Optional<Estudiante> optEst = estudianteRepo.findByNumeroDocumento(documento.trim());
                if (optEst.isEmpty()) {
                    mensajes.add("⚠ Fila " + (i+1) + ": Estudiante con documento '" + documento + "' no encontrado.");
                    continue;
                }

                Estudiante est = optEst.get();

                ResultadoSaberPro r = new ResultadoSaberPro();
                r.setEstudiante(est);

                // Fecha examen columna 1
                String fechaStr = getCellValue(row, 1);
                try {
                    r.setFechaExamen(fechaStr != null ? LocalDate.parse(fechaStr.trim()) : LocalDate.now());
                } catch (Exception e) {
                    r.setFechaExamen(LocalDate.now());
                }

                // Puntaje global columna 2
                String puntajeStr = getCellValue(row, 2);
                if (puntajeStr != null && !puntajeStr.equalsIgnoreCase("ANULADO") && !puntajeStr.isBlank()) {
                    try {
                        double puntaje = Double.parseDouble(puntajeStr.trim());
                        r.setPuntajeGlobal(puntaje);
                        r.setNivelGlobal(BeneficioUtil.calcularNivel(puntaje));
                    } catch (Exception e) {
                        r.setPuntajeGlobal(null);
                        r.setNivelGlobal("ANULADO");
                    }
                } else {
                    r.setPuntajeGlobal(null);
                    r.setNivelGlobal("ANULADO");
                }

                // Competencias columnas 3-18 (puntaje y nivel alternados)
                r.setComunicacionEscrita(getDouble(row, 3));
                r.setComunicacionEscritaNivel(getCellValue(row, 4));
                r.setRazonamientoCuantitativo(getDouble(row, 5));
                r.setRazonamientoCuantitativoNivel(getCellValue(row, 6));
                r.setLecturaCritica(getDouble(row, 7));
                r.setLecturaCriticaNivel(getCellValue(row, 8));
                r.setCompetenciasCiudadanas(getDouble(row, 9));
                r.setCompetenciasCiudadanasNivel(getCellValue(row, 10));
                r.setIngles(getDouble(row, 11));
                r.setInglesNivel(getCellValue(row, 12));
                r.setFormulacionProyectos(getDouble(row, 13));
                r.setFormulacionProyectosNivel(getCellValue(row, 14));
                r.setPensamientoCientifico(getDouble(row, 15));
                r.setPensamientoCientificoNivel(getCellValue(row, 16));
                r.setDisenoSoftware(getDouble(row, 17));
                r.setDisenoSoftwareNivel(getCellValue(row, 18));

                resultadoRepo.save(r);
                mensajes.add("✅ Fila " + (i+1) + ": Resultado importado para " + est.getNombreCompleto());
            }
        }
        return mensajes;
    }

    private static String getCellValue(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default      -> null;
        };
    }

    private static Double getDouble(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case NUMERIC -> cell.getNumericCellValue();
            case STRING  -> {
                try { yield Double.parseDouble(cell.getStringCellValue()); }
                catch (Exception e) { yield null; }
            }
            default -> null;
        };
    }
}