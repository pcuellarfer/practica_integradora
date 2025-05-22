package org.grupof.administracionapp.validations.paso1DTO.generoValido;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupof.administracionapp.repository.GeneroRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GeneroValidoValidator implements ConstraintValidator<GeneroValido, UUID> {

    //validacion para comprobar si el genero que llega esta entre los existentes en la tabla genero

    private final GeneroRepository generoRepository;

    public GeneroValidoValidator(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    @Override
    public boolean isValid(UUID generoId, ConstraintValidatorContext context) {
        // Si es null, que lo valide @NotNull
        if (generoId == null) {
            return true;
        }

        return generoRepository.existsById(generoId);
    }
}