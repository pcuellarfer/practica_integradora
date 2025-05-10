package org.grupof.administracionapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupof.administracionapp.entity.producto.Dimensiones;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductoDTO {
    private String descripcion;
    private double precio;
    private String marca;
    private List<String> categorias;
    private boolean esPerecedero;
    private int unidades;
    private LocalDate fechaFabricacion;
    private Integer ancho, profundo, alto;
    private List<String> colores;
    private String titulo, autor, editorial, tapa;
    private Integer numeroPaginas;
    private Boolean segundaMano;
    private Dimensiones dimensiones;
}