package org.grupof.administracionapp.controller;

import org.grupof.administracionapp.services.CatalogoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import java.net.URI;

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
     * Maneja la subida de un archivo de catálogo y lo procesa.
     * Si el archivo se procesa correctamente, redirige a la página de catálogo con un mensaje de éxito.
     * Si ocurre un error, redirige con un mensaje de error.
     *
     * @param file El archivo de catálogo a subir.
     * @return La redirección a la página de catálogo con el mensaje adecuado.
     */
    @PostMapping("/subir")
    public ResponseEntity<Object> subirCatalogo(@RequestParam("file") MultipartFile file) {
        logger.info("Petición recibida para subir catálogo: nombre del archivo '{}'", file.getOriginalFilename());

        try {
            catalogoService.procesarCatalogo(file);
            // Redirigir al endpoint "/catalogo/ver" después del procesamiento
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/dashboard/subida-catalogo?mensaje=Catalogo+procesado+correctamente"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        } catch (Exception e) {
            logger.error("Error al procesar el catálogo '{}': {}", file.getOriginalFilename(), e.getMessage(), e);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/dashboard/subida-catalogo?error=Error+al+procesar+el+catálogo"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }
    }
}