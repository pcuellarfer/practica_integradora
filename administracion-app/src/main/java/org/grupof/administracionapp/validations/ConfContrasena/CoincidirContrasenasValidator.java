package org.grupof.administracionapp.validations.ConfContrasena;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupof.administracionapp.dto.Usuario.RegistroUsuarioDTO;

public class CoincidirContrasenasValidator implements ConstraintValidator<CoincidirContrasenas, RegistroUsuarioDTO> {

    private String contrasenaField;
    private String contrasenaconfField;

    @Override
    public void initialize(CoincidirContrasenas constraintAnnotation) {
        // Obtener los nombres de los campos que debemos comparar
        this.contrasenaField = constraintAnnotation.contrasena();
        this.contrasenaconfField = constraintAnnotation.confirmContrasena();
    }

    @Override
    public boolean isValid(RegistroUsuarioDTO usuario, ConstraintValidatorContext context) {
        if (usuario == null) {
            return true;
        }

        if (usuario.getContrasena() != null && usuario.getConfirmContrasena() != null
                && !usuario.getContrasena().equals(usuario.getConfirmContrasena())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Las contraseñas no coinciden")
                    .addPropertyNode("confirmContrasena") // Se muestra en confirmContrasena
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
