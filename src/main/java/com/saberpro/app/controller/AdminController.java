package com.saberpro.app.controller;

import com.saberpro.app.entity.*;
import com.saberpro.app.repository.*;
import com.saberpro.app.util.BeneficioUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CoordinadorRepository coordinadorRepo;
    private final UsuarioRepository usuarioRepo;
    private final DirectorRepository directorRepo;
    private final DocenteRepository docenteRepo;
    private final EstudianteRepository estudianteRepo;
    private final ResultadoRepository resultadoRepo;
    private final PasswordEncoder encoder;

    public AdminController(CoordinadorRepository coordinadorRepo,
                           UsuarioRepository usuarioRepo,
                           DirectorRepository directorRepo,
                           DocenteRepository docenteRepo,
                           EstudianteRepository estudianteRepo,
                           ResultadoRepository resultadoRepo,
                           PasswordEncoder encoder) {
        this.coordinadorRepo = coordinadorRepo;
        this.usuarioRepo = usuarioRepo;
        this.directorRepo = directorRepo;
        this.docenteRepo = docenteRepo;
        this.estudianteRepo = estudianteRepo;
        this.resultadoRepo = resultadoRepo;
        this.encoder = encoder;
    }

    // ── Dashboard ─────────────────────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalCoordinadores", coordinadorRepo.count());
        model.addAttribute("totalDirectores", directorRepo.count());
        model.addAttribute("totalDocentes", docenteRepo.count());
        model.addAttribute("totalEstudiantes", estudianteRepo.count());
        return "admin/dashboard";
    }

    // ── CRUD Coordinadores ────────────────────────────────────────
    @GetMapping("/coordinadores")
    public String coordinadores(Model model) {
        model.addAttribute("coordinadores", coordinadorRepo.findAll());
        model.addAttribute("coordinador", new Coordinador());
        return "admin/coordinadores";
    }

    @PostMapping("/coordinador/crear")
    public String crearCoordinador(@ModelAttribute Coordinador coordinador,
                                   @RequestParam String password,
                                   RedirectAttributes ra) {
        if (coordinadorRepo.existsByCorreo(coordinador.getCorreo())) {
            ra.addFlashAttribute("error", "Ya existe un coordinador con ese correo.");
            return "redirect:/admin/coordinadores";
        }
        coordinadorRepo.save(coordinador);
        usuarioRepo.save(new Usuario(coordinador.getCorreo(), encoder.encode(password), Rol.COORDINADOR));
        ra.addFlashAttribute("mensaje", "Coordinador creado correctamente.");
        return "redirect:/admin/coordinadores";
    }

    @PostMapping("/coordinador/desactivar/{id}")
    public String desactivarCoordinador(@PathVariable Long id, RedirectAttributes ra) {
        coordinadorRepo.findById(id).ifPresent(c -> {
            c.setActivo(false);
            coordinadorRepo.save(c);
            usuarioRepo.findByCorreo(c.getCorreo()).ifPresent(u -> { u.setActivo(false); usuarioRepo.save(u); });
        });
        ra.addFlashAttribute("mensaje", "Coordinador desactivado.");
        return "redirect:/admin/coordinadores";
    }

    @PostMapping("/coordinador/activar/{id}")
    public String activarCoordinador(@PathVariable Long id, RedirectAttributes ra) {
        coordinadorRepo.findById(id).ifPresent(c -> {
            c.setActivo(true);
            coordinadorRepo.save(c);
            usuarioRepo.findByCorreo(c.getCorreo()).ifPresent(u -> { u.setActivo(true); usuarioRepo.save(u); });
        });
        ra.addFlashAttribute("mensaje", "Coordinador activado.");
        return "redirect:/admin/coordinadores";
    }

    // ── CRUD Directores ───────────────────────────────────────────
    @GetMapping("/directores")
    public String directores(Model model) {
        model.addAttribute("directores", directorRepo.findAll());
        model.addAttribute("director", new Director());
        return "admin/directores";
    }

    @PostMapping("/director/crear")
    public String crearDirector(@ModelAttribute Director director,
                                @RequestParam String password,
                                RedirectAttributes ra) {
        if (directorRepo.existsByCorreo(director.getCorreo())) {
            ra.addFlashAttribute("error", "Ya existe un director con ese correo.");
            return "redirect:/admin/directores";
        }
        directorRepo.save(director);
        usuarioRepo.save(new Usuario(director.getCorreo(), encoder.encode(password), Rol.DIRECTOR));
        ra.addFlashAttribute("mensaje", "Director creado correctamente.");
        return "redirect:/admin/directores";
    }

    @PostMapping("/director/desactivar/{id}")
    public String desactivarDirector(@PathVariable Long id, RedirectAttributes ra) {
        directorRepo.findById(id).ifPresent(d -> {
            d.setActivo(false);
            directorRepo.save(d);
            usuarioRepo.findByCorreo(d.getCorreo()).ifPresent(u -> { u.setActivo(false); usuarioRepo.save(u); });
        });
        ra.addFlashAttribute("mensaje", "Director desactivado.");
        return "redirect:/admin/directores";
    }

    @PostMapping("/director/activar/{id}")
    public String activarDirector(@PathVariable Long id, RedirectAttributes ra) {
        directorRepo.findById(id).ifPresent(d -> {
            d.setActivo(true);
            directorRepo.save(d);
            usuarioRepo.findByCorreo(d.getCorreo()).ifPresent(u -> { u.setActivo(true); usuarioRepo.save(u); });
        });
        ra.addFlashAttribute("mensaje", "Director activado.");
        return "redirect:/admin/directores";
    }

    // ── CRUD Docentes ─────────────────────────────────────────────
    @GetMapping("/docentes")
    public String docentes(Model model) {
        model.addAttribute("docentes", docenteRepo.findAll());
        model.addAttribute("docente", new Docente());
        return "admin/docentes";
    }

    @PostMapping("/docente/crear")
    public String crearDocente(@ModelAttribute Docente docente,
                               @RequestParam String password,
                               RedirectAttributes ra) {
        if (docenteRepo.existsByCorreo(docente.getCorreo())) {
            ra.addFlashAttribute("error", "Ya existe un docente con ese correo.");
            return "redirect:/admin/docentes";
        }
        docenteRepo.save(docente);
        usuarioRepo.save(new Usuario(docente.getCorreo(), encoder.encode(password), Rol.DOCENTE));
        ra.addFlashAttribute("mensaje", "Docente creado correctamente.");
        return "redirect:/admin/docentes";
    }

    @PostMapping("/docente/desactivar/{id}")
    public String desactivarDocente(@PathVariable Long id, RedirectAttributes ra) {
        docenteRepo.findById(id).ifPresent(d -> {
            d.setActivo(false);
            docenteRepo.save(d);
            usuarioRepo.findByCorreo(d.getCorreo()).ifPresent(u -> { u.setActivo(false); usuarioRepo.save(u); });
        });
        ra.addFlashAttribute("mensaje", "Docente desactivado.");
        return "redirect:/admin/docentes";
    }

    @PostMapping("/docente/activar/{id}")
    public String activarDocente(@PathVariable Long id, RedirectAttributes ra) {
        docenteRepo.findById(id).ifPresent(d -> {
            d.setActivo(true);
            docenteRepo.save(d);
            usuarioRepo.findByCorreo(d.getCorreo()).ifPresent(u -> { u.setActivo(true); usuarioRepo.save(u); });
        });
        ra.addFlashAttribute("mensaje", "Docente activado.");
        return "redirect:/admin/docentes";
    }

    // ── Informes Admin ────────────────────────────────────────────
    @GetMapping("/informe-general")
    public String informeGeneral(Model model) {
        model.addAttribute("estudiantes", estudianteRepo.findAll());
        model.addAttribute("resultados", resultadoRepo.findAll());
        return "admin/informe-general";
    }

    @GetMapping("/informe-detallado")
    public String informeDetallado(@RequestParam(required = false) String programa, Model model) {
        List<ResultadoSaberPro> resultados = (programa != null && !programa.isEmpty())
            ? resultadoRepo.findByEstudiante_Programa(programa)
            : resultadoRepo.findAllByOrderByPuntajeGlobalDesc();
        model.addAttribute("resultados", resultados);
        model.addAttribute("programaFiltro", programa);
        List<String> programas = estudianteRepo.findAll().stream()
            .map(Estudiante::getPrograma).distinct().sorted().toList();
        model.addAttribute("programas", programas);
        return "admin/informe-detallado";
    }

    @GetMapping("/informe-beneficios")
    public String informeBeneficios(Model model) {
        List<ResultadoSaberPro> conBeneficio = resultadoRepo
            .findAllByOrderByPuntajeGlobalDesc().stream()
            .filter(r -> BeneficioUtil.tieneBeneficio(r.getPuntajeGlobal()))
            .toList();
        model.addAttribute("resultados", conBeneficio);
        return "admin/informe-beneficios";
    }

    @GetMapping("/requisitos-saber-pro")
    public String requisitosSaberPro(Model model) {
        model.addAttribute("estudiantes", estudianteRepo.findAll());
        return "admin/requisitos-saber-pro";
    }
}