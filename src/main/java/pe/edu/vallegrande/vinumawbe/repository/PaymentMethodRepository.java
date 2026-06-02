package pe.edu.vallegrande.vinumawbe.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.edu.vallegrande.vinumawbe.model.PaymentMethod;

public interface PaymentMethodRepository extends ReactiveMongoRepository<PaymentMethod, String> {
}
