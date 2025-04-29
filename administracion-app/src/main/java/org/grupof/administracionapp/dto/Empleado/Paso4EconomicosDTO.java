package org.grupof.administracionapp.dto.Empleado;


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
    private CuentaCorriente cuentaCorriente;
    private BigDecimal salario;
    private BigDecimal comision;
    private TarjetaCredito tarjetaCredito;
}
