package pe.edu.vallegrande.vinumawbe.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Data
@Document(collection = "employee")
public class Employee {

    @Id
    private String id;
    @NotBlank(message = "El nombre es un campo obligatorio")
    private String name;
    @NotBlank(message = "Los apellidos son un campo obligatorio")
    private String surname;
    @NotBlank(message = "El Número Telefónico es un campo obligatorio")
    @Size(max = 9)
    private String phoneNum;
    @Indexed(unique = true)
    private String email;
    @Min(18)
    private Integer age;
    @NotBlank(message = "El Tipo de Documento es un campo obligatorio")
    @Size(max = 3)
    private String docType;
    @NotBlank(message = "El Número de Documento es un campo obligatorio")
    @Size(max = 20)
    private String docNum;
    @NotBlank(message = "El cargo es un campo obligatorio")
    private String position;
    @NotBlank(message = "El salario es un campo obligatorio")
    @Min(1025)
    private Double salary;
    private Boolean status;
    private LocalDateTime addedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime restoredAt;
}

