package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/registro")
    public String mostrarFormularioPersonal(Model modelo) {
        modelo.addAttribute("registroEmpleadoDTO", new RegistroEmpleadoDTO());
        return "empleado/auth/FormDatosPersonales";
    }

    @PostMapping("/registro")
    public String guardarDatosPersonales(
            @ModelAttribute("registroEmpleadoDTO") RegistroEmpleadoDTO registroEmpleadoDTO,
            HttpSession sesion)
    {
        sesion.setAttribute("empleado_personal", registroEmpleadoDTO);
        return "redirect:/empleado/registro/empresarial";
    }

    @GetMapping("/registro/empresarial")
    public String mostrarFormularioEmpresarial(
            Model modelo,
            HttpSession sesion) {

        RegistroEmpleadoDTO registroEmpleadoDTO = (RegistroEmpleadoDTO) sesion.getAttribute("empleado_personal");

        if (registroEmpleadoDTO == null) {
            return "redirect:/empleado/registro";
        }

        modelo.addAttribute("registroEmpleadoDTO", registroEmpleadoDTO);
        return "empleado/auth/FormDatosEmpresariales";
    }

    @PostMapping("/registro/empresarial")
    public String guardarDatosEmpresariales(
            @ModelAttribute("registroempleadoDTO") @Validated RegistroEmpleadoDTO registroEmpleadoDTO,
            BindingResult errores,
            HttpSession sesion,
            Model modelo){

        if(errores.hasErrors()) {
            modelo.addAttribute("error", "Corrige los errores del formulario");
            return "empleado/auth/FormDatosEmpresariales";
        }

        //recuperar el usuario de la sesion o mandarlo a hacer usuario
        UsuarioDTO usuarioDTO = (UsuarioDTO) sesion.getAttribute("usuario");
        if(usuarioDTO == null){
            return "redirect:/usuario/signup";
        }

        //recuperar la parte con los datos personales del usuario
        RegistroEmpleadoDTO datosPersonales = (RegistroEmpleadoDTO) sesion.getAttribute("empleado_personal");

        if (datosPersonales != null) {
            registroEmpleadoDTO.setNombre(datosPersonales.getNombre());
            registroEmpleadoDTO.setApellido(datosPersonales.getApellido());
            registroEmpleadoDTO.setFechaNacimiento(datosPersonales.getFechaNacimiento());
            registroEmpleadoDTO.setGenero(datosPersonales.getGenero());
        }

        empleadoService.registrarEmpleado(registroEmpleadoDTO, usuarioDTO);

        sesion.removeAttribute("empleado_personal");

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