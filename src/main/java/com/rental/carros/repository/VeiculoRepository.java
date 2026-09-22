package com.rental.carros.repository;

import com.rental.carros.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório JPA para a entidade Veiculo.
 * Estende JpaRepository para herdar operações CRUD completas:
 * save(), findAll(), findById(), deleteById(), count(), etc.
 */
@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    // Operações básicas herdadas de JpaRepository são suficientes para este escopo.
    // Consultas personalizadas não são obrigatórias conforme requisito da disciplina.
}
