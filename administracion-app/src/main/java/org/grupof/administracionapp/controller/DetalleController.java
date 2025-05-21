package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.grupof.administracionapp.dto.Empleado.EmpleadoDetalleDTO;
import org.grupof.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
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
import java.io.IOException;

@Controller
public class DetalleController {

    private static final Logger logger = LoggerFactory.getLogger(DetalleController.class);

    private final GeneroService generoService;
    private final EmpleadoService empleadoService;
    private final DepartamentoService departamentoService;
    private final PaisService paisService;

    public DetalleController(GeneroService generoService,
                             EmpleadoService empleadoService,
                             DepartamentoService departamentoService,
                             PaisService paisService) {
        this.generoService = generoService;
        this.empleadoService = empleadoService;
        this.departamentoService = departamentoService;
        this.paisService = paisService;
    }

    /**
     * Muestra los detalles del empleado autenticado.
     *
     * Si no hay usuario en sesión o ocurre un error al obtener los datos, redirige al login.
     * Carga en el modelo el usuario y sus detalles para mostrarlos en la vista.
     *
     * @param session sesión HTTP actual
     * @param modelo modelo para pasar datos a la vista
     * @return vista con los detalles del empleado o redirección al login
     */
    @GetMapping("/detalle")
    public String verDetalles(HttpSession session, Model modelo) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");
        Boolean autenticado = (Boolean) session.getAttribute("autenticado");

        if (usuarioDTO == null || autenticado == null || !autenticado) {
            logger.warn("Intento de acceso al dashboard sin usuario en sesión");
            return "redirect:/login/username";
        }
        logger.info("Accediendo a los detalles del empleado");

        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario"); //recuperar el usuario(usuarioDTO) de la sesion
        if (usuario == null) { //si esta vacio(no hay usuario) te manda a iniciar sesion
            logger.warn("Intento de acceso sin sesión iniciada");
            return "redirect:/login/username";
        }

        try {
            EmpleadoDetalleDTO detalle = empleadoService.obtenerDetalleEmpleado(usuario.getId()); //mete en detalle un dto con los datos del empleado
            modelo.addAttribute("usuario", usuario);//añade el usuario al modelo
            modelo.addAttribute("detalle", detalle);//añade los detalles del empleado al modelo
            return "empleado/main/empleadoDetalle";
        } catch (IllegalArgumentException e) {//so no se encuentra empleado
            logger.error("Error al obtener detalles del empleado: {}", e.getMessage());
            return "redirect:/login/username";
        }
    }


    /**
     * Muestra el formulario para editar los datos del empleado.
     *
     * Si no hay usuario en sesión o ocurre un error al obtener los datos, redirige al login.
     * Carga en el modelo el DTO del empleado y las listas necesarias para los selectores del formulario.
     *
     * @param session sesión HTTP actual
     * @param modelo modelo para pasar datos a la vista
     * @return vista del formulario de edición o redirección al login
     */
    @GetMapping("/editarDetalle")
    public String editarDetalle(HttpSession session, Model modelo){
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario"); //recuperar el usuario de la sesion
        Boolean autenticado = (Boolean) session.getAttribute("autenticado");

        if (usuario == null || autenticado == null || !autenticado) {
            logger.warn("Intento de acceso sin sesión activa en /editarDetalle. Redirigiendo a login.");
            return "redirect:/login/username"; //si no hay usuario, a iniciar sesion
        }

        try { //intenta meter en un registroEmpleadoDTO el empleado que tenga el id_usuario
            RegistroEmpleadoDTO empleadoDTO = empleadoService.obtenerRegistroEmpleadoParaEdicion(usuario.getId());

            modelo.addAttribute("registroEmpleado", empleadoDTO);
            modelo.addAttribute("listaPaises", paisService.getAllPaises());
            modelo.addAttribute("listaDepartamentos", departamentoService.getAllDepartamentos());
            modelo.addAttribute("listaGeneros", generoService.getAllGeneros());
            //mete al modelo datos para que se precarguen

            return "empleado/main/empleado-editar-detalle"; //devuelve el editar empleao

        } catch (IllegalArgumentException e) {
            logger.error("No se pudo obtener el empleado para edición: {}", e.getMessage());
            return "redirect:/login/username";
        }
    }

    /**
     * Añade al modelo los datos necesarios para cargar el formulario de empleado.
     *
     * Incluye un DTO vacío y las listas de países, géneros y departamentos.
     *
     * @param modelo modelo donde se añaden los atributos para la vista
     */
    private void prepararModeloFormulario(Model modelo) { //metodo para meter los selects y radios de una al modelo
        modelo.addAttribute("registroEmpleado", new RegistroEmpleadoDTO());
        modelo.addAttribute("paises", paisService.getAllPaises());
        modelo.addAttribute("generos", generoService.getAllGeneros());
        modelo.addAttribute("listaDepartamentos", departamentoService.getAllDepartamentos());
    }

    @Value("${app.upload.dir}")  //pilla lo que haya en el aplication properties
    private String uploadDir; // y lo mete en un string

    /**
     * Procesa el formulario de edición de los datos del empleado.
     *
     * Si no hay usuario en sesión, redirige al login.
     * Si hay errores de validación o al guardar la imagen, vuelve a la vista con mensajes de error.
     * Si todo va bien, actualiza los datos del empleado y redirige a la vista de detalles.
     *
     * @param session sesión HTTP actual
     * @param redirectAttributes atributos para mensajes flash tras redirección
     * @param registroEmpleado DTO con los datos del formulario
     * @param errores resultado de validación del formulario
     * @param modelo modelo para pasar datos a la vista
     * @param foto archivo de imagen subido por el usuario
     * @return redirección o vista del formulario con errores
     */
    @PostMapping("/editarDetalle")
    public String editarDetalle(HttpSession session,
                                RedirectAttributes redirectAttributes,
                                @Valid @ModelAttribute("registroEmpleado") RegistroEmpleadoDTO registroEmpleado,
                                BindingResult errores,
                                Model modelo,
                                @RequestParam("foto") MultipartFile foto) {

        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario"); //pillar el usuario de sesion
        Boolean autenticado = (Boolean) session.getAttribute("autenticado");

        if (usuario == null || autenticado == null || !autenticado) {
            return "redirect:/login/username"; //si no usuario, a iniciar sesion
        }

        if (errores.hasErrors()) {
            prepararModeloFormulario(modelo);
            modelo.addAttribute("error", "error");
            return "empleado/main/empleado-editar-detalle"; //si hay errores de validacion vuelta a la vista
        }

        try {
            empleadoService.actualizarDatosEmpleado(
                    usuario.getId(), registroEmpleado, foto, uploadDir
            );
            redirectAttributes.addFlashAttribute("mensaje", "Detalles actualizados correctamente");
            return "redirect:/detalle";

        } catch (IllegalArgumentException e) { //errores por validacion en el metodo del servicio
            logger.warn("Error de validación en servicio: {}", e.getMessage());
            prepararModeloFormulario(modelo);
            modelo.addAttribute("errorFoto", e.getMessage());
            return "empleado/main/empleado-editar-detalle";

        } catch (IOException e) {//si hay errores a la hora de guardar fisicamente el archvivo
            logger.error("Error al guardar la imagen del empleado", e);
            prepararModeloFormulario(modelo);
            modelo.addAttribute("error", "Error al guardar la imagen del empleado.");
            return "empleado/main/empleado-editar-detalle";
        }
    }
}
