package org.grupof.administracionapp.validations.paso4DTO.anioTarjetaValido;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.time.Year;

@Component
public class AnioCaducidadValidoValidator implements ConstraintValidator<AnioCaducidadValido, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || !value.matches("\\d{4}")) {
            return false;
        }

        try {
            int anio = Integer.parseInt(value);
            int anioActual = Year.now().getValue();
            return anio >= anioActual && anio <= anioActual + 20;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
