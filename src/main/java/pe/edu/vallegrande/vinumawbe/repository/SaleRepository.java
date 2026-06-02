package pe.edu.vallegrande.vinumawbe.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.edu.vallegrande.vinumawbe.model.Sale;

public interface SaleRepository extends ReactiveMongoRepository<Sale, String> {
}
