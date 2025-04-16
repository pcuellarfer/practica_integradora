package org.pcuellar.administracionapp.controller;


import org.pcuellar.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.pcuellar.administracionapp.dto.Usuario.RegistroUsuarioDTO;
import org.pcuellar.administracionapp.services.Empleado.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/empleado")
@SessionAttributes("empleado")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping("/registro")
    public String registro(Model modelo) {
        modelo.addAttribute("empleado", new RegistroEmpleadoDTO());
        return "Empleado/RegistroEmpleado1";
    }

    @PostMapping("/registro")
    public String guardarRegistro(@ModelAttribute("empleado") RegistroEmpleadoDTO registroEmpleadoDTO) {
        return "redirect:/empleado/contrasena";
    }

    @GetMapping("/contrasena")
    public String contrasena(Model modelo) {
        return "Empleado/RegistroEmpleado2";
    }

    @PostMapping("/contrasena")
    public String guardarcontrasena(@ModelAttribute("empleado") RegistroEmpleadoDTO registroEmpleadoDTO, Model model) {
        empleadoService.registrarEmpleado(registroEmpleadoDTO);
        return "redirect:/empleado/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model modelo) {
        return "Usuario/empleados/empleado-dashboard";
    }

}
