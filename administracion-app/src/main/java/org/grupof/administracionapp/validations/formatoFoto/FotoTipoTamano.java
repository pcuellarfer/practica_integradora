package org.grupof.administracionapp.validations.formatoFoto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ ElementType.FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = FotoValidator.class)
@Documented
public @interface FotoTipoTamano {
    String message() default "Archivo o tamaño inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
