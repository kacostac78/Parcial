package com.saberpro.app.controller;

import com.saberpro.app.entity.*;
import com.saberpro.app.entity.PagoSaberPro.EstadoPago;
import com.saberpro.app.repository.*;
import com.saberpro.app.util.BeneficioUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Controller
@RequestMapping("/coordinador")
public class CoordinadorController {

    private final EstudianteRepository estudianteRepo;
    private final ResultadoRepository resultadoRepo;
    private final CoordinadorRepository coordinadorRepo;
    private final UsuarioRepository usuarioRepo;
    private final PagoSaberProRepository pagoRepo;
    private final DirectorRepository directorRepo;
    private final PasswordEncoder encoder;

    public CoordinadorController(EstudianteRepository estudianteRepo,
                                  ResultadoRepository resultadoRepo,
                                  CoordinadorRepository coordinadorRepo,
                                  UsuarioRepository usuarioRepo,
                                  PagoSaberProRepository pagoRepo,
                                  DirectorRepository directorRepo,
                                  PasswordEncoder encoder) {
        this.estudianteRepo = estudianteRepo;
        this.resultadoRepo = resultadoRepo;
        this.coordinadorRepo = coordinadorRepo;
        this.usuarioRepo = usuarioRepo;
        this.pagoRepo = pagoRepo;
        this.directorRepo = directorRepo;
        this.encoder = encoder;
    }

