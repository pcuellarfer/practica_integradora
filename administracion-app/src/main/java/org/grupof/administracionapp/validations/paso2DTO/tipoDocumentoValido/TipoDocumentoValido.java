package org.grupof.administracionapp.validations.paso2DTO.tipoDocumentoValido;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TipoDocumentoValidoValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TipoDocumentoValido {
    String message() default "El tipo de documento seleccionado no es válido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
