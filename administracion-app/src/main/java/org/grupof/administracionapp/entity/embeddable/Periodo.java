package org.grupof.administracionapp.entity.embeddable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Periodo {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    static public Periodo crearPeriodo(LocalDate fechaInicio, LocalDate fechaFin){
        Periodo periodo = new Periodo();
        periodo.fechaInicio = fechaInicio;
        periodo.fechaFin = fechaFin;
        return periodo;
    }
}
