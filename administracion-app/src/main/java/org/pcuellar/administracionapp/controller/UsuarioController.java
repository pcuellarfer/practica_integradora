package org.pcuellar.administracionapp.controller;


import org.pcuellar.administracionapp.dto.RegistroUsuarioDTO;
import org.pcuellar.administracionapp.services.Usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")

public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/registro")
    public String registro(Model modelo) {
        modelo.addAttribute("usuario", new RegistroUsuarioDTO());
        return "Usuario/RegistroUsuario";
    }

    @PostMapping
    public String registrarUsuario(@ModelAttribute("usuario") RegistroUsuarioDTO registroUsuarioDTO, Model model) {
        usuarioService.registrarUsuario(registroUsuarioDTO);
        return "prueba";

    }
}

