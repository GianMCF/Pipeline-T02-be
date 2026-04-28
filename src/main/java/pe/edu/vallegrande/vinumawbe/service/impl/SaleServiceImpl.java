package pe.edu.vallegrande.vinumawbe.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.vinumawbe.dto.SaleRequest;
import pe.edu.vallegrande.vinumawbe.dto.SaleResponse;
import pe.edu.vallegrande.vinumawbe.model.*;
import pe.edu.vallegrande.vinumawbe.model.ref.ClientRef;
import pe.edu.vallegrande.vinumawbe.model.ref.EmployeeRef;
import pe.edu.vallegrande.vinumawbe.model.ref.PaymentMethodRef;
import pe.edu.vallegrande.vinumawbe.repository.SaleRepository;
import pe.edu.vallegrande.vinumawbe.service.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final ClientService clientService;
    private final ProductService productService;
    private final EmployeeService employeeService;
    private final PaymentMethodService paymentMethodService;

    public SaleServiceImpl(
            SaleRepository saleRepository,
            ClientService clientService,
            ProductService productService,
            EmployeeService employeeService,
            PaymentMethodService paymentMethodService
    ) {
        this.saleRepository = saleRepository;
        this.clientService = clientService;
        this.productService = productService;
        this.employeeService = employeeService;
        this.paymentMethodService = paymentMethodService;
    }

    // =========================
    // CRUD BÁSICO
    // =========================

    @Override
    public Flux<Sale> findAll() {
        log.info("Listando todas las ventas");
        return saleRepository.findAll();
    }

    @Override
    public Mono<Sale> findById(String id) {
        log.info("Buscando venta por ID: {}", id);
        return saleRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Venta no encontrada")));
    }

    @Override
    public Mono<Sale> update(Sale sale) {
        log.info("Actualizando venta: {}", sale.getId());
        sale.setUpdatedAt(LocalDateTime.now());
        return saleRepository.save(sale);
    }

    // =========================
    // CREAR VENTA
    // =========================

    @Override
    public Mono<SaleResponse> createSale(SaleRequest request) {

        if (request.getProducts() == null || request.getProducts().isEmpty()) {
            return Mono.error(new RuntimeException("La venta debe tener al menos un producto"));
        }

        return Mono.zip(
                        getClient(request),
                        getEmployee(request),
                        getPaymentMethod(request)
                )
                .flatMap(tuple -> buildSale(request, tuple))
                .flatMap(saleRepository::save)
                .map(this::mapToResponse);
    }

    // =========================
    // MÉTODOS AUXILIARES
    // =========================

    private Mono<Client> getClient(SaleRequest request) {
        return clientService.findById(String.valueOf(request.getClientId()))
                .switchIfEmpty(Mono.error(new RuntimeException("Cliente no encontrado")));
    }

    private Mono<Employee> getEmployee(SaleRequest request) {
        return employeeService.findById(String.valueOf(request.getEmployeeId()))
                .switchIfEmpty(Mono.error(new RuntimeException("Empleado no encontrado")));
    }

    private Mono<PaymentMethod> getPaymentMethod(SaleRequest request) {
        return paymentMethodService.findById(String.valueOf(request.getPaymentMethodId()))
                .switchIfEmpty(Mono.error(new RuntimeException("Método de pago no encontrado")));
    }

    private Mono<Sale> buildSale(SaleRequest request, Tuple3<Client, Employee, PaymentMethod> tuple) {

        ClientRef clientRef = mapClient(tuple.getT1());
        EmployeeRef employeeRef = mapEmployee(tuple.getT2());
        PaymentMethodRef paymentRef = mapPayment(tuple.getT3());

        return buildDetails(request.getProducts())
                .map(details -> {

                    BigDecimal total = calculateTotal(details);

                    return Sale.builder()
                            .client(clientRef)
                            .employee(employeeRef)
                            .paymentMethod(paymentRef)
                            .products(details)
                            .total(total)
                            .status(false)
                            .saleDate(LocalDateTime.now())
                            .build();
                });
    }

    private Mono<List<SaleDetail>> buildDetails(List<SaleRequest.ProductRequest> products) {

        return Flux.fromIterable(products)
                .flatMap(p ->
                        productService.getById(p.getProductId())
                                .switchIfEmpty(Mono.error(new RuntimeException("Producto no encontrado: " + p.getProductId())))
                                .map(product -> {

                                    log.info("Procesando producto: {}", product.getName());

                                    BigDecimal unitPrice = product.getUnitPrice() != null
                                            ? product.getUnitPrice()
                                            : BigDecimal.ZERO;

                                    int qty = p.getAmount();

                                    return SaleDetail.builder()
                                            .productId(product.getId().toString())
                                            .name(product.getName())
                                            .quantity(qty)
                                            .unitPrice(unitPrice)
                                            .subtotal(unitPrice.multiply(BigDecimal.valueOf(qty)))
                                            .build();
                                })
                )
                .collectList();
    }

    private BigDecimal calculateTotal(List<SaleDetail> details) {
        return details.stream()
                .map(SaleDetail::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // =========================
    // MAPPERS
    // =========================

    private ClientRef mapClient(Client client) {
        return ClientRef.builder()
                .clientId(client.getId().toString())
                .name(client.getName())
                .surname(client.getSurname())
                .docNum(client.getDocNum())
                .build();
    }

    private EmployeeRef mapEmployee(Employee employee) {
        return EmployeeRef.builder()
                .employeeId(employee.getId().toString())
                .name(employee.getName())
                .surname(employee.getSurname())
                .position(employee.getPosition())
                .build();
    }

    private PaymentMethodRef mapPayment(PaymentMethod payment) {
        return PaymentMethodRef.builder()
                .paymentMethodId(payment.getId().toString())
                .name(payment.getName())
                .extraCharges(payment.getExtraCharges())
                .build();
    }

    private SaleResponse mapToResponse(Sale sale) {

        SaleResponse response = new SaleResponse();

        response.setSaleId(sale.getId());
        response.setTotal(sale.getTotal());
        response.setSaleDate(sale.getSaleDate());
        response.setStatus(sale.getStatus());

        // Cliente
        SaleResponse.ClientDto clientDto = new SaleResponse.ClientDto();
        clientDto.setClientId(sale.getClient().getClientId());
        clientDto.setName(sale.getClient().getName());
        clientDto.setSurname(sale.getClient().getSurname());
        clientDto.setDocNum(sale.getClient().getDocNum());

        response.setClient(clientDto);

        // Productos
        List<SaleResponse.ProductDto> productDtos = sale.getProducts().stream().map(d -> {
            SaleResponse.ProductDto dto = new SaleResponse.ProductDto();
            dto.setName(d.getName());
            dto.setQuantity(d.getQuantity());
            dto.setSubtotal(d.getSubtotal());
            return dto;
        }).toList();

        response.setProducts(productDtos);

        return response;
    }

    @Override
    public Mono<Sale> delete(String id) {

        log.info("Eliminando cliente (lógico) con id: " + id);

        return saleRepository.findById(id)
                .flatMap(sale -> {
                    sale.setStatus(false);
                    sale.setDeletedAt(LocalDateTime.now());
                    return saleRepository.save(sale);
                });
    }

    @Override
    public Mono<Sale> restore(String id) {

        log.info("Restaurando cliente (lógico) con id: " + id);

        return saleRepository.findById(id)
                .flatMap(sale -> {
                    sale.setStatus(true);
                    sale.setRestoredAt(LocalDateTime.now());
                    return saleRepository.save(sale);
                });
    }
}