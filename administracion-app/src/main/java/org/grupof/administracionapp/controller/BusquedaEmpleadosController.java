package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.services.Departamento.DepartamentoService;
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

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
public class BusquedaEmpleadosController {

    private static final Logger logger = LoggerFactory.getLogger(BusquedaEmpleadosController.class);

    private final GeneroService generoService;
    private final EmpleadoService empleadoService;
    private final DepartamentoService departamentoService;

    public BusquedaEmpleadosController(GeneroService generoService, EmpleadoService empleadoService, DepartamentoService departamentoService) {
        this.generoService = generoService;
        this.empleadoService = empleadoService;
        this.departamentoService = departamentoService;
    }

    /**
     * Obtiene el empleado asociado al usuario en sesión.
     * <p>
     * Si no hay usuario o no se encuentra el empleado, se registra en el log y se devuelve null.
     *
     * @param session sesión HTTP actual
     * @return el empleado correspondiente o null si no se encuentra
     */
    private Empleado obtenerEmpleadoDesdeSesion(HttpSession session) {
        //recupera el usuario de sesion
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        //si no se encuentra se manda un null y avisa al logger
        if (usuario == null) {
            BusquedaEmpleadosController.logger.warn("Sesión no activa.");
            return null;
        }

        //intenta recuperar el empleado con el id del usuario
        Empleado empleado = empleadoService.obtenerEmpleadoPorUsuarioId(usuario.getId()).orElse(null);

        //si no se encuentra se manda un null y avisa al logger
        if (empleado == null) {
            BusquedaEmpleadosController.logger.error("No se encontró el empleado para usuario ID: {}", usuario.getId());
            //no hay un return null porque si el empleado esta vacio la variable empleado se envia vacio ya
        }

        return empleado;
    }

    /**
     * Muestra el formulario de búsqueda de empleados.
     * <p>
     * Si no hay un empleado en la sesión, redirige al formulario de login.
     * Si hay sesión válida, carga los datos necesarios para el formulario:
     * - Lista de géneros
     * - Lista de empleados ordenados
     * - Estado de bloqueo del empleado actual
     *
     * @param modelo  el modelo de Spring MVC para enviar atributos a la vista
     * @param session la sesión HTTP actual para obtener el usuario autenticado
     * @return la vista "empleado/main/empleado-buscar" o redirección al login si no hay sesión válida
     */
    @GetMapping("/buscar")
    public String mostrarFormularioBusqueda(Model modelo, HttpSession session, RedirectAttributes redirectAttributes) {
        logger.info("Accediendo al formulario de búsqueda de empleados");

        Empleado empleado = obtenerEmpleadoDesdeSesion(session);
        //comprobar si hay empleado en sesion
        if (empleado == null) {
            logger.warn("No se encontró un empleado en sesión. Redirigiendo al login.");
            //mensaje de error redirigido a la vista
            redirectAttributes.addFlashAttribute("error", "has intentado acceder a la busqueda de empleados sin un usuario/empleado autorizado, FUERA DE AQUI!");
            return "redirect:/login/username";
        }

        logger.info("Empleado en sesión: {} - Cargando datos para búsqueda", empleado.getNombre());

        boolean estadoBloqueado = empleadoService.obtenerEstadoEmpleado(empleado.getId());
        logger.info("Estado de bloqueo del empleado actual: {}", estadoBloqueado ? "Bloqueado" : "Desbloqueado");

        modelo.addAttribute("estadoBloqueado", estadoBloqueado);
        modelo.addAttribute("generos", generoService.getAllGeneros());
        modelo.addAttribute("departamentos", departamentoService.getAllDepartamentos());
        modelo.addAttribute("resultados", empleadoService.getEmpleadosOrdenados());
        //se los paso vacio porque sino thymeleaf espera esos objetos y se pone triste si no los tiene (excepcion)
        modelo.addAttribute("nombre", "");
        modelo.addAttribute("selectedGeneroId", null);
        modelo.addAttribute("selectedDepartamentoIds", null);
        modelo.addAttribute("fechaInicio", null);
        modelo.addAttribute("fechaFin", null);

        return "empleado/main/empleado-buscar";
    }

