package org.pcuellar.administracionapp.dto.Empleado;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoDTO {
    @NotNull(message = "El nombre no puede estar vacio")
    private String nombre = " ";
    @NotNull
    private String email;
    @NotNull
    private String contrasena;
}
