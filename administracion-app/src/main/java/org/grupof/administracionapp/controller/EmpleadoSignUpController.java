package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.grupof.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.services.Empleado.EmpleadoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador encargado del proceso de registro de un empleado.
 * Maneja las vistas y la lógica de negocio para el login y el registro de un empleado.
 */
@Controller
@RequestMapping("/empleado")
public class EmpleadoSignUpController {

    private final EmpleadoService empleadoService;

    /**
     * Constructor para inyectar el servicio de empleado.
     *
     * @param empleadoService Servicio para gestionar lógica relacionada con empleados.
     */
    public EmpleadoSignUpController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    /**
     * Cierra la sesión del usuario y redirige a la pantalla de login.
     *
     * @param session La sesión HTTP activa.
     * @return Redirección a la pantalla de login.
     */
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login/username";
    }

    /**
     * Muestra el formulario para introducir los datos personales del empleado.
     *
     * @param modelo Modelo para pasar el DTO al formulario.
     * @return Vista del formulario de datos personales.
     */
    @GetMapping("/registro")
    public String mostrarFormularioPersonal(Model modelo) {
        modelo.addAttribute("registroEmpleadoDTO", new RegistroEmpleadoDTO());
        return "FormDatosPersonalesOLD";
    }

    /**
     * Guarda los datos personales del empleado si son válidos.
     *
     * @param registroEmpleadoDTO DTO con los datos introducidos.
     * @param errores Objeto que contiene errores de validación.
     * @param sesion Sesión actual del usuario.
     * @param modelo Modelo para pasar mensajes o datos a la vista.
     * @return Redirección a la siguiente etapa del registro o regreso al formulario con errores.
     */
    @PostMapping("/registro")
    public String guardarDatosPersonales(
            @ModelAttribute("registroEmpleadoDTO") @Valid RegistroEmpleadoDTO registroEmpleadoDTO,
            BindingResult errores,
            HttpSession sesion,
            Model modelo) {

        if (errores.hasErrors()) {
            modelo.addAttribute("error", "Corrige los errores del formulario.");
            return "usuario/auth/signUp-usuario";
        }

        if (registroEmpleadoDTO.getNombre() == null || registroEmpleadoDTO.getNombre().isEmpty()) {
            modelo.addAttribute("error", "El nombre es obligatorio.");
            return "FormDatosPersonalesOLD";
        }

        if (registroEmpleadoDTO.getApellido() == null || registroEmpleadoDTO.getApellido().isEmpty()) {
            modelo.addAttribute("error", "El apellido es obligatorio.");
            return "FormDatosPersonalesOLD";
        }

        if (registroEmpleadoDTO.getFechaNacimiento() == null || registroEmpleadoDTO.getFechaNacimiento().isEmpty()) {
            modelo.addAttribute("error", "La fecha de nacimiento es obligatoria.");
            return "FormDatosPersonalesOLD";
        }

        if (registroEmpleadoDTO.getGenero() == null || registroEmpleadoDTO.getGenero().isEmpty()) {
            modelo.addAttribute("error", "El género es obligatorio.");
            return "FormDatosPersonalesOLD";
        }

        UsuarioDTO usuarioDTO = (UsuarioDTO) sesion.getAttribute("usuario");
        if (usuarioDTO == null) {
            return "redirect:/usuario/signup";
        }

        RegistroEmpleadoDTO empleadoExistente = empleadoService.buscarEmpleadoPorUsuarioId(usuarioDTO.getId());
        if (empleadoExistente != null) {
            modelo.addAttribute("error", "Ya existe un empleado registrado para este usuario.");
            return "FormDatosPersonalesOLD";
        }

        sesion.setAttribute("usuario", usuarioDTO);
        sesion.setAttribute("registroEmpleadoDTO", registroEmpleadoDTO);

        return "redirect:/empleado/registro/empresarial";
    }

    /**
     * Muestra el formulario de datos empresariales del empleado.
     *
     * @param modelo Modelo para pasar los datos.
     * @param sesion Sesión actual para recuperar el DTO de registro.
     * @return Vista del formulario de datos empresariales o redirección si faltan datos.
     */
    @GetMapping("/registro/empresarial")
    public String mostrarFormularioEmpresarial(Model modelo, HttpSession sesion) {
        RegistroEmpleadoDTO registroEmpleadoDTO = (RegistroEmpleadoDTO) sesion.getAttribute("registroEmpleadoDTO");
        UsuarioDTO usuarioDTO = (UsuarioDTO) sesion.getAttribute("usuario");

        if (registroEmpleadoDTO == null) {
            return "redirect:/empleado/registro";
        }

        if (usuarioDTO == null) {
            return "redirect:/usuario/signup";
        }

        modelo.addAttribute("registroEmpleadoDTO", registroEmpleadoDTO);
        return "FormDatosEmpresarialesOLD";
    }

    /**
     * Guarda los datos empresariales del empleado y completa el proceso de registro.
     *
     * @param registroEmpleadoDTO DTO con los datos empresariales.
     * @param errores Objeto de validación.
     * @param sesion Sesión actual para obtener y actualizar datos.
     * @param modelo Modelo para pasar mensajes de error.
     * @return Redirección al dashboard del empleado o regreso al formulario si hay errores.
     */
    @PostMapping("/registro/empresarial")
    public String guardarDatosEmpresariales(
            @ModelAttribute("registroEmpleadoDTO") @Validated RegistroEmpleadoDTO registroEmpleadoDTO,
            BindingResult errores,
            HttpSession sesion,
            Model modelo) {

        if (errores.hasErrors()) {
            modelo.addAttribute("error", "Corrige los errores del formulario");
            return "FormDatosEmpresarialesOLD";
        }

        if (registroEmpleadoDTO.getDepartamento() == null || registroEmpleadoDTO.getDepartamento().isEmpty()) {
            modelo.addAttribute("error", "El departamento es obligatorio.");
            return "FormDatosEmpresarialesOLD";
        }

        if (registroEmpleadoDTO.getPuesto() == null || registroEmpleadoDTO.getPuesto().isEmpty()) {
            modelo.addAttribute("error", "El puesto es obligatorio.");
            return "FormDatosEmpresarialesOLD";
        }

        RegistroEmpleadoDTO dtoSesion = (RegistroEmpleadoDTO) sesion.getAttribute("registroEmpleadoDTO");
        UsuarioDTO usuarioDTO = (UsuarioDTO) sesion.getAttribute("usuario");

        if (dtoSesion == null || usuarioDTO == null) {
            return "redirect:/empleado/registro";
        }

        dtoSesion.setDepartamento(registroEmpleadoDTO.getDepartamento());
        dtoSesion.setPuesto(registroEmpleadoDTO.getPuesto());

        empleadoService.registrarEmpleado(dtoSesion, usuarioDTO);
        sesion.removeAttribute("registroEmpleadoDTO");

        return "redirect:/dashboard/dashboard";
    }
}
