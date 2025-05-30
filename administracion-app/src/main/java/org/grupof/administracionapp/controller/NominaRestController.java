package org.grupof.administracionapp.controller;

import org.grupof.administracionapp.dto.nominas.BusquedaNominaDTO;
import org.grupof.administracionapp.dto.nominas.DetalleNominaDTO;
import org.grupof.administracionapp.dto.nominas.NombreApellidoEmpleadoDTO;
import org.grupof.administracionapp.dto.nominas.NominaDTO;
import org.grupof.administracionapp.services.Empleado.EmpleadoService;
import org.grupof.administracionapp.services.nomina.NominaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
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
    private final EmpleadoService empleadoService;

    public NominaRestController(NominaService nominaService, EmpleadoService empleadoService) {
        this.nominaService = nominaService;
        this.empleadoService = empleadoService;
    }

    /**
     * Devuelve una nómina específica asociada a un empleado. hecho como prueba con José Ramon
     *
     * @param idEmple UUID del empleado.
     * @param idNomina UUID de la nómina.
     * @return ResponseEntity con la nómina correspondiente en formato JSON si se encuentra.
     */
    //ignorar, lo hice con Jose Ramon como prueba
    @GetMapping("{idEmple}/{idNomina}")
    public ResponseEntity<?> devuelveNomina(@PathVariable UUID idEmple, @PathVariable UUID idNomina) {
        logger.info("Solicitando nómina con ID {} para el empleado con ID {}", idNomina, idEmple);

        try {
            Object nomina = nominaService.devuelveNominaPorEmpleadoId(idEmple, idNomina);
            logger.debug("Nómina recuperada correctamente para el empleado {}", idEmple);
            return ResponseEntity.ok(nomina);
        } catch (Exception e) {
            logger.error("Error al obtener la nómina: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener la nómina.");
        }
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

    /**
     * Devuelve una lista con los nombres y apellidos de todos los empleados.
     *
     * @return lista de empleados (nombre y apellido)
     */
    @GetMapping("/devuelveEmpleados")
    public List<NombreApellidoEmpleadoDTO> devuelveEmpleados(){
        return empleadoService.obtenerNombreYApellidoEmpleados();
    }

    /**
     * Busca nóminas según los filtros opcionales: empleado, fecha de inicio y fecha de fin.
     *
     * @param empleadoId ID del empleado (opcional)
     * @param fechaInicio fecha de inicio del periodo (opcional)
     * @param fechaFin fecha de fin del periodo (opcional)
     * @return lista de nóminas que cumplen los filtros
     */
    @GetMapping("/buscar")
    public List<BusquedaNominaDTO> buscarNominas(
            @RequestParam(required = false) UUID empleadoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        return nominaService.buscarNominas(empleadoId, fechaInicio, fechaFin);
    }

    /**
     * Devuelve el detalle completo de una nómina por su ID.
     *
     * @param id identificador de la nómina
     * @return detalle de la nómina
     */
    @GetMapping("/{id}")
    public DetalleNominaDTO obtenerDetalleNomina(@PathVariable UUID id) {
        return nominaService.obtenerDetalleNomina(id);
    }

    //modificacion de una nomina
    @PutMapping("/{id}")
    public ResponseEntity<?> editarNomina(@PathVariable UUID id, @RequestBody NominaDTO dto) {
        logger.info("Solicitud de edición de nómina ID: {}", id);

        try {
            nominaService.editarNomina(id, dto);
            logger.info("Nómina ID {} actualizada correctamente", id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            logger.error("Error de validación al editar nómina: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error inesperado al editar nómina: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar la nómina."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarNomina(@PathVariable UUID id) {
        try {
            nominaService.eliminarNomina(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "No se pudo eliminar la nómina."));
        }
    }
}