package com.rental.carros.controller;

import com.rental.carros.model.Veiculo;
import com.rental.carros.service.VeiculoService;
import com.rental.carros.dto.RentalRequest;
import com.rental.carros.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Segue a arquitetura: Controller → Service → Repository → BD
 */
@Controller
@RequestMapping("/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService veiculoService;

    @Autowired
    private RentalService rentalService;

    /**
     * GET /veiculos — Lista todos os veículos cadastrados.
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("veiculos", veiculoService.listarTodos());
        return "lista";
    }

    /**
     * GET /veiculos/novo — Exibe formulário de cadastro de novo veículo.
     */
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("veiculo", new Veiculo());
        return "formulario";
    }

    /**
     * POST /veiculos — Salva um novo veículo após validação.
     * Se houver erros de validação, reexibe o formulário com os dados preenchidos.
     */
    @PostMapping
    public String salvar(@Valid Veiculo veiculo, BindingResult resultado, RedirectAttributes attrs) {
        if (resultado.hasErrors()) {
            return "formulario";
        }
        veiculoService.salvar(veiculo);
        attrs.addFlashAttribute("mensagemSucesso", "Veículo cadastrado com sucesso!");
        return "redirect:/veiculos";
    }

    /**
     * GET /veiculos/{id}/editar — Exibe formulário pré-preenchido para edição.
     */
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("veiculo", veiculoService.buscarPorId(id));
        return "formulario";
    }

    /**
     * POST /veiculos/{id} — Atualiza um veículo existente após validação.
     */
    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                            @Valid Veiculo veiculo,
                            BindingResult resultado,
                            RedirectAttributes attrs) {
        if (resultado.hasErrors()) {
            veiculo.setId(id); // preservar id para o th:action do formulário
            return "formulario";
        }
        veiculo.setId(id);
        veiculoService.salvar(veiculo);
        attrs.addFlashAttribute("mensagemSucesso", "Veículo atualizado com sucesso!");
        return "redirect:/veiculos";
    }

    /**
     * POST /veiculos/{id}/excluir — Remove um veículo pelo id.
     */
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes attrs) {
        veiculoService.excluir(id);
        attrs.addFlashAttribute("mensagemSucesso", "Veículo excluído com sucesso!");
        return "redirect:/veiculos";
    }

    /**
     * GET /veiculos/{id}/alugar — Exibe formulário para alugar o veículo.
     */
    @GetMapping("/{id}/alugar")
    public String formAlugar(@PathVariable Long id, Model model) {
        Veiculo veiculo = veiculoService.buscarPorId(id);
        if (!Boolean.TRUE.equals(veiculo.getDisponivel())) {
            return "redirect:/veiculos";
        }
        model.addAttribute("veiculo", veiculo);
        RentalRequest request = new RentalRequest();
        request.setCarId(id);
        model.addAttribute("rentalRequest", request);
        return "alugar";
    }

    /**
     * POST /veiculos/{id}/alugar — Processa o aluguel do veículo.
     */
    @PostMapping("/{id}/alugar")
    public String processarAluguel(@PathVariable Long id, 
                                   @Valid RentalRequest rentalRequest, 
                                   BindingResult resultado, 
                                   Model model,
                                   RedirectAttributes attrs) {
        if (resultado.hasErrors()) {
            model.addAttribute("veiculo", veiculoService.buscarPorId(id));
            return "alugar";
        }
        try {
            rentalRequest.setCarId(id);
            rentalService.rentCar(rentalRequest);
            attrs.addFlashAttribute("mensagemSucesso", "Veículo alugado com sucesso!");
            return "redirect:/veiculos";
        } catch (IllegalArgumentException e) {
            model.addAttribute("veiculo", veiculoService.buscarPorId(id));
            model.addAttribute("erro", e.getMessage()); // We can show this in the view, but not strictly needed if we just return
            return "alugar";
        }
    }
}
