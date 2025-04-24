package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.services.Email.EmailService;
import org.grupof.administracionapp.services.Usuario.UsuarioService;
import org.grupof.administracionapp.services.Token.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

/**
 * Controlador responsable de gestionar el inicio y cierre de sesión de los usuarios.
 * Proporciona formularios para introducir el email y la contraseña, validando los datos
 * e interactuando con el servicio de usuario.
 */
@Controller
@RequestMapping("/login")
public class InicioSesionController {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;
    private final EmailService emailService;
    private final TokenService tokenService;

    /**
     * Constructor que inyecta las dependencias necesarias para la gestión de autenticación.
     *
     * @param usuarioService servicio encargado de las operaciones con usuarios.
     * @param passwordEncoder codificador de contraseñas utilizado para validaciones.
     */
    public InicioSesionController(UsuarioService usuarioService, PasswordEncoder passwordEncoder, EmailService emailService, TokenService tokenService) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.tokenService = tokenService;
    }

    /**
     * Añade un objeto {@link UsuarioDTO} al modelo, reutilizando el de la sesión si existe.
     * Este método se ejecuta antes de cada controlador con acceso al modelo "usuario".
     *
     * @param session sesión HTTP actual.
     * @return objeto UsuarioDTO existente o uno nuevo si no hay usuario en sesión.
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
     * Invalida la sesión actual del usuario, cerrando la sesión.
     *
     * @param session sesión HTTP a invalidar.
     * @return redirección al formulario de ingreso del email.
     */
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login/username";
    }

    /**
     * Muestra el formulario para introducir el email del usuario.
     *
     * @param usuario objeto de usuario recuperado del modelo (puede estar en sesión).
     * @return vista del formulario de login por email.
     */
    @GetMapping("/username")
    public String mostrarFormularioNombre(@ModelAttribute("usuario") UsuarioDTO usuario) {
        return "usuario/auth/login-nombre";
    }

    /**
     * Procesa el formulario de email del usuario.
     * Si el email existe, guarda el usuario en sesión y redirige a la vista de contraseña.
     *
     * @param usuarioDTO objeto que contiene el email ingresado.
     * @param result resultado de la validación del formulario.
     * @param session sesión HTTP actual.
     * @param model modelo para comunicar mensajes a la vista.
     * @return vista de contraseña o recarga del formulario de email en caso de error.
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

        boolean usuarioBLoqueado = usuarioService.buscarBloqueado(usuarioDTO.getEmail());

        if (usuarioBLoqueado) {
            model.addAttribute("error", "El usuario está bloqueado.");
            return "usuario/auth/login-nombre";
        }

        session.setAttribute("usuario", usuarioExistente);
        return "usuario/auth/login-contrasena";
    }

    /**
     * Muestra el formulario para ingresar la contraseña.
     * Redirige al formulario de email si no hay email definido en sesión.
     *
     * @param usuarioDTO usuario obtenido del modelo o sesión.
     * @return vista de contraseña o redirección si no hay email.
     */
    @GetMapping("/password")
    public String mostrarFormularioContrasena(@ModelAttribute("usuario") UsuarioDTO usuarioDTO) {
        if (usuarioDTO == null || usuarioDTO.getEmail().isBlank()) {
            return "redirect:/login/username";
        }

        return "usuario/auth/login-contrasena";
    }

    /**
     * Procesa el ingreso de la contraseña del usuario.
     * Si es válida, guarda el usuario en sesión y redirige al dashboard.
     * Si no, muestra mensaje de error y permite reintentar.
     *
     * @param usuarioDTO usuario obtenido del modelo o sesión.
     * @param contrasena contraseña ingresada.
     * @param session sesión HTTP actual.
     * @param model modelo para enviar mensajes a la vista.
     * @return redirección al dashboard o recarga de la vista de contraseña si hay error.
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

        // Buscar en BBDD
        UsuarioDTO usuarioBBDD = usuarioService.buscarPorEmail(usuarioDTO.getEmail());

        // Verificación de contraseña
        if (!passwordEncoder.matches(contrasena, usuarioBBDD.getContrasena())) {
            model.addAttribute("error", "Contraseña incorrecta.");

            // Manejar contador de intentos fallidos en sesión
            Integer intentos = (Integer) session.getAttribute("intentos");
            if (intentos == null) intentos = 0;
            intentos++;

            session.setAttribute("intentos", intentos);

            if (intentos >= 3) {
                // Bloquear el usuario en la base de datos
                usuarioService.bloquearUsuario(usuarioDTO.getEmail(), "Demasiados intentos fallidos");
                model.addAttribute("error", "El usuario bloqueado por demasiados intentos fallidos.");
                session.invalidate();
                return "redirect:/login/username";
            }

            return "usuario/auth/login-contrasena";
        }

        // Si contraseña es correcta
        session.setAttribute("usuario", usuarioBBDD);
        session.removeAttribute("intentos"); // Reiniciar contador
        return "redirect:/dashboard/dashboard";
    }

    /**
     * Endpoint de prueba para verificar el funcionamiento del envío de correo.
     *
     * @return una respuesta HTTP 200 con un mensaje indicando que el correo fue enviado.
     */
    @GetMapping("/enviar-correo")
    public ResponseEntity<String> enviarCorreo() {
        return ResponseEntity.ok("Correo enviado");
    }

    /**
     * Muestra el formulario de recuperación de contraseña.
     *
     * @return vista del formulario de recuperación.
     */
    @GetMapping("/recuperación")
    public String mostrarFormularioRecuperacion() {
        return "usuario/auth/recuperacion";
    }

    /**
     * Procesa el formulario de recuperación de contraseña: genera un token y lo envía al email proporcionado.
     *
     * @param email dirección de correo del usuario que solicita la recuperación.
     * @param model modelo para pasar datos a la vista.
     * @return vista del mismo formulario con un mensaje de confirmación.
     */
    @PostMapping("/recuperación")
    public String procesarFormularioRecuperacion(@RequestParam String email, Model model) {
        String asunto = "Verifica tu cuenta";

        String token = UUID.randomUUID().toString();
        tokenService.guardarToken(email, token);

        String enlace = "http://localhost:8080/verificacion?token=" + token;
        emailService.enviarCorreoConEnlace(email, "Recuperación de contraseña", enlace);

        model.addAttribute("mensaje", "Se ha enviado un enlace de recuperación a tu correo.");
        return "usuario/auth/recuperacion";
    }

    /**
     * Muestra el formulario para establecer una nueva contraseña tras verificar el token.
     *
     * @param token token de verificación proporcionado en el enlace.
     * @param model modelo para pasar el email y token a la vista.
     * @return vista del formulario para introducir la nueva contraseña, o error si el token no es válido.
     */
    @GetMapping("/verificacion")
    public String mostrarFormularioNuevaContrasena(@RequestParam String token, Model model) {
        Optional<String> email = tokenService.validarToken(token);
        if (email.isEmpty()) {
            return "error/token-invalido";
        }

        model.addAttribute("email", email.get());
        model.addAttribute("token", token);
        return "usuario/auth/nueva-contrasena";
    }

    /**
     * Procesa la nueva contraseña introducida por el usuario después de validar el token.
     *
     * @param token           token de verificación.
     * @param nuevaContrasena nueva contraseña a establecer.
     * @param model           modelo para la vista en caso de error.
     * @return redirección al login si todo ha ido bien, o error si el token no es válido.
     */
    @PostMapping("/restablecer-contrasena")
    public String restablecerContrasena(
            @RequestParam String token,
            @RequestParam String nuevaContrasena,
            Model model
    ) {
        Optional<String> email = tokenService.validarToken(token);
        if (email.isEmpty()) {
            return "error/token-invalido";
        }

        usuarioService.actualizarContrasena(email.get(), nuevaContrasena);
        tokenService.eliminarToken(token);
        return "redirect:/login?contrasenaActualizada";
    }


}


