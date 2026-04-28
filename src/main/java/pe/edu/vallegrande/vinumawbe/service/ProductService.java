package pe.edu.vallegrande.vinumawbe.service;

import pe.edu.vallegrande.vinumawbe.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    Flux<Product> getAll();

    Mono<Product> getById(String id);

    Mono<Product> create(Product product);

    Mono<Product> update(String id, Product product);

    Mono<Void> logicalDelete(String id);

    Mono<Void> restore(String id);

    Flux<Product> getByState(boolean active);
}