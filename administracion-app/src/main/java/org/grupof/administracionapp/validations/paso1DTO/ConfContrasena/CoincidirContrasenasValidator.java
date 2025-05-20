package org.grupof.administracionapp.validations.paso1DTO.ConfContrasena;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupof.administracionapp.dto.Usuario.RegistroUsuarioDTO;

/**
 * Validador personalizado para la anotación {@link CoincidirContrasenas}.
 *
 * Esta clase se encarga de comparar los campos de contraseña y confirmación de contraseña
 * en un objeto {@link RegistroUsuarioDTO}, y valida que ambos campos sean iguales.
 * Si no coinciden, se agrega un mensaje de error al campo de confirmación de la contraseña.
 *
 * @author TuNombre
 * @see CoincidirContrasenas
 * @see RegistroUsuarioDTO
 */
public class CoincidirContrasenasValidator implements ConstraintValidator<CoincidirContrasenas, RegistroUsuarioDTO> {

    private String contrasenaField;
    private String contrasenaconfField;

    /**
     * Inicializa el validador con los nombres de los campos de contraseña
     * y confirmación de contraseña definidos en la anotación.
     *
     * @param constraintAnnotation la anotación {@link CoincidirContrasenas} utilizada
     *                             para este validador
     */
    @Override
    public void initialize(CoincidirContrasenas constraintAnnotation) {
        // Obtener los nombres de los campos que debemos comparar
        this.contrasenaField = constraintAnnotation.contrasena();
        this.contrasenaconfField = constraintAnnotation.confirmContrasena();
    }

    /**
     * Valida que los campos de contraseña y confirmación sean iguales.
     *
     * @param usuario el objeto {@link RegistroUsuarioDTO} que contiene las contraseñas
     * @param context el contexto de la validación
     * @return {@code true} si las contraseñas coinciden, {@code false} si no coinciden
     */
    @Override
    public boolean isValid(RegistroUsuarioDTO usuario, ConstraintValidatorContext context) {
        if (usuario == null) {
            return true;
        }

        if (usuario.getContrasena() != null && usuario.getContrasenaConfirmar() != null
                && !usuario.getContrasena().equals(usuario.getContrasenaConfirmar())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Las contraseñas no coinciden")
                    .addPropertyNode("confirmContrasena") // Se muestra en confirmContrasena
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}

