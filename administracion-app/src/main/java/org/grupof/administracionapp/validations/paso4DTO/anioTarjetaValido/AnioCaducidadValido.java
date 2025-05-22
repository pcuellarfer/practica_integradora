package org.grupof.administracionapp.validations.paso4DTO.anioTarjetaValido;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AnioCaducidadValidoValidator.class)
public @interface AnioCaducidadValido {
    String message() default "Debe ser un año válido (desde el actual hasta dentro de 20 años)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
