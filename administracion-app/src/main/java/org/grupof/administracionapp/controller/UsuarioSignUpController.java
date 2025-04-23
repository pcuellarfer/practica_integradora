package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.services.Usuario.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador que gestiona el registro de nuevos usuarios de tipo USUARIO,
 * así como el acceso a su panel principal y el cierre de sesión.
 */
@Controller
@RequestMapping("/usuario")
public class UsuarioSignUpController {

    /**
     * Servicio que gestiona la lógica de negocio relacionada con usuarios.
     */
    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;

    /**
     * Constructor con inyección de dependencias del servicio de usuario.
     *
     * @param usuarioService servicio para gestión de usuarios.
     */
    public UsuarioSignUpController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cierra la sesión actual del usuario invalidando la sesión HTTP.
     *
     * @param session sesión HTTP actual.
     * @return redirección al formulario de inicio de sesión.
     */
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login/username";
    }

    /**
     * Muestra el formulario de registro para un nuevo usuario.
     *
     * @param modelo modelo de datos para la vista.
     * @return nombre de la vista del formulario de registro.
     */
    @GetMapping("/signup")
    public String mostrarFormularioRegistro(Model modelo) {
        modelo.addAttribute("usuarioDTO", new UsuarioDTO());
        return "usuario/auth/signUp-usuario";
    }

    /**
     * Procesa el formulario de registro de un nuevo usuario.
     * Valida los datos introducidos y registra al usuario si todo es correcto.
     *
     * @param errores objeto que contiene los errores de validación.
     * @param session sesión HTTP actual para almacenar el usuario autenticado.
     * @param modelo modelo de datos para la vista.
     * @return redirección al dashboard del usuario registrado o recarga del formulario con errores.
     */
    @PostMapping("/signup")
    public String registrarUsuario(
            @ModelAttribute("usuarioDTO") @Valid UsuarioDTO usuarioDTO,
            BindingResult errores,
            HttpSession session,
            Model modelo) {

        if (errores.hasErrors()) {
            modelo.addAttribute("error", "Corrige los errores del formulario.");
            return "usuario/auth/signUp-usuario";
        }

        if (usuarioService.existePorEmail(usuarioDTO.getEmail())) {
            modelo.addAttribute("error", "Ya existe un usuario con ese email.");
            return "usuario/auth/signUp-usuario";
        }

        usuarioDTO.setEmail(usuarioDTO.getEmail());

        usuarioDTO.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));

        UsuarioDTO registrado = usuarioService.registrarUsuario(usuarioDTO);
        session.setAttribute("usuario", registrado);

        return "redirect:/usuario/dashboard";
    }

    /**
     * Muestra el panel principal del usuario después del registro o login.
     * Si no hay usuario en sesión, redirige al inicio de sesión.
     *
     * @param session sesión HTTP actual.
     * @param model modelo para enviar datos a la vista.
     * @return vista del dashboard del usuario o redirección si no hay sesión activa.
     */
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


