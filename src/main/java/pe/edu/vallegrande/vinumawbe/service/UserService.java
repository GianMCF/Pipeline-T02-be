package pe.edu.vallegrande.vinumawbe.service;

import pe.edu.vallegrande.vinumawbe.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Flux<User> findAll();
    Mono<User> findById(String id);
    Flux<User> findByStatus(Boolean status);
    Mono<User> login(String email, String password);
    Mono<User> save(User user);
    Mono<User> update(User user);
    Mono<User> delete(String id);
    Mono<User> restore(String id);
}
