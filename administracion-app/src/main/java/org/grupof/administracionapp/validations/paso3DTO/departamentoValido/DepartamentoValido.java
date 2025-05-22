package org.grupof.administracionapp.validations.paso3DTO.departamentoValido;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DepartamentoValidoValidator.class)
@Documented
public @interface DepartamentoValido {
    String message() default "El departamento seleccionado no es válido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
