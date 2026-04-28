package pe.edu.vallegrande.vinumawbe.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "purchases")
public class Purchase {

    @Id
    private String id;

    @NotNull
    private BigDecimal total;

    @NotNull
    private Integer suppliersId;

    private Boolean status = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime restoredAt;

    private List<PurchaseDetail> details;
}