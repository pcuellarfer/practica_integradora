package org.grupof.administracionapp.validations.paso4DTO.tipoTarjetaValido;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TipoTarjetaValidoValidator.class)
@Documented
public @interface TipoTarjetaValido {
    String message() default "El tipo de tarjeta seleccionado no es válido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
