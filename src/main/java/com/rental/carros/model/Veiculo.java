package com.rental.carros.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Entidade que representa um veículo disponível para aluguel.
 * Mapeada para a tabela "veiculo" no banco de dados PostgreSQL.
 */
@Entity
@Table(name = "veiculo")
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O modelo é obrigatório")
    @Size(min = 2, max = 100, message = "O modelo deve ter entre 2 e 100 caracteres")
    private String modelo;

    @NotBlank(message = "A marca é obrigatória")
    @Size(min = 2, max = 60, message = "A marca deve ter entre 2 e 60 caracteres")
    private String marca;

    @NotNull(message = "O ano é obrigatório")
    @Min(value = 1900, message = "O ano deve ser maior ou igual a 1900")
    @Max(value = 2030, message = "O ano deve ser menor ou igual a 2030")
    private Integer ano;

    @NotBlank(message = "A placa é obrigatória")
    @Size(min = 7, max = 8, message = "A placa deve ter entre 7 e 8 caracteres")
    private String placa;

    @NotNull(message = "O valor da diária é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor da diária deve ser maior que zero")
    private BigDecimal valorDiaria;

    @NotNull(message = "Informe a disponibilidade do veículo")
    private Boolean disponivel;

    // Construtores
    public Veiculo() {
    }

    public Veiculo(String modelo, String marca, Integer ano, String placa,
                   BigDecimal valorDiaria, Boolean disponivel) {
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.placa = placa;
        this.valorDiaria = valorDiaria;
        this.disponivel = disponivel;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public BigDecimal getValorDiaria() {
        return valorDiaria;
    }

    public void setValorDiaria(BigDecimal valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    @Override
    public String toString() {
        return "Veiculo{" +
                "id=" + id +
                ", modelo='" + modelo + '\'' +
                ", marca='" + marca + '\'' +
                ", ano=" + ano +
                ", placa='" + placa + '\'' +
                ", valorDiaria=" + valorDiaria +
                ", disponivel=" + disponivel +
                '}';
    }
}
