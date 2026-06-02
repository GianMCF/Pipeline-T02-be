package pe.edu.vallegrande.vinumawbe.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.vinumawbe.model.PaymentMethod;
import pe.edu.vallegrande.vinumawbe.service.PaymentMethodService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/api/payment_method")
@Tag(name = "Métodos de pago", description = "CRUD reactivo de métodos de pago")
public class PaymentMethodRest {

    private final PaymentMethodService paymentMethodService;

    @Autowired
    public PaymentMethodRest(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @Operation(summary = "Listar todos los métodos de pago", description = "Lista a todos los  métodos de pago en la base de datos")
    @GetMapping
    public Flux<PaymentMethod> findAll() {
        return paymentMethodService.findAll();
    }

    @Operation(summary = "Hallar método de pago por ID", description = "Lista a un solo método de pago en la base de datos")
    @GetMapping("/{id}")
    public Mono<PaymentMethod> findById(@PathVariable String id) {
        return paymentMethodService.findById(id);
    }

    @Operation(summary = "Registrar método de pago", description = "Registra un nuevo método de pago en la base de datos")
    @PostMapping("/save")
    public Mono<PaymentMethod> save(@RequestBody PaymentMethod paymentMethod) {
        return paymentMethodService.save(paymentMethod);
    }

    @Operation(summary = "Actualizar método de pago", description = "Modifica datos de un método de pago en la base de datos")
    @PutMapping ("/update")
    public Mono<PaymentMethod> update(@RequestBody PaymentMethod paymentMethod) {
        return paymentMethodService.update(paymentMethod);
    }

    @Operation(summary = "Eliminar lógicamente a un método de pago", description = "Desactivar a un método de pago en la base de datos")
    @DeleteMapping("/delete/{id}")
    public Mono<PaymentMethod> delete(@PathVariable String id) {
        return paymentMethodService.delete(id);
    }

    @Operation(summary = "Restaurar lógicamente a un método de pago", description = "Re-activar a un método de pago en la base de datos")
    @PutMapping("/restore/{id}")
    public Mono<PaymentMethod> restore(@PathVariable String id) {
        return paymentMethodService.restore(id);
    }
}