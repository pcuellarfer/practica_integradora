package org.grupof.administracionapp.dto.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor  @Data
public class RegistroUsuarioDTO {

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\p{Punct}).{8,12}$",
            message = "La contraseña debe tener entre 8 y 12 caracteres, incluyendo una mayúscula, una minúscula, un número y un signo de puntuación."
    )
    private String contrasena;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\p{Punct}).{8,12}$",
            message = "La contraseña debe tener entre 8 y 12 caracteres, incluyendo una mayúscula, una minúscula, un número y un signo de puntuación."
    )
    private String contrasenaConfirmar;

}
