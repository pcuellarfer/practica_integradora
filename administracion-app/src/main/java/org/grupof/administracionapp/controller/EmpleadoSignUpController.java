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

    public EmpleadoSignUpController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login/username";
    }

    @GetMapping("/registro")
    public String mostrarFormularioPersonal(Model modelo) {
        modelo.addAttribute("registroEmpleadoDTO", new RegistroEmpleadoDTO());
        return "empleado/auth/FormDatosPersonales";
    }

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
            return "empleado/auth/FormDatosPersonales";
        }

        if (registroEmpleadoDTO.getApellido() == null || registroEmpleadoDTO.getApellido().isEmpty()) {
            modelo.addAttribute("error", "El apellido es obligatorio.");
            return "empleado/auth/FormDatosPersonales";
        }

        if (registroEmpleadoDTO.getFechaNacimiento() == null || registroEmpleadoDTO.getFechaNacimiento().isEmpty()) {
            modelo.addAttribute("error", "La fecha de nacimiento es obligatoria.");
            return "empleado/auth/FormDatosPersonales";
        }

        if (registroEmpleadoDTO.getGenero() == null || registroEmpleadoDTO.getGenero().isEmpty()) {
            modelo.addAttribute("error", "El género es obligatorio.");
            return "empleado/auth/FormDatosPersonales";
        }

        UsuarioDTO usuarioDTO = (UsuarioDTO) sesion.getAttribute("usuario");
        if (usuarioDTO == null) {
            return "redirect:/usuario/signup";
        }

        // Verificar si ya hay un empleado registrado para ese usuario
        RegistroEmpleadoDTO empleadoExistente = empleadoService.buscarEmpleadoPorUsuarioId(usuarioDTO.getId());
        if (empleadoExistente != null) {
            modelo.addAttribute("error", "Ya existe un empleado registrado para este usuario.");
            return "empleado/auth/FormDatosPersonales";
        }

        registroEmpleadoDTO.setNombre(registroEmpleadoDTO.getNombre());
        registroEmpleadoDTO.setApellido(registroEmpleadoDTO.getApellido());
        registroEmpleadoDTO.setFechaNacimiento(registroEmpleadoDTO.getFechaNacimiento());
        registroEmpleadoDTO.setGenero(registroEmpleadoDTO.getGenero());

        sesion.setAttribute("usuario", usuarioDTO);
        sesion.setAttribute("registroEmpleadoDTO", registroEmpleadoDTO);

        return "redirect:/empleado/registro/empresarial";
    }


    @GetMapping("/registro/empresarial")
    public String mostrarFormularioEmpresarial(
            Model modelo,
            HttpSession sesion) {

        RegistroEmpleadoDTO registroEmpleadoDTO = (RegistroEmpleadoDTO) sesion.getAttribute("registroEmpleadoDTO");
        UsuarioDTO usuarioDTO = (UsuarioDTO) sesion.getAttribute("usuario");
        if (registroEmpleadoDTO == null) {
            return "redirect:/empleado/registro";
        }

        if (usuarioDTO == null) {
            return "redirect:/usuario/signup";
        }

        modelo.addAttribute("registroEmpleadoDTO", registroEmpleadoDTO);
        return "empleado/auth/FormDatosEmpresariales";
    }

    @PostMapping("/registro/empresarial")
    public String guardarDatosEmpresariales(
            @ModelAttribute("registroEmpleadoDTO") @Validated RegistroEmpleadoDTO registroEmpleadoDTO,
            BindingResult errores,
            HttpSession sesion,
            Model modelo) {

        if (errores.hasErrors()) {
            modelo.addAttribute("error", "Corrige los errores del formulario");
            return "empleado/auth/FormDatosEmpresariales";
        }

        if (registroEmpleadoDTO.getDepartamento() == null || registroEmpleadoDTO.getDepartamento().isEmpty()) {
            modelo.addAttribute("error", "El departamento es obligatorio.");
            return "empleado/auth/FormDatosEmpresariales";
        }

        if (registroEmpleadoDTO.getPuesto() == null || registroEmpleadoDTO.getPuesto().isEmpty()) {
            modelo.addAttribute("error", "El puesto es obligatorio.");
            return "empleado/auth/FormDatosEmpresariales";
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

        return "redirect:/empleado/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(
            HttpSession sesion,
            Model modelo) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) sesion.getAttribute("usuario");

        if (usuarioDTO == null) {
            return "redirect:/usuario/signup";
        }

        RegistroEmpleadoDTO empleadoDTO = empleadoService.buscarEmpleadoPorUsuarioId(usuarioDTO.getId());

        modelo.addAttribute("usuario", usuarioDTO);
        modelo.addAttribute("empleado", empleadoDTO);

        return "empleado/main/empleado-dashboard";
    }

}