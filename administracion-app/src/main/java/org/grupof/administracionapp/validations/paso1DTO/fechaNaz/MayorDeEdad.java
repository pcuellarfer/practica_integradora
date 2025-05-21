package org.grupof.administracionapp.validations.paso1DTO.fechaNaz;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MayorDeEdadValidator.class)
@Documented
public @interface MayorDeEdad {
    String message() default "Debe ser mayor de 18 años";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
