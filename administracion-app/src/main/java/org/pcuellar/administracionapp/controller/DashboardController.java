package org.pcuellar.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.pcuellar.administracionapp.dto.Usuario.UsuarioDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping("/detalle")
    public String verMisDetalles(HttpSession session, Model model) {
        UsuarioDTO susuario = (UsuarioDTO) session.getAttribute("susuario");

        if (susuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", susuario);
        return "empleadoDetalle";
    }
}
