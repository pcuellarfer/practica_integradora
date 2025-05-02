package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupof.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.repository.EmpleadoRepository;
import org.grupof.administracionapp.repository.GeneroRepository;
import org.grupof.administracionapp.services.Empleado.EmpleadoService;
import org.grupof.administracionapp.services.Genero.GeneroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Controlador para gestionar las vistas del panel principal (dashboard)
 * tanto para usuarios como para empleados.
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    private final EmpleadoService empleadoService;
    private final GeneroService generoService;
    private final EmpleadoRepository empleadoRepository;


    /**
     * Constructor que inyecta el servicio de empleado.
     *
     * @param empleadoService servicio para gestionar empleados
     */
    public DashboardController(EmpleadoService empleadoService, EmpleadoRepository empleadoRepository, GeneroRepository generoRepository, GeneroService generoService) {
        this.empleadoService = empleadoService;
        this.empleadoRepository = empleadoRepository;
        this.generoService = generoService;
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
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");

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

        modelo.addAttribute("usuario", usuarioDTO);
        modelo.addAttribute("empleado", empleadoDTO);

        return "empleado/main/empleado-dashboard";
    }

    /**
     * Muestra la vista con los detalles del empleado logueado.
     * Si el usuario no ha iniciado sesión, se redirige al login.
     *
     * @param session sesión HTTP actual
     * @param modelo   modelo de atributos para la vista
     * @return vista con los detalles del empleado
     */
    @GetMapping("/detalle")
    public String verDetalles(HttpSession session, Model modelo) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario == null) {
            logger.warn("Intento de acceso a detalles de empleado sin sesión iniciada");
            return "redirect:/login";
        }

        logger.info("Accediendo a los detalles del empleado para usuario ID: {}", usuario.getId());

        RegistroEmpleadoDTO empleado = empleadoService.buscarEmpleadoPorUsuarioId(usuario.getId());

        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("empleado", empleado);
        return "empleado/main/empleadoDetalle";
    }


    @GetMapping("/buscar")
    public String mostrarFormularioBusqueda(Model modelo) {
        modelo.addAttribute("generos", generoService.getAllGeneros());
        modelo.addAttribute("genero", null);
        modelo.addAttribute("nombre", "");
        modelo.addAttribute("resultados", Collections.emptyList());
        return "empleado/main/empleado-buscar";
    }

    @PostMapping("/buscar")
    //required en false para genero para que no salte excepcion
    public String procesarBusqueda(@RequestParam String nombre, @RequestParam(required = false) UUID genero, Model modelo) {

        List<Empleado> resultados;

        //nombre+genero
        if (nombre != null && !nombre.trim().isEmpty() && genero != null) {
            resultados = empleadoRepository.findByNombreContainingIgnoreCaseAndGeneroId(nombre, genero);
        }
        //nombre
        else if (nombre != null && !nombre.trim().isEmpty()) {
            resultados = empleadoRepository.findByNombreContainingIgnoreCase(nombre);
        }
        //genero
        else if (genero != null) {
            resultados = empleadoRepository.findByGeneroId(genero);
        } else {
            //NINGUNOOOO, es decir, se muestran todos los empleados
            resultados = empleadoRepository.findAll();
        }

        modelo.addAttribute("nombre", nombre);
        modelo.addAttribute("genero", genero);
        modelo.addAttribute("generos", generoService.getAllGeneros());
        modelo.addAttribute("resultados", resultados);
        return "empleado/main/empleado-buscar";
    }

    @GetMapping("/asignar")
    public String asignarSubordinados(HttpSession session, Model modelo) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login/username";
        }

        Empleado jefe = empleadoRepository.findByUsuarioId(usuario.getId()).orElse(null);

        List<Empleado> posiblesSubordinados = empleadoRepository.findByIdNot(jefe.getId());

        modelo.addAttribute("jefe", jefe);
        modelo.addAttribute("empleados", posiblesSubordinados);
        return "empleado/main/empleado-asignacionSubordinados";
    }


    @PostMapping("/asignar")
    public String procesarAsignacion(@RequestParam List<UUID> subordinadoIds, HttpSession session) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        Empleado jefe = empleadoRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        List<Empleado> subordinados = empleadoRepository.findAllById(subordinadoIds);

        for (Empleado subordinado : subordinados) {
            subordinado.setJefe(jefe);
        }

        empleadoRepository.saveAll(subordinados);

        return "redirect:/dashboard/dashboard";
    }

    @GetMapping("/etiquetado")
    public String mostrarEtiquetado(Model modelo) {

        List<Empleado> empleados = empleadoService.getEmpleadosOrdenados();
        modelo.addAttribute("empleados", empleados);

        return "empleado/main/empleado-etiquetado";
    }

    @PostMapping("/etiquetado")
    public String procesarEtiquetado(Model modelo) {
        return "empleado/main/empleado-etiquetado";
    }

}
