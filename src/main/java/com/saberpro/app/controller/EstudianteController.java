package com.saberpro.app.controller;

import com.saberpro.app.entity.*;
import com.saberpro.app.entity.PagoSaberPro.EstadoPago;
import com.saberpro.app.repository.*;
import com.saberpro.app.util.BeneficioUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    private final EstudianteRepository estudianteRepo;
    private final ResultadoRepository resultadoRepo;
    private final PagoSaberProRepository pagoRepo;

    public EstudianteController(EstudianteRepository estudianteRepo,
                                 ResultadoRepository resultadoRepo,
                                 PagoSaberProRepository pagoRepo) {
        this.estudianteRepo = estudianteRepo;
        this.resultadoRepo = resultadoRepo;
        this.pagoRepo = pagoRepo;
    }

    private Estudiante getEstudiante(Authentication auth) {
        return estudianteRepo.findByCorreo(auth.getName()).orElse(null);
    }

    // ── Dashboard ─────────────────────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        Estudiante est = getEstudiante(auth);
        if (est == null) return "redirect:/login";
        model.addAttribute("estudiante", est);

        Optional<ResultadoSaberPro> ultimo =
            resultadoRepo.findTopByEstudianteOrderByFechaExamenDesc(est);
        ultimo.ifPresent(r -> {
            model.addAttribute("ultimoResultado", r);
            model.addAttribute("nivel",
                r.getPuntajeGlobal() != null
                    ? BeneficioUtil.calcularNivel(r.getPuntajeGlobal())
                    : "ANULADO");
        });

        pagoRepo.findTopByEstudianteOrderByFechaSubidaDesc(est)
            .ifPresent(p -> model.addAttribute("ultimoPago", p));

        return "estudiante/dashboard";
    }

    // ── Datos personales ──────────────────────────────────────────
    @GetMapping("/datos-personales")
    public String datosPersonales(Model model, Authentication auth) {
        Estudiante est = getEstudiante(auth);
        if (est == null) return "redirect:/login";
        model.addAttribute("estudiante", est);
        return "estudiante/datos-personales";
    }

    // ── Último resultado ──────────────────────────────────────────
    @GetMapping("/ultimo-resultado")
    public String ultimoResultado(Model model, Authentication auth) {
        Estudiante est = getEstudiante(auth);
        if (est == null) return "redirect:/login";
        resultadoRepo.findTopByEstudianteOrderByFechaExamenDesc(est).ifPresent(r -> {
            model.addAttribute("resultado", r);
            model.addAttribute("nivel",
                r.getPuntajeGlobal() != null
                    ? BeneficioUtil.calcularNivel(r.getPuntajeGlobal())
                    : "ANULADO");
        });
        model.addAttribute("estudiante", est);
        return "estudiante/ultimo-resultado";
    }

    // ── Todos los resultados ──────────────────────────────────────
    @GetMapping("/todos-resultados")
    public String todosResultados(Model model, Authentication auth) {
        Estudiante est = getEstudiante(auth);
        if (est == null) return "redirect:/login";
        model.addAttribute("estudiante", est);
        model.addAttribute("resultados",
            resultadoRepo.findByEstudianteOrderByFechaExamenDesc(est));
        return "estudiante/todos-resultados";
    }

    // ── Beneficios ────────────────────────────────────────────────
    @GetMapping("/beneficios")
    public String beneficios(Model model, Authentication auth) {
        Estudiante est = getEstudiante(auth);
        if (est == null) return "redirect:/login";

        resultadoRepo.findTopByEstudianteOrderByFechaExamenDesc(est).ifPresent(r -> {
            model.addAttribute("resultado", r);
            model.addAttribute("beneficio",
                BeneficioUtil.calcularBeneficio(r.getPuntajeGlobal()));
            model.addAttribute("nivel",
                r.getPuntajeGlobal() != null
                    ? BeneficioUtil.calcularNivel(r.getPuntajeGlobal())
                    : "ANULADO");
            model.addAttribute("notaGrado",
                BeneficioUtil.getNotaTrabajoGrado(r.getPuntajeGlobal()));
            model.addAttribute("beca",
                BeneficioUtil.getBecaDerechosGrado(r.getPuntajeGlobal()));
            model.addAttribute("vigencia",
                BeneficioUtil.getVigencia(r.getFechaExamen()));
            model.addAttribute("tieneBeneficio",
                BeneficioUtil.tieneBeneficio(r.getPuntajeGlobal()));
        });

        model.addAttribute("estudiante", est);
        return "estudiante/beneficios";
    }

    // ── Requisitos Saber Pro ──────────────────────────────────────
    @GetMapping("/requisitos-saber-pro")
    public String requisitosSaberPro(Model model, Authentication auth) {
        Estudiante est = getEstudiante(auth);
        if (est == null) return "redirect:/login";
        model.addAttribute("estudiante", est);
        pagoRepo.findTopByEstudianteOrderByFechaSubidaDesc(est)
            .ifPresent(p -> model.addAttribute("ultimoPago", p));
        return "estudiante/requisitos-saber-pro";
    }

    // ── Pago ──────────────────────────────────────────────────────
    @GetMapping("/pago")
    public String formPago(Model model, Authentication auth) {
        Estudiante est = getEstudiante(auth);
        if (est == null) return "redirect:/login";
        model.addAttribute("estudiante", est);

        List<PagoSaberPro> pagos =
            pagoRepo.findByEstudianteOrderByFechaSubidaDesc(est);
        model.addAttribute("pagos", pagos);

        boolean puedeSubir = true;

        if (!pagos.isEmpty()) {
            PagoSaberPro ultimo = pagos.get(0);
            model.addAttribute("ultimoPago", ultimo);
            EstadoPago estado = ultimo.getEstado();

            if (estado == EstadoPago.PENDIENTE) {
                puedeSubir = false;

            } else if (estado == EstadoPago.ACEPTADO) {
                Optional<ResultadoSaberPro> ultimoRes =
                    resultadoRepo.findTopByEstudianteOrderByFechaExamenDesc(est);

                if (ultimoRes.isEmpty()) {
                    // Pago aceptado sin resultado aún — esperar
                    puedeSubir = false;
                } else {
                    // Solo puede volver a pagar si el resultado fue ANULADO
                    String nivel = ultimoRes.get().getNivelGlobal();
                    puedeSubir = "ANULADO".equals(nivel);
                }

            } else if (estado == EstadoPago.RECHAZADO) {
                puedeSubir = true;
            }
        }

        model.addAttribute("puedeSubir", puedeSubir);
        return "estudiante/pago";
    }

    @PostMapping("/pago/subir")
    public String subirPago(@RequestParam("archivo") MultipartFile archivo,
                             Authentication auth,
                             RedirectAttributes ra) {
        Estudiante est = getEstudiante(auth);
        if (est == null) return "redirect:/login";

        try {
            String uploadsDir = System.getProperty("user.home") + "/saberpro-uploads/";
            Files.createDirectories(Paths.get(uploadsDir));
            String nombreArchivo = System.currentTimeMillis() + "_"
                + archivo.getOriginalFilename();
            Path destino = Paths.get(uploadsDir + nombreArchivo);
            Files.copy(archivo.getInputStream(), destino,
                StandardCopyOption.REPLACE_EXISTING);

            PagoSaberPro pago = new PagoSaberPro();
            pago.setEstudiante(est);
            pago.setNombreArchivo(archivo.getOriginalFilename());
            pago.setRutaArchivo(uploadsDir + nombreArchivo);
            pago.setEstado(EstadoPago.PENDIENTE);
            pago.setFechaSubida(LocalDateTime.now());
            pagoRepo.save(pago);

            ra.addFlashAttribute("mensaje",
                "Comprobante subido correctamente. En espera de revisión del coordinador.");
        } catch (IOException e) {
            ra.addFlashAttribute("error",
                "Error al subir el archivo. Intenta de nuevo.");
        }
        return "redirect:/estudiante/pago";
    }

    // ── Ver comprobante ───────────────────────────────────────────
    @GetMapping("/pago/ver/{id}")
    public void verComprobante(@PathVariable Long id,
                                Authentication auth,
                                HttpServletResponse response) throws Exception {
        Estudiante est = getEstudiante(auth);
        if (est == null) {
            response.sendRedirect("/login");
            return;
        }

        Optional<PagoSaberPro> pagoOpt = pagoRepo.findById(id);
        if (pagoOpt.isEmpty()) {
            response.setStatus(404);
            return;
        }

        PagoSaberPro p = pagoOpt.get();

        // Verificar que el pago pertenece al estudiante
        if (!p.getEstudiante().getId().equals(est.getId())) {
            response.setStatus(403);
            return;
        }

        File archivo = new File(p.getRutaArchivo());
        if (!archivo.exists()) {
            response.setStatus(404);
            response.getWriter().write("Archivo no encontrado.");
            return;
        }

        String nombre = p.getNombreArchivo().toLowerCase();
        if (nombre.endsWith(".pdf")) {
            response.setContentType("application/pdf");
        } else if (nombre.endsWith(".png")) {
            response.setContentType("image/png");
        } else if (nombre.endsWith(".jpg") || nombre.endsWith(".jpeg")) {
            response.setContentType("image/jpeg");
        } else {
            response.setContentType("application/octet-stream");
        }

        response.setHeader("Content-Disposition",
            "inline; filename=\"" + p.getNombreArchivo() + "\"");
        Files.copy(archivo.toPath(), response.getOutputStream());
        response.getOutputStream().flush();
    }
}