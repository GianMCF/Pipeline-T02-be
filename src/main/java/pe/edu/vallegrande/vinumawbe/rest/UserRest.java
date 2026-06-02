package pe.edu.vallegrande.vinumawbe.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.vinumawbe.dto.AuthRequest;
import pe.edu.vallegrande.vinumawbe.dto.AuthResponse;
import pe.edu.vallegrande.vinumawbe.model.User;
import pe.edu.vallegrande.vinumawbe.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;

@RestController
@RequestMapping("/v1/api/user")
@Tag(name = "Usuarios", description = "CRUD reactivo de usuarios")
public class UserRest {

    private final UserService userService;

    @Autowired
    public UserRest(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Listar todos los usuarios", description = "Lista a todos los usuario en la base de datos")
    @GetMapping
    public Flux<User> findAll() {
        return userService.findAll();
    }

    @Operation(summary = "Hallar usuario por ID", description = "Lista a un solo usuario en la base de datos")
    @GetMapping("/{id}")
    public Mono<User> findById(@PathVariable String id) {
        return userService.findById(id);
    }

    @Operation(summary = "Hallar usuarios por Estado", description = "Listar usuarios en la base de datos por Estado")
    @GetMapping("/status/{status}")
    public Flux<User> findByStatus(@PathVariable Boolean status) {
        return userService.findByStatus(status);
    }

    @Operation(summary = "Registrar usuario", description = "Registra un nuevo usuario en la base de datos")
    @PostMapping("/register")
    public Mono<User> register(@RequestBody User user) {
        return userService.register(user);
    }

    @Operation(summary = "Inicio de sesión de usuario", description = "Permite un inicio de sesión de usuario")
    @PostMapping("/login")
    public Mono<AuthResponse> login(
            @RequestBody AuthRequest request) {
        return userService.login(request);
    }

    // MÉTODOS DE USUARIO PARA FRONTEND
    @Operation(summary = "Listar datos de un usuario", description = "Listar datos de a un usuario en la base de datos")
    @GetMapping("/me")
    public Mono<User> getCurrentUser() {

        return ReactiveSecurityContextHolder.getContext()

                .map(ctx -> ctx.getAuthentication())

                .map(auth -> auth.getName())

                .flatMap(userService::findByUsername);
    }
    @Operation(summary = "Actualizar a un usuario", description = "Actualizar a un usuario en la base de datos")
    @PutMapping("/me")
    public Mono<User> updateCurrentUser(
            @RequestBody User updatedUser
    ) {

        return ReactiveSecurityContextHolder.getContext()

                .map(ctx -> ctx.getAuthentication())

                .map(auth -> auth.getName())

                .flatMap(username ->
                        userService.updateCurrentUser(
                                username,
                                updatedUser
                        )
                );
    }
    @Operation(summary = "Eliminar lógicamente a un usuario", description = "Desactivar a un usuario en la base de datos")
    @DeleteMapping("/me")
    public Mono<User> deleteCurrentUser() {

        return ReactiveSecurityContextHolder.getContext()

                .map(ctx -> ctx.getAuthentication())

                .map(auth -> auth.getName())

                .flatMap(userService::deleteCurrentUser);
    }

    @Operation(summary = "Restaurar lógicamente a un usuario", description = "Re-activar a un usuario en la base de datos")
    @PutMapping("/restore/{username}")
    public Mono<ResponseEntity<?>> restoreUser(
            @PathVariable String username
    ) {

        return userService
                .restoreCurrentUser(username)
                .map(user -> ResponseEntity.ok(user));
    }
}