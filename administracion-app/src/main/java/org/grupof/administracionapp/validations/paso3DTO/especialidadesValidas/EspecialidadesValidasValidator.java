package org.grupof.administracionapp.validations.paso3DTO.especialidadesValidas;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupof.administracionapp.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
public class EspecialidadesValidasValidator implements ConstraintValidator<EspecialidadesValidas, Set<UUID>> {

    private final EspecialidadRepository especialidadRepository;

    @Autowired
    public EspecialidadesValidasValidator(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    @Override
    public boolean isValid(Set<UUID> especialidades, ConstraintValidatorContext context) {
        if (especialidades == null || especialidades.isEmpty()) return true; //lo gestiona @NotEmpty

        return especialidades.stream().allMatch(especialidadRepository::existsById);
    }
}
