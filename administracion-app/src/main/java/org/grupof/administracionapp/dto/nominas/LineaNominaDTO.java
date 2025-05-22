package org.grupof.administracionapp.dto.nominas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor @NoArgsConstructor @Data

public class LineaNominaDTO {
    private String concepto;
    private BigDecimal porcentaje;
    private BigDecimal cantidad;
}
