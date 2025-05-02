package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.grupof.administracionapp.dto.Usuario.RegistroUsuarioDTO;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.services.Usuario.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador que gestiona el registro de nuevos usuarios de tipo USUARIO,
 * así como el acceso a su panel principal y el cierre de sesión.
 */
@Controller
@RequestMapping("/registro")
public class UsuarioSignUpController {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;
    private final Logger logger = LoggerFactory.getLogger(UsuarioSignUpController.class);

    public UsuarioSignUpController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cierra la sesión actual del usuario invalidando la sesión HTTP.
     *
     * @param session sesión HTTP actual.
     * @return una redirección al formulario de inicio de sesión.
     */
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        logger.info("Cerrando sesión para el usuario.");
        session.invalidate();
        logger.info("Sesión cerrada exitosamente.");
        return "redirect:/login/username";
    }

    /**
     * Muestra el formulario de registro para que un nuevo usuario se registre en el sistema.
     *
     * @param modelo modelo de datos para la vista.
     * @return el nombre de la vista correspondiente al formulario de registro.
     */
    @GetMapping("/usuario")
    public String mostrarFormularioRegistro(Model modelo) {
        logger.info("Mostrando formulario de registro de usuario.");
        modelo.addAttribute("registroUsuarioDTO", new RegistroUsuarioDTO());
        return "usuario/auth/signUp-usuario";
    }

    /**
     * Procesa el formulario de registro de usuario. Valida los datos recibidos y, si son correctos,
     * registra un nuevo usuario en el sistema y lo almacena en la sesión actual.
     *
     * @param registroUsuarioDTO objeto que contiene los datos introducidos en el formulario.
     * @param errores    objeto que contiene los errores de validación del formulario.
     * @param session    sesión HTTP actual para almacenar al usuario autenticado.
     * @param modelo     modelo de datos para la vista.
     * @return redirección al dashboard del usuario si el registro es exitoso,
     * o recarga del formulario en caso de errores.
     */
    @PostMapping("/usuario/signup")
    public String registrarUsuario(
            @ModelAttribute("registroUsuarioDTO") @Valid RegistroUsuarioDTO registroUsuarioDTO,
            BindingResult errores,
            HttpSession session,
            Model modelo) {

        logger.info("Iniciando proceso de registro para usuario con email: {}", registroUsuarioDTO.getEmail());

        if (usuarioService.existePorEmail(registroUsuarioDTO.getEmail())) {
            logger.warn("Intento de registro con email ya registrado: {}", registroUsuarioDTO.getEmail());
            modelo.addAttribute("error", "Ya existe un usuario con ese email.");
            return "usuario/auth/signUp-usuario";
        }

        if (errores.hasErrors()) {
            logger.warn("Errores de validación en el formulario de registro para el email: {}", registroUsuarioDTO.getEmail());
            return "usuario/auth/signUp-usuario";
        }

        logger.info("Contraseña cifrada y registro de usuario en proceso.");
        registroUsuarioDTO.setContrasena(passwordEncoder.encode(registroUsuarioDTO.getContrasena()));

        UsuarioDTO usuarioDTO = usuarioService.registrarUsuario(registroUsuarioDTO);
        logger.info("Usuario registrado exitosamente con ID: {}", usuarioDTO.getId());

        session.setAttribute("usuario", usuarioDTO);
        logger.info("Usuario almacenado en sesión con ID: {}", usuarioDTO.getId());

        return "redirect:/dashboard/dashboard";
    }
}
