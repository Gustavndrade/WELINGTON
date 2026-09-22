package com.rental.carros.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller de redirecionamento da raiz para a listagem de veículos.
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String home() {
        return "redirect:/veiculos";
    }
}
