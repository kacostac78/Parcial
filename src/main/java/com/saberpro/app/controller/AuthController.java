package com.saberpro.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

	@GetMapping("/login")
	public String login(
	        @RequestParam(value = "error", required = false) String error,
	        @RequestParam(value = "logout", required = false) String logout,
	        Model model) {

	    if (error != null)  model.addAttribute("error", "Correo o contraseña incorrectos.");
	    // Quitamos el mensaje de logout para no interferir con el layout
	    // if (logout != null) model.addAttribute("mensaje", "Sesión cerrada correctamente.");

	    return "login";
	}

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }
}