    // ── Dashboard ─────────────────────────────────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        coordinadorRepo.findByCorreo(auth.getName()).ifPresent(c ->
            model.addAttribute("coordinador", c));
        List<Estudiante> todos = estudianteRepo.findAll();
        long conResultados = todos.stream()
            .filter(e -> !resultadoRepo.findByEstudiante(e).isEmpty()).count();
        long pagosPendientes = pagoRepo.findByEstado(EstadoPago.PENDIENTE).size();
        model.addAttribute("totalEstudiantes", todos.size());
        model.addAttribute("conResultados", conResultados);
        model.addAttribute("sinResultados", todos.size() - conResultados);
        model.addAttribute("pagosPendientes", pagosPendientes);
        return "coordinador/dashboard";
    }

    // ── Gestión estudiantes ───────────────────────────────────────────────────
    @GetMapping("/estudiantes")
    public String gestionEstudiantes(Model model) {
        model.addAttribute("estudiantes", estudianteRepo.findAll());
        model.addAttribute("estudiante", new Estudiante());
        model.addAttribute("directores", directorRepo.findAll());
        return "coordinador/gestion-estudiantes";
    }

    @PostMapping("/estudiantes/crear")
    public String crearEstudiante(@ModelAttribute Estudiante estudiante,
                                   @RequestParam(required = false) Long directorId,
                                   RedirectAttributes ra) {
        if (estudianteRepo.existsByNumeroDocumento(estudiante.getNumeroDocumento())) {
            ra.addFlashAttribute("error", "Ya existe un estudiante con ese documento.");
            return "redirect:/coordinador/estudiantes";
        }
        if (directorId != null) {
            directorRepo.findById(directorId).ifPresent(estudiante::setDirector);
        }
        estudianteRepo.save(estudiante);
        usuarioRepo.save(new Usuario(
            estudiante.getCorreo(),
            encoder.encode(estudiante.getNumeroDocumento()),
            Rol.ESTUDIANTE
        ));
        ra.addFlashAttribute("mensaje", "Estudiante creado correctamente.");
        return "redirect:/coordinador/estudiantes";
    }

    @GetMapping("/estudiantes/editar/{id}")
    public String formEditar(@PathVariable Long id, Model model) {
        estudianteRepo.findById(id).ifPresent(e -> model.addAttribute("estudiante", e));
        model.addAttribute("directores", directorRepo.findAll());
        return "coordinador/editar-estudiante";
    }

    @PostMapping("/estudiantes/editar/{id}")
    public String editarEstudiante(@PathVariable Long id,
                                    @ModelAttribute Estudiante datos,
                                    @RequestParam(required = false) Long directorId,
                                    RedirectAttributes ra) {
        estudianteRepo.findById(id).ifPresent(e -> {
            e.setPrimerNombre(datos.getPrimerNombre());
            e.setSegundoNombre(datos.getSegundoNombre());
            e.setPrimerApellido(datos.getPrimerApellido());
            e.setSegundoApellido(datos.getSegundoApellido());
            e.setCorreo(datos.getCorreo());
            e.setTelefono(datos.getTelefono());
            e.setNumeroRegistro(datos.getNumeroRegistro());
            e.setSemestre(datos.getSemestre());
            e.setPrograma(datos.getPrograma());
            e.setTipoPrograma(datos.getTipoPrograma());
            if (directorId != null) {
                directorRepo.findById(directorId).ifPresent(e::setDirector);
            }
            estudianteRepo.save(e);
        });
        ra.addFlashAttribute("mensaje", "Estudiante actualizado.");
        return "redirect:/coordinador/estudiantes";
    }

    @PostMapping("/estudiantes/desactivar/{id}")
    public String desactivarEstudiante(@PathVariable Long id, RedirectAttributes ra) {
        estudianteRepo.findById(id).ifPresent(e -> {
            e.setActivo(false);
            estudianteRepo.save(e);
        });
        ra.addFlashAttribute("mensaje", "Estudiante desactivado.");
        return "redirect:/coordinador/estudiantes";
    }

    @PostMapping("/estudiantes/activar/{id}")
    public String activarEstudiante(@PathVariable Long id, RedirectAttributes ra) {
        estudianteRepo.findById(id).ifPresent(e -> {
            e.setActivo(true);
            estudianteRepo.save(e);
        });
        ra.addFlashAttribute("mensaje", "Estudiante activado.");
        return "redirect:/coordinador/estudiantes";
    }

    // ── Directores JSON para autocompletado ───────────────────────────────────
    @GetMapping("/estudiantes/directores-json")
    @ResponseBody
    public List<Map<String, Object>> directoresJson() {
        return directorRepo.findAll().stream().map(d -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id",                 d.getId());
            m.put("nombreCoordinacion", d.getNombreCoordinacion());
            m.put("nombreCompleto",     d.getNombreCompleto());
            m.put("tipoPrograma",       detectarTipo(d.getNombreCoordinacion()));
            return m;
        }).toList();
    }

    private String detectarTipo(String nombre) {
        if (nombre == null) return "Profesional";
        String n = nombre.toLowerCase();
        if (n.contains("tecnolog")) return "Tecnológico";
        return "Profesional";
    }

    // ── Calificar ─────────────────────────────────────────────────────────────
    @GetMapping("/estudiantes/calificar/{id}")
    public String formCalificar(@PathVariable Long id, Model model) {
        estudianteRepo.findById(id).ifPresent(e -> model.addAttribute("estudiante", e));
        model.addAttribute("resultado", new ResultadoSaberPro());
        return "coordinador/calificar-estudiante";
    }

    @PostMapping("/estudiantes/calificar/{id}")
    public String calificarEstudiante(@PathVariable Long id,
                                       @ModelAttribute ResultadoSaberPro resultado,
                                       RedirectAttributes ra) {
        estudianteRepo.findById(id).ifPresent(e -> {
            resultado.setEstudiante(e);
            resultado.setNivelGlobal(BeneficioUtil.calcularNivel(resultado.getPuntajeGlobal()));
            if (resultado.getFechaExamen() == null) resultado.setFechaExamen(LocalDate.now());
            resultadoRepo.save(resultado);
        });
        ra.addFlashAttribute("mensaje", "Resultado registrado correctamente.");
        return "redirect:/coordinador/estudiantes";
    }

    // ── Pagos ─────────────────────────────────────────────────────────────────
    @GetMapping("/pagos")
    public String pagos(Model model) {
        model.addAttribute("pagos", pagoRepo.findByEstado(EstadoPago.PENDIENTE));
        model.addAttribute("pagosAceptados", pagoRepo.findByEstado(EstadoPago.ACEPTADO));
        model.addAttribute("pagosRechazados", pagoRepo.findByEstado(EstadoPago.RECHAZADO));
        return "coordinador/pagos";
    }

    @PostMapping("/pagos/aceptar/{id}")
    public String aceptarPago(@PathVariable Long id,
                               Authentication auth,
                               RedirectAttributes ra) {
        pagoRepo.findById(id).ifPresent(p -> {
            p.setEstado(EstadoPago.ACEPTADO);
            p.setFechaRevision(LocalDateTime.now());
            coordinadorRepo.findByCorreo(auth.getName()).ifPresent(p::setCoordinador);
            pagoRepo.save(p);
        });
        ra.addFlashAttribute("mensaje", "Pago aceptado correctamente.");
        return "redirect:/coordinador/pagos";
    }

    @PostMapping("/pagos/rechazar/{id}")
    public String rechazarPago(@PathVariable Long id,
                                @RequestParam String motivoRechazo,
                                Authentication auth,
                                RedirectAttributes ra) {
        pagoRepo.findById(id).ifPresent(p -> {
            p.setEstado(EstadoPago.RECHAZADO);
            p.setMotivoRechazo(motivoRechazo);
            p.setFechaRevision(LocalDateTime.now());
            coordinadorRepo.findByCorreo(auth.getName()).ifPresent(p::setCoordinador);
            pagoRepo.save(p);
        });
        ra.addFlashAttribute("mensaje", "Pago rechazado.");
        return "redirect:/coordinador/pagos";
    }

    // ── Ver comprobante de pago ───────────────────────────────────────────────
    @GetMapping("/pagos/ver/{id}")
    public void verComprobante(@PathVariable Long id,
                                HttpServletResponse response) throws Exception {
        pagoRepo.findById(id).ifPresentOrElse(p -> {
            try {
                java.io.File archivo = new java.io.File(p.getRutaArchivo());
                if (!archivo.exists()) {
                    response.setStatus(404);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, () -> response.setStatus(404));
    }

    // ── Informes ──────────────────────────────────────────────────────────────
    @GetMapping("/informe-general")
    public String informeGeneral(Model model, Authentication auth) {
        coordinadorRepo.findByCorreo(auth.getName()).ifPresent(c ->
            model.addAttribute("coordinador", c));
        model.addAttribute("estudiantes", estudianteRepo.findAll());
        model.addAttribute("resultados", resultadoRepo.findAll());
        return "coordinador/informe-general";
    }

    @GetMapping("/informe-detallado")
    public String informeDetallado(@RequestParam(required = false) String programa,
                                    Model model) {
        List<ResultadoSaberPro> resultados = (programa != null && !programa.isEmpty())
            ? resultadoRepo.findByEstudiante_Programa(programa)
            : resultadoRepo.findAllByOrderByPuntajeGlobalDesc();
        model.addAttribute("resultados", resultados);
        model.addAttribute("programaFiltro", programa);
        List<String> programas = estudianteRepo.findAll().stream()
            .map(Estudiante::getPrograma).distinct().sorted().toList();
        model.addAttribute("programas", programas);
        return "coordinador/informe-detallado";
    }

    @GetMapping("/informe-beneficios")
    public String informeBeneficios(Model model) {
        List<ResultadoSaberPro> conBeneficio = resultadoRepo
            .findAllByOrderByPuntajeGlobalDesc().stream()
            .filter(r -> BeneficioUtil.tieneBeneficio(r.getPuntajeGlobal()))
            .toList();
        model.addAttribute("resultados", conBeneficio);
        return "coordinador/informe-beneficios";
    }

    @GetMapping("/requisitos-saber-pro")
    public String requisitosSaberPro(Model model) {
        model.addAttribute("estudiantes", estudianteRepo.findAll());
        model.addAttribute("pagos", pagoRepo.findAll());
        return "coordinador/requisitos-saber-pro";
    }

    // ── Importar resultados desde Excel ───────────────────────────────────────
    @GetMapping("/importar-resultados")
    public String formImportarResultados(Model model) {
        return "coordinador/importar-resultados";
    }

    @PostMapping("/importar-resultados")
    public String importarResultados(@RequestParam("archivo") MultipartFile archivo,
                                      RedirectAttributes ra) {
        try {
            List<String> mensajes = com.saberpro.app.util.ExcelImportUtil.importarResultados(
                archivo, estudianteRepo, resultadoRepo);
            ra.addFlashAttribute("mensajes", mensajes);
            ra.addFlashAttribute("mensaje", "Importación completada: "
                + mensajes.size() + " registros procesados.");
        } catch (Exception e) {
            ra.addFlashAttribute("error",
                "Error al procesar el archivo: " + e.getMessage());
        }
        return "redirect:/coordinador/importar-resultados";
    }
}