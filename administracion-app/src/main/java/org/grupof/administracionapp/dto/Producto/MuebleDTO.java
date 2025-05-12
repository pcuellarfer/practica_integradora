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
public class MuebleDTO extends ProductoDTO {
    private String tipoMadera;
    private String estilo;
    private Dimensiones dimensiones;
}
