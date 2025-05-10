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

import java.util.Collections;
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

    //metodo para comprobar si existe usuario y empleado
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
