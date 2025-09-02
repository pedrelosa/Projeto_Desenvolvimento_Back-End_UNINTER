package br.com.SGHSS.VidaPlus.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF para facilitar testes via Postman
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2/**").permitAll() // Libera o console do H2
                        .anyRequest().authenticated()         // Todas as outras rotas precisam de login
                )
                .httpBasic(); // Usa Basic Auth (envia user:pass no Header)

        // Necessário para liberar o uso de frames no console H2
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}