    /**
     * Procesa la búsqueda de empleados filtrando por nombre y género.
     * <p>
     * Si no hay un empleado en sesión, redirige al login.
     * Si la sesión es válida, realiza la búsqueda y carga los resultados junto con los filtros aplicados.
     *
     * @param nombre  texto parcial o completo del nombre del empleado a buscar
     * @param genero  identificador del género (opcional)
     * @param modelo  el modelo de Spring MVC para pasar atributos a la vista
     * @param session la sesión HTTP actual para verificar autenticación
     * @return la vista "empleado/main/empleado-buscar" con los resultados o redirección al login
     */
    @PostMapping("/buscar")
    public String procesarBusqueda(@RequestParam String nombre,
                                   @RequestParam(required = false) UUID genero,
                                   @RequestParam(required = false) List<UUID> departamentos,
                                   @RequestParam(required = false) String fechaInicio,
                                   @RequestParam(required = false) String fechaFin,
                                   Model modelo,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        logger.info("Procesando búsqueda de empleados. Nombre: '{}', Género ID: {}", nombre, genero);

        Empleado empleado = obtenerEmpleadoDesdeSesion(session);
        if (empleado == null) {
            //mensaje de error redirigido a la vista
            redirectAttributes.addFlashAttribute("error", "has intentado acceder a la busqueda de empleados sin un usuario/empleado autorizado, FUERA DE AQUI!");
            logger.warn("No se encontró un empleado en sesión. Redirigiendo al login. /buscar POST");
            return "redirect:/login/username";
        }

        //variables vacias para meter las fechas
        LocalDate fechaInicioBuena = null;
        LocalDate fechaFinBuena = null;

        //intentar convertir las fechas de string a LocalDate, si las fechas vienen vacias devuelve una excepcion, por eso el required false en fechas
        try {
            if (fechaInicio != null && !fechaInicio.isBlank()) {
                fechaInicioBuena = LocalDate.parse(fechaInicio);
            }
            if (fechaFin != null && !fechaFin.isBlank()) {
                fechaFinBuena = LocalDate.parse(fechaFin);
            }
        } catch (DateTimeParseException e) {
            modelo.addAttribute("mensaje", "Formato de fecha inválido. Usa aaaa-mm-dd");
        }

        //una lista de empleados filtrados por los parametros recibidos del formulario
        List<Empleado> resultados = empleadoService.buscarEmpleados(nombre, genero, departamentos, fechaInicioBuena, fechaFinBuena);
        logger.info("Se encontraron {} empleados que coinciden con los criterios de búsqueda", resultados.size());

        boolean estadoBloqueado = empleadoService.obtenerEstadoEmpleado(empleado.getId());
        logger.info("Estado de bloqueo del empleado actual /buscar POST: {}", estadoBloqueado ? "Bloqueado" : "Desbloqueado");

        List<UUID> depIds; //estas dos lineas de codigo son por si la lista de departamentos llega nula, convertirla a una lista VACIA, evitando NullPointer
        depIds = Objects.requireNonNullElse(departamentos, Collections.emptyList());

        modelo.addAttribute("estadoBloqueado", estadoBloqueado);
        //aqui van los empleados filtrados
        modelo.addAttribute("resultados", resultados);
        //estos para rellenar el formulario con lo que haya puesto el usuario antes
        modelo.addAttribute("nombre", nombre);
        modelo.addAttribute("selectedGeneroId", genero);
        modelo.addAttribute("generos", generoService.getAllGeneros());
        modelo.addAttribute("departamentos", departamentoService.getAllDepartamentos());
        modelo.addAttribute("selectedDepartamentoIds", depIds);
        modelo.addAttribute("fechaInicio", fechaInicio);
        modelo.addAttribute("fechaFin", fechaFin);

        return "empleado/main/empleado-buscar";
    }


    /**
     * Maneja la solicitud POST para bloquear a un empleado específico.
     * <p>
     * Este métdo verifica si hay un empleado autenticado en sesión. Si no lo hay, redirige al formulario de login.
     * Si el empleado está autenticado, se llama al servicio para bloquear al empleado con el motivo proporcionado.
     * Después, redirige a la vista de búsqueda con un mensaje de éxito.
     *
     * @param empleadoId          UUID del empleado que se desea bloquear.
     * @param motivoBloqueo       Motivo proporcionado para el bloqueo del empleado.
     * @param session             Sesión HTTP actual desde la cual se recupera el empleado autenticado.
     * @param redirectAttributes  Atributos utilizados para enviar mensajes flash al redirigir.
     * @return Redirección a la página de login si no hay sesión válida, o a la vista de búsqueda tras el bloqueo.
     */
    @PostMapping("/bloquear")
    public String bloquearUsuario(@RequestParam("empleadoId") UUID empleadoId,
                                  @RequestParam("motivoBloqueo") String motivoBloqueo,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        Empleado empleado = obtenerEmpleadoDesdeSesion(session);
        if (empleado == null) {
            redirectAttributes.addFlashAttribute("error", "has intentado bloquear un empleado sin un usuario/empleado autorizado, FUERA DE AQUI!");
            return "redirect:/login/username";
        }

        empleadoService.bloquearEmpleado(empleadoId, motivoBloqueo);
        redirectAttributes.addFlashAttribute("mensaje", "Empleado bloqueado correctamente");
        return "redirect:/buscar";
    }

    /**
     * Desbloquea a un empleado por su ID.
     * <p>
     * Si no hay usuario en sesión, redirige al login.
     * Añade un mensaje flash tras la operación.
     *
     * @param empleadoId         ID del empleado a desbloquear
     * @param session            sesión HTTP actual
     * @param redirectAttributes atributos para mensajes flash
     * @return redirección al formulario de búsqueda
     */
    @PostMapping("/desbloquear")
    public String desbloquearUsuario(@RequestParam("empleadoId") UUID empleadoId, HttpSession session, RedirectAttributes redirectAttributes) {

        Empleado empleado = obtenerEmpleadoDesdeSesion(session);
        if (empleado == null) {
            redirectAttributes.addFlashAttribute("error", "has intentado desbloquear un empleado sin un usuario/empleado autorizado, FUERA DE AQUI!");
            return "redirect:/login/username";
        }

        empleadoService.desbloquearEmpleado(empleadoId);
        redirectAttributes.addFlashAttribute("mensaje", "Empleado desbloqueado correctamente");
        return "redirect:/buscar";
    }
}
