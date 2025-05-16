package org.grupof.administracionapp.services.Producto;

import org.grupof.administracionapp.entity.producto.Producto;
import org.grupof.administracionapp.entity.producto.enums.TipoProducto;
import org.grupof.administracionapp.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementación del servicio {@link ProductoService} que gestiona las operaciones
 * relacionadas con productos, tipos de producto y filtrado de catálogo.
 */
@Service
public abstract class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductoServiceImpl.class);

    /**
     * Constructor que inyecta el repositorio de productos.
     *
     * @param productoRepository el repositorio que permite acceder a los datos de productos
     */
    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * {@inheritDoc}
     * Obtiene todos los tipos de producto disponibles.
     *
     * @return lista de tipos de productos
     */
    @Override
    public List<TipoProducto> mostrarTiposProducto() {
        logger.info("Obteniendo todos los tipos de producto");

        List<TipoProducto> tiposProducto = Arrays.asList(TipoProducto.values());

        logger.debug("Número de tipos de producto encontrados: {}", tiposProducto.size());
        return tiposProducto;
    }

    /**
     * {@inheritDoc}
     * Filtra el catálogo de productos según el tipo de producto y la categoría proporcionados.
     *
     * @param tipoProducto el tipo de producto a filtrar (puede ser vacío o nulo)
     * @param categoria la categoría a filtrar (puede ser vacía o nula)
     * @return lista de productos filtrados
     */
    @Override
    public List<Producto> filtrarCatalogo(String tipoProducto, String categoria) {
        logger.info("Filtrando productos con tipoProducto: {} y categoría: {}", tipoProducto, categoria);

        // Obtener todos los productos de la base de datos
        List<Producto> productos = productoRepository.findAll();
        logger.debug("Total productos en la base de datos antes del filtro: {}", productos.size());

        // Filtrar productos por tipo y categoría
        List<Producto> productosFiltrados = productos.stream()
                .filter(p -> tipoProducto == null || tipoProducto.isEmpty() ||
                        p.getTipoProducto().name().equalsIgnoreCase(tipoProducto))
                .filter(p -> categoria == null || categoria.isEmpty() ||
                        p.getCategorias().stream()
                                .anyMatch(c -> c.getNombre().equalsIgnoreCase(categoria)))
                .collect(Collectors.toList());

        logger.debug("Total productos encontrados después del filtro: {}", productosFiltrados.size());
        return productosFiltrados;
    }

    /**
     * Obtiene un producto por su identificador único (UUID).
     *
     * @param id el UUID que identifica al producto a buscar.
     * @return el objeto {@link Producto} correspondiente al ID proporcionado.
     * @throws RuntimeException si no se encuentra ningún producto con el ID dado.
     */
    @Override
    public Producto obtenerProductoPorId(UUID id) {
        Optional<Producto> producto = productoRepository.findById(id);
        return producto.orElseThrow(() -> new RuntimeException("Producto no encontrado con el id: " + id));
    }

    /**
     * {@inheritDoc}
     *
     * @param id el ID del producto a eliminar
     * @return true si el producto fue eliminado, false si no se encontró
     */
    @Override
    public boolean eliminarProductoPorId(UUID id) {
        logger.info("Eliminando producto con ID: {}", id);
        Optional<Producto> producto = productoRepository.findById(id);
        if (producto.isPresent()) {
            productoRepository.delete(producto.get());
            logger.info("Producto con ID: {} eliminado correctamente", id);
            return true;
        } else {
            logger.warn("No se encontró el producto con ID: {}", id);
            return false;
        }
    }
}
