package org.grupof.administracionapp.entity.producto;

import jakarta.persistence.*;
import org.grupof.administracionapp.entity.producto.enums.Color;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
public class Mueble extends Producto{

    private LocalDate fechaFabricacion;

    @Embedded
    private Dimensiones dimensiones;

    @ElementCollection //necesario para poder meter listas, crea una tabla donde guarda por cada fila guarda mueble-color
    @Enumerated(EnumType.STRING)
    private List<Color> colores;
}
