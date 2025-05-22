package org.grupof.administracionapp.entity.producto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro extends Producto {
    private String titulo;
    private String autor;
    private String editorial;
    private String tapa;
    private Integer numeroPaginas;
    private Boolean segundaMano;
    private Dimensiones dimensiones;
}
