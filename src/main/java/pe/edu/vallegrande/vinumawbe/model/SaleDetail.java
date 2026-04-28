package pe.edu.vallegrande.vinumawbe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDetail {

    private String productId;
    private String name;
    private Integer quantity;
    private BigDecimal unitPrice;

    private BigDecimal subtotal;
}
