package org.grupof.administracionapp.validations.ConfContrasena;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Definimos la anotación
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CoincidirContrasenasValidator.class)  // Indica qué validador utilizar
public @interface CoincidirContrasenas {
    String message() default "Las contraseñas no coinciden"; // Mensaje de error predeterminado
    Class<?>[] groups() default {}; // Agrupar validaciones
    Class<? extends Payload>[] payload() default {}; // Datos adicionales de la validación
    String contrasena(); // El nombre del campo 'contrasena'
    String confirmContrasena(); // El nombre del campo 'confirmarContrasena'
}

