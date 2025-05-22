package org.grupof.administracionapp.validations.paso1DTO.edadIgualAFecha;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupof.administracionapp.dto.Empleado.Paso1PersonalDTO;

import java.time.LocalDate;
import java.time.Period;

public class EdadConsistenteValidator implements ConstraintValidator<EdadConsistente, Paso1PersonalDTO> {

    @Override
    public boolean isValid(Paso1PersonalDTO dto, ConstraintValidatorContext context) {
        if (dto.getFechaNacimiento() == null || dto.getEdad() == null) {
            return true; //que lo valide null
        }

        int edadCalculada = Period.between(dto.getFechaNacimiento(), LocalDate.now()).getYears();
        return dto.getEdad().equals(edadCalculada);
    }
}
