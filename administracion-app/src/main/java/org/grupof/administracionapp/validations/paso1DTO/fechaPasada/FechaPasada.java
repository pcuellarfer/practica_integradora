package org.grupof.administracionapp.validations.paso1DTO.fechaPasada;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FechaPasadaValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FechaPasada {
    String message() default "La fecha debe ser en el pasado y en formato dd-MM-yyyy";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
