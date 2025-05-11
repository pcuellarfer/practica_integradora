package org.grupof.administracionapp.dto.Nomina;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class LineaNominaDTO {

    private String concepto;

    //private BigDecimal cantidad;

    private BigDecimal importe;
}
