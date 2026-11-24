package com.rental.carros.service;

import com.rental.carros.model.Cliente;
import com.rental.carros.model.RentalRecord;
import com.rental.carros.model.StatusAluguel;
import com.rental.carros.model.Usuario;
import com.rental.carros.model.Veiculo;
import com.rental.carros.repository.ClienteRepository;
import com.rental.carros.repository.RentalRecordRepository;
import com.rental.carros.repository.UsuarioRepository;
import com.rental.carros.repository.VeiculoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RentalService {

    private final VeiculoRepository veiculoRepository;
    private final ClienteRepository clienteRepository;
    private final RentalRecordRepository rentalRecordRepository;
    private final UsuarioRepository usuarioRepository;

    public RentalService(VeiculoRepository veiculoRepository,
                         ClienteRepository clienteRepository,
                         RentalRecordRepository rentalRecordRepository,
                         UsuarioRepository usuarioRepository) {
        this.veiculoRepository = veiculoRepository;
        this.clienteRepository = clienteRepository;
        this.rentalRecordRepository = rentalRecordRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Regras de vaga: so um aluguel ATIVO ocupa o veiculo.
     * O cliente e informado pelo chamador (admin escolhe; a conta
     * logada resolve o seu antes de chamar este metodo).
     */
    @Transactional
    public RentalRecord alugar(Long veiculoId, Long clienteId,
                               LocalDateTime retirada, LocalDateTime devolucao) {
        if (veiculoId == null) {
            throw new IllegalStateException("Selecione um veículo.");
        }
        if (clienteId == null) {
            throw new IllegalStateException("Selecione um cliente.");
        }
        if (retirada == null || devolucao == null) {
            throw new IllegalStateException("Informe a retirada e a devolução.");
        }
        if (!retirada.isAfter(LocalDateTime.now())) {
            throw new IllegalStateException(
                    "A retirada deve ser posterior ao momento atual.");
        }
        if (!devolucao.isAfter(retirada)) {
            throw new IllegalStateException(
                    "A devolução deve ser posterior à retirada.");
        }

        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new IllegalStateException("Veículo não encontrado."));
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalStateException("Cliente não encontrado."));

        if (!Boolean.TRUE.equals(veiculo.getDisponivel())) {
            throw new IllegalStateException(
                    "Este veículo não está disponível para aluguel.");
        }
        if (rentalRecordRepository.existsByVeiculoIdAndStatus(
                veiculo.getId(), StatusAluguel.ATIVO)) {
            throw new IllegalStateException(
                    "Este veículo já possui um aluguel ativo.");
        }
        if (rentalRecordRepository.existsByVeiculoIdAndClienteIdAndStatus(
                veiculo.getId(), cliente.getId(), StatusAluguel.ATIVO)) {
            throw new IllegalStateException(
                    "Este cliente já possui um aluguel ativo deste veículo.");
        }

        RentalRecord registro = new RentalRecord();
        registro.setVeiculo(veiculo);
        registro.setCliente(cliente);
        registro.setRenterName(cliente.getNome());
        registro.setRenterContact(cliente.getTelefone());
        registro.setRentalStartDate(retirada);
        registro.setExpectedReturnDate(devolucao);
        registro.setDataAluguel(LocalDateTime.now());
        registro.setStatus(StatusAluguel.ATIVO);
        registro.setDataCancelamento(null);

        veiculo.setDisponivel(false);
        veiculoRepository.save(veiculo);
        return rentalRecordRepository.save(registro);
    }

    /**
     * Aluguel da conta autenticada. O cliente vem do Usuario localizado
     * pelo e-mail da sessao, nunca do formulario.
     */
    @Transactional
    public RentalRecord alugarUsuario(Long veiculoId, LocalDateTime retirada,
                                      LocalDateTime devolucao, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado."));
        Cliente cliente = usuario.getCliente();
        if (cliente == null) {
            throw new IllegalStateException(
                    "Esta conta não possui cliente vinculado.");
        }
        return alugar(veiculoId, cliente.getId(), retirada, devolucao);
    }

    @Transactional(readOnly = true)
    public List<Veiculo> listarCatalogo() {
        Set<Long> ocupados = Set.copyOf(rentalRecordRepository
                .findVeiculoIdsByStatus(StatusAluguel.ATIVO));
        return veiculoRepository.findAll().stream()
                .filter(v -> Boolean.TRUE.equals(v.getDisponivel()))
                .filter(v -> !ocupados.contains(v.getId()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RentalRecord> listarDoUsuario(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado."));
        if (usuario.getCliente() == null) {
            return List.of();
        }
        return rentalRecordRepository.findByClienteIdOrderByDataAluguelDesc(
                usuario.getCliente().getId());
    }

    /**
     * Cancelamento logico: confere a titularidade, mantem o registro
     * no historico e devolve a disponibilidade do veiculo.
     */
    @Transactional
    public void cancelarMeuAluguel(Long aluguelId, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado."));
        if (usuario.getCliente() == null) {
            throw new IllegalStateException("Conta sem cliente vinculado.");
        }
        RentalRecord registro = rentalRecordRepository.findById(aluguelId)
                .orElseThrow(() -> new IllegalStateException("Aluguel não encontrado."));

        Long dono = usuario.getCliente().getId();
        if (registro.getCliente() == null || !dono.equals(registro.getCliente().getId())) {
            throw new IllegalStateException("Você não pode cancelar outro aluguel.");
        }
        if (registro.getStatus() != StatusAluguel.ATIVO) {
            throw new IllegalStateException("Este aluguel já está cancelado.");
        }
        if (registro.jaIniciou()) {
            throw new IllegalStateException(
                    "Não é possível cancelar após o início do aluguel.");
        }
        registro.setStatus(StatusAluguel.CANCELADO);
        registro.setDataCancelamento(LocalDateTime.now());
        Veiculo veiculo = registro.getVeiculo();
        veiculo.setDisponivel(true);
        veiculoRepository.save(veiculo);
    }

    public List<RentalRecord> listarTodos() {
        return rentalRecordRepository.findAll();
    }

    public List<RentalRecord> listarPorVeiculo(Long veiculoId) {
        return rentalRecordRepository.findByVeiculoId(veiculoId);
    }

    public boolean possuiAluguelAtivo(Long veiculoId) {
        return rentalRecordRepository.existsByVeiculoIdAndStatus(
                veiculoId, StatusAluguel.ATIVO);
    }

    public long contarAtivos() {
        return rentalRecordRepository.countByStatus(StatusAluguel.ATIVO);
    }

    public long contarTodos() {
        return rentalRecordRepository.count();
    }

    @Transactional
    public void excluir(Long id) {
        RentalRecord registro = rentalRecordRepository.findById(id).orElse(null);
        if (registro == null) {
            return;
        }
        if (registro.getStatus() == StatusAluguel.ATIVO && registro.getVeiculo() != null) {
            Veiculo veiculo = registro.getVeiculo();
            veiculo.setDisponivel(true);
            veiculoRepository.save(veiculo);
        }
        rentalRecordRepository.delete(registro);
    }
}
