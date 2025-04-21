package org.pcuellar.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.pcuellar.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.pcuellar.administracionapp.services.Empleado.EmpleadoService;
import org.pcuellar.administracionapp.auxiliar.TipoUsuario;
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
    @GetMapping("/registro")
    public String mostrarFormularioPersonal(Model model) {
        model.addAttribute("registroEmpleadoDTO", new RegistroEmpleadoDTO());
        return "empleado/auth/FormDatosPersonales";
    }
}