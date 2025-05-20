package org.grupof.administracionapp.validations.paso1DTO.paisValido;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupof.administracionapp.repository.PaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaisValidoValidator implements ConstraintValidator<PaisValido, UUID> {

    @Autowired
    private PaisRepository paisRepository;

    @Override
    public boolean isValid(UUID paisId, ConstraintValidatorContext context) {
        if (paisId == null) {
            return true; //que lo valide null
        }

        return paisRepository.existsById(paisId);
    }
}
