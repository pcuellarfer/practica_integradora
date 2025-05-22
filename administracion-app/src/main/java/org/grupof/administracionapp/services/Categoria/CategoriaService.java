package org.grupof.administracionapp.services.Categoria;

import org.grupof.administracionapp.entity.producto.Categoria;
import java.util.List;

public interface CategoriaService {

    /**
     * Obtiene una lista de todas las categorías de productos disponibles.
     *
     * @return una lista de objetos {@link Categoria}
     */
    List<Categoria> obtenerTodasLasCategorias();
}

