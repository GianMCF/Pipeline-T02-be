package pe.edu.vallegrande.vinumawbe.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.edu.vallegrande.vinumawbe.model.Purchase;
import reactor.core.publisher.Flux;

public interface PurchaseRepository extends ReactiveMongoRepository<Purchase, String> {

    Flux<Purchase> findByStatus(boolean status);
}