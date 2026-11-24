package com.rental.carros.controller;

import com.rental.carros.dto.AluguelForm;
import com.rental.carros.model.Veiculo;
import com.rental.carros.service.ClienteService;
import com.rental.carros.service.RentalService;
import com.rental.carros.service.VeiculoService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AluguelController {

    private final RentalService rentalService;
    private final VeiculoService veiculoService;
    private final ClienteService clienteService;

    public AluguelController(RentalService rentalService,
                             VeiculoService veiculoService,
                             ClienteService clienteService) {
        this.rentalService = rentalService;
        this.veiculoService = veiculoService;
        this.clienteService = clienteService;
    }

    @GetMapping("/alugueis")
    public String listar(Model model) {
        model.addAttribute("alugueis", rentalService.listarTodos());
        return "lista-alugueis";
    }

    @GetMapping("/alugueis/novo")
    public String novo(Model model) {
        if (!model.containsAttribute("aluguel")) {
            model.addAttribute("aluguel", new AluguelForm());
        }
        carregarListas(model);
        return "form-aluguel";
    }

    @PostMapping("/alugueis/salvar")
    public String salvar(@Valid AluguelForm aluguel, BindingResult resultado,
                         Model model, RedirectAttributes attrs) {
        if (resultado.hasErrors()) {
            model.addAttribute("aluguel", aluguel);
            carregarListas(model);
            return "form-aluguel";
        }
        try {
            rentalService.alugar(
                    aluguel.getVeiculoId(),
                    aluguel.getClienteId(),
                    aluguel.getRetirada(),
                    aluguel.getDevolucao());
            attrs.addFlashAttribute("mensagemSucesso", "Aluguel registrado com sucesso!");
            return "redirect:/alugueis";
        } catch (IllegalStateException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("aluguel", aluguel);
            carregarListas(model);
            return "form-aluguel";
        }
    }

    @PostMapping("/alugueis/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes attrs) {
        rentalService.excluir(id);
        attrs.addFlashAttribute("mensagemSucesso", "Aluguel excluído com sucesso!");
        return "redirect:/alugueis";
    }

    @GetMapping("/veiculos/{id}/alugueis")
    public String alugueisDoVeiculo(@PathVariable Long id, Model model) {
        Veiculo veiculo;
        try {
            veiculo = veiculoService.buscarPorId(id);
        } catch (RuntimeException e) {
            return "redirect:/veiculos";
        }
        model.addAttribute("veiculo", veiculo);
        model.addAttribute("alugueis", rentalService.listarPorVeiculo(id));
        return "alugueis-veiculo";
    }

    @GetMapping("/catalogo")
    public String catalogo(Model model) {
        model.addAttribute("veiculos", rentalService.listarCatalogo());
        return "catalogo";
    }

    @PostMapping("/alugueis/meu")
    public String alugarMinhaConta(
            @RequestParam Long veiculoId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime retirada,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime devolucao,
            Authentication authentication,
            RedirectAttributes redirect) {
        try {
            rentalService.alugarUsuario(
                    veiculoId, retirada, devolucao, authentication.getName());
            redirect.addFlashAttribute("sucesso", "Aluguel realizado!");
            return "redirect:/meus-alugueis";
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
            return "redirect:/catalogo";
        }
    }

    @GetMapping("/meus-alugueis")
    public String meusAlugueis(Authentication auth, Model model) {
        model.addAttribute("alugueis", rentalService.listarDoUsuario(auth.getName()));
        return "meus-alugueis";
    }

    @PostMapping("/meus-alugueis/{id}/cancelar")
    public String cancelarMeuAluguel(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirect) {
        try {
            rentalService.cancelarMeuAluguel(id, authentication.getName());
            redirect.addFlashAttribute("sucesso", "Aluguel cancelado com sucesso.");
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/meus-alugueis";
    }

    private void carregarListas(Model model) {
        model.addAttribute("veiculos", veiculoService.listarTodos());
        model.addAttribute("clientes", clienteService.listarTodos());
    }
}
