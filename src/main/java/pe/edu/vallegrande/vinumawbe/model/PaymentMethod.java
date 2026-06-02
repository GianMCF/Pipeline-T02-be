package pe.edu.vallegrande.vinumawbe.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "payment_method")
public class PaymentMethod {

    @Id
    private String id;
    @NotBlank(message = "El nombre del método es un campo obligatorio")
    private String name;
    private String description;
    @Min(0)
    private Double extraCharges;
    private Boolean status;
    private LocalDateTime addedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime restoredAt;
}