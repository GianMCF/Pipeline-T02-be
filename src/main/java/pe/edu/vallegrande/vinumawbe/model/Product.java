package pe.edu.vallegrande.vinumawbe.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 150)
    private String name;

    @NotBlank(message = "La categoría es requerida")
    @Size(max = 30)
    private String category;

    @Min(0)
    private Integer stock = 0;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal unitPrice;

    private Boolean status = true;

   
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt; // SIN valor inicial
    private LocalDateTime deletedAt;
    private LocalDateTime restoredAt;

    // Extras
    @Min(0)
    private Integer volumeMl;

    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private BigDecimal alcoholPercentage;
}