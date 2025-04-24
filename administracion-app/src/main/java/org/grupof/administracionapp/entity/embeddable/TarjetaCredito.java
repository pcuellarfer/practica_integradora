package org.grupof.administracionapp.entity.embeddable;

import jakarta.persistence.Embeddable;

@Embeddable
public class TarjetaCredito {
    //tipo tarjeta deberia ser una entidad para almacenar los tipos de tarjeta que pueda meter el usuario??
    private String tipoTarjeta;
    private String numTarjeta;
    private Integer mesCaducidad;
    private Integer anoCaducidad;
    private Integer cvc;
}
