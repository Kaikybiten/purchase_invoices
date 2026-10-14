package com.example.purchase_invoices.config;

import com.example.purchase_invoices.filter.AnonymousSessionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final AnonymousSessionFilter anonymousSessionFilter;

    public SecurityConfig(AnonymousSessionFilter anonymousSessionFilter) {
        this.anonymousSessionFilter = anonymousSessionFilter;
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth ->

                auth.anyRequest().permitAll()

        ).formLogin(AbstractHttpConfigurer::disable) // Desativando formulario
                .httpBasic(AbstractHttpConfigurer::disable) // Não use usuário e senha enviados pelo HTTP Basic.
                .addFilterBefore(
                        // Adiciona o AnonymousSessionFilter à cadeia de filtros, antes do UsernamePasswordAuthenticationFilter
                        anonymousSessionFilter,
                        // Assim, a verificação da sessão acontece antes do processamento da autenticação por formulário de usuário e senha.
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();

    }

}
