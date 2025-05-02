package org.grupof.administracionapp.entity.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Data

@Embeddable
public class CuentaCorriente {
    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
//    Si se recibe un código de entidad bancaria
//    que no se encuentra entre los que aparecen en
//    la tabla de entidades bancarias, se producirá
//    un Error de la aplicación.
    private UUID banco;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
//    Debe cumplir el patrón de un número de
//    cuenta corriente IBAN, es decir:
//            · La longitud de la cadena que representa la
//    cuenta es 24, de los que los 2 primeros sin
//    caracteres y los 22 siguientes, dígitos.
//· El código de país debe coincidir con el de la
//    entidad bancaria (ej. si la entidad es BNP
//            Paribas, que es francesa, el código debe ser
//                              FRXX).
//            10 | 14
//            · El código numérico de país (los 2 dígitos a
//            continuación de la sigla de país) debe coincidir
//    con el algoritmo (buscar online).
//            · El código de entidad debe coincidir con el de
//    la entidad bancaria seleccionada (ej. Banco de
//Santander es el 0049).
//            - Comprobar la validez del IBAN siguiendo
//    alguno de los algoritmos que hay online.
    private String numCuenta;
}
