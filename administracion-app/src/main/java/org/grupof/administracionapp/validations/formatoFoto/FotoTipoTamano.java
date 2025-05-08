<<<<<<<< HEAD:administracion-app/src/main/java/org/grupof/administracionapp/validations/archivo/FotoTipoTamano.java
package org.grupof.administracionapp.validations.archivo;
========
package org.grupof.administracionapp.validations.formatoFoto;
>>>>>>>> b3478ef7d51786e245b5f796ef6079acf741a22c:administracion-app/src/main/java/org/grupof/administracionapp/validations/formatoFoto/FotoTipoTamano.java

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
