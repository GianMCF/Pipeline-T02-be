package pe.edu.vallegrande.vinumawbe.model.ref;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientRef {
    private String clientId;
    private String name;
    private String surname;
    private String docNum;
}