package org.pcuellar.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.pcuellar.administracionapp.Model.Empleado;
import org.pcuellar.administracionapp.services.Empleado.EmpleadoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/empleado")
@SessionAttributes("empleado")
public class EmpleadoSignUpController {

    private final EmpleadoService empleadoService;

    public EmpleadoSignUpController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @ModelAttribute("empleado")
    public Empleado getEmpleado(HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleado");
        if (empleado == null) {
            empleado = new Empleado();
        }
        return empleado;
    }

    @GetMapping("/reinicio")
    public String reinicio(HttpSession session) {
        session.invalidate();
        return "redirect:/empleado/login";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "Empleado/RegistroEmpleado1";
    }

    @PostMapping("/login")
    public String guardarDatosEmpleado(@ModelAttribute("empleado") Empleado empleado,
                                       @Validated BindingResult result,
                                       HttpSession session, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("error", "Por favor, complete todos los campos requeridos.");
            return "Empleado/RegistroEmpleado1";
        }
        session.setAttribute("empleado", empleado);
        return "redirect:/empleado/login-password";
    }

    @GetMapping("/login-password")
    public String mostrarLoginContrasena(@ModelAttribute("empleado") Empleado empleado, Model model) {
        if (empleado == null || empleado.getNombre().isBlank()) {
            return "redirect:/empleado/login";
        }
        return "Empleado/RegistroEmpleado2";
    }

    @PostMapping("/login-password")
    public String procesarLoginContrasena(@ModelAttribute("empleado") Empleado empleado,
                                          @RequestParam String contrasena,
                                          Model model,
                                          SessionStatus sessionStatus) {
        if (empleado == null || empleado.getNombre().isBlank()) {
            return "redirect:/empleado/login";
        }

        if (empleado.getContrasena().equals(contrasena)) {
            // Guardamos el empleado en la base de datos
            empleadoService.registrarEmpleado(empleado);

            // Limpiamos la sesión
            sessionStatus.setComplete();
            return "redirect:/empleado/dashboard";
        } else {
            model.addAttribute("error", "Contraseña incorrecta.");
            return "Empleado/RegistroEmpleado2";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "Usuario/empleados/empleado-dashboard";
    }
}
