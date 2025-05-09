package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.entity.Etiqueta;
import org.grupof.administracionapp.repository.EmpleadoRepository;
import org.grupof.administracionapp.repository.EtiquetaRepository;
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


    private final EmpleadoRepository empleadoRepository;
    private final EtiquetaService etiquetaService;
    private final EtiquetaRepository etiquetaRepository;

    public EtiquetadoController(EmpleadoRepository empleadoRepository, EtiquetaService etiquetaService, EtiquetaRepository etiquetaRepository) {
        this.empleadoRepository = empleadoRepository;
        this.etiquetaService = etiquetaService;
        this.etiquetaRepository = etiquetaRepository;
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

        return "redirect:/dashboard/submenu-etiquetado";
    }

    /**
     * Muestra la vista para crear etiquetas. Verifica que el usuario tenga una sesión activa
     * y que sea un jefe registrado. Carga las etiquetas existentes y prepara una etiqueta nueva para el formulario.
     *
     * @param session sesión HTTP actual, usada para obtener el usuario autenticado.
     * @param modelo modelo para pasar atributos a la vista.
     * @return nombre de la vista para gestión de etiquetas o redirección al login.
     */
    @GetMapping("/crearEtiquetas")
    public String crearEtiquetas(HttpSession session, Model modelo) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario == null) {
            logger.warn("Intento de acceso sin sesión activa en /etiquetas. Redirigiendo a login.");
            return "redirect:/login/username";
        }

        Empleado jefe = empleadoRepository.findByUsuarioId(usuario.getId()).orElse(null);
        if (jefe == null) {
            logger.error("no hay un empleado con usuario_id en crearEtiquetas: {}", usuario.getId());
            return "redirect:/login/username";
        }

        modelo.addAttribute("jefe", jefe);
        modelo.addAttribute("etiquetas", jefe.getEtiquetasDefinidas());
        //mandarle el objeto vacio para la vista
        modelo.addAttribute("nuevaEtiqueta", new Etiqueta());

        logger.info("Mostrando vista de gestión de etiquetas para el jefe {}", jefe.getNombre());
        return "empleado/main/empleado-etiquetas";
    }

    /**
     * Procesa el formulario para crear una nueva etiqueta. Asocia la etiqueta al jefe actual
     * y la guarda mediante el servicio correspondiente. Maneja errores como duplicidad.
     *
     * @param etiqueta objeto etiqueta obtenido del formulario.
     * @param session sesión HTTP actual.
     * @param redirectAttributes atributos para mostrar mensajes flash en la redirección.
     * @return redirección a la vista de creación de etiquetas o al login si no hay sesión.
     */
    @PostMapping("/crearEtiquetas")
    public String crearEtiqueta(@ModelAttribute("nuevaEtiqueta") Etiqueta etiqueta,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario == null) {
            logger.warn("Intento de crear etiqueta sin sesión activa");
            return "redirect:/login/username";
        }

        Empleado jefe = empleadoRepository.findByUsuarioId(usuario.getId()).orElse(null);
        if (jefe == null) {
            logger.error("No se encontró el jefe para usuario ID: {}", usuario.getId());
            return "redirect:/login/username";
        }

        etiqueta.setJefe(jefe);
        try {
            etiquetaService.guardarEtiqueta(etiqueta);
        } catch (Exception e) {
            logger.error("Error al crear etiqueta: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Ya has has puesto esta etiqueta crack");
        }

        return "redirect:/crearEtiquetas";
    }

    /**
     * Muestra la vista de etiquetado, donde el jefe puede asignar etiquetas a sus subordinados.
     * Verifica la sesión activa y carga empleados y etiquetas disponibles.
     *
     * @param session sesión HTTP actual.
     * @param modelo modelo para pasar atributos a la vista.
     * @return nombre de la vista de etiquetado o redirección al login si no hay sesión.
     */
    @GetMapping("/etiquetado")
    public String mostrarEtiquetado(HttpSession session, Model modelo) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario == null) {
            logger.warn("Intento de acceso sin sesión activa en /etiquetado. Redirigiendo a login.");
            return "redirect:/login/username";
        }

        Empleado jefe = empleadoRepository.findByUsuarioId(usuario.getId()).orElse(null);
        if (jefe == null) {
            logger.error("no hay un empleado con usuario_id: {}", usuario.getId());
            return "redirect:/login/username";
        }

        List<Empleado> subordinados = empleadoRepository.findByJefe(jefe);
        List<Etiqueta> etiquetas = jefe.getEtiquetasDefinidas();

        modelo.addAttribute("empleados", subordinados);
        modelo.addAttribute("etiquetas", etiquetas);

        logger.info("Vista de etiquetado mostrada para el jefe {} con {} subordinados.", jefe.getNombre(), subordinados.size());
        return "empleado/main/empleado-etiquetado";
    }

    /**
     * Procesa la asignación de etiquetas a empleados seleccionados. Verifica la validez de la sesión,
     * recupera las entidades desde la base de datos y realiza la asociación.
     *
     * @param empleadosIds lista de IDs de empleados seleccionados.
     * @param etiquetasIds lista de IDs de etiquetas a aplicar.
     * @param session sesión HTTP actual.
     * @return redirección a la vista de etiquetado.
     */
    @PostMapping("/etiquetado")
    public String procesarEtiquetado(@RequestParam("empleados") List<UUID> empleadosIds,
                                     @RequestParam("etiquetas") List<UUID> etiquetasIds,
                                     HttpSession session) {

        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login/username";
        }

        Empleado jefe = empleadoRepository.findByUsuarioId(usuario.getId()).orElse(null);
        if (jefe == null) {
            return "redirect:/login/username";
        }

        List<Empleado> empleados = empleadoRepository.findAllById(empleadosIds);
        List<Etiqueta> etiquetas = etiquetaRepository.findAllById(etiquetasIds);

        for (Etiqueta etiqueta : etiquetas) {
            etiqueta.getEmpleadosEtiquetados().addAll(empleados);
            etiquetaRepository.save(etiqueta);
        }

        logger.info("Etiquetas {} asignadas a empleados {}", etiquetasIds, empleadosIds);
        return "empleado/main/empleado-etiquetado";
    }

    /**
     * Muestra el formulario para eliminar etiquetas asignadas a un empleado específico.
     * Carga la lista de subordinados y las etiquetas asignadas al empleado seleccionado (si se proporciona).
     *
     * @param empleadoId ID del empleado seleccionado, puede ser nulo.
     * @param session sesión HTTP actual.
     * @param modelo modelo para pasar datos a la vista.
     * @return nombre de la vista para eliminar etiquetas o redirección al login si no hay sesión.
     */
    @GetMapping("/etiquetado/eliminar")
    public String mostrarFormularioEliminacion(@RequestParam(name = "empleadoId", required = false) UUID empleadoId,
                                               HttpSession session, Model modelo) {
        logger.info("Accediendo al formulario de eliminación de etiquetas");

        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null) {
            logger.warn("Sesión no activa. Redirigiendo a login en /etiquetado/eliminar GET");
            return "redirect:/login/username";
        }

        logger.info("Usuario autenticado en /etiquetado/eliminar: {}", usuario.getId());

        Empleado jefe = empleadoRepository.findByUsuarioId(usuario.getId()).orElse(null);
        if (jefe == null) {
            logger.error("No se encontró el jefe con usuario ID en etiquetado/eliminar: {}", usuario.getId());
            return "redirect:/login/username";
        }

        List<Empleado> subordinados = empleadoRepository.findByJefe(jefe);
        logger.info("Se encontraron {} subordinados del jefe con ID: {}", subordinados.size(), jefe.getId());
        modelo.addAttribute("empleados", subordinados);

        if (empleadoId != null) {
            logger.info("Se recibió el ID del empleado a consultar: {}", empleadoId);
            Empleado empleadoSeleccionado = empleadoRepository.findById(empleadoId).orElse(null);
            if (empleadoSeleccionado != null) {
                logger.info("Empleado seleccionado encontrado: {}", empleadoSeleccionado.getId());
                modelo.addAttribute("empleadoSeleccionado", empleadoSeleccionado);

                List<Etiqueta> etiquetasAsignadas = etiquetaRepository.findByEmpleadosEtiquetados_Id(empleadoId);
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
     * Procesa la eliminación de etiquetas específicas de un empleado.
     * Verifica la sesión, obtiene las entidades involucradas y actualiza las relaciones en la base de datos.
     *
     * @param empleadoId ID del empleado del que se eliminarán etiquetas.
     * @param etiquetasIds lista de IDs de etiquetas a eliminar del empleado.
     * @param session sesión HTTP actual.
     * @return redirección a la misma vista con el empleado seleccionado.
     */
    @PostMapping("/etiquetado/eliminar")
    public String eliminarEtiquetas(@RequestParam UUID empleadoId,
                                    @RequestParam List<UUID> etiquetasIds,
                                    HttpSession session) {
        logger.info("Inicio de eliminación de etiquetas para empleado ID: {}", empleadoId);

        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null) {
            logger.warn("Sesión no activa. Redirigiendo a login en /etiquetado/eliminar");
            return "redirect:/login/username";
        }

        logger.info("Usuario autenticado /etiquetado/eliminar: {}", usuario.getId());

        Empleado jefe = empleadoRepository.findByUsuarioId(usuario.getId()).orElse(null);
        if (jefe == null) {
            logger.error("No se encontró el jefe con usuario ID: {}", usuario.getId());
            return "redirect:/login/username";
        }

        Empleado empleado = empleadoRepository.findById(empleadoId).orElse(null);
        if (empleado == null) {
            logger.error("Empleado a etiquetar no encontrado: {}", empleadoId);
            return "redirect:/etiquetado/eliminar";
        }

        logger.info("Empleado encontrado: {}. Etiquetas a eliminar: {}", empleado.getId(), etiquetasIds.size());

        for (UUID etiquetaId : etiquetasIds) {
            Etiqueta etiqueta = etiquetaRepository.findById(etiquetaId).orElse(null);
            if (etiqueta != null) {
                if (etiqueta.getJefe().equals(jefe)) {
                    etiqueta.getEmpleadosEtiquetados().remove(empleado);
                    etiquetaRepository.save(etiqueta);
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
