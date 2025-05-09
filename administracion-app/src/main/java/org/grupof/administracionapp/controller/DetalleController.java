package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupof.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.services.Departamento.DepartamentoService;
import org.grupof.administracionapp.services.Empleado.EmpleadoService;
import org.grupof.administracionapp.services.Genero.GeneroService;
import org.grupof.administracionapp.services.Pais.PaisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Controller
public class DetalleController {

    private static final Logger logger = LoggerFactory.getLogger(DetalleController.class);

    private final GeneroService generoService;
    private final EmpleadoService empleadoService;
    private final DepartamentoService departamentoService;
    private final PaisService paisService;

    public DetalleController(GeneroService generoService, EmpleadoService empleadoService, DepartamentoService departamentoService, PaisService paisService) {
        this.generoService = generoService;
        this.empleadoService = empleadoService;
        this.departamentoService = departamentoService;
        this.paisService = paisService;
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
        Optional<Empleado> empleadoEntidadOptional = empleadoService.obtenerEmpleadoPorUsuarioId(usuario.getId());
        if (empleadoEntidadOptional.isEmpty()) {
            logger.error("No hay un empleado con usuario_id en detalle: {}", usuario.getId());
            return "redirect:/login/username";
        }
        Empleado empleadoEntidad = empleadoEntidadOptional.get();
        logger.info("Empleado encontrado: {}", empleadoEntidad.getId());

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
        String rutaFoto = empleadoEntidad.getFotoUrl();
        if (rutaFoto != null && !rutaFoto.isBlank()) {
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

    @GetMapping("/editarDetalle")
    public String editarDetalle(HttpSession session, Model modelo){
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario == null) {
            logger.warn("Intento de acceso sin sesión activa en /editarDetalle. Redirigiendo a login.");
            return "redirect:/login/username";
        }

        // Buscar empleado por ID de usuario
        Optional<Empleado> empleadoOptional = empleadoService.obtenerEmpleadoPorUsuarioId(usuario.getId());
        if (empleadoOptional.isEmpty()) {
            logger.error("No hay un empleado con usuario_id en editarDetalle: {}", usuario.getId());
            return "redirect:/login/username";
        }
        Empleado empleado = empleadoOptional.get();

        logger.info("Empleado encontrado en editarDetalle: {}", empleado.getId());

        modelo.addAttribute("listaPaises", paisService.getAllPaises());
        modelo.addAttribute("listaDepartamentos", departamentoService.getAllDepartamentos());
        modelo.addAttribute("listaGeneros", generoService.getAllGeneros());
        modelo.addAttribute("empleado", empleado);
        return "empleado/main/empleado-editar-detalle";
    }

    @Value("${app.upload.dir}")
    private String uploadDir;

    /**
     * Maneja la solicitud POST para editar los detalles personales de un empleado.
     * Este método verifica que el usuario tenga una sesión activa, valida los datos del formulario,
     * procesa la imagen de perfil (verificando tipo y tamaño), la guarda en disco y actualiza los
     * datos del empleado en la base de datos. Si ocurre algún error, redirige al formulario con los
     * mensajes correspondientes.
     *
     * @param session               Sesión HTTP actual, utilizada para obtener al usuario autenticado.
     * @param redirectAttributes    Atributos para redirigir con mensajes flash.
     * @param registroEmpleado      DTO que contiene los datos del formulario del empleado.
     * @param errores               Resultado de la validación del formulario.
     * @param modelo                Modelo usado para pasar atributos a la vista.
     * @param foto                  Imagen de perfil subida por el usuario.
     * @param generoId              Identificador del género seleccionado.
     * @param paisId                Identificador del país seleccionado.
     * @param nombre                Nuevo nombre del empleado.
     * @param apellido              Nuevo apellido del empleado.
     * @return Redirección al detalle del dashboard si se actualiza correctamente, o la vista de edición si hay errores.
     */
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

        logger.info("Inicio de edición de detalle de empleado");

        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario == null) {
            logger.warn("Sesión no activa. Redirigiendo a login.");
            return "redirect:/login/username";
        }

