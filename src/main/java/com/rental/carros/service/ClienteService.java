package com.rental.carros.service;

import com.rental.carros.model.Cliente;
import com.rental.carros.repository.ClienteRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public Cliente salvar(Cliente cliente) {
        if (cliente.getEmail() != null) {
            cliente.setEmail(cliente.getEmail().trim().toLowerCase());
        }
        if (cliente.getNome() != null) {
            cliente.setNome(cliente.getNome().trim());
        }
        repository.findByEmailIgnoreCase(cliente.getEmail()).ifPresent(existente -> {
            if (cliente.getId() == null || !existente.getId().equals(cliente.getId())) {
                throw new IllegalStateException("Já existe um cliente com este e-mail.");
            }
        });
        return repository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
