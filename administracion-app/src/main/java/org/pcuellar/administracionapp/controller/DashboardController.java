package org.pcuellar.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.pcuellar.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.pcuellar.administracionapp.dto.Usuario.UsuarioDTO;
import org.pcuellar.administracionapp.services.Empleado.EmpleadoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final EmpleadoService empleadoService;

    public DashboardController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping("/detalle")
    public String verMisDetalles(HttpSession session, Model model) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        RegistroEmpleadoDTO empleado = empleadoService.buscarEmpleado(usuario.getId());

        model.addAttribute("usuario", usuario);
        model.addAttribute("empleado", empleado);
        return "empleado/main/empleadoDetalle";
    }
}
