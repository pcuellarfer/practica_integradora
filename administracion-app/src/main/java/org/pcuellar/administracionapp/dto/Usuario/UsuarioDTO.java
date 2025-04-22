package org.pcuellar.administracionapp.dto.Usuario;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.pcuellar.administracionapp.auxiliar.TipoUsuario;

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
    private String contrasena;
    private TipoUsuario tipoUsuario;
    private boolean estadoBloqueado = false;
    private LocalDateTime bloqueoFechaHora;
    private String motivoBloqueo;
}
