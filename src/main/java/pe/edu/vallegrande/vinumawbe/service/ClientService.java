package pe.edu.vallegrande.vinumawbe.service;
import pe.edu.vallegrande.vinumawbe.model.Client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientService {

    Flux<Client> findAll();
    Mono<Client> findById(String id);
    Mono<Client> save(Client client);
    Mono<Client> update(Client client);
    Mono<Client> delete(String id);
    Mono<Client> restore(String id);
}