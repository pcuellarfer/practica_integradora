package org.grupof.administracionapp.dto.Producto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupof.administracionapp.entity.producto.Categoria;
import org.grupof.administracionapp.entity.producto.enums.Color;
import org.grupof.administracionapp.entity.producto.enums.TipoProducto;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    private String descripcion;
    private double precio;
    private String marca;
    private List<Categoria> categorias;
    private boolean esPerecedero;
    private int unidades;
    private LocalDate fechaFabricacion;
    private List<Color> colores;
    private TipoProducto tipoProducto;
}
