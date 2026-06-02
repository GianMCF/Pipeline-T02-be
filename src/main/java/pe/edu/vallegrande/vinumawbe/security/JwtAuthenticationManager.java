package pe.edu.vallegrande.vinumawbe.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager
        implements ReactiveAuthenticationManager {

    private final JwtService jwtService;

    @Override
    public Mono<Authentication> authenticate(
            Authentication authentication
    ) {

        String token = authentication.getCredentials().toString();

        // VALIDAR TOKEN
        if (!jwtService.validateToken(token)) {

            return Mono.empty();
        }

        // EXTRAER DATOS
        String username = jwtService.getUsername(token);

        String role = jwtService.getRole(token);

        // ROLE_USER / ROLE_ADMIN
        List<SimpleGrantedAuthority> authorities =
                List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_" + role
                        )
                );

        // AUTH OBJECT
        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities
                );

        return Mono.just(auth);
    }
}