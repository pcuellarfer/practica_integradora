package org.grupof.administracionapp.controller;

import org.grupof.administracionapp.dto.nominas.NominaDTO;
import org.grupof.administracionapp.services.nomina.NominaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/nominas")
public class NominaRestController {

    private final NominaService nominaService;

    public NominaRestController(NominaService nominaService) {
        this.nominaService = nominaService;
    }

    @GetMapping("{idEmple}/{idNomina}")
    public ResponseEntity<?> devuelveNomina(@PathVariable UUID idEmple, @PathVariable UUID idNomina) {
        return ResponseEntity.ok(nominaService.devuelveNominaPorEmpleadoId(idEmple, idNomina));
    }

    @PostMapping
    public ResponseEntity<?> crearNomina(@RequestBody NominaDTO dto) {
        try {
            nominaService.crearNomina(dto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
