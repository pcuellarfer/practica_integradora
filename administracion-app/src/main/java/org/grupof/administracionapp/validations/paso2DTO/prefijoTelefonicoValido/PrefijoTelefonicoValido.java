package org.grupof.administracionapp.validations.paso2DTO.prefijoTelefonicoValido;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PrefijoTelefonicoValidoValidator.class)
@Documented
public @interface PrefijoTelefonicoValido {
    String message() default "El prefijo telefónico seleccionado no es válido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
