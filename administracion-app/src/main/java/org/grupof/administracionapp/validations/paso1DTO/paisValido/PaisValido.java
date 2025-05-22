package org.grupof.administracionapp.validations.paso1DTO.paisValido;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PaisValidoValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PaisValido {
    String message() default "El país seleccionado no es válido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}