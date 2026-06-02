package pe.edu.vallegrande.vinumawbe.model.ref;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentMethodRef {

    private String paymentMethodId;
    private String name;
    private Double extraCharges;
}