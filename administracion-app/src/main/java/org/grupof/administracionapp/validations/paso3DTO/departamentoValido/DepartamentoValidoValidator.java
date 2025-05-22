package org.grupof.administracionapp.validations.paso3DTO.departamentoValido;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupof.administracionapp.repository.DepartamentoRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DepartamentoValidoValidator implements ConstraintValidator<DepartamentoValido, UUID> {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoValidoValidator(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public boolean isValid(UUID departamentoId, ConstraintValidatorContext context) {
        if (departamentoId == null) return true; //lo controla @NotNull
        return departamentoRepository.existsById(departamentoId);
    }
}
