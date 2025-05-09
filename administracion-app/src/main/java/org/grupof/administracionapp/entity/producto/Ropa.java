package org.grupof.administracionapp.entity.producto;

import jakarta.persistence.*;
import org.grupof.administracionapp.entity.producto.enums.Color;
import org.grupof.administracionapp.entity.producto.enums.Talla;

import java.util.List;
import java.util.UUID;

@Entity
public class Ropa extends Producto{

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Color> colores;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Talla> tallas;

}
