package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.dto.nominas.LineaNominaDTO;
import org.grupof.administracionapp.dto.nominas.NominaDTO;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.services.Empleado.EmpleadoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;
import java.util.UUID;

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

        if (usuarioDTO == null) {
            logger.warn("Intento de acceso a /nominas/seleccionar-empleado sin usuario en sesión");
            return "redirect:/login/username";
        }

        List<Empleado> empleados = empleadoService.getEmpleadosOrdenados();
        model.addAttribute("empleados", empleados);
        logger.info("Vista de selección de empleado cargada con {} empleados", empleados.size());
        return "empleado/main/seleccionar-empleado";
    }

    /**
     * Muestra el formulario para dar de alta una nueva nómina para un empleado.
     * Si no hay usuario en sesión, redirige al login.
     * Si el empleado no existe, lanza excepción.
     *
     * @param empleadoId ID del empleado para quien se crea la nómina
     * @param model      modelo para pasar datos a la vista
     * @param session    sesión HTTP actual
     * @return vista del formulario de alta de nómina o redirección al login
     */
    @GetMapping("/alta")
    public String mostrarFormularioAlta(@RequestParam UUID empleadoId, Model model, HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioDTO == null) {
            logger.warn("Intento de acceso a /nominas/alta sin usuario en sesión");
            return "redirect:/login/username";
        }

        Empleado empleado = empleadoService.buscarPorId(empleadoId)
                .orElseThrow(() -> {
                    logger.error("Empleado con ID {} no encontrado para alta de nómina", empleadoId);
                    return new IllegalArgumentException("Empleado no encontrado");
                });

        NominaDTO nominadto = new NominaDTO();
        nominadto.setEmpleadoId(empleadoId);

        nominadto.setFechaInicio(null);
        nominadto.setFechaFin(null);

        //primera linea de nomina del salario base
        LineaNominaDTO lineaInicial = new LineaNominaDTO();
        lineaInicial.setConcepto("Sueldo base");
        nominadto.setLineasNomina((Set<LineaNominaDTO>) List.of(lineaInicial));

        model.addAttribute("empleado", empleado);
        model.addAttribute("altaNominaDTO", nominadto);
        logger.info("Formulario de alta de nómina cargado para empleado con ID {}", empleadoId);
        return "empleado/main/formulario-alta";
    }

}
