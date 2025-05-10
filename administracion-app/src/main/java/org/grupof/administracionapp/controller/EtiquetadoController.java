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

    private Empleado obtenerJefeDesdeSesion(HttpSession session, Logger logger) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario == null) {
            logger.warn("Sesión no activa.");
            return null;
        }

        Empleado jefe = empleadoService.obtenerEmpleadoPorUsuarioId(usuario.getId()).orElse(null);
        if (jefe == null) {
            logger.error("No se encontró el jefe para usuario ID: {}", usuario.getId());
        }

        return jefe;
    }

    @GetMapping("/asignar")
    public String asignarSubordinados(HttpSession session, Model modelo) {

        Empleado jefe = obtenerJefeDesdeSesion(session, logger);
        if (jefe == null) {
            return "redirect:/login/username";
        }

        List<Empleado> posiblesSubordinados = empleadoService.buscarTodosMenos(jefe.getId());

        logger.info("Mostrando vista de asignación de subordinados para el jefe: {}", jefe.getNombre());
        modelo.addAttribute("jefe", jefe);
        modelo.addAttribute("empleados", posiblesSubordinados);

        return "empleado/main/empleado-asignacionSubordinados";
    }

    @PostMapping("/asignar")
    public String procesarAsignacion(@RequestParam List<UUID> subordinadoIds, HttpSession session) {

        Empleado jefe = obtenerJefeDesdeSesion(session, logger);
        if (jefe == null) {
            return "redirect:/login/username";
        }

        List<Empleado> subordinados = empleadoService.buscarPorIds(subordinadoIds);
        for (Empleado subordinado : subordinados) {
            subordinado.setJefe(jefe);
            logger.info("Asignado subordinado {} al jefe {}", subordinado.getNombre(), jefe.getNombre());
        }

        empleadoService.guardarTodos(subordinados);
        logger.info("Asignación de subordinados completada para el jefe: {}", jefe.getNombre());

        return "redirect:/dashboard/submenu-etiquetado";
    }

    @GetMapping("/crearEtiquetas")
    public String crearEtiquetas(HttpSession session, Model modelo) {

        Empleado jefe = obtenerJefeDesdeSesion(session, logger);
        if (jefe == null) {
            return "redirect:/login/username";
        }

        modelo.addAttribute("jefe", jefe);
        modelo.addAttribute("etiquetas", jefe.getEtiquetasDefinidas());
        //mandarle el objeto vacio para la vista
        modelo.addAttribute("nuevaEtiqueta", new Etiqueta());

        logger.info("Mostrando vista de gestión de etiquetas para el jefe {}", jefe.getNombre());
        return "empleado/main/empleado-etiquetas";
    }

    @PostMapping("/crearEtiquetas")
    public String crearEtiqueta(@ModelAttribute("nuevaEtiqueta") Etiqueta etiqueta,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        Empleado jefe = obtenerJefeDesdeSesion(session, logger);
        if (jefe == null) {
            return "redirect:/login/username";
        }

        etiqueta.setJefe(jefe);
        try {
            etiquetaService.guardarEtiqueta(etiqueta);
        } catch (IllegalArgumentException e) {
            logger.warn("Etiqueta duplicada: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Ya has has puesto esta etiqueta crack");
        }

        return "redirect:/crearEtiquetas";
    }

    @GetMapping("/etiquetado")
    public String mostrarEtiquetado(HttpSession session, Model modelo) {

        Empleado jefe = obtenerJefeDesdeSesion(session, logger);
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

    @PostMapping("/etiquetado")
    public String procesarEtiquetado(@RequestParam(name = "empleados", required = false) List<UUID> empleadosIds,
                                     @RequestParam(name = "etiquetas", required = false) List<UUID> etiquetasIds,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {

        Empleado jefe = obtenerJefeDesdeSesion(session, logger);
        if (jefe == null) {
            return "redirect:/login/username";
        }

        if (empleadosIds == null || empleadosIds.isEmpty() || etiquetasIds == null || etiquetasIds.isEmpty()) {
            logger.warn("No se seleccionaron empleados o etiquetas para asignar.");
            redirectAttributes.addFlashAttribute("error", "Debes seleccionar al menos un empleado y una etiqueta.");
            return "redirect:/etiquetado";
        }

        List<Empleado> empleados = empleadoService.buscarPorIds(empleadosIds);
        List<Etiqueta> etiquetas = etiquetaService.buscarPorIds(etiquetasIds);

        try {
            for (Etiqueta etiqueta : etiquetas) {
                etiqueta.getEmpleadosEtiquetados().addAll(empleados);
                etiquetaService.guardarEtiqueta(etiqueta);
            }
            logger.info("Etiquetas {} asignadas a empleados {}", etiquetasIds, empleadosIds);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al etiquetar: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Alguna de las etiquetas ya está definida para ese jefe.");
            return "redirect:/etiquetado";
        }

        logger.info("Etiquetas {} asignadas a empleados {}", etiquetasIds, empleadosIds);
        return "empleado/main/empleado-etiquetado";
    }

    @GetMapping("/etiquetado/eliminar")
    public String mostrarFormularioEliminacion(@RequestParam(name = "empleadoId", required = false) UUID empleadoId,
                                               HttpSession session, Model modelo) {
        logger.info("Accediendo al formulario de eliminación de etiquetas");

        Empleado jefe = obtenerJefeDesdeSesion(session, logger);
        if (jefe == null) {
            return "redirect:/login/username";
        }

        List<Empleado> subordinados = empleadoService.buscarPorJefe(jefe);
        logger.info("Se encontraron {} subordinados del jefe con ID: {}", subordinados.size(), jefe.getId());
        modelo.addAttribute("empleados", subordinados);

        if (empleadoId != null) {
            logger.info("Se recibió el ID del empleado a consultar: {}", empleadoId);
            Empleado empleadoSeleccionado = empleadoService.buscarPorId(empleadoId).orElse(null);
            if (empleadoSeleccionado != null) {
                logger.info("Empleado seleccionado encontrado: {}", empleadoSeleccionado.getId());
                modelo.addAttribute("empleadoSeleccionado", empleadoSeleccionado);

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

    @PostMapping("/etiquetado/eliminar")
    public String eliminarEtiquetas(@RequestParam UUID empleadoId,
                                    @RequestParam List<UUID> etiquetasIds,
                                    HttpSession session) {
        logger.info("Inicio de eliminación de etiquetas para empleado ID: {}", empleadoId);

        Empleado jefe = obtenerJefeDesdeSesion(session, logger);
        if (jefe == null) {
            return "redirect:/login/username";
        }

        Empleado empleado = empleadoService.buscarPorId(empleadoId).orElse(null);
        if (empleado == null) {
            logger.error("Empleado a etiquetar no encontrado: {}", empleadoId);
            return "redirect:/etiquetado/eliminar";
        }

        logger.info("Empleado encontrado: {}. Etiquetas a eliminar: {}", empleado.getId(), etiquetasIds.size());

        if (etiquetasIds.isEmpty()) {
            logger.warn("No se proporcionaron etiquetas para eliminar.");
            return "redirect:/etiquetado/eliminar?empleadoId=" + empleadoId;
        }

        for (UUID etiquetaId : etiquetasIds) {
            Etiqueta etiqueta = etiquetaService.buscarPorId(etiquetaId).orElse(null);
            if (etiqueta != null) {
                if (etiqueta.getJefe().equals(jefe)) {
                    etiqueta.getEmpleadosEtiquetados().remove(empleado);
                    etiquetaService.guardarEtiqueta(etiqueta);
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
