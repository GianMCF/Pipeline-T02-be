package pe.edu.vallegrande.vinumawbe.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.vinumawbe.model.Purchase;
import pe.edu.vallegrande.vinumawbe.repository.PurchaseRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl {

    private final PurchaseRepository repository;

    // =============================
    // CREATE
    // =============================
    public Mono<Purchase> create(Purchase purchase) {

        if (purchase.getDetails() == null || purchase.getDetails().isEmpty()) {
            return Mono.error(new RuntimeException("Debe tener al menos un detalle"));
        }

        purchase.getDetails().forEach(d -> {
            BigDecimal subtotal = d.getUnitPrice()
                    .multiply(BigDecimal.valueOf(d.getQuantity()));
            d.setSubtotal(subtotal);
        });

        BigDecimal total = purchase.getDetails().stream()
                .map(d -> d.getUnitPrice()
                        .multiply(BigDecimal.valueOf(d.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        purchase.setTotal(total);
        purchase.setStatus(true);
        purchase.setCreatedAt(LocalDateTime.now());
        purchase.setUpdatedAt(null);
        purchase.setDeletedAt(null);
        purchase.setRestoredAt(null);

        return repository.save(purchase);
    }

    // =============================
    // UPDATE
    // =============================
    public Mono<Purchase> update(String id, Purchase purchase) {

        if (purchase.getDetails() == null || purchase.getDetails().isEmpty()) {
            return Mono.error(new RuntimeException("Debe tener detalles"));
        }

        return repository.findById(id)
                .flatMap(existing -> {

                    purchase.getDetails().forEach(d -> {
                        BigDecimal subtotal = d.getUnitPrice()
                                .multiply(BigDecimal.valueOf(d.getQuantity()));
                        d.setSubtotal(subtotal);
                    });

                    BigDecimal total = purchase.getDetails().stream()
                            .map(d -> d.getUnitPrice()
                                    .multiply(BigDecimal.valueOf(d.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    existing.setSuppliersId(purchase.getSuppliersId());
                    existing.setDetails(purchase.getDetails());
                    existing.setTotal(total);
                    existing.setUpdatedAt(LocalDateTime.now());

                    return repository.save(existing);
                });
    }

    // =============================
    // READ ALL
    // =============================
    public Flux<Purchase> getAll() {
        return repository.findAll();
    }

    // =============================
    // READ BY ID
    // =============================
    public Mono<Purchase> getById(String id) {
        return repository.findById(id);
    }

    // =============================
    // DELETE LÓGICO
    // =============================
    public Mono<Purchase> delete(String id) {
        return repository.findById(id)
                .filter(p -> p.getStatus())
                .flatMap(p -> {
                    p.setStatus(false);
                    p.setDeletedAt(LocalDateTime.now());
                    return repository.save(p);
                });
    }

    // =============================
    // RESTORE (CORREGIDO ✅)
    // =============================
    public Mono<Purchase> restore(String id) {
        return repository.findById(id)
                .filter(p -> !p.getStatus())
                .flatMap(p -> {
                    p.setStatus(true);
                    p.setRestoredAt(LocalDateTime.now());
                    // 🔥 IMPORTANTE: NO borrar deletedAt
                    return repository.save(p);
                });
    }

    // =============================
    // FILTER BY STATUS
    // =============================
    public Flux<Purchase> getByState(boolean active) {
        return repository.findByStatus(active);
    }
}