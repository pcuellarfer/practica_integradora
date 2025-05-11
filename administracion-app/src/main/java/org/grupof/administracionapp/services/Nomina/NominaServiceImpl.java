package org.grupof.administracionapp.services.Nomina;

import jakarta.transaction.Transactional;
import org.grupof.administracionapp.dto.Nomina.NominaDTO;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.entity.LineaNomina;
import org.grupof.administracionapp.entity.Nomina;
import org.grupof.administracionapp.mapper.NominaMapper;
import org.grupof.administracionapp.repository.EmpleadoRepository;
import org.grupof.administracionapp.repository.LineaNominaRepository;
import org.grupof.administracionapp.repository.NominaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NominaServiceImpl implements NominaService {

    private static final Logger logger = LoggerFactory.getLogger(NominaServiceImpl.class);

    private final NominaRepository nominaRepository;
    private final EmpleadoRepository empleadoRepository;
    private final LineaNominaRepository lineaNominaRepository;
    private final NominaMapper nominaMapper;

    public NominaServiceImpl(NominaRepository nominaRepository, EmpleadoRepository empleadoRepository, LineaNominaRepository lineaNominaRepository, NominaMapper nominaMapper) {
        this.nominaRepository = nominaRepository;
        this.empleadoRepository = empleadoRepository;
        this.lineaNominaRepository = lineaNominaRepository;
        this.nominaMapper = nominaMapper;
    }

    @Transactional
    @Override
    public NominaDTO altaNomina(NominaDTO nominaDTO, UUID empleadoId) {
        logger.info("Guardando nueva nómina para el empleado con ID: {}", empleadoId);
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado con ID: " + empleadoId));

        Nomina nomina = nominaMapper.toEntity(nominaDTO);
        nomina.setEmpleado(empleado);

        List<LineaNomina> lineasNomina = nominaDTO.getLineas().stream()
                .map(lineaNominaDTO -> {
                    LineaNomina linea = new LineaNomina();
                    linea.setConcepto(lineaNominaDTO.getConcepto());
                    linea.setImporte(lineaNominaDTO.getImporte());
                    linea.setNomina(nomina);
                    return linea;
                })
                .collect(Collectors.toList());
        nomina.setLineas(lineasNomina);

        Nomina nominaGuardada = nominaRepository.save(nomina);
        return nominaMapper.toDTO(nominaGuardada);
    }

    @Override
    public NominaDTO obtenerNominaPorId(UUID id) {
        logger.info("Obteniendo nómina con ID: {}", id);
        Nomina nomina = nominaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nómina no encontrada con ID: " + id));
        return nominaMapper.toDTO(nomina);
    }

    @Override
    public List<NominaDTO> obtenerNominasEmpleado(UUID empleadoId) {
        logger.info("Obteniendo nóminas del empleado con ID: {}", empleadoId);
        List<Nomina> nominas = nominaRepository.findByEmpleado_Id(empleadoId);
        return nominas.stream()
                .map(nominaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarLineaNomina(UUID lineaNominaId) {
        logger.info("Eliminando línea de nómina con ID: {}", lineaNominaId);
        if (!lineaNominaRepository.existsById(lineaNominaId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Línea de nómina no encontrada con ID: " + lineaNominaId);
        }
        lineaNominaRepository.deleteById(lineaNominaId);
    }

    @Transactional
    @Override
    public void eliminarNomina(UUID nominaId) {
        logger.info("Eliminando nómina con ID: {}", nominaId);
        if (!nominaRepository.existsById(nominaId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nómina no encontrada con ID: " + nominaId);
        }
        nominaRepository.deleteById(nominaId);
        // Si la relación entre Nomina y LineaNomina no es CASCADE.DELETE,
        // podrías necesitar eliminar las líneas asociadas aquí.
        // Por ejemplo: lineaNominaRepository.deleteByNomina_Id(nominaId);
    }

    @Override
    public UUID obtenerEmpleadoIdDeNomina(UUID nominaId) {
        logger.info("Obteniendo ID del empleado de la nómina con ID: {}", nominaId);
        Nomina nomina = nominaRepository.findById(nominaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nómina no encontrada con ID: " + nominaId));
        return nomina.getEmpleado().getId();
    }
}


