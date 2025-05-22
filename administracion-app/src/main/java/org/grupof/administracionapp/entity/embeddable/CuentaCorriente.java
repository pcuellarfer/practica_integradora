package org.grupof.administracionapp.entity.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupof.administracionapp.validations.paso4DTO.bancoValido.BancoValido;

import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Data

@Embeddable
public class CuentaCorriente {

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
    @BancoValido
    private UUID banco;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    @Pattern( //patron de IBAN, dos letras mayusculas 22 digitos
            regexp = "^[A-Z]{2}\\d{22}$",
            message = "Debe contener exactamente 2 letras seguidas de 22 dígitos"
    )
    private String numCuenta;
}
