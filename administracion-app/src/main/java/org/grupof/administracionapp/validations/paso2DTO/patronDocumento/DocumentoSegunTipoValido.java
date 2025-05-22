package org.grupof.administracionapp.validations.paso2DTO.patronDocumento;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DocumentoSegunTipoValidoValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DocumentoSegunTipoValido {
    String message() default "El documento no cumple el formato para el tipo seleccionado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
