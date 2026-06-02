package pe.edu.vallegrande.vinumawbe.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private final JwtAuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {

        String path = exchange.getRequest()
                .getPath()
                .value();

        System.out.println("PATH -> " + path);

        // RUTAS PUBLICAS
        if (
                path.startsWith("/swagger-ui") ||
                        path.startsWith("/api-docs") ||
                        path.startsWith("/webjars") ||
                        path.startsWith("/v1/api/user/login") ||
                        path.startsWith("/v1/api/user/register")
        ) {

            System.out.println("RUTA PUBLICA");

            return Mono.empty();
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        System.out.println("AUTH HEADER -> " + authHeader);

        if (
                authHeader == null ||
                        !authHeader.startsWith("Bearer ")
        ) {

            System.out.println("TOKEN NO ENVIADO");

            return Mono.empty();
        }

        String token = authHeader.substring(7);

        System.out.println("TOKEN EXTRAIDO -> " + token);

        return authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                token,
                                token
                        )
                )
                .doOnNext(auth ->
                        System.out.println(
                                "AUTH OK -> " + auth.getName()
                        )
                )
                .map(SecurityContextImpl::new);
    }
}