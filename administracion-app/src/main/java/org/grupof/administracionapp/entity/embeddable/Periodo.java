package org.grupof.administracionapp.entity.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Periodo {

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaInicio;

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaFin;

    static public Periodo crearPeriodo(LocalDate fechaInicio, LocalDate fechaFin){
        Periodo periodo = new Periodo();
        periodo.fechaInicio = fechaInicio;
        periodo.fechaFin = fechaFin;
        return periodo;
    }
}
