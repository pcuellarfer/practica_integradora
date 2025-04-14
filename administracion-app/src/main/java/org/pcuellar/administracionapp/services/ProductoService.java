package org.pcuellar.administracionapp.services;

import org.pcuellar.administracionapp.dto.ProductoDTO;

import java.util.List;

public interface ProductoService {
    ProductoDTO crearProducto(ProductoDTO dto);
    List<ProductoDTO> listarProductos();
    List<ProductoDTO> cargarProductosDesdeFichero();
}
