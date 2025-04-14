package org.pcuellar.administracionapp.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class EmpleadoDTO {
    private UUID id;
    private String departamento;
    private String puesto;
    private BigDecimal salario;
    private NominaDTO nomina;
}
