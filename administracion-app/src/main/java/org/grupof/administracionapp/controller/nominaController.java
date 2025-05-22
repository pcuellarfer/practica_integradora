package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.services.Empleado.EmpleadoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

/**
 * Controlador encargado de manejar las vistas y acciones relacionadas con las nóminas.
 */
@RequestMapping("/nominas")
@Controller
public class nominaController {

    private final EmpleadoService empleadoService;
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(nominaController.class);

    public nominaController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    /**
     * Muestra una vista para seleccionar un empleado del sistema.
     * Si no hay usuario en sesión, redirige al login.
     *
     * @param model   modelo para pasar datos a la vista
     * @param session sesión HTTP actual
     * @return vista con la lista de empleados o redirección al login
     */
    @GetMapping("/seleccionar-empleado")
    public String seleccionarEmpleado(Model model, HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");
        Boolean autenticado = (Boolean) session.getAttribute("autenticado");

        if (usuarioDTO == null || autenticado == null || !autenticado) {
            logger.warn("Intento de acceso a /nominas/seleccionar-empleado sin usuario en sesión");
            return "redirect:/login/username";
        }

        List<Empleado> empleados = empleadoService.getEmpleadosOrdenados();
        model.addAttribute("empleados", empleados);
        logger.info("Vista de selección de empleado cargada con {} empleados", empleados.size());
        return "empleado/main/seleccionar-empleado";
    }

    /**
     * Muestra la vista para buscar nóminas si el usuario está en sesión.
     * Si no hay usuario, redirige al login.
     *
     * @param session sesión HTTP actual
     * @return vista de búsqueda de nóminas o redirección al login
     */
    @GetMapping("/buscarNominas")
    public String buscarNominas(HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");
        Boolean autenticado = (Boolean) session.getAttribute("autenticado");

        if (usuarioDTO == null || autenticado == null || !autenticado) {
            logger.warn("Intento de acceso a /buscarNominas sin usuario en sesión");
            return "redirect:/login/username";
        }
        return "empleado/main/empleado-buscar-nominas";
    }

    /**
     * Muestra la vista con el detalle de una nómina si el usuario está en sesión.
     * Si no hay usuario, redirige al login.
     *
     * @param session sesión HTTP actual
     * @return vista de detalle de nómina o redirección al login
     */
    @GetMapping("/detalle")
    public String mostrarDetalleNomina(HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");
        Boolean autenticado = (Boolean) session.getAttribute("autenticado");

        if (usuarioDTO == null || autenticado == null || !autenticado) {
            logger.warn("Intento de acceso a /detalle sin usuario en sesión");
            return "redirect:/login/username";
        }
        return "empleado/main/empleado-detalle-nomina";
    }

    /**
     * Muestra el formulario general de nómina para el empleado si el usuario está autenticado.
     * Si no hay usuario en sesión o no está autenticado, redirige a la página de login.
     *
     * @param session sesión HTTP para verificar la autenticación del usuario.
     * @return la vista del formulario o redirección al login.
     */
    @GetMapping("/formulario")
    public String mostrarFormularioGeneral(HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");
        Boolean autenticado = (Boolean) session.getAttribute("autenticado");

        if (usuarioDTO == null || autenticado == null || !autenticado) {
            logger.warn("Intento de acceso a /formulario sin usuario en sesión");
            return "redirect:/login/username";
        }
        return "empleado/main/empleado-nomina-form";
    }

}