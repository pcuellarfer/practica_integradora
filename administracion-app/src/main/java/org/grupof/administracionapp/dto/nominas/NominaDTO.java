package org.grupof.administracionapp.dto.nominas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class NominaDTO {

    private UUID empleadoId; //a quien pertenece

    //periodo
    private LocalDate fechaInicio; //cuando inicia
    private LocalDate fechaFin; //cuando acaba

    //he tenido que usar list para identificar las posiciones por que Set no garantiza ningún orden al iterar, lo que impide identificar de forma fiable la primera línea
    private List<LineaNominaDTO> lineasNomina; //las lineas de nomina
    //todos los demas datos los seteo en el backend
}
