package com.rental.carros.dto;

public class OcupacaoVeiculo {

    private final String descricao;
    private final String placa;
    private final boolean disponivel;
    private final boolean aluguelAtivo;

    public OcupacaoVeiculo(String descricao, String placa,
            boolean disponivel, boolean aluguelAtivo) {
        this.descricao = descricao;
        this.placa = placa;
        this.disponivel = disponivel;
        this.aluguelAtivo = aluguelAtivo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getPlaca() {
        return placa;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public boolean isAluguelAtivo() {
        return aluguelAtivo;
    }
}
