package org.grupof.administracionapp.validations.paso2DTO.prefijoTelefonicoValido;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupof.administracionapp.repository.PaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrefijoTelefonicoValidoValidator implements ConstraintValidator<PrefijoTelefonicoValido, String> {

    private final PaisRepository paisRepository;

    @Autowired
    public PrefijoTelefonicoValidoValidator(PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    @Override
    public boolean isValid(String prefijo, ConstraintValidatorContext context) {
        if (prefijo == null || prefijo.trim().isEmpty()) return true; //lo controla @NotBlank

        return paisRepository.existsByPrefijoTelefonico(prefijo.trim());
    }
}
