package org.grupof.administracionapp.dto.Producto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LibroDTO.class, name = "LIBRO"),
        @JsonSubTypes.Type(value = RopaDTO.class, name = "ROPA"),
        @JsonSubTypes.Type(value = MuebleDTO.class, name = "MUEBLE")
})
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
