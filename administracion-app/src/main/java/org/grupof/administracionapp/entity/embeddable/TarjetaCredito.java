package org.grupof.administracionapp.entity.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Embeddable
public class TarjetaCredito {

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
    private UUID tipoTarjeta;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    private String numTarjeta;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    private String mesCaducidad;

    @NotBlank
    private String anoCaducidad;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    private String cvc;
}
