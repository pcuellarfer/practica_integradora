package org.grupof.administracionapp.validations.paso2DTO.tipoDocumentoValido;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupof.administracionapp.repository.TipoDocumentoRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TipoDocumentoValidoValidator implements ConstraintValidator<TipoDocumentoValido, UUID> {

    private final TipoDocumentoRepository tipoDocumentoRepository;

    public TipoDocumentoValidoValidator(TipoDocumentoRepository tipoDocumentoRepository) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext context) {
        if (id == null) {
            return true; //lo controla @NotNull
        }
        return tipoDocumentoRepository.existsById(id);
    }
}
