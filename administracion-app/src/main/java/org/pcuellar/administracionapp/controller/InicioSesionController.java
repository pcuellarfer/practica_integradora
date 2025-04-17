package org.pcuellar.administracionapp.controller;

import org.pcuellar.administracionapp.services.Usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/iniciosesion")
@SessionAttributes("Susuario")
public class InicioSesionController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/nombre")
    public String mostrarFormularioNombre(Model model) {
        return "Usuario/auth/InicioSesionNombre";
    }

    @PostMapping("/procesarNombre")
    public String procesarFormularioNombre(@RequestParam("nombre") String nombre, Model model) {
        if(usuarioService.existeNombre(nombre)) {
            model.addAttribute("Susuario", nombre);
            return "redirect:/iniciosesion/contrasena";
        }else{
         model.addAttribute("error", "No existe usuario con ese nombre");
         return "Usuario/auth/InicioSesionNombre";
        }
    }

    @GetMapping("/contrasena")
    public String mostrarFormularioContrasena(@ModelAttribute("Susuario") String susuario, Model model) {
        if(susuario == null || susuario.isBlank()) {
            return "redirect:/iniciosesion/nombre";
        }
        return "Usuario/auth/InicioSesionContrasena";
    }
}
