package org.grupof.administracionapp.services;

import org.grupof.administracionapp.dto.ProductoDTO;

import java.util.List;

public interface ProductoService {
    ProductoDTO crearProducto(ProductoDTO dto);
    List<ProductoDTO> listarProductos();
    List<ProductoDTO> cargarProductosDesdeFichero();
}
