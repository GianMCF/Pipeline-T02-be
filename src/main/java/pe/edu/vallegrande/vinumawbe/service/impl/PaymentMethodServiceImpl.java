package pe.edu.vallegrande.vinumawbe.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.vinumawbe.model.PaymentMethod;
import pe.edu.vallegrande.vinumawbe.repository.PaymentMethodRepository;
import pe.edu.vallegrande.vinumawbe.service.PaymentMethodService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    @Autowired
    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public Flux<PaymentMethod> findAll() {
        log.info("Mostrando datos");
        return paymentMethodRepository.findAll();
    }

    @Override
    public Mono<PaymentMethod> findById(String id) {
        log.info("Mostrando datos por ID ");
        return paymentMethodRepository.findById(id);
    }

    @Override
    public Mono<PaymentMethod> save(PaymentMethod paymentMethod) {
        log.info("Registrando datos " + paymentMethod.toString());
        paymentMethod.setStatus(true);
        paymentMethod.setAddedAt(LocalDateTime.now());
        return paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public Mono<PaymentMethod> update(PaymentMethod paymentMethod) {
        log.info("Actualizando datos " + paymentMethod.toString());
        paymentMethod.setStatus(true);
        paymentMethod.setUpdatedAt(LocalDateTime.now());
        return paymentMethodRepository.save(paymentMethod);
    }
    @Override
    public Mono<PaymentMethod> delete(String id) {

        log.info("Eliminando cliente (lógico) con id: " + id);

        return paymentMethodRepository.findById(id)
                .flatMap(paymentMethod -> {
                    paymentMethod.setStatus(false);
                    paymentMethod.setDeletedAt(LocalDateTime.now());
                    return paymentMethodRepository.save(paymentMethod);
                });
    }

    @Override
    public Mono<PaymentMethod> restore(String id) {

        log.info("Restaurando cliente (lógico) con id: " + id);

        return paymentMethodRepository.findById(id)
                .flatMap(paymentMethod -> {
                    paymentMethod.setStatus(true);
                    paymentMethod.setRestoredAt(LocalDateTime.now());
                    return paymentMethodRepository.save(paymentMethod);
                });
    }
}
