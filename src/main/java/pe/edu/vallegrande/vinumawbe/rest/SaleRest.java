package pe.edu.vallegrande.vinumawbe.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.vinumawbe.dto.SaleRequest;
import pe.edu.vallegrande.vinumawbe.dto.SaleResponse;
import pe.edu.vallegrande.vinumawbe.model.Sale;
import pe.edu.vallegrande.vinumawbe.service.SaleService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/api/sale")
@Tag(name = "Ventas", description = "CRUD reactivo de ventas")
public class SaleRest {

    private final SaleService SaleService;

    @Autowired
    public SaleRest(SaleService SaleService) {
        this.SaleService = SaleService;
    }

    @Operation(summary = "Listar todas las ventas", description = "Lista a todas las ventas en la base de datos")
    @GetMapping
    public Flux<Sale> findAll() {
        return SaleService.findAll();
    }

    @Operation(summary = "Hallar venta por ID", description = "Lista a un solo venta en la base de datos")
    @GetMapping("/{id}")
    public Mono<Sale> findById(@PathVariable String id) {
        return SaleService.findById(id);
    }

    @Operation(summary = "Registrar venta", description = "Registra una nueva venta en la base de datos")
    @PostMapping("/save")
    public Mono<SaleResponse> save(@RequestBody SaleRequest request) {
        return SaleService.createSale(request);
    }

    @Operation(summary = "Actualizar venta", description = "Modifica datos de una venta en la base de datos")
    @PutMapping ("/update")
    public Mono<Sale> update(@RequestBody Sale sale) {
        return SaleService.update(sale);
    }

    @Operation(summary = "Eliminar lógicamente a una venta", description = "Desactivar a una venta en la base de datos")
    @DeleteMapping("/delete/{id}")
    public Mono<Sale> delete(@PathVariable String id) {
        return SaleService.delete(id);
    }

    @Operation(summary = "Restaurar lógicamente a una venta", description = "Re-activar a una venta en la base de datos")
    @PutMapping("/restore/{id}")
    public Mono<Sale> restore(@PathVariable String id) {
        return SaleService.restore(id);
    }
}