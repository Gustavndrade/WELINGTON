package com.rental.carros.controller;

import com.rental.carros.model.Cliente;
import com.rental.carros.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/clientes")
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "lista-clientes";
    }

    @GetMapping("/clientes/novo")
    public String novo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "form-cliente";
    }

    @PostMapping("/clientes/salvar")
    public String salvar(@Valid Cliente cliente, BindingResult resultado,
                         RedirectAttributes attrs) {
        if (resultado.hasErrors()) {
            return "form-cliente";
        }
        try {
            clienteService.salvar(cliente);
        } catch (IllegalStateException e) {
            resultado.rejectValue("email", "", e.getMessage());
            return "form-cliente";
        }
        attrs.addFlashAttribute("mensagemSucesso", "Cliente salvo com sucesso!");
        return "redirect:/clientes";
    }

    @GetMapping("/clientes/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id);
        if (cliente == null) {
            return "redirect:/clientes";
        }
        model.addAttribute("cliente", cliente);
        return "form-cliente";
    }

    @PostMapping("/clientes/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes attrs) {
        try {
            clienteService.excluir(id);
            attrs.addFlashAttribute("mensagemSucesso", "Cliente excluído com sucesso!");
        } catch (DataIntegrityViolationException e) {
            attrs.addFlashAttribute("mensagemErro",
                    "Não é possível excluir um cliente vinculado a uma conta ou a um aluguel.");
        }
        return "redirect:/clientes";
    }
}
