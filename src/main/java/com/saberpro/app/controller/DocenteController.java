package com.saberpro.app.controller;

import com.saberpro.app.entity.Docente;
import com.saberpro.app.entity.ResultadoSaberPro;
import com.saberpro.app.repository.DocenteRepository;
import com.saberpro.app.repository.ResultadoRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/docente")
public class DocenteController {

    private final DocenteRepository docenteRepo;
    private final ResultadoRepository resultadoRepo;

    public DocenteController(DocenteRepository docenteRepo,
                              ResultadoRepository resultadoRepo) {
        this.docenteRepo = docenteRepo;
        this.resultadoRepo = resultadoRepo;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        docenteRepo.findByCorreo(auth.getName())
            .ifPresent(d -> model.addAttribute("docente", d));
        return "docente/dashboard";
    }

    @GetMapping("/consultar")
    public String consultar(@RequestParam(required = false) String query,
                             Model model, Authentication auth) {
        docenteRepo.findByCorreo(auth.getName())
            .ifPresent(d -> model.addAttribute("docente", d));

        if (query != null && !query.trim().isEmpty()) {
            List<ResultadoSaberPro> resultados =
                resultadoRepo.buscarPorNombreOCedula(query.trim());
            model.addAttribute("resultados", resultados);
            model.addAttribute("query", query);
        }
        return "docente/consultar";
    }
}