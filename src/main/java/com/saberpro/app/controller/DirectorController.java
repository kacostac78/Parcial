package com.saberpro.app.controller;

import com.saberpro.app.entity.*;
import com.saberpro.app.repository.*;
import com.saberpro.app.util.BeneficioUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Controller
@RequestMapping("/director")
public class DirectorController {

    private final DirectorRepository directorRepo;
    private final EstudianteRepository estudianteRepo;
    private final ResultadoRepository resultadoRepo;
    private final PagoSaberProRepository pagoRepo;

    public DirectorController(DirectorRepository directorRepo,
                               EstudianteRepository estudianteRepo,
                               ResultadoRepository resultadoRepo,
                               PagoSaberProRepository pagoRepo) {
        this.directorRepo = directorRepo;
        this.estudianteRepo = estudianteRepo;
        this.resultadoRepo = resultadoRepo;
        this.pagoRepo = pagoRepo;
    }

    private Director getDirector(Authentication auth) {
        return directorRepo.findByCorreo(auth.getName()).orElse(null);
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        Director director = getDirector(auth);
        if (director == null) return "redirect:/login";
        model.addAttribute("director", director);
        List<Estudiante> estudiantes = estudianteRepo
            .findByDirector_NombreCoordinacion(director.getNombreCoordinacion());
        long conResultados = estudiantes.stream()
            .filter(e -> !resultadoRepo.findByEstudiante(e).isEmpty()).count();
        model.addAttribute("totalEstudiantes", estudiantes.size());
        model.addAttribute("conResultados", conResultados);
        model.addAttribute("sinResultados", estudiantes.size() - conResultados);
        return "director/dashboard";
    }

    @GetMapping("/estudiantes")
    public String estudiantes(Model model, Authentication auth) {
        Director director = getDirector(auth);
        if (director == null) return "redirect:/login";
        model.addAttribute("director", director);
        model.addAttribute("estudiantes",
            estudianteRepo.findByDirector_NombreCoordinacion(director.getNombreCoordinacion()));
        return "director/estudiantes";
    }

    @GetMapping("/informe-general")
    public String informeGeneral(Model model, Authentication auth) {
        Director director = getDirector(auth);
        if (director == null) return "redirect:/login";
        List<Estudiante> estudiantes = estudianteRepo
            .findByDirector_NombreCoordinacion(director.getNombreCoordinacion());
        model.addAttribute("director", director);
        model.addAttribute("estudiantes", estudiantes);
        model.addAttribute("resultados", resultadoRepo.findAll());
        return "director/informe-general";
    }

    @GetMapping("/informe-detallado")
    public String informeDetallado(Model model, Authentication auth) {
        Director director = getDirector(auth);
        if (director == null) return "redirect:/login";
        List<ResultadoSaberPro> resultados = resultadoRepo
            .findByEstudiante_Director_NombreCoordinacion(director.getNombreCoordinacion());
        model.addAttribute("director", director);
        model.addAttribute("resultados", resultados);
        return "director/informe-detallado";
    }

    @GetMapping("/informe-beneficios")
    public String informeBeneficios(Model model, Authentication auth) {
        Director director = getDirector(auth);
        if (director == null) return "redirect:/login";
        List<ResultadoSaberPro> conBeneficio = resultadoRepo
            .findByEstudiante_Director_NombreCoordinacion(director.getNombreCoordinacion())
            .stream()
            .filter(r -> BeneficioUtil.tieneBeneficio(r.getPuntajeGlobal()))
            .toList();
        model.addAttribute("director", director);
        model.addAttribute("resultados", conBeneficio);
        return "director/informe-beneficios";
    }

    @GetMapping("/requisitos-saber-pro")
    public String requisitosSaberPro(Model model, Authentication auth) {
        Director director = getDirector(auth);
        if (director == null) return "redirect:/login";
        List<Estudiante> estudiantes = estudianteRepo
            .findByDirector_NombreCoordinacion(director.getNombreCoordinacion());
        model.addAttribute("director", director);
        model.addAttribute("estudiantes", estudiantes);
        model.addAttribute("pagos", pagoRepo.findAll());
        return "director/requisitos-saber-pro";
    }
}