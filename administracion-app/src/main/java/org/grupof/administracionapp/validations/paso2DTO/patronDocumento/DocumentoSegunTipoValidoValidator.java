package org.grupof.administracionapp.validations.paso2DTO.patronDocumento;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grupof.administracionapp.dto.Empleado.Paso2ContactoDTO;
import org.grupof.administracionapp.entity.registroEmpleado.TipoDocumento;
import org.grupof.administracionapp.repository.TipoDocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class DocumentoSegunTipoValidoValidator implements ConstraintValidator<DocumentoSegunTipoValido, Paso2ContactoDTO> {

    private final TipoDocumentoRepository tipoDocumentoRepository;

    private static final Pattern PATRON_DNI = Pattern.compile("^[0-9]{8}[A-HJ-NP-TV-Z]$");
    private static final Pattern PATRON_NIE = Pattern.compile("^[XYZ][0-9]{7}[A-Z]$");

    @Autowired
    public DocumentoSegunTipoValidoValidator(TipoDocumentoRepository tipoDocumentoRepository) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    @Override
    public boolean isValid(Paso2ContactoDTO dto, ConstraintValidatorContext context) {
        if (dto.getTipoDocumento() == null || dto.getDocumento() == null) {
            return true; //lo controla @NotNull y @NotBlank
        }

        Optional<TipoDocumento> tipoOpt = tipoDocumentoRepository.findById(dto.getTipoDocumento());
        if (tipoOpt.isEmpty()) {
            return true; //ya lo valida @TipoDocumentoValido
        }

        String tipo = tipoOpt.get().getTipo().toUpperCase().trim();
        String doc = dto.getDocumento().toUpperCase().trim();

        boolean valido = switch (tipo) {
            case "DNI" -> PATRON_DNI.matcher(doc).matches();
            case "NIE" -> PATRON_NIE.matcher(doc).matches();
            default -> true;
        };

        if (!valido) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("El formato del documento no es válido para el tipo seleccionado")
                    .addPropertyNode("documento")
                    .addConstraintViolation();
        }

        return valido;
    }
}





