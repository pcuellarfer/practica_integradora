package org.grupof.administracionapp.entity.producto;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Mueble { //extends producto

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDate fechaFabricacion;

    @Embedded
    private Dimensiones dimensiones;

    //array de colores
}
