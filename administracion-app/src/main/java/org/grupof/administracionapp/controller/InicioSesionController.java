package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.services.Usuario.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador responsable de gestionar el inicio y cierre de sesión de los usuarios,
 * Proporciona formularios para introducir el nombre de usuario (email) y la contraseña,
 * validando los datos e interactuando con el servicio de usuario.
 */
@Controller
@RequestMapping("/login")
public class InicioSesionController {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;

    /**
     * Constructor que inyecta el servicio encargado de la lógica relacionada con usuarios.
     *
     * @param usuarioService servicio de gestión de usuarios.
     */
    public InicioSesionController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Obtiene o crea un objeto UsuarioDTO para la sesión.
     * Se utiliza como modelo compartido en los formularios de login.
     *
     * @param session sesión HTTP actual.
     * @return objeto UsuarioDTO existente en sesión o uno nuevo si no existe.
     */
    @ModelAttribute("usuario")
    public UsuarioDTO getUsuario(HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");
        if (usuarioDTO == null) {
            usuarioDTO = new UsuarioDTO();
        }
        return usuarioDTO;
    }

    /**
     * Cierra la sesión actual, invalidando todos los datos de usuario.
     *
     * @param session sesión HTTP actual.
     * @return redirección al formulario de introducción del email.
     */
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login/username";
    }

    /**
     * Muestra el formulario para introducir el email del usuario.
     * Si ya hay un usuario en sesión, continúa con el proceso normal.
     *
     * @param usuario usuario almacenado en sesión.
     * @return vista del formulario de email.
     */
    @GetMapping("/username")
    public String mostrarFormularioNombre(@ModelAttribute("usuario") UsuarioDTO usuario) {
        return "usuario/auth/login-nombre";
    }

    /**
     * Procesa el formulario de ingreso del email.
     * Verifica si el email existe en el sistema y guarda el usuario en sesión.
     *
     * @param usuarioDTO objeto usuario con el email ingresado.
     * @param result resultado de la validación del objeto.
     * @param session sesión HTTP actual.
     * @param model modelo para enviar mensajes de error a la vista.
     * @return redirección al formulario de contraseña o mensaje de error si el email no existe.
     */
    @PostMapping("/username")
    public String procesarFormularioNombre(@ModelAttribute("usuario") UsuarioDTO usuarioDTO,
                                           BindingResult result,
                                           HttpSession session,
                                           Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Corrige los errores del formulario.");
            return "usuario/auth/login-nombre";
        }

        UsuarioDTO usuarioExistente = usuarioService.buscarPorEmail(usuarioDTO.getEmail());

        if (usuarioExistente == null) {
            model.addAttribute("error", "No existe ese email.");
            return "usuario/auth/login-nombre";
        }

        session.setAttribute("usuario", usuarioExistente);
        return "usuario/auth/login-contrasena";
    }

    /**
     * Muestra el formulario para introducir la contraseña del usuario.
     *
     * @param usuarioDTO usuario actual obtenido de la sesión.
     * @return vista del formulario de contraseña o redirección si no hay email definido.
     */
    @GetMapping("/password")
    public String mostrarFormularioContrasena(@ModelAttribute("usuario") UsuarioDTO usuarioDTO) {
        if (usuarioDTO == null || usuarioDTO.getEmail().isBlank()) {
            return "redirect:/login/username";
        }

        return "usuario/auth/login-contrasena";
    }

    /**
     * Procesa el formulario de ingreso de contraseña.
     * Si la contraseña es correcta, autentica al usuario y lo redirige a su dashboard.
     *
     * @param usuarioDTO usuario actual obtenido de la sesión.
     * @param contrasena contraseña ingresada por el usuario.
     * @param model modelo para enviar mensajes de error a la vista.
     * @return redirección al panel correspondiente al tipo de usuario o mensaje de error.
     */
    @PostMapping("/password")
    public String procesarFormularioContrasena(@ModelAttribute("usuario") UsuarioDTO usuarioDTO,
                                               @RequestParam String contrasena,
                                               HttpSession session,
                                               Model model) {
        if (usuarioDTO == null || usuarioDTO.getEmail().isBlank()) {
            return "redirect:/login/username";
        }

        if (contrasena == null || contrasena.isBlank()) {
            model.addAttribute("error", "La contraseña no puede estar vacía.");
            return "usuario/auth/login-contrasena";
        }
//        if (!usuarioService.validarContrasena(usuarioDTO.getEmail(), contrasena)) {
//            model.addAttribute("error", "Contraseña incorrecta.");
//            Integer intentos = (Integer) session.getAttribute("intentos");
//            if (intentos == null) {
//                intentos = 0;
//            }
//            intentos++;
//            session.setAttribute("intentos", intentos);
//
//            if (intentos >= 3) {
//                session.invalidate();
//                return "redirect:/login/bloqueado";
//            }
//            return "usuario/auth/login-contrasena";
//        }

        UsuarioDTO usuarioBBDD = usuarioService.buscarPorEmail(usuarioDTO.getEmail());

        if (!passwordEncoder.matches(contrasena, usuarioBBDD.getContrasena())){
            model.addAttribute("error", "Contraseña incorrecta.");
            return "usuario/auth/login-contrasena";
        }

        session.setAttribute("usuario", usuarioBBDD);
        return "redirect:/login/dashboard";
    }

    /**
     * Muestra un dashboard genérico para usuarios cuyo tipo no está claramente definido,
     * o cuando se accede directamente a la ruta de dashboard.
     *
     * @param session sesión HTTP actual.
     * @param model modelo para pasar el nombre del usuario a la vista.
     * @return vista del panel general de usuario.
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        model.addAttribute("email", usuario.getEmail());
        return "usuario/main/usuario-dashboard";
    }
}

