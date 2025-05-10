package org.grupof.administracionapp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.grupof.administracionapp.dto.CatalogoDTO;
import org.grupof.administracionapp.dto.ProductoDTO;
import org.grupof.administracionapp.entity.producto.Producto;
import org.grupof.administracionapp.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Servicio encargado de procesar archivos de catálogos de productos en formato JSON
 * y persistir los productos en la base de datos.
 */
@Service
public class CatalogoService {

    private static final Logger logger = LoggerFactory.getLogger(CatalogoService.class);

    private final ProductoRepository productoRepo;

    /**
     * Constructor con inyección del repositorio de productos.
     *
     * @param productoRepo Repositorio que permite guardar entidades {@link Producto}.
     */
    public CatalogoService(ProductoRepository productoRepo) {
        this.productoRepo = productoRepo;
    }

    /**
     * Procesa un archivo JSON que contiene un catálogo de productos. Extrae cada producto
     * del catálogo, lo convierte a entidad {@link Producto} y lo guarda en la base de datos.
     *
     * @param file archivo JSON que contiene los datos del catálogo.
     * @throws IOException si ocurre un error al leer o deserializar el archivo.
     */
    public void procesarCatalogo(MultipartFile file) throws IOException {
        logger.info("Inicio del procesamiento del catálogo: {}", file.getOriginalFilename());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        CatalogoDTO catalogo = mapper.readValue(file.getInputStream(), CatalogoDTO.class);
        logger.info("Proveedor: {}, Fecha de envío: {}, Productos: {}",
                catalogo.getProveedor(),
                catalogo.getFechaEnvioCatalogo(),
                catalogo.getProductos().size());

        for (ProductoDTO dto : catalogo.getProductos()) {
            logger.debug("Procesando producto: {}", dto.getDescripcion());

            Producto producto = new Producto();
            producto.setDescripcion(dto.getDescripcion());
            producto.setPrecio(dto.getPrecio());
            producto.setMarca(dto.getMarca());
            producto.setCategorias(dto.getCategorias());
            producto.setEsPerecedero(dto.isEsPerecedero());
            producto.setUnidades(dto.getUnidades());
            producto.setFechaFabricacion(dto.getFechaFabricacion());
            producto.setDimensiones(dto.getDimensiones());
            producto.setColores(dto.getColores());

            if (dto.getTitulo() != null) {
                producto.setTitulo(dto.getTitulo());
                producto.setAutor(dto.getAutor());
                producto.setEditorial(dto.getEditorial());
                producto.setTapa(dto.getTapa());
                producto.setNumeroPaginas(dto.getNumeroPaginas());
                producto.setSegundaMano(dto.getSegundaMano());
                logger.debug("Producto con información de libro: Título '{}'", dto.getTitulo());
            }

            productoRepo.save(producto);
            logger.info("Producto '{}' guardado correctamente", producto.getDescripcion());
        }

        logger.info("Procesamiento del catálogo finalizado con éxito.");
    }

    /**
     * Obtiene una lista de todos los productos almacenados en la base de datos.
     * Este método utiliza el repositorio de productos para recuperar todos los registros
     * de productos y devolverlos en una lista.
     *
     * @return una lista de objetos {@link Producto} que representa todos los productos
     *         almacenados en la base de datos.
     */
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepo.findAll();
    }
}
