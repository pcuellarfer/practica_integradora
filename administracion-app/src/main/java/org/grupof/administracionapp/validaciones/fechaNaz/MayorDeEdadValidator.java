package org.grupof.administracionapp.validaciones.fechaNaz;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class MayorDeEdadValidator implements ConstraintValidator<MayorDeEdad, LocalDate> {

    @Override
    public boolean isValid(LocalDate fechaNacimiento, ConstraintValidatorContext context) {
        if (fechaNacimiento == null) {
            return true;  // Si la fecha es null delega a @NotNull
        }

        //compara la fecha con la fecha actual y calcula la edad
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        return edad >= 18;
    }
}