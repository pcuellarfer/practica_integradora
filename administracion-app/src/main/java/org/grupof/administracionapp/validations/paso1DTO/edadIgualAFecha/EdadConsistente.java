package org.grupof.administracionapp.validations.paso1DTO.edadIgualAFecha;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EdadConsistenteValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EdadConsistente {
    String message() default "La edad no coincide con la fecha de nacimiento";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
