package org.grupof.administracionapp.controller;

import org.grupof.administracionapp.dto.Empleado.EmpleadoDTO;
import org.grupof.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.grupof.administracionapp.dto.Nomina.NominaDTO;
import org.grupof.administracionapp.services.Empleado.EmpleadoService;
import org.grupof.administracionapp.services.Nomina.NominaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.UUID;

/**
 * Controlador para la gestión de nóminas a través de la interfaz de usuario web.
 * Permite la creación, visualización y listado de nóminas asociadas a empleados.
 */
@Controller
@RequestMapping("/nomina")
public class NominaController {

    private final Logger logger = LoggerFactory.getLogger(NominaController.class);

    private final NominaService nominaService;
    private final EmpleadoService empleadoService; // Se necesita el EmpleadoService si quieres mostrar info del empleado

    /**
     * Constructor que inyecta las dependencias de {@link NominaService} y {@link EmpleadoService}.
     *
     * @param nominaService   el servicio para la lógica de negocio de las nóminas.
     * @param empleadoService el servicio para la lógica de negocio de los empleados.
     */
    public NominaController(NominaService nominaService, EmpleadoService empleadoService) {
        this.nominaService = nominaService;
        this.empleadoService = empleadoService;
    }

    /**
     * Lista todas las nóminas asociadas a un empleado específico.
     * Recibe el ID del empleado como una variable de ruta.
     *
     * @param empleadoId el ID único del empleado del que se quieren listar las nóminas.
     * @param model      el modelo Spring para pasar la lista de nóminas a la vista.
     * @return el nombre de la vista (`nomina/listanominas`) que muestra la lista de nóminas del empleado.
     */
    @GetMapping("/empleado/{empleadoId}/listar")
    public String listarNominasEmpleado(@PathVariable UUID empleadoId, Model model) {
        logger.info("Listando nóminas del empleado con ID: {}", empleadoId);
        EmpleadoDTO empleadoDTO = empleadoService.obtenerEmpleadoPorId(empleadoId);
        List<NominaDTO> nominasDTO = nominaService.obtenerNominasEmpleado(empleadoId);
        model.addAttribute("empleado", empleadoDTO);
        model.addAttribute("nominas", nominasDTO);
        return "nomina/listanominas";
    }

    /**
     * Muestra el formulario para crear una nueva nómina para un empleado específico.
     * Recibe el ID del empleado como parámetro de consulta.
     *
     * @param empleadoId el ID único del empleado para el que se creará la nómina.
     * @param model      el modelo Spring para pasar datos a la vista.
     * @return el nombre de la vista (`nomina/vistanomina`) que contiene el formulario de alta de nómina.
     */
    @GetMapping("/alta/nomina")
    public String mostrarFormularioAltaNomina(@RequestParam UUID empleadoId, Model model) {
        logger.info("Mostrando formulario de alta de nómina para el empleado con ID: {}", empleadoId);
        EmpleadoDTO empleadoDTO = empleadoService.obtenerEmpleadoPorId(empleadoId);
        model.addAttribute("empleadoId", empleadoId);
        model.addAttribute("empleado", empleadoDTO);
        model.addAttribute("nominaDTO", new NominaDTO());
        return "nomina/vistanomina";
    }

    /**
     * Endpoint para eliminar una nómina específica a través de la interfaz de usuario web.
     * Recibe el ID de la nómina como parámetro de solicitud.
     * Después de la eliminación, redirige a la página que lista las nóminas del empleado asociado.
     *
     * @param id                 el ID único de la nómina a eliminar, proporcionado como parámetro de solicitud.
     * @param redirectAttributes el objeto {@link RedirectAttributes} para añadir atributos flash para la redirección.
     * @return la cadena de redirección a la página que lista las nóminas del empleado asociado
     * (`redirect:/empleados/{empleadoId}/nominas`).
     */
    @PostMapping("/nomina/eliminar")
    public String eliminarNominaMvc(@RequestParam UUID id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando nómina con ID (MVC): {}", id);
        UUID empleadoId = nominaService.obtenerEmpleadoIdDeNomina(id); // Necesitas obtener el ID del empleado para la redirección
        nominaService.eliminarNomina(id);
        redirectAttributes.addAttribute("empleadoId", empleadoId);
        return "redirect:/empleados/{empleadoId}/nominas";
    }

    /**
     * Guarda una nueva nómina creada a partir de los datos del formulario.
     * Recibe un objeto {@link NominaDTO} con los datos de la nómina y el ID del empleado.
     * Realiza validaciones y, si son exitosas, persiste la nómina y redirige a la lista de nóminas del empleado.
     *
     * @param nominaDTO         el DTO que contiene los datos de la nómina a guardar, provenientes del formulario.
     * @param empleadoId        el ID único del empleado al que pertenece la nómina.
     * @param result            el objeto {@link BindingResult} que contiene los resultados de la validación.
     * @param redirectAttributes el objeto para añadir atributos flash para la redirección.
     * @param model             el modelo Spring para pasar datos a la vista (en caso de errores).
     * @return la cadena de redirección a la lista de nóminas del empleado (`redirect:/nomina/empleado/{empleadoId}/listar`)
     * o el nombre de la vista del formulario de alta de nómina (`nomina/vistanomina`) en caso de errores de validación.
     */
    @PostMapping("/guardar")
    public RedirectView guardarNomina(@ModelAttribute NominaDTO nominaDTO, @RequestParam UUID empleadoId) {
        logger.info("Guardando nómina para el empleado con ID: {}", empleadoId);
        nominaService.altaNomina(nominaDTO, empleadoId);
        return new RedirectView("/nomina/empleado/" + empleadoId + "/listar");
    }

}
