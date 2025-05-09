package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupof.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.entity.Etiqueta;
import org.grupof.administracionapp.repository.EmpleadoRepository;
import org.grupof.administracionapp.repository.EtiquetaRepository;
import org.grupof.administracionapp.services.Departamento.DepartamentoService;
import org.grupof.administracionapp.services.Empleado.EmpleadoService;
import org.grupof.administracionapp.services.Genero.GeneroService;
import org.grupof.administracionapp.services.Pais.PaisService;
import org.grupof.administracionapp.services.etiqueta.EtiquetaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final EtiquetaService etiquetaService;
    private final EtiquetaRepository etiquetaRepository;
    private final DepartamentoService departamentoService;
    private final PaisService paisService;


    /**
     * Constructor que inyecta el servicio de empleado.
     *
     * @param empleadoService servicio para gestionar empleados
     */
    public DashboardController(EmpleadoService empleadoService,
                               EmpleadoRepository empleadoRepository,
                               GeneroService generoService,
                               EtiquetaService etiquetaService,
                               EtiquetaRepository etiquetaRepository, DepartamentoService departamentoService, PaisService paisService) {
        this.empleadoService = empleadoService;
        this.empleadoRepository = empleadoRepository;
        this.generoService = generoService;
        this.etiquetaService = etiquetaService;
        this.etiquetaRepository = etiquetaRepository;
        this.departamentoService = departamentoService;
        this.paisService = paisService;
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

    /**
     * Muestra los detalles del empleado asociado al usuario actualmente autenticado.
     *
     * <p>Este método verifica si hay un usuario autenticado en la sesión. Si es así,
     * obtiene el empleado correspondiente a ese usuario, junto con información adicional
     * como el nombre del género, nombre del departamento y la ruta de la foto, y lo añade
     * al modelo para renderizar la vista.</p>
     *
     * @param session la sesión HTTP actual que contiene el usuario autenticado
     * @param modelo el objeto Model para añadir atributos que serán utilizados en la vista Thymeleaf
     * @return la ruta a la plantilla Thymeleaf que muestra los detalles del empleado;
     *         si no hay usuario en sesión o no se encuentra el empleado, redirige a la página de login
     */
    @GetMapping("/detalle")
    public String verDetalles(HttpSession session, Model modelo) {
        logger.info("Accediendo a los detalles del empleado...");

        // Obtener usuario desde la sesión
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        // Comprobar si el usuario está autenticado
        if (usuario == null) {
            logger.warn("Intento de acceso a detalles de empleado sin sesión iniciada");
            return "redirect:/login/username";
        }
        logger.info("Usuario encontrado: {}", usuario.getId());

        // Buscar empleado por ID de usuario
        Empleado enmpleado = empleadoRepository.findByUsuarioId(usuario.getId()).orElse(null);
        if (enmpleado == null) {
            logger.error("No hay un empleado con usuario_id en detalle: {}", usuario.getId());
            return "redirect:/login/username";
        }
        logger.info("Empleado encontrado: {}", enmpleado.getId());

        // Obtener detalles adicionales del empleado
        RegistroEmpleadoDTO empleado = empleadoService.buscarEmpleadoPorUsuarioId(usuario.getId());
        logger.info("Detalles del empleado obtenidos: {}", empleado);

        // Obtener nombre del género
        UUID idGenero = empleado.getPaso1PersonalDTO().getGenero();
        String nombreGenero = generoService.obtenerNombreGenero(idGenero);
        logger.info("Género obtenido: {}", nombreGenero);

        // Obtener nombre del departamento
        UUID idDepartamento = empleado.getPaso3ProfesionalDTO().getDepartamento();
        String nombreDepartamento = departamentoService.obtenerNombreDepartamento(idDepartamento);
        logger.info("Departamento obtenido: {}", nombreDepartamento);

        // Obtener URL de la foto
        Empleado empleadoEntidad = empleadoRepository.findByUsuarioId(usuario.getId()).orElse(null);
        if (empleadoEntidad != null) {
            String rutaFoto = empleadoEntidad.getFotoUrl();
            String nombreFoto = Paths.get(empleadoEntidad.getFotoUrl()).getFileName().toString(); // solo el nombre del archivo
            logger.info("Ruta de la foto del empleado: {}", nombreFoto);
            modelo.addAttribute("nombreFoto", nombreFoto);
        } else {
            logger.warn("No se encontró la entidad de empleado para el usuario ID: {}", usuario.getId());
        }

        // Agregar atributos al modelo
        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("empleado", empleado);
        modelo.addAttribute("nombreGenero", nombreGenero);
        modelo.addAttribute("nombreDepartamento", nombreDepartamento);

        return "empleado/main/empleadoDetalle";
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

    @GetMapping("/submenu-etiquetado")
    public String mostrarSubmenuSubordinados() {
        return "empleado/main/empleado-submenu-etiquetado";
    }

    @GetMapping("/submenu-productos")
    public String mostrarSubmenuProductos() {
        return "empleado/main/empleado-submenu-productos";
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

        return "redirect:/dashboard/crearEtiquetas";
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
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login/username";
        }

        Empleado jefe = empleadoRepository.findByUsuarioId(usuario.getId()).orElse(null);
        if (jefe == null) {
            return "redirect:/login/username";
        }

        List<Empleado> subordinados = empleadoRepository.findByJefe(jefe);
        modelo.addAttribute("empleados", subordinados);

        if (empleadoId != null) {
            Empleado empleadoSeleccionado = empleadoRepository.findById(empleadoId).orElse(null);
            if (empleadoSeleccionado != null) {
                modelo.addAttribute("empleadoSeleccionado", empleadoSeleccionado);
                List<Etiqueta> etiquetasAsignadas = etiquetaRepository.findByEmpleadosEtiquetados_Id(empleadoId);
                modelo.addAttribute("etiquetasAsignadas", etiquetasAsignadas);
            }
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
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login/username";

        Empleado jefe = empleadoRepository.findByUsuarioId(usuario.getId()).orElse(null);
        if (jefe == null) return "redirect:/login/username";

        Empleado empleado = empleadoRepository.findById(empleadoId).orElse(null);
        if (empleado == null) return "redirect:/dashboard/etiquetado/eliminar";

        for (UUID etiquetaId : etiquetasIds) {
            Etiqueta etiqueta = etiquetaRepository.findById(etiquetaId).orElse(null);
            if (etiqueta != null && etiqueta.getJefe().equals(jefe)) {
                etiqueta.getEmpleadosEtiquetados().remove(empleado);
                etiquetaRepository.save(etiqueta);
            }
        }

        return "redirect:/dashboard/etiquetado/eliminar?empleadoId=" + empleadoId;
    }

    @GetMapping("/editarDetalle")
    public String editarDetalle(HttpSession session, Model modelo){
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario == null) {
            logger.warn("Intento de acceso sin sesión activa en /editarDetalle. Redirigiendo a login.");
            return "redirect:/login/username";
        }

        // Buscar empleado por ID de usuario
        Empleado empleado = empleadoRepository.findByUsuarioId(usuario.getId()).orElse(null);
        if (empleado == null) {
            logger.error("No hay un empleado con usuario_id en editarDetalle: {}", usuario.getId());
            return "redirect:/login/username";
        }
        logger.info("Empleado encontrado en editarDetalle: {}", empleado.getId());

        modelo.addAttribute("listaPaises", paisService.getAllPaises());
        modelo.addAttribute("listaDepartamentos", departamentoService.getAllDepartamentos());
        modelo.addAttribute("listaGeneros", generoService.getAllGeneros());
        modelo.addAttribute("empleado", empleado);
        return "empleado/main/empleado-editar-detalle";
    }

    @Value("${app.upload.dir}")
    private String uploadDir;

    @PostMapping("/editarDetalle")
    public String editarDetalle(HttpSession session,
                                RedirectAttributes redirectAttributes,
                                @ModelAttribute("registroEmpleado") RegistroEmpleadoDTO registroEmpleado,
                                BindingResult errores,
                                Model modelo,
                                @RequestParam("foto") MultipartFile foto,
                                @RequestParam("genero") UUID generoId,
                                @RequestParam("pais") UUID paisId,
                                @RequestParam("nombre") String nombre,
                                @RequestParam("apellido") String apellido) {

        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario == null) {
            logger.warn("Intento de acceso sin sesión activa en /editarDetalle POST. Redirigiendo a login.");
            return "redirect:/login/username";
        }

        if (errores.hasErrors()) {
            modelo.addAttribute("paises", paisService.getAllPaises());
            modelo.addAttribute("generos", generoService.getAllGeneros());
            modelo.addAttribute("listaDepartamentos", departamentoService.getAllDepartamentos());
            modelo.addAttribute("error", "error");
            logger.warn("Errores en el formulario de datos personales para usuario ID: {}", usuario.getId());
            return "empleado/main/empleado-editar-detalle";
        }

        String tipo = foto.getContentType();
        if (!"image/png".equals(tipo) && !"image/gif".equals(tipo)) {
            modelo.addAttribute("errorFoto", "Solo se permiten imágenes PNG o GIF.");
            modelo.addAttribute("paises", paisService.getAllPaises());
            modelo.addAttribute("generos", generoService.getAllGeneros());
            return "empleado/auth/FormDatosPersonales";
        }

        if (foto.getSize() > 200 * 1024) {
            modelo.addAttribute("errorFoto", "La imagen debe pesar menos de 200 KB.");
            modelo.addAttribute("paises", paisService.getAllPaises());
            modelo.addAttribute("generos", generoService.getAllGeneros());
            return "empleado/auth/FormDatosPersonales";
        }

        try {
            registroEmpleado.setFotoBytes(foto.getBytes());
            registroEmpleado.setFotoTipo(foto.getContentType());
        } catch (IOException e) {
            logger.error("Error al leer la imagen", e);
            errores.rejectValue("foto", "foto.error", "Error al procesar la imagen.");
            return "empleado/auth/FormDatosPersonales";
        }

        Empleado empleado = empleadoRepository.findByUsuarioId(usuario.getId()).orElse(null);
        if (empleado == null) {
            logger.error("No hay un empleado con usuario_id en editarDetalle POST: {}", usuario.getId());
            return "redirect:/login/username";
        }
        logger.info("Empleado encontrado en editarDetalle POST: {}", empleado.getId());

        // Guardar la imagen en disco y establecer la ruta en el empleado
        if (registroEmpleado.getFotoBytes() != null && registroEmpleado.getFotoBytes().length > 0 && registroEmpleado.getFotoTipo() != null) {
            try {
                String extension = registroEmpleado.getFotoTipo().equals("image/png") ? ".png" : ".gif";
                String nombreArchivo = empleado.getId() + extension;

                File directorioBase = new File(uploadDir);
                String rutaAbsoluta = directorioBase.getAbsolutePath();
                Path ruta = Paths.get(rutaAbsoluta, nombreArchivo);

                Files.createDirectories(ruta.getParent());
                Files.write(ruta, registroEmpleado.getFotoBytes());

                empleado.setFotoUrl("/uploads/empleados/" + nombreArchivo);

            } catch (IOException e) {
                logger.error("Error al guardar la foto del empleado", e);
                return "redirect:/registro/paso5";
            }
        }

        empleado.setNombre(nombre);
        empleado.setApellido(apellido);
        empleado.setGenero(generoService.getGeneroById(generoId));
        empleado.setPais(paisService.getPaisById(paisId));

        empleadoRepository.save(empleado);

        redirectAttributes.addFlashAttribute("mensaje", "Detalles actualizados correctamente");
        return "redirect:/dashboard/detalle";
    }

}
