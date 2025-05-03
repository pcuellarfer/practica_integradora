package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupof.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.repository.EmpleadoRepository;
import org.grupof.administracionapp.services.Empleado.EmpleadoService;
import org.grupof.administracionapp.services.Genero.GeneroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public DashboardController(EmpleadoService empleadoService, EmpleadoRepository empleadoRepository, GeneroService generoService) {
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

        modelo.addAttribute("contador", session.getAttribute("contador"));
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

    /**
     * Muestra el formulario de búsqueda de empleados.
     * Este método maneja las solicitudes GET a la ruta "/buscar".
     * Inicializa el modelo con un nombre vacío y una lista vacía de resultados.
     *
     * @param modelo el modelo que se pasa a la vista
     * @return la vista del formulario de búsqueda de empleados
     */
    @GetMapping("/buscar")
    public String mostrarFormularioBusqueda(Model modelo, HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioDTO == null) {
            logger.warn("Intento de acceso al dashboard sin usuario en sesión");
            return "redirect:/login/username";
        }

        modelo.addAttribute("generos", generoService.getAllGeneros());
        modelo.addAttribute("genero", null);
        modelo.addAttribute("nombre", "");
        modelo.addAttribute("resultados", Collections.emptyList());
        return "empleado/main/empleado-buscar";
    }

    /**
     * Procesa la búsqueda de empleados por nombre.
     * Este método maneja las solicitudes POST a la ruta "/buscar".
     * Utiliza el nombre proporcionado para buscar coincidencias en el repositorio,
     * y añade los resultados al modelo para mostrarlos en la vista.
     *
     * @param nombre el nombre o parte del nombre del empleado a buscar
     * @param modelo el modelo que se pasa a la vista
     * @return la vista con los resultados de la búsqueda de empleados
     */
    @PostMapping("/buscar")
    //required en false para genero para que no salte excepcion
    public String procesarBusqueda(@RequestParam String nombre, @RequestParam(required = false) UUID genero,
                                   Model modelo,
                                   HttpSession session) {

        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioDTO == null) {
            logger.warn("Intento de acceso al dashboard sin usuario en sesión");
            return "redirect:/login/username";
        }

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

    /**
     * Bloquea un empleado y registra la acción en los logs.
     * <p>
     * Este método se encarga de bloquear al empleado especificado por su ID. Antes de
     * proceder, verifica que haya un usuario autenticado en la sesión y registra la
     * acción en los logs. Después de bloquear al empleado, se agrega un mensaje flash
     * al modelo para ser mostrado en la siguiente solicitud.
     *
     * @param empleadoId el identificador único del empleado que se desea bloquear
     * @param session la sesión HTTP que contiene la información del usuario autenticado
     * @param redirectAttributes los atributos de redirección que permiten enviar el mensaje
     *        flash al modelo para ser mostrado después de la redirección
     * @return la vista de redirección a la página de búsqueda de empleados
     */
    @PostMapping("/bloquear")
    public String bloquearUsuario(@RequestParam("empleadoId") UUID empleadoId, HttpSession session, RedirectAttributes redirectAttributes) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");  // Recuperar usuario autenticado
        if (usuario != null) {
            logger.info("Bloqueando empleado ID: {} por usuario ID: {}", empleadoId, usuario.getId());
        } else {
            logger.warn("Usuario no identificado intentando bloquear empleado ID: {}", empleadoId);
        }

        empleadoService.bloquearEmpleado(empleadoId);
        redirectAttributes.addFlashAttribute("mensaje", "Empleado bloqueado correctamente");
        return "redirect:/dashboard/buscar";
    }

    /**
     * Desbloquea un empleado y registra la acción en los logs.
     * <p>
     * Este método se encarga de desbloquear al empleado especificado por su ID. Antes de
     * proceder, verifica que haya un usuario autenticado en la sesión y registra la
     * acción en los logs. Después de desbloquear al empleado, se agrega un mensaje flash
     * al modelo para ser mostrado en la siguiente solicitud.
     *
     * @param empleadoId el identificador único del empleado que se desea desbloquear
     * @param session la sesión HTTP que contiene la información del usuario autenticado
     * @param redirectAttributes los atributos de redirección que permiten enviar el mensaje
     *        flash al modelo para ser mostrado después de la redirección
     * @return la vista de redirección a la página de búsqueda de empleados
     */
    @PostMapping("/desbloquear")
    public String desbloquearUsuario(@RequestParam("empleadoId") UUID empleadoId, HttpSession session, RedirectAttributes redirectAttributes) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario != null) {
            logger.info("Desbloqueando empleado ID: {} por usuario ID: {}", empleadoId, usuario.getId());
        } else {
            logger.warn("Usuario no identificado intentando desbloquear empleado ID: {}", empleadoId);
        }

        empleadoService.desbloquearEmpleado(empleadoId);
        redirectAttributes.addFlashAttribute("mensaje", "Empleado desbloqueado correctamente");
        return "redirect:/dashboard/buscar";
    }

    /**
     * Muestra la vista para asignar subordinados a un jefe.
     *
     * @param session sesión HTTP para obtener el usuario autenticado.
     * @param modelo  modelo de la vista para incluir jefe y empleados disponibles.
     * @return la plantilla de asignación de subordinados o redirección al login si no hay sesión.
     */
    @GetMapping("/asignar")
    public String asignarSubordinados(HttpSession session, Model modelo) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario == null) {
            logger.warn("Intento de acceso sin sesión activa. Redirigiendo a login.");
            return "redirect:/login/username";
        }

        Empleado jefe = empleadoRepository.findByUsuarioId(usuario.getId()).orElse(null);

        if (jefe == null) {
            logger.error("No se encontró el empleado asociado al usuario con ID: {}", usuario.getId());
            return "redirect:/login/username";
        }

        List<Empleado> posiblesSubordinados = empleadoRepository.findByIdNot(jefe.getId());

        logger.info("Mostrando vista de asignación de subordinados para el jefe: {}", jefe.getNombre());
        modelo.addAttribute("jefe", jefe);
        modelo.addAttribute("empleados", posiblesSubordinados);

        return "empleado/main/empleado-asignacionSubordinados";
    }

    /**
     * Procesa la asignación de subordinados seleccionados a un jefe.
     *
     * @param subordinadoIds lista de IDs de empleados seleccionados como subordinados.
     * @param session        sesión HTTP para identificar al jefe actual.
     * @return redirección al dashboard tras asignar subordinados.
     */
    @PostMapping("/asignar")
    public String procesarAsignacion(@RequestParam List<UUID> subordinadoIds, HttpSession session) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario == null) {
            logger.warn("Intento de asignación sin usuario en sesión. Redirigiendo.");
            return "redirect:/login";
        }

        Empleado jefe = empleadoRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> {
                    logger.error("Empleado no encontrado para el usuario con ID: {}", usuario.getId());
                    return new RuntimeException("Empleado no encontrado");
                });

        List<Empleado> subordinados = empleadoRepository.findAllById(subordinadoIds);
        for (Empleado subordinado : subordinados) {
            subordinado.setJefe(jefe);
            logger.info("Asignado subordinado {} al jefe {}", subordinado.getNombre(), jefe.getNombre());
        }

        empleadoRepository.saveAll(subordinados);
        logger.info("Asignación de subordinados completada para el jefe: {}", jefe.getNombre());

        return "redirect:/dashboard/dashboard";
    }

    /**
     * Muestra la vista de empleados ordenados para su etiquetado.
     *
     * @param modelo modelo de la vista para incluir la lista de empleados.
     * @return vista del etiquetado de empleados.
     */
    @GetMapping("/etiquetado")
    public String mostrarEtiquetado(Model modelo) {
        List<Empleado> empleados = empleadoService.getEmpleadosOrdenados();
        modelo.addAttribute("empleados", empleados);

        logger.info("Vista de etiquetado mostrada con {} empleados.", empleados.size());
        return "empleado/main/empleado-etiquetado";
    }

    /**
     * Procesa el etiquetado de empleados (a completar según funcionalidad futura).
     *
     * @return vista del etiquetado sin cambios aún definidos.
     */
    @PostMapping("/etiquetado")
    public String procesarEtiquetado() {
        logger.info("Formulario de etiquetado procesado (aún sin lógica definida).");
        return "empleado/main/empleado-etiquetado";
    }

}
