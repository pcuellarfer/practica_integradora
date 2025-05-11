package org.grupof.administracionapp.dto.Empleado;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class EmpleadoDTO {

    @NotNull
    private UUID id;
}
