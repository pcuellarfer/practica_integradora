package org.grupof.administracionapp.dto.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.grupof.administracionapp.auxiliar.TipoUsuario;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UsuarioDTO {
    private UUID id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\p{Punct}).{8,12}$",
            message = "La contraseña debe tener entre 8 y 12 caracteres, incluyendo una mayúscula, una minúscula, un número y un signo de puntuación."
    )
    private String contrasena;

    private TipoUsuario tipoUsuario;
    private boolean estadoBloqueado = false;
    private LocalDateTime bloqueoFechaHora;
    private String motivoBloqueo;
}
