package pe.edu.vallegrande.vinumawbe.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import pe.edu.vallegrande.vinumawbe.model.Purchase;
import pe.edu.vallegrande.vinumawbe.service.impl.PurchaseServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/api/purchases")
@RequiredArgsConstructor
@Tag(name = "Compras", description = "CRUD reactivo de compras")
public class PurchaseRest {

    private final PurchaseServiceImpl service;

    // =============================
    // CREAR
    // =============================
    @Operation(
        summary = "Registrar compra",
        description = "Crea una compra con múltiples detalles. Calcula automáticamente subtotal y total, y registra la fecha de creación."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Purchase> create(@Valid @RequestBody Purchase purchase) {
        return service.create(purchase);
    }

    // =============================
    // ACTUALIZAR
    // =============================
    @Operation(
        summary = "Actualizar compra",
        description = "Actualiza una compra existente, recalcula el total y registra la fecha de actualización."
    )
    @PutMapping("/{id}")
    public Mono<Purchase> update(
            @PathVariable String id,
            @Valid @RequestBody Purchase purchase) {
        return service.update(id, purchase);
    }

    // =============================
    // LISTAR TODO
    // =============================
    @Operation(
        summary = "Listar compras",
        description = "Obtiene todas las compras con su cabecera y detalle embebido."
    )
    @GetMapping
    public Flux<Purchase> getAll() {
        return service.getAll();
    }

    // =============================
    // LISTAR POR ID
    // =============================
    @Operation(
        summary = "Buscar compra por ID",
        description = "Obtiene una compra específica incluyendo su detalle."
    )
    @GetMapping("/{id}")
    public Mono<Purchase> getById(@PathVariable String id) {
        return service.getById(id);
    }

    // =============================
    // DELETE LÓGICO
    // =============================
    @Operation(
        summary = "Eliminar compra (lógico)",
        description = "Desactiva una compra sin eliminarla físicamente. Cambia el estado a inactivo y registra la fecha de eliminación."
    )
    @PatchMapping("/{id}/delete")
    public Mono<Purchase> delete(@PathVariable String id) {
        return service.delete(id);
    }

    // =============================
    // RESTORE
    // =============================
    @Operation(
        summary = "Restaurar compra",
        description = "Reactiva una compra eliminada lógicamente y registra la fecha de restauración."
    )
    @PatchMapping("/{id}/restore")
    public Mono<Purchase> restore(@PathVariable String id) {
        return service.restore(id);
    }

    // =============================
    // FILTRAR POR ESTADO
    // =============================
    @Operation(
        summary = "Filtrar compras por estado",
        description = "Permite obtener compras activas o inactivas mediante el parámetro active."
    )
    @GetMapping("/by-state")
    public Flux<Purchase> getByState(@RequestParam boolean active) {
        return service.getByState(active);
    }
}