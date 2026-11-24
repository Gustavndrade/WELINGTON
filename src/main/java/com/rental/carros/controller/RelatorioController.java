package com.rental.carros.controller;

import com.rental.carros.dto.OcupacaoVeiculo;
import com.rental.carros.service.ClienteService;
import com.rental.carros.service.RentalService;
import com.rental.carros.service.VeiculoService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RelatorioController {

    private final VeiculoService veiculoService;
    private final ClienteService clienteService;
    private final RentalService rentalService;

    public RelatorioController(VeiculoService veiculoService,
                               ClienteService clienteService,
                               RentalService rentalService) {
        this.veiculoService = veiculoService;
        this.clienteService = clienteService;
        this.rentalService = rentalService;
    }

    @GetMapping("/relatorios")
    public String relatorioGeral(Model model) {
        var veiculos = veiculoService.listarTodos();
        List<OcupacaoVeiculo> ocupacoes = veiculos.stream()
                .map(veiculo -> new OcupacaoVeiculo(
                        veiculo.getMarca() + " " + veiculo.getModelo(),
                        veiculo.getPlaca(),
                        Boolean.TRUE.equals(veiculo.getDisponivel()),
                        rentalService.possuiAluguelAtivo(veiculo.getId())))
                .toList();

        model.addAttribute("totalVeiculos", veiculos.size());
        model.addAttribute("totalClientes", clienteService.listarTodos().size());
        model.addAttribute("alugueisAtivos", rentalService.contarAtivos());
        model.addAttribute("totalHistorico", rentalService.contarTodos());
        model.addAttribute("ocupacoes", ocupacoes);
        return "relatorio";
    }
}
