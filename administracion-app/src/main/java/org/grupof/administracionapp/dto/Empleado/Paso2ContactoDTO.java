package org.grupof.administracionapp.dto.Empleado;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.grupof.administracionapp.entity.embeddable.Direccion;
import org.grupof.administracionapp.validations.paso2DTO.patronDocumento.DocumentoSegunTipoValido;
import org.grupof.administracionapp.validations.paso2DTO.prefijoTelefonicoValido.PrefijoTelefonicoValido;
import org.grupof.administracionapp.validations.paso2DTO.tipoDocumentoValido.TipoDocumentoValido;

import java.util.UUID;

@Getter
@Setter

@DocumentoSegunTipoValido
public class Paso2ContactoDTO {

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
    @TipoDocumentoValido
    private UUID tipoDocumento;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    //comprobar pattern segun el tipo de documento, validacion de clase
    private String documento;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    @PrefijoTelefonicoValido
    private String prefijoTelefono;

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
    @Min(value = 100000000, message = "Debe tener exactamente 9 dígitos") //un poco chapa, pero estoy cansadisimo de validaciones
    @Max(value = 999999999, message = "Debe tener exactamente 9 dígitos")
    private Integer telefono;

    @Valid
    private Direccion direccion;
}
