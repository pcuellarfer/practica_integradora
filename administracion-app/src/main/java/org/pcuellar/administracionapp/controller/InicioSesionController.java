package org.pcuellar.administracionapp.controller;

import org.pcuellar.administracionapp.services.Usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/iniciosesion")
@SessionAttributes("susuario")
public class InicioSesionController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/nombre")
    public String mostrarFormularioNombre(Model model) {
        return "Usuario/auth/login-nombre";
    }

    @PostMapping("/procesarNombre")
    public String procesarFormularioNombre(@RequestParam("nombre") String nombre, Model model) {
        if(usuarioService.existeNombre(nombre)) {
            model.addAttribute("susuario", nombre);
            return "redirect:/iniciosesion/contrasena";
        }else{
         model.addAttribute("error", "No existe usuario con ese nombre");
         return "Usuario/auth/login-nombre";
        }
    }

    @GetMapping("/contrasena")
    public String mostrarFormularioContrasena(@ModelAttribute("susuario") String susuario, Model model) {
        if(susuario == null || susuario.isBlank()) {
            return "redirect:/iniciosesion/nombre";
        }
        return "Usuario/auth/login-contrasena";
    }

    @PostMapping("/procesarContrasena")
    public String procesarFormularioContrasena(@RequestParam String contrasena,
                                               Model model,
                                               @ModelAttribute("susuario") String susuario,
                                               SessionStatus sessionStatus) {
        if(susuario == null || susuario.isBlank()) {
            return "redirect:/iniciosesion/nombre";
        }

        if(usuarioService.validarNombreContrasena(susuario, contrasena)) {
            sessionStatus.setComplete();
            return "redirect:/empleado/dashboard";
        }else{
            sessionStatus.setComplete();
            model.addAttribute("error", "Contraseña incorrecta.");
            return "Usuario/auth/login-contrasena";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model modelo) {
        return "Usuario/empleados/empleado-dashboard";
    }

}
