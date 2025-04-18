package org.pcuellar.administracionapp.Model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Getter
@Setter
@Data
@Component
@SessionScope
@AllArgsConstructor
@NoArgsConstructor
public class Empleado {
    @NotNull(message = "El nombre no puede estar vacio")
    private String nombre = " ";
    @NotNull
    private String email;
    @NotNull
    private String contrasena;
}
