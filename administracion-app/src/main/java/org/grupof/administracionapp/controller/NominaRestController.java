package org.grupof.administracionapp.controller;

import org.grupof.administracionapp.dto.Nomina.NominaDTO;
import org.grupof.administracionapp.services.Nomina.NominaService;
import org.grupof.administracionapp.services.Nomina.NominaServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controlador REST para la gestión de nóminas.
 * Expone endpoints para la creación y eliminación de nóminas.
 */
@RestController
@RequestMapping("/api/nominas")
public class NominaRestController {

    private final NominaService nominaService;
    private final Logger logger = LoggerFactory.getLogger(NominaRestController.class);

    /**
     * Constructor que inyecta la dependencia de {@link NominaService}.
     *
     * @param nominaService la instancia del servicio de nóminas a utilizar.
     */
    public NominaRestController(NominaServiceImpl nominaService) {
        this.nominaService = nominaService;
    }

    /**
     * Endpoint para guardar una nueva nómina para un empleado específico.
     * Recibe los datos de la nómina en el cuerpo de la petición y el ID del empleado como parámetro de consulta.
     *
     * @param nominaDTO   los datos de la nómina a guardar, proporcionados en el cuerpo de la petición (formato JSON).
     * @param empleadoId  el ID único del empleado al que se asociará la nómina, proporcionado como parámetro de consulta.
     * @return {@link ResponseEntity} con la {@link NominaDTO} de la nómina creada y el estado HTTP 201 (CREATED).
     */
    @PostMapping
    public ResponseEntity<NominaDTO> guardarNominaRest(@RequestBody NominaDTO nominaDTO, @RequestParam UUID empleadoId) {
        logger.info("Guardando nueva nómina para el empleado con ID (REST): {}", empleadoId);

        logger.info("ENTRANDO al método guardarNominaRest (REST) para empleado ID: {}", empleadoId);
        logger.info("Datos de la nómina recibidos (REST): {}", nominaDTO);
        // Lógica para asociar la nómina al empleado
        NominaDTO nuevaNomina = nominaService.altaNomina(nominaDTO, empleadoId);
        return new ResponseEntity<>(nuevaNomina, HttpStatus.CREATED);
    }

    /**
     * Endpoint para eliminar una línea de nómina específica por su ID.
     * Recibe el ID de la línea de nómina como una variable de ruta.
     *
     * @param lineaNominaId el ID único de la línea de nómina a eliminar.
     * @return {@link ResponseEntity} con el estado HTTP 204 (NO_CONTENT) indicando que la línea de nómina ha sido eliminada exitosamente.
     */
    @DeleteMapping("/{lineaNominaId}")
    public ResponseEntity<Void> eliminarLineaNominaRest(@PathVariable UUID lineaNominaId) {
        logger.info("Eliminando línea de nómina con ID (REST): {}", lineaNominaId);
        nominaService.eliminarLineaNomina(lineaNominaId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
