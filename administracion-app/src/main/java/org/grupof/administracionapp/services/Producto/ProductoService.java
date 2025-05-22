package org.grupof.administracionapp.services.Producto;

import org.grupof.administracionapp.entity.producto.Producto;
import org.grupof.administracionapp.entity.producto.enums.TipoProducto;
import java.util.List;
import java.util.UUID;

/**
 * Servicio para la gestión de productos y operaciones relacionadas con el catálogo.
 */
public interface ProductoService {

    /**
     * Obtiene la lista de todos los tipos de producto disponibles.
     *
     * @return una lista de valores del enum {@link TipoProducto}
     */
    List<TipoProducto> mostrarTiposProducto();

    /**
     * Filtra el catálogo de productos según el tipo de producto y la categoría proporcionados.
     * Si alguno de los parámetros está vacío o nulo, se omite ese criterio de filtrado.
     *
     * @param tipoProducto el tipo de producto a filtrar (puede ser vacío o nulo)
     * @param categoria la categoría a filtrar (puede ser vacía o nula)
     * @return una lista de productos que coinciden con los criterios de filtrado
     */
    List<Producto> filtrarCatalogo(String tipoProducto, String categoria);

    /**
     * Obtiene un producto a partir de su identificador único.
     *
     * @param id el UUID que identifica de forma única al producto.
     * @return el {@link Producto} correspondiente al ID proporcionado.
     * @throws RuntimeException si no se encuentra ningún producto con el ID especificado.
     */
    Producto obtenerProductoPorId(UUID id);

    /**
     * Elimina un producto del catálogo por su ID.
     *
     * @param id el ID del producto a eliminar
     * @return true si el producto fue eliminado, false si no se encontró
     */
    boolean eliminarProductoPorId(UUID id);
}

