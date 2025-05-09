package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.repository.EmpleadoRepository;
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
    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoService empleadoService;

    public BusquedaEmpleadosController(GeneroService generoService, EmpleadoRepository empleadoRepository, EmpleadoService empleadoService) {
        this.generoService = generoService;
        this.empleadoRepository = empleadoRepository;
        this.empleadoService = empleadoService;
    }

    /**
     * Muestra el formulario de búsqueda de empleados.
     * <p>
     * Este método verifica si hay un usuario autenticado en la sesión.
     * Si no hay usuario en sesión, redirige al formulario de inicio de sesión.
     * Si hay usuario, carga la lista de géneros disponibles y atributos iniciales para el formulario.
     *
     * @param modelo   el objeto {@link Model} utilizado para pasar atributos a la vista.
     * @param session  la sesión HTTP actual, de donde se extrae el usuario autenticado.
     * @return el nombre de la vista para el formulario de búsqueda, o una redirección al login si no hay sesión activa.
     */
    @GetMapping("/buscar")
    public String mostrarFormularioBusqueda(Model modelo, HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioDTO == null) {
            logger.warn("Intento de acceso al dashboard sin usuario en sesión en /buscar");
            return "redirect:/login/username";
        }

        modelo.addAttribute("generos", generoService.getAllGeneros());
        modelo.addAttribute("genero", null);
        modelo.addAttribute("nombre", "");
        modelo.addAttribute("resultados", Collections.emptyList());
        return "empleado/main/empleado-buscar";
    }

    /**
     * Procesa la búsqueda de empleados según los parámetros proporcionados.
     * <p>
     * Este método permite buscar empleados filtrando por nombre, género o ambos.
     * Si no se especifica ningún filtro, se devuelven todos los empleados.
     * También verifica si el usuario está autenticado en la sesión. En caso contrario, redirige al login.
     *
     * @param nombre   el nombre (o parte de él) por el que se desea buscar empleados.
     * @param genero   el ID del género por el que se desea filtrar (puede ser null).
     * @param modelo   el objeto {@link Model} usado para pasar datos a la vista.
     * @param session  la sesión HTTP actual, utilizada para validar la autenticación del usuario.
     * @return el nombre de la vista de búsqueda con los resultados obtenidos o redirección al login si no hay sesión activa.
     */
    @PostMapping("/buscar")
    //required en false para genero para que no salte excepcion
    public String procesarBusqueda(@RequestParam String nombre, @RequestParam(required = false) UUID genero,
                                   Model modelo,
                                   HttpSession session) {

        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioDTO == null) {
            logger.warn("Intento de acceso al dashboard sin usuario en sesión en /buscar POST");
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
}
