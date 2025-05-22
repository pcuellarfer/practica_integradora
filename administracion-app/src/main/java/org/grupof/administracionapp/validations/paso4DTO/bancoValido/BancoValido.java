package org.grupof.administracionapp.validations.paso4DTO.bancoValido;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BancoValidoValidator.class)
@Documented
public @interface BancoValido {
    String message() default "La entidad bancaria seleccionada no es válida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
