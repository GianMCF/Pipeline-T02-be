package pe.edu.vallegrande.vinumawbe.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.edu.vallegrande.vinumawbe.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Flux<User> findByStatus(boolean status);
    Mono<User> findByEmail(String email);
}