package org.grupof.administracionapp.dto.nominas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleNominaDTO {

    private UUID idNomina;

    private UUID empleadoId;

    private String nombreEmpleado;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private BigDecimal salarioNeto;

    private List<LineaNominaDTO> lineasNomina;
}
