package org.grupof.administracionapp.entity.producto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupof.administracionapp.entity.producto.enums.Color;
import org.grupof.administracionapp.entity.producto.enums.TipoProducto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Producto {
    @Id
    @GeneratedValue
    private UUID id;

    private String descripcion;
    private double precio;
    private String marca;

    @ManyToMany
    private List<Categoria> categorias;

    private boolean esPerecedero;
    private int unidades;

    private LocalDate fechaFabricacion;

    @ElementCollection
    private List<Color> colores;

    private TipoProducto tipoProducto;
}
