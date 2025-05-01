package org.grupof.administracionapp.dto.Empleado;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.grupof.administracionapp.entity.embeddable.Direccion;

import java.util.UUID;

@Getter
@Setter

public class Paso2ContactoDTO {

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
//    Si se recibe un tipo de documento de cliente
//    que no se encuentra entre los que aparecen en
//    la tabla de tipos de documento de empleado,
//    se producirá un Error de la aplicación
    private UUID tipoDocumento;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    //Si tipo de documento es un DNI o un NIE, se
    //debe validar que cumple el patrón del tipo
    private String documento;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    //cambiar a select simple
//    Si se recibe un código de país que no se
//    encuentra entre los que aparecen en la tabla
//    de países, se producirá un Error de la
//            aplicación
    private String prefijoTelefono;

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
//    Debe contener un teléfono móvil válido, es
//    decir, se comprobará que se reciben de 9
//    dígitos exactamente
    private Integer telefono;

    @Valid
    private Direccion direccion;
}
