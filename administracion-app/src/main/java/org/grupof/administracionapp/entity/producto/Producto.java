package org.grupof.administracionapp.entity.producto;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Producto {
    @Id
    @GeneratedValue
    private UUID id;

    private String descripcion;
    private double precio;
    private String marca;

    @ElementCollection
    private List<String> categorias;

    private boolean esPerecedero;
    private int unidades;

    private LocalDate fechaFabricacion;

    @ElementCollection
    private List<String> colores;

    private String titulo, autor, editorial, tapa;
    private Integer numeroPaginas;
    private Boolean segundaMano;
    private Dimensiones dimensiones;
}
