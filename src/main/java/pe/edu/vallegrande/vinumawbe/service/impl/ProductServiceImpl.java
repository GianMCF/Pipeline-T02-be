package pe.edu.vallegrande.vinumawbe.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.vinumawbe.model.Product;
import pe.edu.vallegrande.vinumawbe.repository.ProductRepository;
import pe.edu.vallegrande.vinumawbe.service.ProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Override
    public Flux<Product> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Product> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Product> create(Product product) {
        product.setId(null);
        product.setStatus(true);

        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(null); // 

        product.setDeletedAt(null);
        product.setRestoredAt(null);

        return repository.save(product);
    }

    @Override
    public Mono<Product> update(String id, Product product) {
        return repository.findById(id)
                .filter(existing -> existing.getStatus()) // solo activos
                .flatMap(existing -> {

                    existing.setName(product.getName());
                    existing.setCategory(product.getCategory());
                    existing.setStock(product.getStock());
                    existing.setUnitPrice(product.getUnitPrice());
                    existing.setVolumeMl(product.getVolumeMl());
                    existing.setAlcoholPercentage(product.getAlcoholPercentage());

                
                    existing.setUpdatedAt(LocalDateTime.now());

                    return repository.save(existing);
                });
    }

    @Override
    public Mono<Void> logicalDelete(String id) {
        return repository.findById(id)
                .filter(product -> product.getStatus()) // evitar doble delete
                .flatMap(product -> {

                    product.setStatus(false);

                
                    if (product.getDeletedAt() == null) {
                        product.setDeletedAt(LocalDateTime.now());
                    }

                    return repository.save(product);
                })
                .then();
    }

    @Override
    public Mono<Void> restore(String id) {
        return repository.findById(id)
                .filter(product -> !product.getStatus()) // solo eliminados
                .flatMap(product -> {

                    product.setStatus(true);
                    product.setRestoredAt(LocalDateTime.now());

            

                    return repository.save(product);
                })
                .then();
    }

    @Override
    public Flux<Product> getByState(boolean active) {
        return repository.findByStatus(active);
    }
}