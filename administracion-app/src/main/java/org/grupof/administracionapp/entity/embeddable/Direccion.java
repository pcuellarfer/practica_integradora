package org.grupof.administracionapp.entity.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Data

@Embeddable
public class Direccion {

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
//    Si se recibe un tipo de vía que no se
//    encuentra entre los que aparecen en la tabla
//    de tipos de vía, se producirá un Error de la
//    aplicación.
    private UUID tipoVia;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    private String nombreDireccion;

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
//    Debe sen un número entero.
    private Integer numeroDireccion;

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
