package org.grupof.administracionapp.validations.paso4DTO.bancoValido;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupof.administracionapp.repository.BancoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BancoValidoValidator implements ConstraintValidator<BancoValido, UUID> {

    private final BancoRepository bancoRepository;

    @Autowired
    public BancoValidoValidator(BancoRepository bancoRepository) {
        this.bancoRepository = bancoRepository;
    }

    @Override
    public boolean isValid(UUID idBanco, ConstraintValidatorContext context) {
        if (idBanco == null) return true; //lo controla @NotNull
        return bancoRepository.existsById(idBanco);
    }
}
