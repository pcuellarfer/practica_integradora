package org.grupof.administracionapp.dto.Empleado;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.grupof.administracionapp.entity.embeddable.Direccion;

import java.util.UUID;

@Getter
@Setter

public class Paso2ContactoDTO {

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
    private UUID tipoDocumento;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    private String documento;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    private String prefijoTelefono;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    private Integer telefono;

    private Direccion direccion;
}
