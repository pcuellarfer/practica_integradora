package org.grupof.administracionapp.dto.Nomina;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PeriodoDTO {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

}
