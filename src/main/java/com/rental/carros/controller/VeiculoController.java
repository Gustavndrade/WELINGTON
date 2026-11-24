package com.rental.carros.controller;

import com.rental.carros.model.Veiculo;
import com.rental.carros.service.VeiculoService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsável por gerenciar as requisições HTTP
 * relacionadas à entidade Veiculo.
 * Segue a arquitetura: Controller → Service → Repository → BD.
 * O aluguel feito pelo cliente fica em AluguelController.
 */
@Controller
@RequestMapping("/veiculos")
public class VeiculoController {

    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("veiculos", veiculoService.listarTodos());
        return "lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("veiculo", new Veiculo());
        return "formulario";
    }

    @PostMapping
    public String salvar(@Valid Veiculo veiculo, BindingResult resultado, RedirectAttributes attrs) {
        if (resultado.hasErrors()) {
            return "formulario";
        }
        veiculoService.salvar(veiculo);
        attrs.addFlashAttribute("mensagemSucesso", "Veículo cadastrado com sucesso!");
        return "redirect:/veiculos";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("veiculo", veiculoService.buscarPorId(id));
        return "formulario";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                            @Valid Veiculo veiculo,
                            BindingResult resultado,
                            RedirectAttributes attrs) {
        if (resultado.hasErrors()) {
            veiculo.setId(id);
            return "formulario";
        }
        veiculo.setId(id);
        veiculoService.salvar(veiculo);
        attrs.addFlashAttribute("mensagemSucesso", "Veículo atualizado com sucesso!");
        return "redirect:/veiculos";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes attrs) {
        try {
            veiculoService.excluir(id);
            attrs.addFlashAttribute("mensagemSucesso", "Veículo excluído com sucesso!");
        } catch (DataIntegrityViolationException e) {
            attrs.addFlashAttribute("mensagemErro",
                    "Não é possível excluir um veículo que possui aluguéis.");
        }
        return "redirect:/veiculos";
    }
}
