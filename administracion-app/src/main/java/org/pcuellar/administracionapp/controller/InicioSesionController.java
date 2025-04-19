package org.pcuellar.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.pcuellar.administracionapp.auxiliar.TipoUsuario;
import org.pcuellar.administracionapp.dto.Empleado.EmpleadoDTO;
import org.pcuellar.administracionapp.dto.Usuario.UsuarioDTO;
import org.pcuellar.administracionapp.services.Usuario.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
@SessionAttributes("susuario")
public class InicioSesionController {

    private final UsuarioService usuarioService;

    public InicioSesionController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @ModelAttribute("susuario")
    public UsuarioDTO getUsuario(HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("susuario");
        if (usuarioDTO == null) {
            usuarioDTO = new EmpleadoDTO();
        }
        return usuarioDTO;
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate(); // Esto cierra la sesión
        return "redirect:/login/username"; // Redirige a la página de login
    }

    @GetMapping("/username")
    public String mostrarFormularioNombre(@ModelAttribute("susuario") UsuarioDTO susuario) {
        return "usuario/auth/login-nombre";
    }

    @PostMapping("/username")
    public String procesarFormularioNombre(@ModelAttribute("susuario") UsuarioDTO usuarioDTO,
                                           @RequestParam String nombre,
                                           @Validated BindingResult result,
                                           HttpSession session, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("error", "Por favor, complete todos los campos requeridos.");
            return "usuario/auth/login-nombre";
        }

        if (nombre == null || nombre.isBlank()) {
            model.addAttribute("error", "El nombre no puede estar vacío.");
            return "usuario/auth/login-nombre";
        }

        usuarioDTO.setNombre(nombre);
        session.setAttribute("susuario", usuarioDTO);

        return "usuario/auth/login-contrasena";
    }

    @GetMapping("/password")
    public String mostrarFormularioContrasena(@ModelAttribute("susuario") UsuarioDTO usuarioDTO) {
        if (usuarioDTO == null || usuarioDTO.getNombre().isBlank()) {
            return "redirect:/login/username";
        }
        return "usuario/auth/login-contrasena";
    }

    @PostMapping("/password")
    public String procesarFormularioContrasena(@ModelAttribute("susuario") UsuarioDTO usuarioDTO,
                                               @RequestParam String contrasena,
                                               Model model) {
        if (usuarioDTO == null || usuarioDTO.getNombre().isBlank()) {
            return "redirect:/login/username";
        }

        if (contrasena == null || contrasena.isBlank()) {
            model.addAttribute("error", "La contraseña no puede estar vacía.");
            return "usuario/auth/login-contrasena";
        }

        usuarioDTO.setContrasena(contrasena);
        usuarioService.iniciarSesion(usuarioDTO);

        if (usuarioDTO.getTipoUsuario() == TipoUsuario.EMPLEADO) {
            return "empleado/main/empleado-dashboard";
        }
        //TODO: Esto debe ser manejado por la clase UsuarioSignUPController
        return "redirect:/login/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        UsuarioDTO susuario = (UsuarioDTO) session.getAttribute("susuario");
        model.addAttribute("nombre", susuario.getNombre());
        return "usuario/main/usuario-dashboard";
    }
}