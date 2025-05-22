package org.grupof.administracionapp.validations.paso1DTO.fechaPasada;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FechaPasadaValidator implements ConstraintValidator<FechaPasada, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // Permite valores nulos o vacíos si no son obligatorios
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate fecha = LocalDate.parse(value, formatter);
            if (fecha.isAfter(LocalDate.now())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("La fecha debe ser en el pasado").addConstraintViolation();
                return false;
            }
            return true;
        } catch (DateTimeParseException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Formato de fecha incorrecto (dd-MM-yyyy)").addConstraintViolation();
            return false;
        }
    }
}