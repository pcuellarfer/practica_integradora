package org.grupof.administracionapp.validations.paso3DTO.especialidadesValidas;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EspecialidadesValidasValidator.class)
@Documented
public @interface EspecialidadesValidas {
    String message() default "Una o más especialidades seleccionadas no son válidas";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
