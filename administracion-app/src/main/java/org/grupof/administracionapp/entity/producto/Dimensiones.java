package org.grupof.administracionapp.entity.producto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data

@Embeddable
public class Dimensiones {

    private double ancho;

    private double profundidad;

    private double alto;

}
