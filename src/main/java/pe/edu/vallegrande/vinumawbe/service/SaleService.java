package pe.edu.vallegrande.vinumawbe.service;

import pe.edu.vallegrande.vinumawbe.dto.SaleRequest;
import pe.edu.vallegrande.vinumawbe.dto.SaleResponse;
import pe.edu.vallegrande.vinumawbe.model.Sale;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SaleService {
    Flux<Sale> findAll();
    Mono<Sale> findById(String id);
    Mono<SaleResponse> createSale(SaleRequest request);
    Mono<Sale> update(Sale sale);
    Mono<Sale> delete(String id);
    Mono<Sale> restore(String id);
}
