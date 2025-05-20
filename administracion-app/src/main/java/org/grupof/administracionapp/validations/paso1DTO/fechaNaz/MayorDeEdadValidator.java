package org.grupof.administracionapp.validations.paso1DTO.fechaNaz;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class MayorDeEdadValidator implements ConstraintValidator<MayorDeEdad, LocalDate> {

    @Override
    public boolean isValid(LocalDate fechaNacimiento, ConstraintValidatorContext context) {
        if (fechaNacimiento == null) {
            return true;  //si la fecha es null delega a @NotNull
        }

        if (fechaNacimiento.isAfter(LocalDate.now())) return false;

        //compara la fecha con la fecha actual y calcula la edad
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        return edad >= 18;
    }
}