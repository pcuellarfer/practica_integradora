package org.grupof.administracionapp.entity.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupof.administracionapp.validations.paso2DTO.tipoViaValido.TipoViaValido;

import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Data

@Embeddable
public class Direccion {

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
    @TipoViaValido
    private UUID tipoVia;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    private String nombreDireccion;

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
    @Pattern(regexp = "\\d+", message = "Solo se permiten números enteros positivos")
    private String numeroDireccion;

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
    private String portal;

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
    @Pattern(regexp = "\\d+", message = "Solo se permiten números enteros positivos")
    public String planta;

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
    public String puerta;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    private String localidad;

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
    private String region;

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
    private Integer codigoPostal;
}
