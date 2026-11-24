package com.rental.carros.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Cadastro manual de aluguel feito pelo administrador.
 * O cliente vem deste formulario; na area do cliente, o cliente
 * vem da conta autenticada.
 */
public class AluguelForm {

    @NotNull(message = "Selecione um veículo")
    private Long veiculoId;

    @NotNull(message = "Selecione um cliente")
    private Long clienteId;

    @NotNull(message = "Informe a data e hora de retirada")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime retirada;

    @NotNull(message = "Informe a data e hora de devolução")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime devolucao;

    public Long getVeiculoId() {
        return veiculoId;
    }

    public void setVeiculoId(Long veiculoId) {
        this.veiculoId = veiculoId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDateTime getRetirada() {
        return retirada;
    }

    public void setRetirada(LocalDateTime retirada) {
        this.retirada = retirada;
    }

    public LocalDateTime getDevolucao() {
        return devolucao;
    }

    public void setDevolucao(LocalDateTime devolucao) {
        this.devolucao = devolucao;
    }
}
