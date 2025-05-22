package org.grupof.administracionapp.validations.paso2DTO.tipoViaValido;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TipoViaValidoValidator.class)
@Documented
public @interface TipoViaValido {
    String message() default "El tipo de vía seleccionado no es válido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
