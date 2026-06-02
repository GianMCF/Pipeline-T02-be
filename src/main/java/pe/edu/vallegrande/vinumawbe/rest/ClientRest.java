package pe.edu.vallegrande.vinumawbe.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import pe.edu.vallegrande.vinumawbe.model.Client;
import pe.edu.vallegrande.vinumawbe.service.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/api/client")
@Tag(name = "Clientes", description = "CRUD reactivo de clientes")
public class ClientRest {

    private final ClientService clientService;

    @Autowired
    public ClientRest(ClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(summary = "Listar todos los clientes", description = "Lista a todos los clientes en la base de datos")
    @GetMapping
    public Flux<Client> findAll() {
        return clientService.findAll();
    }

    @Operation(summary = "Hallar cliente por ID", description = "Lista a un solo cliente en la base de datos")
    @GetMapping("/{id}")
    public Mono<Client> findById(@PathVariable String id) {
        return clientService.findById(id);
    }

    @Operation(summary = "Registrar cliente", description = "Registra un nuevo cliente en la base de datos")
    @PostMapping("/save")
    public Mono<Client> save(@RequestBody Client client) {
        return clientService.save(client);
    }

    @Operation(summary = "Actualizar cliente", description = "Modifica datos de un cliente en la base de datos")
    @PutMapping ("/update")
    public Mono<Client> update(@RequestBody Client client) {
        return clientService.update(client);
    }

    @Operation(summary = "Eliminar lógicamente a un cliente", description = "Desactivar a un cliente en la base de datos")
    @DeleteMapping("/delete/{id}")
    public Mono<Client> delete(@PathVariable String id) {
        return clientService.delete(id);
    }

    @Operation(summary = "Restaurar lógicamente a un cliente", description = "Re-activar a un cliente en la base de datos")
    @PutMapping("/restore/{id}")
    public Mono<Client> restore(@PathVariable String id) {
        return clientService.restore(id);
    }
}
