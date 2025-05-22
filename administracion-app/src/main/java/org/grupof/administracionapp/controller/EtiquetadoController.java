package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.entity.Etiqueta;
import org.grupof.administracionapp.services.Empleado.EmpleadoService;
import org.grupof.administracionapp.services.etiqueta.EtiquetaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
public class EtiquetadoController {

    private static final Logger logger = LoggerFactory.getLogger(EtiquetadoController.class);


    private final EtiquetaService etiquetaService;
    private final EmpleadoService empleadoService;


    public EtiquetadoController(
            EtiquetaService etiquetaService,
            EmpleadoService empleadoService) {
        this.etiquetaService = etiquetaService;
        this.empleadoService = empleadoService;
    }

    /**
     * Obtiene el jefe asociado al usuario en sesión.
     * Si no hay usuario o no se encuentra el jefe, se registra en el log y se devuelve null.
     *
     * @param session sesión HTTP actual
     * @return el jefe correspondiente o null si no se encuentra
     */
    private Empleado obtenerJefeDesdeSesion(HttpSession session) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario == null) {
            EtiquetadoController.logger.warn("Sesión no activa.");
            return null;
        }

        Empleado jefe = empleadoService.obtenerEmpleadoPorUsuarioId(usuario.getId()).orElse(null);
        if (jefe == null) {
            EtiquetadoController.logger.error("No se encontró el jefe para usuario ID: {}", usuario.getId());
        }

        return jefe;
    }

    /**
     * Muestra la vista para asignar subordinados a un jefe.
     * Si no hay jefe en sesión, redirige al login.
     * Carga en el modelo el jefe y la lista de empleados disponibles para asignar.
     *
     * @param session sesión HTTP actual
     * @param modelo  modelo para pasar datos a la vista
     * @return vista de asignación de subordinados o redirección al login
     */
    @GetMapping("/asignar")
    public String asignarSubordinados(HttpSession session, Model modelo) {

        Empleado jefe = obtenerJefeDesdeSesion(session);
        if (jefe == null) {
            return "redirect:/login/username";
        }

        //lista de posibles subordinados, sin el empleado mismo, sus subordinados y los subordinados de este y sin sus jefes y los jefes de este
        List<Empleado> posiblesSubordinados = empleadoService.buscarTodosMenosConJerarquia(jefe.getId());

        //meter al modelo el jefe y sus posibles subordinados
        logger.info("Mostrando vista de asignación de subordinados para el jefe: {}", jefe.getNombre());
        modelo.addAttribute("jefe", jefe);
        modelo.addAttribute("empleados", posiblesSubordinados);

        return "empleado/main/empleado-asignacionSubordinados";
    }

    /**
     * Procesa la asignación de subordinados a un jefe.
     * Si no hay jefe en sesión, redirige al login.
     * Asocia los empleados seleccionados como subordinados del jefe y guarda los cambios.
     *
     * @param subordinadoIds lista de IDs de empleados seleccionados
     * @param session        sesión HTTP actual
     * @return redirección al submenú de etiquetado
     */
    @PostMapping("/asignar")
    public String procesarAsignacion(@RequestParam (value=  "subordinadoIds", required = false) List<UUID> subordinadoIds,
                                     HttpSession session) {

        Empleado jefe = obtenerJefeDesdeSesion(session);
        if (jefe == null) {
            return "redirect:/login/username";
        }

        if (subordinadoIds == null || subordinadoIds.isEmpty()) {
            // No se seleccionó ningún empleado → volver a la vista con mensaje o simplemente redirigir
            logger.warn("No se seleccionó ningún subordinado");
            return "redirect:/asignar"; // o volver a mostrar la página actual
        }

        List<Empleado> subordinados = empleadoService.buscarPorIds(subordinadoIds); //mete en una lista los empleados con las ids que se hayan seleccionado
        for (Empleado subordinado : subordinados) { //bucle para asignar jefe a los subordinados que lleguen de la vista
            subordinado.setJefe(jefe);
            logger.info("Asignado subordinado {} al jefe {}", subordinado.getNombre(), jefe.getNombre());
        }

        empleadoService.guardarTodos(subordinados); //guardar todos los subordinados
        logger.info("Asignación de subordinados completada para el jefe: {}", jefe.getNombre());

        return "redirect:/dashboard/submenu-etiquetado";
    }

    /**
     * Muestra la vista de gestión de etiquetas definidas por el jefe.
     * Si no hay jefe en sesión, redirige al login.
     * Carga en el modelo el jefe, sus etiquetas actuales y una nueva etiqueta vacía para crear.
     *
     * @param session sesión HTTP actual
     * @param modelo  modelo para pasar datos a la vista
     * @return vista de gestión de etiquetas o redirección al login
     */
    @GetMapping("/crearEtiquetas")
    public String crearEtiquetas(HttpSession session, Model modelo) {

        Empleado jefe = obtenerJefeDesdeSesion(session);
        if (jefe == null) {
            return "redirect:/login/username";
        }

        modelo.addAttribute("jefe", jefe);
        modelo.addAttribute("etiquetas", jefe.getEtiquetasDefinidas()); //le pasa las etiquetas definidas que ya tenga el jefe
        //mandarle el objeto vacio para la vista
        modelo.addAttribute("nuevaEtiqueta", new Etiqueta());

        logger.info("Mostrando vista de gestión de etiquetas para el jefe {}", jefe.getNombre());
        return "empleado/main/empleado-etiquetas";
    }

    /**
     * Guarda una nueva etiqueta definida por el jefe.
     * Si no hay jefe en sesión, redirige al login.
     * Asocia la etiqueta al jefe y la guarda. Si ya existe, añade un mensaje de error.
     *
     * @param etiqueta           objeto etiqueta recibido del formulario
     * @param session            sesión HTTP actual
     * @param redirectAttributes atributos para mensajes flash
     * @return redirección a la vista de gestión de etiquetas
     */
    @PostMapping("/crearEtiquetas")
    public String crearEtiqueta(@ModelAttribute("nuevaEtiqueta") Etiqueta etiqueta,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        Empleado jefe = obtenerJefeDesdeSesion(session);
        if (jefe == null) {
            return "redirect:/login/username";
        }

        etiqueta.setJefe(jefe);
        try {
            etiquetaService.guardarEtiqueta(etiqueta); //intenta guardar la etiqueta, si ya existe la combinacion jefe-etiqueta, salta la exciepcion
        } catch (IllegalArgumentException e) {
            logger.warn("Etiqueta duplicada: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Ya has has puesto esta etiqueta crack");
        }

        return "redirect:/crearEtiquetas";
    }

    /**
     * Muestra la vista de etiquetado de subordinados.
     * Si no hay jefe en sesión, redirige al login.
     * Carga en el modelo la lista de subordinados y las etiquetas definidas por el jefe.
     *
     * @param session sesión HTTP actual
     * @param modelo  modelo para pasar datos a la vista
     * @return vista de etiquetado o redirección al login
     */
    @GetMapping("/etiquetado")
    public String mostrarEtiquetado(HttpSession session, Model modelo) {

        Empleado jefe = obtenerJefeDesdeSesion(session);
        if (jefe == null) {
            return "redirect:/login/username";
        }

        List<Empleado> subordinados = empleadoService.buscarPorJefe(jefe);
        List<Etiqueta> etiquetas = jefe.getEtiquetasDefinidas();

        modelo.addAttribute("empleados", subordinados);
        modelo.addAttribute("etiquetas", etiquetas);

        logger.info("Vista de etiquetado mostrada para el jefe {} con {} subordinados.", jefe.getNombre(), subordinados.size());
        return "empleado/main/empleado-etiquetado";
    }

    /**
     * Procesa la asignación de etiquetas a los empleados seleccionados.
     * Si no hay jefe en sesión, redirige al login.
     * Si no se seleccionan empleados o etiquetas, muestra un mensaje de error.
     * Asocia las etiquetas a los empleados y guarda los cambios.
     *
     * @param empleadosIds       lista de IDs de empleados seleccionados
     * @param etiquetasIds       lista de IDs de etiquetas seleccionadas
     * @param session            sesión HTTP actual
     * @param redirectAttributes atributos para mensajes flash
     * @return vista de etiquetado o redirección con mensaje de error
     */
    @PostMapping("/etiquetado")
    public String procesarEtiquetado(@RequestParam(name = "empleados", required = false) List<UUID> empleadosIds,
                                     @RequestParam(name = "etiquetas", required = false) List<UUID> etiquetasIds,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {

        Empleado jefe = obtenerJefeDesdeSesion(session);
        if (jefe == null) {
            return "redirect:/login/username";
        }

        if (empleadosIds == null || empleadosIds.isEmpty() || etiquetasIds == null || etiquetasIds.isEmpty()) {
            logger.warn("No se seleccionaron empleados o etiquetas para asignar.");
            redirectAttributes.addFlashAttribute("error", "Debes seleccionar al menos un empleado y una etiqueta.");
            return "redirect:/etiquetado";
        }

        //pasar de ids a objetos
        List<Empleado> empleados = empleadoService.buscarPorIds(empleadosIds);
        List<Etiqueta> etiquetas = etiquetaService.buscarPorIds(etiquetasIds);

        try {
            for (Etiqueta etiqueta : etiquetas) {
                etiqueta.getEmpleadosEtiquetados().addAll(empleados);
                etiquetaService.guardarEtiqueta(etiqueta);
            }
            logger.info("Etiquetas {} asignadas a empleados {}", etiquetasIds, empleadosIds);
        } catch (IllegalArgumentException e) { //si ya existe esa combinacion etiqueta-empleado lo tira para atras
            logger.warn("Error al etiquetar: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Alguna de las etiquetas ya está definida para ese jefe.");
            return "redirect:/etiquetado";
        }

        logger.info("Etiquetas {} asignadas a empleados {}", etiquetasIds, empleadosIds);
        return "empleado/main/empleado-etiquetado";
    }

    /**
     * Muestra el formulario para eliminar etiquetas asignadas a empleados.
     * Si no hay jefe en sesión, redirige al login.
     * Si se proporciona un ID de empleado, carga sus etiquetas asignadas y lo añade al modelo.
     * Siempre carga la lista de subordinados del jefe para selección.
     *
     * @param empleadoId ID opcional del empleado a consultar
     * @param session    sesión HTTP actual
     * @param modelo     modelo para pasar datos a la vista
     * @return vista del formulario de eliminación de etiquetas o redirección al login
     */
    @GetMapping("/etiquetado/eliminar")
    public String mostrarFormularioEliminacion(@RequestParam(name = "empleadoId", required = false) UUID empleadoId,
                                               HttpSession session, Model modelo) {
        logger.info("Accediendo al formulario de eliminación de etiquetas");

        Empleado jefe = obtenerJefeDesdeSesion(session);
        if (jefe == null) {
            return "redirect:/login/username";
        }

        //mete en lista los subordinados del jefe
        List<Empleado> subordinados = empleadoService.buscarPorJefe(jefe);
        logger.info("Se encontraron {} subordinados del jefe con ID: {}", subordinados.size(), jefe.getId());
        modelo.addAttribute("empleados", subordinados);

        //esto es para la segunda vez que se hace la solicitud get, no se hara la primera vez porque empleado id llega null
        if (empleadoId != null) {
            logger.info("Se recibió el ID del empleado a consultar: {}", empleadoId);
            //pasa de id a objeto
            Empleado empleadoSeleccionado = empleadoService.buscarPorId(empleadoId).orElse(null);
            if (empleadoSeleccionado != null) {
                logger.info("Empleado seleccionado encontrado: {}", empleadoSeleccionado.getId());
                modelo.addAttribute("empleadoSeleccionado", empleadoSeleccionado);

                //estas etiquetas serviran para rellenar el select de etiquetas
                List<Etiqueta> etiquetasAsignadas = etiquetaService.buscarPorEmpleadoId(empleadoId);
                logger.info("Etiquetas asignadas encontradas: {}", etiquetasAsignadas.size());
                modelo.addAttribute("etiquetasAsignadas", etiquetasAsignadas);
            } else {
                logger.warn("No se encontró el empleado con ID: {}", empleadoId);
            }
        } else {
            logger.info("No se recibió ID de empleado. Solo se mostrará la lista de subordinados.");
        }

        return "empleado/main/empleado-eliminar-etiqueta";
    }

    /**
     * Elimina etiquetas asignadas a un empleado específico.
     * Si no hay jefe en sesión, redirige al login.
     * Verifica que el empleado y las etiquetas existan, y que las etiquetas pertenezcan al jefe actual.
     * Elimina la relación entre cada etiqueta y el empleado indicado.
     *
     * @param empleadoId   ID del empleado al que se le eliminarán las etiquetas
     * @param etiquetasIds lista de IDs de etiquetas a eliminar
     * @param session      sesión HTTP actual
     * @return redirección a la vista de eliminación con el empleado seleccionado
     */
    @PostMapping("/etiquetado/eliminar")
    public String eliminarEtiquetas(@RequestParam UUID empleadoId,
                                    @RequestParam(required = false) List<UUID> etiquetasIds,
                                    HttpSession session) {
        logger.info("Inicio de eliminación de etiquetas para empleado ID: {}", empleadoId);

        Empleado jefe = obtenerJefeDesdeSesion(session);
        if (jefe == null) {
            return "redirect:/login/username";
        }

        Empleado empleado = empleadoService.buscarPorId(empleadoId).orElse(null);
        if (empleado == null) {
            logger.error("Empleado a etiquetar no encontrado: {}", empleadoId);
            return "redirect:/etiquetado/eliminar";
        }

        if (etiquetasIds == null || etiquetasIds.isEmpty()) {
            logger.warn("No se proporcionaron etiquetas para eliminar.");
            return "redirect:/etiquetado/eliminar?empleadoId=" + empleadoId;
        }

        logger.info("Empleado encontrado: {}. Etiquetas a eliminar: {}", empleado.getId(), etiquetasIds.size());
        //si tanto etiqueta id como empleado tienen contenido
        for (UUID etiquetaId : etiquetasIds) {
            //por cada etiqueta pasa de id a objeto
            Etiqueta etiqueta = etiquetaService.buscarPorId(etiquetaId).orElse(null);
            if (etiqueta != null) {
                if (etiqueta.getJefe().equals(jefe)) { //comprueba qu la etiqueta pertenece al jefe que la borra, el equals devuelve si o no
                    etiqueta.getEmpleadosEtiquetados().remove(empleado);//en la etiqueta, en su lista de empleados etiquetados, borra el empleado seleccionado
                    etiquetaService.guardarEtiqueta(etiqueta); //guarda la nueva etiqueta sin el empleado
                    logger.info("Etiqueta eliminada: {} para empleado: {}", etiquetaId, empleadoId);
                } else {
                    logger.warn("Etiqueta {} no pertenece al jefe actual. No se elimina.", etiquetaId);
                }
            } else {
                logger.warn("Etiqueta no encontrada: {}", etiquetaId);
            }
        }

        logger.info("Finalización de eliminación de etiquetas. Redirigiendo a vista de eliminación.");
        return "redirect:/etiquetado/eliminar?empleadoId=" + empleadoId;
    }
}
