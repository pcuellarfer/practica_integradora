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
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import java.net.URI;
import java.util.List;

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
     * Maneja la subida de múltiples archivos JSON que representan catálogos de productos.
     * <p>
     * Cada archivo es procesado individualmente mediante el servicio {@code catalogoService}.
     * Si todos los archivos se procesan correctamente, se redirige al usuario con un mensaje de éxito.
     * En caso de error durante el procesamiento de cualquiera de los archivos, se registra el error
     * y se redirige con un mensaje de fallo.
     * </p>
     *
     * @param files Lista de archivos JSON enviados en el formulario bajo el nombre "files".
     * @return Una respuesta HTTP 303 See Other con redirección a la vista de resultado del dashboard,
     *         incluyendo un mensaje de éxito o error como parámetro en la URL.
     */
    @PostMapping("/subir")
    public ResponseEntity<Object> subirCatalogo(@RequestParam("files") List<MultipartFile> files) {
        logger.info("Petición recibida para subir catálogos");

        try {
            for (MultipartFile file : files) {
                logger.info("Procesando archivo: '{}'", file.getOriginalFilename());
                catalogoService.procesarCatalogo(file);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("http://apache-integradora/dashboard/catalogo-apache.html?mensaje=Catálogos+procesados+correctamente"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        } catch (Exception e) {
            logger.error("Error al procesar los catálogos: {}", e.getMessage(), e);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("http://apache-integradora/dashboard/catalogo-apache.html?mensaje=Error+al+procesar+los+catálogos"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }
    }

}