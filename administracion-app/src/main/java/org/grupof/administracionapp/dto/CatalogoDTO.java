package org.grupof.administracionapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grupof.administracionapp.dto.Producto.ProductoDTO;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CatalogoDTO {
    private String proveedor;
    private LocalDate fechaEnvioCatalogo;
    private List<ProductoDTO> productos;
}