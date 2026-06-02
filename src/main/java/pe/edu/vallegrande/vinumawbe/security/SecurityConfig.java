package pe.edu.vallegrande.vinumawbe.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityContextRepository securityContextRepository;

    @Value("${cors.allowed-origin}")
    private String allowedOrigins;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // =========================
    // CORS CONFIG
    // =========================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(
                Arrays.asList(allowedOrigins.split(","))
        );

        config.setAllowedMethods(
                Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        config.setAllowedHeaders(
                List.of("*")
        );

        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }

    // =========================
    // SECURITY
    // =========================

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http
    ) {

        return http

                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)

                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)

                .cors(cors ->
                        cors.configurationSource(
                                corsConfigurationSource()
                        )
                )

                .securityContextRepository(
                        securityContextRepository
                )

                .authorizeExchange(exchange -> exchange

                        .pathMatchers("/").permitAll()

                        // AUTH - USER
                        .pathMatchers(
                                "/v1/api/user/login",
                                "/v1/api/user/register"
                        ).permitAll()

                        // PERFIL - AUTH REQUIRED
                        .pathMatchers(
                                "/v1/api/user/me"
                        ).authenticated()

                        .pathMatchers(
                                "/v1/api/user/restore/**"
                        ).permitAll()

                        // PERFIL - LOGICAL DELETE
                        .pathMatchers(HttpMethod.DELETE,
                                "/v1/api/user/me"
                        ).authenticated()

                        // SWAGGER
                        .pathMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/api-docs/**",
                                "/webjars/**"
                        ).permitAll()

                        // PRODUCTS LIST
                        .pathMatchers(HttpMethod.GET,
                                "/v1/api/products/**"
                        ).permitAll()

                        // EVERYTHING ELSE
                        .anyExchange()
                        .hasRole("ADMIN")
                )
                .build();
    }
}