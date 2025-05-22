package org.grupof.administracionapp.validations.paso1DTO.generoValido;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = GeneroValidoValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface GeneroValido {

    String message() default "genero inexistente en la lista de generos generosos";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