        logger.info("Usuario autenticado: {}", usuario.getId());

        if (errores.hasErrors()) {
            logger.warn("Errores de validación detectados para el usuario ID: {}", usuario.getId());
            modelo.addAttribute("paises", paisService.getAllPaises());
            modelo.addAttribute("generos", generoService.getAllGeneros());
            modelo.addAttribute("listaDepartamentos", departamentoService.getAllDepartamentos());
            modelo.addAttribute("error", "error");
            return "empleado/main/empleado-editar-detalle";
        }

        logger.info("Validación exitosa. Verificando la imagen...");

        String tipo = foto.getContentType();
        if (!"image/png".equals(tipo) && !"image/gif".equals(tipo)) {
            logger.warn("Tipo de archivo inválido: {}", tipo);
            modelo.addAttribute("errorFoto", "Solo se permiten imágenes PNG o GIF.");
            modelo.addAttribute("paises", paisService.getAllPaises());
            modelo.addAttribute("generos", generoService.getAllGeneros());
            return "empleado/main/empleado-editar-detalle";
        }

        if (foto.getSize() > 200 * 1024) {
            logger.warn("Imagen demasiado grande: {} bytes", foto.getSize());
            modelo.addAttribute("errorFoto", "La imagen debe pesar menos de 200 KB.");
            modelo.addAttribute("paises", paisService.getAllPaises());
            modelo.addAttribute("generos", generoService.getAllGeneros());
            return "empleado/main/empleado-editar-detalle";
        }

        try {
            registroEmpleado.setFotoBytes(foto.getBytes());
            registroEmpleado.setFotoTipo(foto.getContentType());
            logger.info("Imagen procesada correctamente. Tamaño: {} bytes", foto.getSize());
        } catch (IOException e) {
            logger.error("Error al leer la imagen", e);
            errores.rejectValue("foto", "foto.error", "Error al procesar la imagen.");
            return "empleado/main/empleado-editar-detalle";
        }

        Optional<Empleado> empleadoOptional = empleadoService.obtenerEmpleadoPorUsuarioId(usuario.getId());
        if (empleadoOptional.isEmpty()) {
            logger.error("Empleado no encontrado para usuario ID: {}", usuario.getId());
            return "redirect:/login/username";
        }
        Empleado empleado = empleadoOptional.get();
        logger.info("Empleado encontrado en editarDetalle POST: {}", empleado.getId());

        if (registroEmpleado.getFotoBytes() != null && registroEmpleado.getFotoBytes().length > 0 && registroEmpleado.getFotoTipo() != null) {
            try {
                String extension = registroEmpleado.getFotoTipo().equals("image/png") ? ".png" : ".gif";
                String nombreArchivo = empleado.getId() + extension;

                File directorioBase = new File(uploadDir);
                String rutaAbsoluta = directorioBase.getAbsolutePath();
                Path ruta = Paths.get(rutaAbsoluta, nombreArchivo);

                Files.createDirectories(ruta.getParent());
                Files.write(ruta, registroEmpleado.getFotoBytes());

                String urlFoto = "/uploads/empleados/" + nombreArchivo;
                empleado.setFotoUrl(urlFoto);
                logger.info("Foto guardada correctamente en: {}", urlFoto);

            } catch (IOException e) {
                logger.error("Error al guardar la foto del empleado en disco", e);
                modelo.addAttribute("error" , "Error al guardar la foto del empleado.");
                return "empleado/main/empleado-editar-detalle";
            }
        }

        empleado.setNombre(nombre);
        empleado.setApellido(apellido);
        empleado.setGenero(generoService.getGeneroById(generoId));
        empleado.setPais(paisService.getPaisById(paisId));

        empleadoService.guardarEmpleado(empleado);
        logger.info("Datos del empleado actualizados y guardados en la base de datos");

        redirectAttributes.addFlashAttribute("mensaje", "Detalles actualizados correctamente");
        logger.info("Redirigiendo al detalle del dashboard");
        return "redirect:/dashboard/detalle";
    }
}
