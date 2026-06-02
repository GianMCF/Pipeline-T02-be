package pe.edu.vallegrande.vinumawbe.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "user")
public class User{

    @Id
    private String id;
    @NotBlank(message = "El nombre es un campo obligatorio")
    @Indexed(unique = true)
    private String username;
    @NotBlank(message = "El correo es un campo obligatorio")
    @Indexed(unique = true)
    private String email;
    @NotBlank(message = "La contraseña es un campo obligatorio")
    @Size(min = 6)
    private String password;
    private String role;
    private Boolean status;
    private LocalDateTime addedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime restoredAt;
}
