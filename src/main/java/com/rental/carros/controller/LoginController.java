package com.rental.carros.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Apenas exibe a pagina; o Spring Security valida o POST /login.
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
