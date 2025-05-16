package org.grupof.administracionapp.services.nomina;

import org.grupof.administracionapp.dto.nominas.LineaNominaDTO;
import org.grupof.administracionapp.dto.nominas.NominaDTO;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.entity.embeddable.Periodo;
import org.grupof.administracionapp.entity.nomina.LineaNomina;
import org.grupof.administracionapp.entity.nomina.Nomina;
import org.grupof.administracionapp.repository.EmpleadoRepository;
import org.grupof.administracionapp.repository.NominaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class NominaServiceImpl implements NominaService {

    private final EmpleadoRepository empleadoRepository;
    private final NominaRepository nominaRepository;

    public NominaServiceImpl(EmpleadoRepository empleadoRepository,
                             NominaRepository nominaRepository) {
        this.empleadoRepository = empleadoRepository;
        this.nominaRepository = nominaRepository;
    }

    @Override
    public void crearNomina(NominaDTO nominaDTO) {

        // 1. Validar y obtener el empleado
        Empleado empleado = empleadoRepository.findById(nominaDTO.getEmpleadoId())
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));

        // 2. Parsear fechas desde el DTO (String → LocalDate)
        LocalDate fechaInicio = nominaDTO.getFechaInicio();
        LocalDate fechaFin = nominaDTO.getFechaFin();

        // 3. Validar fechas
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la de inicio.");
        }

        // 4. Verificar solapamiento con otras nóminas
        for (Nomina existente : empleado.getNominas()) {
            LocalDate inicio = existente.getPeriodo().getFechaInicio();
            LocalDate fin = existente.getPeriodo().getFechaFin();
            boolean solapa = !(fechaFin.isBefore(inicio) || fechaInicio.isAfter(fin));
            if (solapa) {
                throw new IllegalArgumentException("Ya existe una nómina que solapa con este período.");
            }
        }

        // 5. Crear objeto Nomina
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
        nomina.setPerfilProfesional("Desconocido"); //falta en entidad empleado
        nomina.setDepartamento(empleado.getDepartamento().getNombre());

        // 6. Procesar líneas
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

        // 7. Guardar
        nominaRepository.save(nomina);
    }

    @Override
    public NominaDTO devuelveNominaPorEmpleadoId(UUID emp, UUID nom) {

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
        if(emp.equals(nominaDTO.getEmpleadoId())){
            return nominaDTO;}
        else{
            return null;
        }
    }
}
