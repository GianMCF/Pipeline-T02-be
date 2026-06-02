package pe.edu.vallegrande.vinumawbe.service;

import pe.edu.vallegrande.vinumawbe.model.PaymentMethod;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentMethodService {
    Flux<PaymentMethod> findAll();
    Mono<PaymentMethod> findById(String id);
    Mono<PaymentMethod> save(PaymentMethod paymentMethod);
    Mono<PaymentMethod> update(PaymentMethod paymentMethod);
    Mono<PaymentMethod> delete(String id);
    Mono<PaymentMethod> restore(String id);
}
