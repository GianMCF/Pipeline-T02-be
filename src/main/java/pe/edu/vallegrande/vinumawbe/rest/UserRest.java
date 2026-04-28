package pe.edu.vallegrande.vinumawbe.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.vinumawbe.model.User;
import pe.edu.vallegrande.vinumawbe.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@CrossOrigin(origins = "*")
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

    @Operation(summary = "Inicio de sesión de usuario", description = "Permite un inicio de sesión de usuario")
    @PostMapping("/login")
    public Mono<User> login(@RequestBody User user) {
        return userService.login(user.getEmail(), user.getPassword())
                .switchIfEmpty(Mono.error(new RuntimeException("Credenciales incorrectas")));
    }

    @Operation(summary = "Registrar usuario", description = "Registra un nuevo usuario en la base de datos")
    @PostMapping("/register")
    public Mono<User> save(@RequestBody User user) {
        return userService.save(user);
    }

    @Operation(summary = "Actualizar usuario", description = "Modifica datos de un usuario en la base de datos")
    @PutMapping ("/update")
    public Mono<User> update(@RequestBody User user) {
        return userService.update(user);
    }

    @Operation(summary = "Eliminar lógicamente a un usuario", description = "Desactivar a un usuario en la base de datos")
    @DeleteMapping("/delete/{id}")
    public Mono<User> delete(@PathVariable String id) {
        return userService.delete(id);
    }

    @Operation(summary = "Restaurar lógicamente a un usuario", description = "Re-activar a un usuario en la base de datos")
    @PutMapping("/restore/{id}")
    public Mono<User> restore(@PathVariable String id) {
        return userService.restore(id);
    }
}
