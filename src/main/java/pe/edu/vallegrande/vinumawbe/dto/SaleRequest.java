package pe.edu.vallegrande.vinumawbe.dto;

import lombok.*;
import java.util.List;

@Data
public class SaleRequest {

    private String clientId;
    private String employeeId;
    private String paymentMethodId;
    private List<ProductRequest> products;

    @Data
    public static class ProductRequest {
        private String productId;
        private Integer amount;
    }
}