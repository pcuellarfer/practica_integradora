package org.grupof.administracionapp.entity.embeddable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Embeddable
public class TarjetaCredito {

    private UUID tipoTarjeta;
    private String numTarjeta;
    private Integer mesCaducidad;
    private Integer anoCaducidad;
    private Integer cvc;
}
