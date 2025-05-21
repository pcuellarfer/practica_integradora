package org.grupof.administracionapp.services.nomina;

import org.grupof.administracionapp.dto.nominas.BusquedaNominaDTO;
import org.grupof.administracionapp.dto.nominas.DetalleNominaDTO;
import org.grupof.administracionapp.dto.nominas.NominaDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Interfaz que define los servicios relacionados con la gestión de nóminas.
 */
public interface NominaService {

    /**
     * Crea una nueva nómina a partir del DTO proporcionado.
     *
     * @param nomina objeto {@link NominaDTO} que contiene la información para crear la nómina.
     */
    void crearNomina(NominaDTO nomina);

    /**
     * Devuelve una nómina específica asociada a un empleado dado.
     *
     * @param emp UUID que identifica al empleado.
     * @param nom UUID que identifica la nómina.
     * @return un {@link NominaDTO} con la información de la nómina solicitada.
     */
    NominaDTO devuelveNominaPorEmpleadoId(UUID emp, UUID nom);

    /**
     * Busca nóminas asociadas a un empleado dentro de un rango de fechas.
     *
     * @param empleadoId UUID del empleado para filtrar las nóminas.
     * @param fechaInicio fecha de inicio del rango para la búsqueda (inclusive).
     * @param fechaFin fecha de fin del rango para la búsqueda (inclusive).
     * @return una lista de objetos {@link BusquedaNominaDTO} que coinciden con los criterios de búsqueda.
     */
    List<BusquedaNominaDTO> buscarNominas(UUID empleadoId, LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Obtiene el detalle completo de una nómina específica.
     *
     * @param id UUID que identifica la nómina cuyo detalle se desea obtener.
     * @return un objeto {@link DetalleNominaDTO} con la información detallada de la nómina.
     */
    DetalleNominaDTO obtenerDetalleNomina(UUID id);
}