package com.titan.commerce.infra.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Permite o uso do @PreAuthorize nos Controllers (Usuários e Endereços)
@RequiredArgsConstructor
public class SecurityConfigurations {

    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize

                        // 1. SWAGGER E DOCS
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // 2. AUTENTICAÇÃO E CADASTRO (Acesso Total)
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()

                        // 3. CARRINHO DE COMPRAS (Acesso Total - Suporta usuários logados ou anônimos via Cookie)
                        .requestMatchers("/api/cart/**").permitAll()

                        // 4. CATÁLOGO - LEITURA (Acesso Total para vitrine do E-commerce)
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/product-variants/**").permitAll()

                        // 5. CATÁLOGO - ESCRITA (Acesso Restrito a Administradores)
                        .requestMatchers(HttpMethod.POST, "/api/categories/**", "/api/products/**", "/api/product-variants/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**", "/api/products/**", "/api/product-variants/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/categories/**", "/api/products/**", "/api/product-variants/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**", "/api/products/**", "/api/product-variants/**").hasRole("ADMIN")

                        // 6. USUÁRIOS, ENDEREÇOS E CHECKOUT
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}