package pe.edu.vallegrande.vinumawbe.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import pe.edu.vallegrande.vinumawbe.model.Product;
import pe.edu.vallegrande.vinumawbe.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/api/products")
@Tag(name = "Productos", description = "CRUD reactivo de productos")
public class ProductRest {

    @Autowired
    private ProductService service;

    // =============================
    // LISTAR TODOS
    // =============================
    @Operation(summary = "Listar todos los productos")
    @GetMapping
    public Flux<Product> getAll() {
        return service.getAll();
    }

    // =============================
    // BUSCAR POR ID
    // =============================
    @Operation(summary = "Buscar producto por ID")
    @GetMapping("/{id}")
    public Mono<Product> getById(@PathVariable String id) {
        return service.getById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Producto no encontrado con ID: " + id)));
    }

    // =============================
    // CREAR PRODUCTO
    // =============================
    @Operation(summary = "Crear producto")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> create(@Valid @RequestBody Product product) {
        return service.create(product);
    }

    // =============================
    // ACTUALIZAR PRODUCTO
    // =============================
    @Operation(summary = "Actualizar producto")
    @PutMapping("/{id}")
    public Mono<Product> update(@PathVariable String id,
                               @Valid @RequestBody Product product) {
        return service.update(id, product)
                .switchIfEmpty(Mono.error(new RuntimeException("No se pudo actualizar. Producto no encontrado o inactivo")));
    }

    // =============================
    // ELIMINADO LÓGICO
    // =============================
    @Operation(summary = "Eliminar producto (lógico)")
    @PatchMapping("/{id}/delete")
    public Mono<String> logicalDelete(@PathVariable String id) {
        return service.logicalDelete(id)
                .then(Mono.just("Producto eliminado correctamente (lógico)"))
                .onErrorResume(e -> Mono.just("Error al eliminar: " + e.getMessage()));
    }

    // =============================
    // RESTAURAR PRODUCTO
    // =============================
    @Operation(summary = "Restaurar producto")
    @PatchMapping("/{id}/restore")
    public Mono<String> restore(@PathVariable String id) {
        return service.restore(id)
                .then(Mono.just("Producto restaurado correctamente"))
                .onErrorResume(e -> Mono.just("Error al restaurar: " + e.getMessage()));
    }

    // =============================
    // FILTRAR POR ESTADO
    // =============================
    @Operation(summary = "Listar productos por estado")
    @GetMapping("/by-state")
    public Flux<Product> getByState(@RequestParam boolean active) {
        return service.getByState(active);
    }
}