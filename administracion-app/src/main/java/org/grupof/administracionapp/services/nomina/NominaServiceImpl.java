package org.grupof.administracionapp.services.nomina;

import org.grupof.administracionapp.dto.nominas.*;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.entity.embeddable.Periodo;
import org.grupof.administracionapp.entity.nomina.LineaNomina;
import org.grupof.administracionapp.entity.nomina.Nomina;
import org.grupof.administracionapp.repository.EmpleadoRepository;
import org.grupof.administracionapp.repository.NominaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Implementación del servicio para la gestión de nóminas.
 * Proporciona operaciones para crear nóminas, buscar nóminas por empleado y fechas,
 * obtener detalles de nóminas y obtener nóminas específicas.
 */
@Service
public class NominaServiceImpl implements NominaService {

    private final EmpleadoRepository empleadoRepository;
    private final NominaRepository nominaRepository;

    /**
     * Constructor que inyecta los repositorios necesarios.
     *
     * @param empleadoRepository repositorio para acceso a empleados.
     * @param nominaRepository   repositorio para acceso a nóminas.
     */
    public NominaServiceImpl(EmpleadoRepository empleadoRepository,
                             NominaRepository nominaRepository) {
        this.empleadoRepository = empleadoRepository;
        this.nominaRepository = nominaRepository;
    }

