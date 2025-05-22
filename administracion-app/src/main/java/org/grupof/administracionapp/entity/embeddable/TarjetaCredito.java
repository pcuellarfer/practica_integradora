package org.grupof.administracionapp.entity.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupof.administracionapp.validations.paso4DTO.anioTarjetaValido.AnioCaducidadValido;
import org.grupof.administracionapp.validations.paso4DTO.tipoTarjetaValido.TipoTarjetaValido;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Embeddable
public class TarjetaCredito {

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
    @TipoTarjetaValido
    private UUID tipoTarjeta;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    @Pattern(regexp = "^\\d{16}$", message = "Debe tener exactamente 16 dígitos")
    private String numTarjeta;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    @Pattern(regexp = "^(0[1-9]|1[0-2])$", message = "Debe ser un mes válido entre 01 y 12")
    private String mesCaducidad;

    @NotBlank
    @AnioCaducidadValido
    private String anoCaducidad;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    private String cvc;
}
