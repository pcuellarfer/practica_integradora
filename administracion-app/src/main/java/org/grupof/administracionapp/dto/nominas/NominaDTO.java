package org.grupof.administracionapp.dto.nominas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupof.administracionapp.entity.nomina.LineaNomina;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class NominaDTO {

    private UUID empleadoId; //a quien pertenece

    //periodo
    private LocalDate fechaInicio; //cuando inicia
    private LocalDate fechaFin; //cuando acaba

    private Set<LineaNominaDTO> lineasNomina; //las lineas de nomina
    //todos los demas datos los seteo en el backend
}
