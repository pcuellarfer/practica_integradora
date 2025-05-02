package org.grupof.administracionapp.dto.Empleado;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupof.administracionapp.entity.embeddable.CuentaCorriente;
import org.grupof.administracionapp.entity.embeddable.TarjetaCredito;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Paso4EconomicosDTO {

    @Valid
    private CuentaCorriente cuentaCorriente;

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
//    Debe ser un número real positivo.
//            - El formato es de 8 dígitos enteros y 2
//    decimales.
    private BigDecimal salario;

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
//    Debe ser un número real positivo.
//            - El formato es de 8 dígitos enteros y 2
//    decimales.
    private BigDecimal comision;

    @Valid
    private TarjetaCredito tarjetaCredito;
}
