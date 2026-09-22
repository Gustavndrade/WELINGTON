package com.rental.carros.service;

import com.rental.carros.model.Veiculo;
import com.rental.carros.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Camada de serviço para operações de negócio relacionadas à entidade Veiculo.
 * Intermedia o Controller e o Repository, encapsulando a lógica de negócio.
 */
@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository veiculoRepository;

    /**
     * Salva um veículo no banco de dados (criação ou atualização).
     *
     * @param veiculo o veículo a ser persistido
     * @return o veículo salvo com id gerado
     */
    public Veiculo salvar(Veiculo veiculo) {
        return veiculoRepository.save(veiculo);
    }

    /**
     * Retorna a lista completa de veículos cadastrados.
     *
     * @return lista de todos os veículos
     */
    public List<Veiculo> listarTodos() {
        return veiculoRepository.findAll();
    }

    /**
     * Busca um veículo pelo seu identificador.
     *
     * @param id identificador do veículo
     * @return o veículo encontrado
     * @throws RuntimeException se nenhum veículo for encontrado com o id informado
     */
    public Veiculo buscarPorId(Long id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com id: " + id));
    }

    /**
     * Exclui um veículo pelo seu identificador.
     *
     * @param id identificador do veículo a ser removido
     */
    public void excluir(Long id) {
        veiculoRepository.deleteById(id);
    }
}
