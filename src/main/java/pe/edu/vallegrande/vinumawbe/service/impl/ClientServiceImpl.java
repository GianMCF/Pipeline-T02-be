package pe.edu.vallegrande.vinumawbe.service.impl;
import pe.edu.vallegrande.vinumawbe.model.Client;
import pe.edu.vallegrande.vinumawbe.repository.ClientRepository;
import pe.edu.vallegrande.vinumawbe.service.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository userRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.userRepository = clientRepository;
    }

    @Override
    public Flux<Client> findAll() {
        log.info("Mostrando datos");
        return userRepository.findAll();
    }

    @Override
    public Mono<Client> findById(String id) {
        log.info("Mostrando datos por ID ");
        return userRepository.findById(id);
    }

    @Override
    public Mono<Client> save(Client client) {
        log.info("Registrando datos " + client.toString());
        client.setStatus(true);
        client.setAddedAt(LocalDateTime.now());
        return userRepository.save(client);
    }

    @Override
    public Mono<Client> update(Client client) {
        log.info("Actualizando datos " + client.toString());
        client.setStatus(true);
        client.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(client);
    }
    @Override
    public Mono<Client> delete(String id) {

        log.info("Eliminando cliente (lógico) con id: " + id);

        return userRepository.findById(id)
                .flatMap(client -> {
                    client.setStatus(false);
                    client.setDeletedAt(LocalDateTime.now());
                    return userRepository.save(client);
                });
    }

    @Override
    public Mono<Client> restore(String id) {

        log.info("Restaurando cliente (lógico) con id: " + id);

        return userRepository.findById(id)
                .flatMap(client -> {
                    client.setStatus(true);
                    client.setRestoredAt(LocalDateTime.now());
                    return userRepository.save(client);
                });
    }

}