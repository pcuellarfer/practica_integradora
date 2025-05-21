package org.grupof.administracionapp.validations.paso4DTO.tipoTarjetaValido;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupof.administracionapp.repository.TipoTarjetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TipoTarjetaValidoValidator implements ConstraintValidator<TipoTarjetaValido, UUID> {

    private final TipoTarjetaRepository tipoTarjetaRepository;

    @Autowired
    public TipoTarjetaValidoValidator(TipoTarjetaRepository tipoTarjetaRepository) {
        this.tipoTarjetaRepository = tipoTarjetaRepository;
    }

    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext context) {
        if (id == null) return true; //lo valida @NotNull
        return tipoTarjetaRepository.existsById(id);
    }
}
