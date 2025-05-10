package org.grupof.administracionapp.controller;

import org.grupof.administracionapp.services.CatalogoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Controlador REST para manejar operaciones relacionadas con el catálogo de productos.
 * Permite la subida de archivos JSON que contienen información de productos
 * y delega su procesamiento al servicio correspondiente.
 */
@RestController
@RequestMapping("/catalogo")
public class CatalogoController {

    Logger logger = LoggerFactory.getLogger(CatalogoController.class);
    private final CatalogoService catalogoService;

    /**
     * Constructor para inyectar el servicio que gestiona la lógica de negocio del catálogo.
     *
     * @param catalogoService servicio encargado del procesamiento del catálogo.
     */
    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    /**
     * Endpoint para subir un archivo JSON con información del catálogo.
     * El archivo debe tener la estructura esperada por el DTO {@code CatalogoDTO}.
     *
     * @param file archivo JSON con el catálogo de productos.
     * @return una respuesta HTTP indicando el resultado de la operación.
     *         Devuelve 200 OK si el catálogo se procesa correctamente,
     *         o 400 Bad Request si ocurre un error.
     */
    @PostMapping("/subir")
    public ResponseEntity<?> subirCatalogo(@RequestParam("file") MultipartFile file) {
        logger.info("Petición recibida para subir catálogo: nombre del archivo '{}'", file.getOriginalFilename());

        try {
            catalogoService.procesarCatalogo(file);
            logger.info("Catálogo '{}' procesado correctamente", file.getOriginalFilename());
            return ResponseEntity.ok().body(Map.of("mensaje", "Catálogo procesado correctamente."));
        } catch (Exception e) {
            logger.error("Error al procesar el catálogo '{}': {}", file.getOriginalFilename(), e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al procesar el catálogo: " + e.getMessage()));
        }
    }

}