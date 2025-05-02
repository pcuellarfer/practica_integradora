package org.grupof.administracionapp.entity.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Embeddable
public class TarjetaCredito {

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
//    Si se recibe un código de tipo de tarjeta que
//    no se encuentra entre los que aparecen en la
//    tabla de tipos de tarjeta, se producirá un Error
//    de la aplicación
    private UUID tipoTarjeta;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
//    Debe tener una longitud de 16 caracteres,
//    siendo todos dígitos.
//- Debe cumplir el algoritmo de verificación de
//    tarjetas bancarias.
    private String numTarjeta;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
//   Debe tener una longitud de 2 caracteres,
//siendo todos dígitos, y representando un mes
//válido, es decir, del 01 al 12
    private String mesCaducidad;

    @NotBlank
//    Debe tener una longitud de 4 caracteres,
//    siendo todos dígitos, y representando un año
//    válido a partir del año actual y hasta 20 años en
//    el futuro.
    private String anoCaducidad;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
//    - Debe seguir el algoritmo para estos códigos
//            (ver online).
    private String cvc;
}
