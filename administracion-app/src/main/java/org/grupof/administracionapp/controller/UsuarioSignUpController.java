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

/**
 * Controlador que gestiona el registro de nuevos usuarios de tipo USUARIO,
 * así como el acceso a su panel principal y el cierre de sesión.
 */
@Controller
@RequestMapping("/registro")
public class UsuarioSignUpController {

    /**
     * Codificador de contraseñas para almacenar las contraseñas de forma segura.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Servicio que gestiona la lógica de negocio relacionada con usuarios.
     */
    private final UsuarioService usuarioService;

    /**
     * Constructor que inyecta las dependencias necesarias para la gestión de usuarios.
     *
     * @param usuarioService  servicio para gestionar operaciones relacionadas con usuarios.
     * @param passwordEncoder codificador para cifrar contraseñas antes de guardarlas.
     */
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
        session.invalidate();
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
    @PostMapping("/signup")
    public String registrarUsuario(
            @ModelAttribute("registroUsuarioDTO") @Valid RegistroUsuarioDTO registroUsuarioDTO,
            BindingResult errores,
            HttpSession session,
            Model modelo) {

        if (usuarioService.existePorEmail(registroUsuarioDTO.getEmail())) {
            modelo.addAttribute("error", "Ya existe un usuario con ese email.");
            return "usuario/auth/signUp-usuario";
        }

        if (errores.hasErrors()) {
            System.err.println("tiene errores");
            return "usuario/auth/signUp-usuario";
        }

        registroUsuarioDTO.setContrasena(passwordEncoder.encode(registroUsuarioDTO.getContrasena()));

        UsuarioDTO usuarioDTO = usuarioService.registrarUsuario(registroUsuarioDTO);
        session.setAttribute("usuario", usuarioDTO);

        return "redirect:/dashboard/dashboard";
    }
}



