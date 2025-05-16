package org.grupof.administracionapp.services.Categoria;

import org.grupof.administracionapp.entity.producto.Categoria;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Servicio para la gestión de categorías de productos.
 * Proporciona métodos para obtener todas las categorías disponibles.
 */
@Service
public interface CategoriaService {

    /**
     * Obtiene una lista de todas las categorías de productos disponibles.
     *
     * @return una lista de objetos {@link Categoria}
     */
    List<Categoria> obtenerTodasLasCategorias();
}

