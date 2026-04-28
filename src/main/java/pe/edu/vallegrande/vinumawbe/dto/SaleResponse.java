package pe.edu.vallegrande.vinumawbe.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaleResponse {

    private String saleId;
    private BigDecimal total;
    private LocalDateTime saleDate;
    private Boolean status;

    private ClientDto client;
    private List<ProductDto> products;

    @Data
    public static class ClientDto {
        private String clientId;
        private String name;
        private String surname;
        private String docNum;
    }

    @Data
    public static class ProductDto {
        private String name;
        private Integer quantity;
        private BigDecimal subtotal;
    }
}
