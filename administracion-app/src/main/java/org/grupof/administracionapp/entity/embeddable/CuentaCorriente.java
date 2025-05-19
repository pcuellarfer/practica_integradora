package org.grupof.administracionapp.entity.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Data

@Embeddable
public class CuentaCorriente {
    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
    private UUID banco;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    private String numCuenta;
}
