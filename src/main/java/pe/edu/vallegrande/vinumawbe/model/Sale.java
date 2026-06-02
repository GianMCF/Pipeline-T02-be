package pe.edu.vallegrande.vinumawbe.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pe.edu.vallegrande.vinumawbe.model.ref.ClientRef;
import pe.edu.vallegrande.vinumawbe.model.ref.EmployeeRef;
import pe.edu.vallegrande.vinumawbe.model.ref.PaymentMethodRef;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "sale")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sale {

    @Id
    private String id;

    @NotBlank(message = "El cliente es un campo obligatorio")
    private ClientRef client;
    @NotBlank(message = "El empleado es un campo obligatorio")
    private EmployeeRef employee;
    @NotBlank(message = "El método de pago es un campo obligatorio")
    private PaymentMethodRef paymentMethod;

    private LocalDateTime saleDate = LocalDateTime.now();
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime restoredAt;
    private Boolean status = true;

    private List<SaleDetail> products;

    private BigDecimal total;
}