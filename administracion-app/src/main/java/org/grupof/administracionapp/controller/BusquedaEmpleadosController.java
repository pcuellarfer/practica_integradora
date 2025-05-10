package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.services.Empleado.EmpleadoService;
import org.grupof.administracionapp.services.Genero.GeneroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
public class BusquedaEmpleadosController {

    private static final Logger logger = LoggerFactory.getLogger(BusquedaEmpleadosController.class);

    private final GeneroService generoService;
    private final EmpleadoService empleadoService;

    public BusquedaEmpleadosController(GeneroService generoService, EmpleadoService empleadoService) {
        this.generoService = generoService;
        this.empleadoService = empleadoService;
    }

    /**
     * Obtiene el empleado asociado al usuario en sesión.
     *
     * Si no hay usuario o no se encuentra el empleado, se registra en el log y se devuelve null.
     *
     * @param session sesión HTTP actual
     * @param logger logger para registrar avisos y errores
     * @return el empleado correspondiente o null si no se encuentra
     */
    private Empleado obtenerEmpleadoDesdeSesion(HttpSession session, Logger logger) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario == null) {
            logger.warn("Sesión no activa.");
            return null;
        }

        Empleado empleado = empleadoService.obtenerEmpleadoPorUsuarioId(usuario.getId()).orElse(null);
        if (empleado == null) {
            logger.error("No se encontró el empleado para usuario ID: {}", usuario.getId());
        }

        return empleado;
    }

    /**
     * Muestra el formulario de búsqueda de empleados.
     *
     * Si no hay usuario en sesión, redirige al login.
     * Carga la lista de géneros y todos los empleados ordenados.
     *
     * @param modelo modelo para pasar datos a la vista
     * @param session sesión HTTP actual
     * @return vista del formulario de búsqueda
     */
    @GetMapping("/buscar")
    public String mostrarFormularioBusqueda(Model modelo, HttpSession session) {
        Empleado empleado = obtenerEmpleadoDesdeSesion(session, logger);
        if (empleado == null) {
            return "redirect:/login/username";
        }

        modelo.addAttribute("generos", generoService.getAllGeneros());
        modelo.addAttribute("resultados", empleadoService.getEmpleadosOrdenados()); // o lo que tengas
        modelo.addAttribute("nombre", "");
        modelo.addAttribute("selectedGeneroId", null);
        return "empleado/main/empleado-buscar";
    }

    /**
     * Procesa la búsqueda de empleados por nombre y género.
     *
     * Si no hay usuario en sesión, redirige al login.
     * Añade al modelo los resultados de la búsqueda y los filtros aplicados.
     *
     * @param nombre texto a buscar en el nombre del empleado
     * @param genero ID del género seleccionado (opcional)
     * @param modelo modelo para pasar datos a la vista
     * @param session sesión HTTP actual
     * @return vista con los resultados de la búsqueda
     */
    @PostMapping("/buscar")
    //required en false para genero para que no salte excepcion
    public String procesarBusqueda(@RequestParam String nombre,
                                   @RequestParam(required = false) UUID genero,
                                   Model modelo,
                                   HttpSession session) {
        Empleado empleado = obtenerEmpleadoDesdeSesion(session, logger);
        if (empleado == null) {
            return "redirect:/login/username";
        }

        List<Empleado> resultados = empleadoService.buscarEmpleados(nombre, genero);

        modelo.addAttribute("resultados", resultados);
        modelo.addAttribute("nombre", nombre);
        modelo.addAttribute("selectedGeneroId", genero);
        modelo.addAttribute("generos", generoService.getAllGeneros());
        return "empleado/main/empleado-buscar";
    }

    /**
     * Bloquea a un empleado por su ID.
     *
     * Si no hay usuario en sesión, redirige al login.
     * Añade un mensaje flash tras la operación.
     *
     * @param empleadoId ID del empleado a bloquear
     * @param session sesión HTTP actual
     * @param redirectAttributes atributos para mensajes flash
     * @return redirección al formulario de búsqueda
     */
    @PostMapping("/bloquear")
    public String bloquearUsuario(@RequestParam("empleadoId") UUID empleadoId, HttpSession session, RedirectAttributes redirectAttributes) {

        Empleado empleado = obtenerEmpleadoDesdeSesion(session, logger);
        if (empleado == null) {
            return "redirect:/login/username";
        }

        empleadoService.bloquearEmpleado(empleadoId);
        redirectAttributes.addFlashAttribute("mensaje", "Empleado bloqueado correctamente");
        return "redirect:/dashboard/buscar";
    }

    /**
     * Desbloquea a un empleado por su ID.
     *
     * Si no hay usuario en sesión, redirige al login.
     * Añade un mensaje flash tras la operación.
     *
     * @param empleadoId ID del empleado a desbloquear
     * @param session sesión HTTP actual
     * @param redirectAttributes atributos para mensajes flash
     * @return redirección al formulario de búsqueda
     */
    @PostMapping("/desbloquear")
    public String desbloquearUsuario(@RequestParam("empleadoId") UUID empleadoId, HttpSession session, RedirectAttributes redirectAttributes) {

        Empleado empleado = obtenerEmpleadoDesdeSesion(session, logger);
        if (empleado == null) {
            return "redirect:/login/username";
        }

        empleadoService.desbloquearEmpleado(empleadoId);
        redirectAttributes.addFlashAttribute("mensaje", "Empleado desbloqueado correctamente");
        return "redirect:/dashboard/buscar";
    }
}
