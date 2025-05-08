package org.grupof.administracionapp.entity.producto;

import jakarta.persistence.*;
import org.grupof.administracionapp.entity.producto.enums.Tapa;

import java.util.UUID;

@Entity
public class Libro extends Producto{

    private String titulo;

    private String autor;

    private String editorial;

    @Enumerated(EnumType.STRING)
    private Tapa tapa;

    private int numPaginas;

    private Boolean segundaMano;
}
