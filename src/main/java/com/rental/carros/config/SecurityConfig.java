package com.rental.carros.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // A autenticacao usa o UsuarioDetailsService (@Service), que o Spring
    // Security detecta automaticamente com o PasswordEncoder acima.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/login", "/cadastro", "/error")
                    .permitAll()
                // Area do cliente (antes das regras de ADMIN)
                .requestMatchers("/catalogo", "/meus-alugueis").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.POST, "/alugueis/meu").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.POST, "/meus-alugueis/*/cancelar")
                    .hasRole("CLIENTE")
                .requestMatchers(
                        "/veiculos/**", "/clientes/**",
                        "/alugueis/**", "/relatorios/**")
                    .hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
        return http.build();
    }
}
