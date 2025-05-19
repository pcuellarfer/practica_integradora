package org.grupof.administracionapp.dto.nominas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor @Data

//DTO para la primera busqueda de nominas, donde solo va a aparecer el nombre del empleado, la fecha de inicio y la fecha de fin
public class BusquedaNominaDTO {
    private UUID idNomina;

    private String nombreEmpleado;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;
}
