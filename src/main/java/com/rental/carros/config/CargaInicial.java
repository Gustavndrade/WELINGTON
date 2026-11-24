package com.rental.carros.config;

import com.rental.carros.model.Cliente;
import com.rental.carros.model.PerfilUsuario;
import com.rental.carros.model.Usuario;
import com.rental.carros.repository.ClienteRepository;
import com.rental.carros.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Carga inicial para demonstracao: cria um ADMIN e um CLIENTE de teste,
 * caso ainda nao existam. Remova ou troque as credenciais fora do ambiente de aula.
 */
@Configuration
public class CargaInicial {

    @Bean
    CommandLineRunner criarUsuariosIniciais(
            UsuarioRepository usuarioRepository,
            ClienteRepository clienteRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.email}") String emailAdmin,
            @Value("${app.admin.password}") String senhaAdmin) {
        return args -> {
            String adminEmail = emailAdmin.trim().toLowerCase();
            if (usuarioRepository.findByEmail(adminEmail).isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setEmail(adminEmail);
                admin.setSenha(passwordEncoder.encode(senhaAdmin));
                admin.setPerfil(PerfilUsuario.ADMIN);
                usuarioRepository.save(admin);
            }

            String clienteEmail = "cliente@aluguel.com";
            if (usuarioRepository.findByEmail(clienteEmail).isEmpty()) {
                Cliente cliente = clienteRepository
                        .findByEmailIgnoreCase(clienteEmail)
                        .orElseGet(() -> {
                            Cliente c = new Cliente();
                            c.setNome("Cliente Teste");
                            c.setEmail(clienteEmail);
                            c.setTelefone("(00) 00000-0000");
                            c.setDocumento("000.000.000-00");
                            return clienteRepository.save(c);
                        });

                Usuario usuario = new Usuario();
                usuario.setNome("Cliente Teste");
                usuario.setEmail(clienteEmail);
                usuario.setSenha(passwordEncoder.encode("teste123"));
                usuario.setPerfil(PerfilUsuario.CLIENTE);
                usuario.setCliente(cliente);
                usuarioRepository.save(usuario);
            }
        };
    }
}
