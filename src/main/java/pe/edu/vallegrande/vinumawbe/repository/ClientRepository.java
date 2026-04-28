package pe.edu.vallegrande.vinumawbe.repository;
import pe.edu.vallegrande.vinumawbe.model.Client;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ClientRepository extends ReactiveMongoRepository<Client, String> {

}