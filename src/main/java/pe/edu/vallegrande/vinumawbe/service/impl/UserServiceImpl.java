package pe.edu.vallegrande.vinumawbe.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.vinumawbe.model.User;
import pe.edu.vallegrande.vinumawbe.repository.UserRepository;
import pe.edu.vallegrande.vinumawbe.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Flux<User> findAll() {
        log.info("Mostrando datos");
        return userRepository.findAll();
    }

    @Override
    public Mono<User> findById(String id) {
        log.info("Mostrando datos por ID ");
        return userRepository.findById(id);
    }

    @Override
    public Flux<User> findByStatus(Boolean status) {
        log.info("Mostrando datos por Estado ");
        return userRepository.findByStatus(status);
    }

    @Override
    public Mono<User> save(User user) {
        log.info("Registrando datos " + user.toString());
        user.setStatus(true);
        user.setAddedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public Mono<User> login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password));
    }

    @Override
    public Mono<User> update(User user) {
        log.info("Actualizando datos " + user.toString());
        user.setStatus(true);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    @Override
    public Mono<User> delete(String id) {

        log.info("Eliminando cliente (lógico) con id: " + id);

        return userRepository.findById(id)
                .flatMap(client -> {
                    client.setStatus(false);
                    client.setDeletedAt(LocalDateTime.now());
                    return userRepository.save(client);
                });
    }

    @Override
    public Mono<User> restore(String id) {

        log.info("Restaurando cliente (lógico) con id: " + id);

        return userRepository.findById(id)
                .flatMap(client -> {
                    client.setStatus(true);
                    client.setRestoredAt(LocalDateTime.now());
                    return userRepository.save(client);
                });
    }

}