    /**
     * Crea y guarda una nueva nómina a partir de un DTO.
     * <p>
     * Realiza validaciones sobre fechas y solapamiento con nóminas existentes.
     * Calcula devengos, deducciones y salario neto.
     * </p>
     *
     * @param nominaDTO DTO con los datos necesarios para crear la nómina.
     * @throws IllegalArgumentException si el empleado no existe, las fechas son inválidas,
     *                                  hay solapamientos o el salario neto es inválido.
     */
    @Override
    public void crearNomina(NominaDTO nominaDTO) {
        Empleado empleado = empleadoRepository.findById(nominaDTO.getEmpleadoId())
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));

        LocalDate fechaInicio = nominaDTO.getFechaInicio();
        LocalDate fechaFin = nominaDTO.getFechaFin();

        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la de inicio.");
        }

        for (Nomina existente : empleado.getNominas()) {
            LocalDate inicio = existente.getPeriodo().getFechaInicio();
            LocalDate fin = existente.getPeriodo().getFechaFin();
            boolean solapa = !(fechaFin.isBefore(inicio) || fechaInicio.isAfter(fin));
            if (solapa) {
                throw new IllegalArgumentException("Ya existe una nómina que solapa con este período.");
            }
        }

        Nomina nomina = new Nomina();
        nomina.setEmpleado(empleado);
        nomina.setPeriodo(new Periodo(fechaInicio, fechaFin));
        nomina.setNombreEmpresa("EfeCorp");
        nomina.setCIF("A12345678");
        nomina.setFechaAltaEmp(empleado.getFechaContratacion());
        nomina.setDireccion(null);
        nomina.setNombreEmp(empleado.getNombre() + " " + empleado.getApellido());
        nomina.setDocumentoEmpleado(empleado.getDocumento());
        nomina.setDireccionEmpleado(empleado.getDireccion());
        nomina.setPerfilProfesional("Desconocido");
        nomina.setDepartamento(empleado.getDepartamento().getNombre());

        BigDecimal devengos = BigDecimal.ZERO;
        BigDecimal deducciones = BigDecimal.ZERO;
        List<LineaNomina> lineas = new ArrayList<>();

        for (LineaNominaDTO lineaDTO : nominaDTO.getLineasNomina()) {
            if (lineaDTO.getConcepto() == null || lineaDTO.getConcepto().isBlank()) continue;

            LineaNomina linea = new LineaNomina();
            linea.setConcepto(lineaDTO.getConcepto());
            linea.setPorcentaje(lineaDTO.getPorcentaje());
            linea.setCantidad(lineaDTO.getCantidad());
            linea.setNomina(nomina);

            if (linea.getCantidad() == null || linea.getCantidad().compareTo(BigDecimal.ZERO) == 0) {
                throw new IllegalArgumentException("La línea de nómina '" + linea.getConcepto() + "' tiene cantidad inválida.");
            }

            if (linea.getCantidad().compareTo(BigDecimal.ZERO) > 0) {
                devengos = devengos.add(linea.getCantidad());
            } else {
                deducciones = deducciones.add(linea.getCantidad().abs());
            }

            lineas.add(linea);
        }

        nomina.setLineasNomina(lineas);
        nomina.setDevengos(devengos);
        nomina.setDeducciones(deducciones);

        BigDecimal neto = devengos.subtract(deducciones);
        if (neto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El salario neto debe ser mayor que 0.");
        }
        nomina.setSalarioNeto(neto);

        nominaRepository.save(nomina);
    }

    /**
     * Devuelve una nómina específica de un empleado.
     *
     * @param emp UUID del empleado.
     * @param nom UUID de la nómina.
     * @return un DTO con la nómina encontrada, o null si no coincide con el empleado.
     */
    @Override
    public NominaDTO devuelveNominaPorEmpleadoId(UUID emp, UUID nom) {
        // Implementación simplificada para ejemplo
        NominaDTO nominaDTO = new NominaDTO();
        nominaDTO.setEmpleadoId(UUID.fromString("f6e6c3d6-fb3d-45e6-b08b-208e2854cde0"));
        nominaDTO.setFechaInicio(LocalDate.of(2025, 5, 1));
        nominaDTO.setFechaFin(LocalDate.of(2025, 5, 31));

        Set<LineaNominaDTO> lineas = new HashSet<>();
        LineaNominaDTO linea = new LineaNominaDTO();
        linea.setConcepto("Sal inicial");
        linea.setCantidad(BigDecimal.valueOf(1500));
        lineas.add(linea);
        // nominaDTO.setLineasNomina(lineas);

        if (emp.equals(nominaDTO.getEmpleadoId())) {
            return nominaDTO;
        } else {
            return null;
        }
    }

    /**
     * Busca nóminas filtrando por empleado y rango de fechas.
     *
     * @param empleadoId UUID del empleado (puede ser null para no filtrar).
     * @param fechaInicio fecha inicial del rango (puede ser null).
     * @param fechaFin fecha final del rango (puede ser null).
     * @return lista de DTOs con las nóminas que cumplen el filtro.
     */
    @Override
    public List<BusquedaNominaDTO> buscarNominas(UUID empleadoId, LocalDate fechaInicio, LocalDate fechaFin) {
        return nominaRepository.findAll().stream()
                .filter(n -> empleadoId == null || n.getEmpleado().getId().equals(empleadoId))
                .filter(n -> fechaInicio == null || !n.getPeriodo().getFechaInicio().isBefore(fechaInicio))
                .filter(n -> fechaFin == null || !n.getPeriodo().getFechaFin().isAfter(fechaFin))
                .map(n -> new BusquedaNominaDTO(
                        n.getId(),
                        n.getNombreEmp(),
                        n.getPeriodo().getFechaInicio(),
                        n.getPeriodo().getFechaFin()
                ))
                .toList();
    }

    /**
     * Obtiene el detalle completo de una nómina por su ID.
     *
     * @param id UUID de la nómina.
     * @return DTO con los detalles de la nómina.
     * @throws IllegalArgumentException si la nómina no existe.
     */
    @Override
    public DetalleNominaDTO obtenerDetalleNomina(UUID id) {
        Nomina nomina = nominaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nómina no encontrada"));

        List<LineaNominaDTO> lineasDTO = nomina.getLineasNomina().stream()
                .map(l -> new LineaNominaDTO(
                        l.getConcepto(),
                        l.getPorcentaje(),
                        l.getCantidad()
                ))
                .toList();

        return new DetalleNominaDTO(
                nomina.getId(),
                nomina.getEmpleado().getId(),
                nomina.getNombreEmp(),
                nomina.getPeriodo().getFechaInicio(),
                nomina.getPeriodo().getFechaFin(),
                nomina.getSalarioNeto(),
                lineasDTO
        );
    }

    @Override
    @Transactional
    public void editarNomina(UUID id, NominaDTO dto) {
        Nomina nomina = nominaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la nómina con ID: " + id));

        if (dto.getFechaInicio() == null || dto.getFechaFin() == null) {
            throw new IllegalArgumentException("Las fechas no pueden estar vacías.");
        }

        if (dto.getFechaFin().isBefore(dto.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la de inicio.");
        }

        if (dto.getLineasNomina() == null || dto.getLineasNomina().isEmpty()) {
            throw new IllegalArgumentException("Debe incluir al menos una línea.");
        }

        //sctualizar solo fechas y líneas
        nomina.setPeriodo(new Periodo(dto.getFechaInicio(), dto.getFechaFin()));
        nomina.getLineasNomina().clear();

        BigDecimal devengos = BigDecimal.ZERO;
        BigDecimal deducciones = BigDecimal.ZERO;

        for (LineaNominaDTO lineaDTO : dto.getLineasNomina()) {
            LineaNomina linea = new LineaNomina();
            linea.setConcepto(lineaDTO.getConcepto());
            linea.setCantidad(lineaDTO.getCantidad());
            linea.setPorcentaje(lineaDTO.getPorcentaje());
            linea.setNomina(nomina);

            if (linea.getCantidad().compareTo(BigDecimal.ZERO) >= 0) {
                devengos = devengos.add(linea.getCantidad());
            } else {
                deducciones = deducciones.add(linea.getCantidad().abs());
            }

            nomina.getLineasNomina().add(linea);
        }

        nomina.setDevengos(devengos);
        nomina.setDeducciones(deducciones);
        nomina.setSalarioNeto(devengos.subtract(deducciones));

        nominaRepository.save(nomina);
    }

    @Override
    public void eliminarNomina(UUID id) {
        Nomina nomina = nominaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nómina no encontrada con ID: " + id));

        nominaRepository.delete(nomina);
    }
}
