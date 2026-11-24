package com.rental.carros.service;

import com.rental.carros.dto.CadastroUsuarioForm;
import com.rental.carros.model.Cliente;
import com.rental.carros.model.PerfilUsuario;
import com.rental.carros.model.Usuario;
import com.rental.carros.repository.ClienteRepository;
import com.rental.carros.repository.UsuarioRepository;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          ClienteRepository clienteRepository,
                          PasswordEncoder encoder) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.encoder = encoder;
    }

    /**
     * Cria a conta (sempre CLIENTE) e o Cliente vinculado,
     * na mesma transacao.
     */
    @Transactional
    public Usuario cadastrar(CadastroUsuarioForm form) {
        String email = form.getEmail().trim().toLowerCase(Locale.ROOT);

        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalStateException("Este e-mail já está cadastrado.");
        }
        // Nao vincula automaticamente a um cliente antigo so pelo e-mail.
        if (clienteRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalStateException(
                    "Já existe um cliente com este e-mail.");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(form.getNome().trim());
        cliente.setEmail(email);
        cliente.setTelefone(form.getTelefone().trim());
        cliente.setDocumento(form.getDocumento().trim());
        cliente = clienteRepository.save(cliente);

        Usuario usuario = new Usuario();
        usuario.setNome(form.getNome().trim());
        usuario.setEmail(email);
        usuario.setSenha(encoder.encode(form.getSenha()));
        usuario.setPerfil(PerfilUsuario.CLIENTE);
        usuario.setCliente(cliente);
        return usuarioRepository.save(usuario);
    }
}
