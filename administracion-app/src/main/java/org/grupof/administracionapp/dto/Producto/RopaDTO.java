package org.grupof.administracionapp.dto.Producto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RopaDTO extends ProductoDTO {
    private String talla;
    private String material;
    private String genero;
}
