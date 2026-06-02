package pe.edu.vallegrande.vinumawbe.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.vallegrande.vinumawbe.dto.AuthResponse;
import pe.edu.vallegrande.vinumawbe.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import pe.edu.vallegrande.vinumawbe.dto.AuthRequest;
import pe.edu.vallegrande.vinumawbe.repository.UserRepository;
import pe.edu.vallegrande.vinumawbe.security.JwtService;


import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final EncryptionService encryptionService;

    private final BCryptPasswordEncoder bcryptPasswordEncoder;

    private final JwtService jwtService;

    public Flux<User> findAll() {
        log.info("Mostrando datos");
        return userRepository.findAll();
    }
    
    public Mono<User> findById(String id) {
        log.info("Mostrando datos por ID ");
        return userRepository.findById(id);
    }

    public Flux<User> findByStatus(Boolean status) {
        log.info("Mostrando datos por Estado ");
        return userRepository.findByStatus(status);
    }

    // SEE USERNAME IN PROFILE
    public Mono<User> findByUsername(String username) {

        String encryptedUsername =
                encryptionService.encrypt(username);

        return userRepository
                .findByUsername(encryptedUsername)

                .map(user -> {

                    user.setUsername(
                            encryptionService.decrypt(
                                    user.getUsername()
                            )
                    );

                    user.setRole(
                            encryptionService.decrypt(
                                    user.getRole()
                            )
                    );

                    return user;
                });
    }

    public Mono<User> findCurrentUser(String username) {

        return userRepository.findAll()

                .filter(user ->

                        encryptionService.decrypt(
                                user.getUsername()
                        ).equals(username)
                )

                .next();
    }

    public Mono<User> register(User user) {

        // VALIDACIONES

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            return Mono.error(
                    new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Username is required"
                    )
            );
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            return Mono.error(
                    new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Password is required"
                    )
            );
        }

        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }

        // ENCRIPTAR DATOS

        user.setUsername(
                encryptionService.encrypt(
                        user.getUsername()
                )
        );

        user.setRole(
                encryptionService.encrypt(
                        user.getRole()
                )
        );

        // HASH PASSWORD

        user.setPassword(
                bcryptPasswordEncoder.encode(
                        user.getPassword()
                )
        );

        user.setStatus(true);

        user.setAddedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public Mono<AuthResponse> login(AuthRequest request) {

        return userRepository.findAll()

                .filter(user ->

                        encryptionService.decrypt(
                                user.getUsername()
                        ).equals(request.getUsername())
                )

                .next()

                .switchIfEmpty(
                        Mono.error(
                                new ResponseStatusException(
                                        HttpStatus.UNAUTHORIZED,
                                        "Invalid credentials"
                                )
                        )
                )

                // VALIDAR PASSWORD
                .filter(user ->
                        bcryptPasswordEncoder.matches(
                                request.getPassword(),
                                user.getPassword()
                        )
                )

                .switchIfEmpty(
                        Mono.error(
                                new ResponseStatusException(
                                        HttpStatus.UNAUTHORIZED,
                                        "Invalid credentials"
                                )
                        )
                )

                // VALIDAR STATUS
                .flatMap(user -> {

                    if (!Boolean.TRUE.equals(user.getStatus())) {

                        return Mono.error(
                                new ResponseStatusException(
                                        HttpStatus.FORBIDDEN,
                                        "Account disabled"
                                )
                        );
                    }

                    String role =
                            encryptionService.decrypt(
                                    user.getRole()
                            );

                    String username =
                            encryptionService.decrypt(
                                    user.getUsername()
                            );

                    String token =
                            jwtService.generateToken(
                                    username,
                                    role
                            );

                    return Mono.just(
                            new AuthResponse(
                                    token,
                                    role,
                                    username
                            )
                    );
                });
    }

    public Mono<User> updateCurrentUser(
            String username,
            User updatedUser
    ) {

        String encryptedUsername =
                encryptionService.encrypt(username);

        return userRepository
                .findByUsername(encryptedUsername)

                .flatMap(user -> {

                    if (
                            updatedUser.getUsername() != null &&
                                    !updatedUser.getUsername().isBlank()
                    ) {

                        user.setUsername(
                                encryptionService.encrypt(
                                        updatedUser.getUsername()
                                )
                        );
                    }

                    user.setEmail(updatedUser.getEmail());

                    if (
                            updatedUser.getPassword() != null &&
                                    !updatedUser.getPassword().isBlank()
                    ) {

                        user.setPassword(
                                bcryptPasswordEncoder.encode(
                                        updatedUser.getPassword()
                                )
                        );
                    }

                    user.setUpdatedAt(LocalDateTime.now());

                    return userRepository.save(user);
                })

                .map(savedUser -> {

                    savedUser.setUsername(
                            encryptionService.decrypt(
                                    savedUser.getUsername()
                            )
                    );

                    savedUser.setRole(
                            encryptionService.decrypt(
                                    savedUser.getRole()
                            )
                    );

                    return savedUser;
                });
    }

    public Mono<User> deleteCurrentUser(String username) {

        String encryptedUsername =
                encryptionService.encrypt(username);

        return userRepository
                .findByUsername(encryptedUsername)

                .flatMap(user -> {

                    user.setStatus(false);

                    user.setDeletedAt(LocalDateTime.now());

                    return userRepository.save(user);
                });
    }

    public Mono<User> restoreCurrentUser(String username) {

        String encryptedUsername =
                encryptionService.encrypt(username);

        return userRepository
                .findByUsername(encryptedUsername)

                .switchIfEmpty(
                        Mono.error(
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "User not found"
                                )
                        )
                )

                .flatMap(user -> {

                    if (Boolean.TRUE.equals(user.getStatus())) {

                        return Mono.error(
                                new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Account already active"
                                )
                        );
                    }

                    user.setStatus(true);

                    user.setRestoredAt(LocalDateTime.now());

                    user.setUpdatedAt(LocalDateTime.now());

                    return userRepository.save(user);
                });
    }

}