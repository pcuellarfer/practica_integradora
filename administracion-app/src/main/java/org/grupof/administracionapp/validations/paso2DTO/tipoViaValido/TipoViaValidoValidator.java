package org.grupof.administracionapp.validations.paso2DTO.tipoViaValido;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupof.administracionapp.repository.TipoViaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TipoViaValidoValidator implements ConstraintValidator<TipoViaValido, UUID> {

    private final TipoViaRepository tipoViaRepository;

    @Autowired
    public TipoViaValidoValidator(TipoViaRepository tipoViaRepository) {
        this.tipoViaRepository = tipoViaRepository;
    }

    @Override
    public boolean isValid(UUID tipoViaId, ConstraintValidatorContext context) {
        if (tipoViaId == null) return true; // lo valida @NotNull
        return tipoViaRepository.existsById(tipoViaId);
    }
}
