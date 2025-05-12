package org.grupof.administracionapp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.grupof.administracionapp.dto.CatalogoDTO;
import org.grupof.administracionapp.dto.Producto.*;
import org.grupof.administracionapp.entity.producto.Libro;
import org.grupof.administracionapp.entity.producto.Mueble;
import org.grupof.administracionapp.entity.producto.Producto;
import org.grupof.administracionapp.entity.producto.Ropa;
import org.grupof.administracionapp.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.grupof.administracionapp.entity.producto.enums.TipoProducto.*;

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

            Producto producto;

            switch (dto.getTipoProducto()) {
                case LIBRO -> {
                    LibroDTO libroDTO = (LibroDTO) dto;
                    Libro libro = new Libro();
                    libro.setTitulo(libroDTO.getTitulo());
                    libro.setAutor(libroDTO.getAutor());
                    libro.setEditorial(libroDTO.getEditorial());
                    libro.setTapa(libroDTO.getTapa());
                    libro.setNumeroPaginas(libroDTO.getNumeroPaginas());
                    libro.setSegundaMano(libroDTO.isSegundaMano());
                    libro.setDimensiones(libroDTO.getDimensiones());
                    producto = libro;
                    logger.debug("Producto identificado como LIBRO: '{}'", libro.getTitulo());
                }
                case ROPA -> {
                    RopaDTO ropaDTO = (RopaDTO) dto;
                    Ropa ropa = new Ropa();
                    ropa.setTalla(ropaDTO.getTalla());
                    ropa.setMaterial(ropaDTO.getMaterial());
                    ropa.setGenero(ropaDTO.getGenero());
                    producto = ropa;
                    logger.debug("Producto identificado como ROPA");
                }
                case MUEBLE -> {
                    MuebleDTO muebleDTO = (MuebleDTO) dto;
                    Mueble mueble = new Mueble();
                    mueble.setTipoMadera(muebleDTO.getTipoMadera());
                    mueble.setEstilo(muebleDTO.getEstilo());
                    mueble.setDimensiones(muebleDTO.getDimensiones());
                    producto = mueble;
                    logger.debug("Producto identificado como MUEBLE");
                }
                default -> {
                    logger.warn("Tipo de producto no reconocido: {}", dto.getTipoProducto());
                    continue;
                }
            }

            // Rellenar campos comunes del producto
            producto.setDescripcion(dto.getDescripcion());
            producto.setPrecio(dto.getPrecio());
            producto.setMarca(dto.getMarca());
            producto.setCategorias(dto.getCategorias());
            producto.setEsPerecedero(dto.isEsPerecedero());
            producto.setUnidades(dto.getUnidades());
            producto.setFechaFabricacion(dto.getFechaFabricacion());
            producto.setColores(dto.getColores());
            producto.setTipoProducto(dto.getTipoProducto());

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
