package org.grupof.administracionapp.validations.ConfContrasena;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación personalizada para validar que las contraseñas coincidan.
 *
 * Esta anotación debe aplicarse a una clase y utiliza el validador {@link CoincidirContrasenasValidator}
 * para comparar los valores de los campos de contraseña.
 *
 * @see CoincidirContrasenasValidator
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CoincidirContrasenasValidator.class)
public @interface CoincidirContrasenas {

    /**
     * Mensaje de error por defecto si las contraseñas no coinciden.
     *
     * @return el mensaje de error
     */
    String message() default "Las contraseñas no coinciden";

    /**
     * Los grupos de validación.
     *
     * @return los grupos de validación
     */
    Class<?>[] groups() default {};

    /**
     * Datos adicionales de la validación.
     *
     * @return datos adicionales
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * El campo de la contraseña.
     *
     * @return el nombre del campo de la contraseña
     */
    String contrasena();

    /**
     * El campo de confirmación de la contraseña.
     *
     * @return el nombre del campo de confirmación
     */
    String confirmContrasena();
}

