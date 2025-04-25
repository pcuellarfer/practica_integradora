package org.grupof.administracionapp.entity.embeddable;

import jakarta.persistence.Embeddable;

@Embeddable
public class Direccion {

    private String calle;
    private Integer numero;
    private String ciudad;
    private String estado;
    private Integer codigoPostal;
}
