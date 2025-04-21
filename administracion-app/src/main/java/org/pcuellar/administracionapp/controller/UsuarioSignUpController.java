package org.pcuellar.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.pcuellar.administracionapp.dto.Usuario.RegistroUsuarioDTO;
import org.pcuellar.administracionapp.dto.Usuario.UsuarioDTO;
import org.pcuellar.administracionapp.services.Usuario.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador que gestiona el registro de nuevos usuarios del tipo USUARIO.
 */
@Controller
@RequestMapping("/usuario")
public class UsuarioSignUpController {

    //inyeccion del servicio de usuario
    private final UsuarioService usuarioService;

    public UsuarioSignUpController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    //muestra la vista del formulario de registro de usuario
    @GetMapping("/signup")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("registroUsuarioDTO", new RegistroUsuarioDTO());
        return "usuario/auth/signUp-usuario";
    }


    @PostMapping("/signup")
    public String registrarUsuario(
            @ModelAttribute("registroUsuarioDTO") @Validated RegistroUsuarioDTO registroUsuarioDTO,
           //guarda los errores si alguna validacion del DTO falla
            BindingResult errores,
            HttpSession sesion,
            Model modelo) {

        if (errores.hasErrors()) {
            modelo.addAttribute("error", "Corrige los errores del formulario.");
            return "usuario/auth/signUp-usuario";
        }

        //usa el metodo del servicio para comprobar si existe alguien con ese email
        //si existe devuelve la vista de registro
        if (usuarioService.existePorEmail(registroUsuarioDTO.getEmail())) {
            modelo.addAttribute("error", "Ya existe un usuario con ese email.");
            return "usuario/auth/signUp-usuario";
        }

        //se crea un usuarioDTO nuevo por si se necesitan añadir camapos del dto
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setEmail(registroUsuarioDTO.getEmail());
        usuarioDTO.setContrasena(registroUsuarioDTO.getContrasena());

        usuarioService.registrarUsuario(usuarioDTO);

        //guarda el usuario creado en la sesion para luego
        sesion.setAttribute("usuario", usuarioDTO);

        return "redirect:/usuario/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");
        if (usuarioDTO == null) {
            return "redirect:/login/signup";
        }

        model.addAttribute("email", usuarioDTO.getEmail());
        return "usuario/main/usuario-dashboard";
    }
}

