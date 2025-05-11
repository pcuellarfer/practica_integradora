package org.grupof.administracionapp.dto.Nomina;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.entity.LineaNomina;
import org.grupof.administracionapp.entity.embeddable.Periodo;

import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

@Getter
@Setter
public class NominaDTO {

    private UUID empleadoId;

    @Valid
    private PeriodoDTO periodo;

    private List<LineaNomina> lineas;
}
