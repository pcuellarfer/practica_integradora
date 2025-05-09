package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupof.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.services.Empleado.EmpleadoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para gestionar las vistas del panel principal (dashboard)
 * tanto para usuarios como para empleados.
 */
@Controller
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    private final EmpleadoService empleadoService;


    /**
     * Constructor que inyecta el servicio de empleado.
     *
     * @param empleadoService servicio para gestionar empleados
     */
    public DashboardController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;

    }

    /**
     * Muestra la vista del dashboard principal dependiendo del tipo de usuario.
     * Si el usuario tiene un empleado asociado, se redirige al dashboard de empleado,
     * en caso contrario, se muestra el dashboard genérico de usuario.
     *
     * @param session sesión HTTP actual
     * @param modelo  modelo de atributos para la vista
     * @return vista correspondiente al dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model modelo) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario"); //casting

        if (usuarioDTO == null) {
            logger.warn("Intento de acceso al dashboard sin usuario en sesión");
            return "redirect:/login/username";
        }

        logger.info("Accediendo al dashboard con usuario ID: {}", usuarioDTO.getId());

        RegistroEmpleadoDTO empleadoDTO = empleadoService.buscarEmpleadoPorUsuarioId(usuarioDTO.getId());

        if (empleadoDTO == null) {
            logger.info("Usuario ID {} no tiene empleado asociado. Mostrando dashboard de usuario.", usuarioDTO.getId());
            modelo.addAttribute("usuario", usuarioDTO);
            return "usuario/main/usuario-dashboard";
        }

        logger.info("Usuario ID {} es también empleado. Mostrando dashboard de empleado.", usuarioDTO.getId());

        modelo.addAttribute("contadorSesiones", session.getAttribute("contador"));
        modelo.addAttribute("usuario", usuarioDTO);
        modelo.addAttribute("empleado", empleadoDTO);

        return "empleado/main/empleado-dashboard";
    }

    @GetMapping("dashboard/submenu-etiquetado")
    public String mostrarSubmenuSubordinados() {
        return "empleado/main/empleado-submenu-etiquetado";
    }

    @GetMapping("dashboard/submenu-productos")
    public String mostrarSubmenuProductos() {
        return "empleado/main/empleado-submenu-productos";
    }
}