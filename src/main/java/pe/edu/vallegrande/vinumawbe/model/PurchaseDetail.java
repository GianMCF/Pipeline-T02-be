package pe.edu.vallegrande.vinumawbe.model;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class PurchaseDetail {

    @NotNull
    private Integer supplyId;

    @Min(1)
    private Integer quantity;

    @NotNull
    private BigDecimal unitPrice;

    private BigDecimal subtotal;
}