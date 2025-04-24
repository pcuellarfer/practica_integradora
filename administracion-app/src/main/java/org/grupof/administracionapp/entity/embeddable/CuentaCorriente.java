package org.grupof.administracionapp.entity.embeddable;

import jakarta.persistence.Embeddable;

@Embeddable
public class CuentaCorriente {
    private String banco;
    private String numCuenta;
}
