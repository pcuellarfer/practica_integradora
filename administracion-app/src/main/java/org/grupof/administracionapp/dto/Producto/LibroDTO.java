package org.grupof.administracionapp.dto.Producto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.grupof.administracionapp.entity.producto.Dimensiones;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroDTO extends ProductoDTO {
    private String titulo;
    private String autor;
    private String editorial;
    private String tapa;
    private Integer numeroPaginas;
    private boolean segundaMano;
    private Dimensiones dimensiones;
}
