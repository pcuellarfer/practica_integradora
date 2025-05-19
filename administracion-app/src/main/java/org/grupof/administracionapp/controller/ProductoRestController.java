package org.grupof.administracionapp.controller;

import org.grupof.administracionapp.services.Producto.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controlador REST para operaciones relacionadas con productos.
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {

    private static final Logger logger = LoggerFactory.getLogger(ProductoRestController.class);
    private final ProductoService productoService;

    public ProductoRestController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * Elimina un producto identificado por su UUID.
     *
     * @param id UUID del producto a eliminar.
     * @return ResponseEntity con mensaje y estado HTTP según el resultado de la operación.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrarProducto(@PathVariable UUID id) {
        logger.info("Solicitud para eliminar producto con ID: {}", id);

        boolean eliminado = productoService.eliminarProductoPorId(id);

        if (eliminado) {
            logger.info("Producto con ID {} eliminado correctamente", id);
            return ResponseEntity.ok().body("Producto eliminado");
        } else {
            logger.warn("Producto con ID {} no encontrado para eliminar", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        }
    }
}
