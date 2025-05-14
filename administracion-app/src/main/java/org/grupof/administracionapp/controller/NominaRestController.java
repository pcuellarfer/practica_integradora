package org.grupof.administracionapp.controller;

import org.grupof.administracionapp.dto.nominas.NominaDTO;
import org.grupof.administracionapp.services.nomina.NominaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

/**
 * Controlador REST que maneja las operaciones relacionadas con la creación de nóminas.
 */
@RestController
@RequestMapping("/api/nominas")
public class NominaRestController {

    private final NominaService nominaService;
    private static final Logger logger = LoggerFactory.getLogger(NominaRestController.class);

    public NominaRestController(NominaService nominaService) {
        this.nominaService = nominaService;
    }


    @GetMapping("{idEmple}/{idNomina}")
    public ResponseEntity<?> devuelveNomina(@PathVariable UUID idEmple, @PathVariable UUID idNomina) {
        return ResponseEntity.ok(nominaService.devuelveNominaPorEmpleadoId(idEmple, idNomina));
    }

    /**
     * Crea una nueva nómina a partir de los datos proporcionados.
     * Devuelve HTTP 201 (CREATED) si se guarda correctamente.
     * Devuelve HTTP 400 (BAD REQUEST) si ocurre algún error de validación.
     *
     * @param dto objeto que contiene los datos de la nómina
     * @return respuesta HTTP indicando el resultado de la operación
     */
    @PostMapping
    public ResponseEntity<?> crearNomina(@RequestBody NominaDTO dto) {
        try {
            logger.info("Solicitud de creación de nómina para empleado ID: {}", dto.getEmpleadoId());
            nominaService.crearNomina(dto);
            logger.info("Nómina creada exitosamente para empleado ID: {}", dto.getEmpleadoId());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            logger.error("Error al crear la nómina: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}

