package org.grupof.administracionapp.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Usuario;
import org.grupof.administracionapp.services.Email.EmailService;
import org.grupof.administracionapp.services.Usuario.UsuarioService;
import org.grupof.administracionapp.services.Token.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private static final Logger logger = LoggerFactory.getLogger(InicioSesionController.class);


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
        logger.info("Cerrando sesión para el usuario: {}", session.getAttribute("usuario"));
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
    public String mostrarFormularioNombre(@ModelAttribute("usuario") UsuarioDTO usuario,
                                          HttpSession session) {
        if (session.getAttribute("usuario") != null && session.getAttribute("contraseña") != null) {
            logger.info("Sesión activa detectada. Redirigiendo al dashboard.");
            return "redirect:/dashboard"; // O la URL de la página que quieras redirigir
        }
        logger.info("Mostrando formulario para ingresar email.");
        return "usuario/auth/login-nombre";
    }

    /**
     * Procesa el formulario de login por email (nombre de usuario).
     *
     * <p>Verifica si el formulario contiene errores, si el usuario existe,
     * y si está bloqueado. Si el usuario está bloqueado y la fecha de desbloqueo
     * ya pasó, lo desbloquea. Si todo es correcto, guarda el usuario en sesión y
     * redirige al formulario de contraseña.
     *
     * @param usuarioDTO Objeto que contiene el email introducido por el usuario.
     * @param result Resultado de la validación del formulario.
     * @param session Sesión HTTP para guardar al usuario autenticado.
     * @param model Modelo para enviar mensajes de error a la vista.
     * @return Nombre de la vista a mostrar (formulario actual o el de contraseña).
     */
    @PostMapping("/username")
    public String procesarFormularioNombre(@ModelAttribute("usuario") UsuarioDTO usuarioDTO,
                                           BindingResult result,
                                           HttpSession session,
                                           Model model) {
        if (result.hasErrors()) {
            logger.warn("Errores en el formulario de email.");
            model.addAttribute("error", "Corrige los errores del formulario.");
            return "usuario/auth/login-nombre";
        }

        UsuarioDTO usuarioExistente = usuarioService.buscarPorEmail(usuarioDTO.getEmail());
        if (usuarioExistente == null) {
            logger.warn("Email no encontrado: {}", usuarioDTO.getEmail());
            model.addAttribute("error", "No existe ese email.");
            return "usuario/auth/login-nombre";
        }

        Usuario usuario = usuarioService.buscarPorEmailFecha(usuarioDTO.getEmail());

        if(usuario.getBloqueadoHasta() != null) {
            LocalDateTime fechaActual = LocalDateTime.now(ZoneId.of("Europe/Madrid"));
            if (usuario.getBloqueadoHasta().isAfter(fechaActual)) {
                logger.warn("Usuario bloqueado hasta: {}", usuario.getBloqueadoHasta());
                model.addAttribute("error", "El usuario está bloqueado hasta: " + usuario.getBloqueadoHasta());
                return "usuario/auth/login-nombre";
            }

            boolean usuarioBLoqueado = usuarioService.buscarBloqueado(usuarioDTO.getEmail());
            if (usuarioBLoqueado) {
                logger.warn("Usuario bloqueado: {}", usuarioDTO.getEmail());
                model.addAttribute("error", "El usuario está bloqueado.");
                return "usuario/auth/login-nombre";
            }

            logger.info("Usuario desbloqueado: {}", usuarioDTO.getEmail());
            usuarioService.desbloquearUsuario(usuarioDTO.getEmail());
            usuario.setBloqueadoHasta(null);
        }

        logger.info("Usuario encontrado y no bloqueado: {}", usuarioExistente.getEmail());
        session.setAttribute("usuario", usuarioExistente);
        return "usuario/auth/login-contrasena";
    }

    /**
     * Muestra el formulario para restablecer la contraseña del usuario.
     * Si el objeto {@link UsuarioDTO} es nulo o el correo electrónico está vacío,
     * redirige al usuario a la página de inicio de sesión.
     *
     * @param usuarioDTO El objeto que contiene la información del usuario.
     * @return El nombre de la vista a mostrar, o una redirección si el correo es inválido.
     */
    @GetMapping("/password")
    public String mostrarFormularioContrasena(@ModelAttribute("usuario") UsuarioDTO usuarioDTO) {
        // Verificamos si usuarioDTO es null o si el campo email es null o está vacío
        if (usuarioDTO == null || usuarioDTO.getEmail() == null || usuarioDTO.getEmail().isBlank()) {
            return "redirect:/login/username";
        }
        return "usuario/auth/login-contrasena";
    }

    /**
     * Procesa el formulario de inicio de sesión con contraseña.
     *
     * <p>Valida la contraseña ingresada por el usuario. Si es incorrecta, incrementa el contador
     * de intentos fallidos en sesión y, tras 3 intentos, bloquea al usuario temporalmente.
     * Si la contraseña es correcta, reinicia los intentos, guarda el usuario en sesión y redirige al dashboard.</p>
     *
     * @param usuarioDTO Datos del usuario enviados desde el formulario, principalmente el email.
     * @param contrasena Contraseña ingresada por el usuario.
     * @param session Sesión HTTP para guardar datos temporales del usuario.
     * @param model Modelo para pasar datos a la vista en caso de error.
     * @param redirectAttributes Atributos para redirección, si fueran necesarios (no se usan en este fragmento).
     * @return Redirección a la vista correspondiente: login con contraseña o dashboard.
     */
    @PostMapping("/password")
    public String procesarFormularioContrasena(@ModelAttribute("usuario") UsuarioDTO usuarioDTO,
                                               @RequestParam String contrasena,
                                               HttpSession session,
                                               Model model,
                                               RedirectAttributes redirectAttributes,
                                               HttpServletRequest request,
                                               HttpServletResponse response) {
        if (usuarioDTO == null || usuarioDTO.getEmail().isBlank()) {
            logger.warn("Intento de acceso sin email en sesión.");
            return "redirect:/login/username";
        }

        if (contrasena == null || contrasena.isBlank()) {
            logger.warn("Contraseña vacía recibida.");
            model.addAttribute("error", "La contraseña no puede estar vacía.");
            return "usuario/auth/login-contrasena";
        }

        UsuarioDTO usuarioBBDD = usuarioService.buscarPorEmail(usuarioDTO.getEmail());

        if (!passwordEncoder.matches(contrasena, usuarioBBDD.getContrasena())) {
            logger.warn("Contraseña incorrecta para el usuario: {}", usuarioDTO.getEmail());
            model.addAttribute("error", "La contraseña no es correcta.");
            Integer intentos = (Integer) session.getAttribute("intentos");
            if (intentos == null) intentos = 0;
            intentos++;
            session.setAttribute("intentos", intentos);

            if (intentos >= 3) {
                logger.error("Usuario bloqueado por múltiples intentos fallidos: {}", usuarioDTO.getEmail());
                usuarioService.bloquearUsuario(usuarioDTO.getEmail(), "Demasiados intentos fallidos");
                usuarioService.actualizarTiempoDesbloqueo(usuarioDTO.getEmail(), LocalDateTime.now().plusSeconds(30));
                session.invalidate();
                return "redirect:/login/username";
            }

            return "usuario/auth/login-contrasena";
        }

        logger.info("Inicio de sesión exitoso para: {}", usuarioDTO.getEmail());

        // 1. Buscar cookie de navegador
        String navegadorId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("navegador_id".equals(cookie.getName())) {
                    navegadorId = cookie.getValue();
                    break;
                }
            }
        }

        // 2. Si no existe, generar uno nuevo y crear cookie persistente
        if (navegadorId == null) {
            navegadorId = UUID.randomUUID().toString();
            Cookie nuevaCookie = new Cookie("navegador_id", navegadorId);
            nuevaCookie.setMaxAge(60 * 60 * 24 * 365); // 1 año
            nuevaCookie.setPath("/");
            response.addCookie(nuevaCookie);
            logger.info("Generado nuevo navegador_id: {}", navegadorId);
        }

        // 3. Actualizar contador por navegador en la base de datos
        usuarioService.actualizarContadorPorNavegador(usuarioDTO.getEmail(), navegadorId);
        logger.info("Contador de navegador actualizado para: {}", usuarioDTO.getEmail());

        // Obtener o inicializar contador de inicios de sesión
        int contador = usuarioService.getContadorInicios(usuarioBBDD.getEmail());
        contador++;
        usuarioService.actualizarContadorInicios(usuarioBBDD.getEmail(), contador);

        logger.info("Número de accesos al dashboard en esta sesión: {}", contador);

        session.setAttribute("usuario", usuarioBBDD);
        session.setAttribute("contador", contador);
        redirectAttributes.addFlashAttribute("contador", contador);
        redirectAttributes.addFlashAttribute("navegadorId", navegadorId);
        session.removeAttribute("intentos");
        return "redirect:/dashboard";
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
        logger.info("Solicitud de recuperación de contraseña para: {}", email);
        String asunto = "Verifica tu cuenta";

        String token = UUID.randomUUID().toString();
        tokenService.guardarToken(email, token);

        String enlace = "http://localhost:8080/login/verificacion?token=" + token;
        emailService.enviarCorreoConEnlace(email, asunto, enlace);

        logger.info("Enlace de recuperación enviado a: {}", email);
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
     * Muestra el formulario para introducir una nueva contraseña.
     * Valida que el token proporcionado sea válido y esté asociado a un usuario.
     *
     * @param token el token de restablecimiento recibido por correo electrónico.
     * @param model el modelo para pasar datos a la vista.
     * @return la vista para introducir una nueva contraseña o una vista de error si el token no es válido.
     */
    @GetMapping("/restablecer-contrasena")
    public String mostrarFormularioRestablecerContrasena(@RequestParam String token, Model model) {
        Optional<String> email = tokenService.validarToken(token);
        if (email.isEmpty()) {
            return "error/token-invalido";
        }

        model.addAttribute("email", email.get());
        model.addAttribute("token", token);
        return "usuario/auth/nueva-contrasena";
    }

    /**
     * Procesa el formulario de restablecimiento de contraseña.
     * Valida el token, compara las contraseñas ingresadas y, si todo es correcto,
     * actualiza la contraseña del usuario y elimina el token.
     *
     * @param token el token recibido por correo.
     * @param email el email del usuario.
     * @param contrasena la nueva contraseña ingresada.
     * @param contrasenaRecuperacion la confirmación de la nueva contraseña.
     * @param model el modelo para pasar datos a la vista en caso de error.
     * @return redirección a la pantalla de éxito o retorno a la vista del formulario si hay errores.
     */
    @PostMapping("/restablecer-contrasena")
    public String restablecerContrasena(
            @RequestParam String token,
            @RequestParam String email,
            @RequestParam String contrasena,
            @RequestParam String contrasenaRecuperacion,
            Model model
    ) {
        Optional<String> emailToken = tokenService.validarToken(token);

        if (emailToken.isEmpty() || !emailToken.get().equals(email)) {
            logger.error("Token inválido o email no coincide para restablecer contraseña.");
            return "error/token-invalido";
        }

        if (!contrasena.equals(contrasenaRecuperacion)) {
            logger.warn("Las contraseñas no coinciden para email: {}", email);
            model.addAttribute("email", email);
            model.addAttribute("token", token);
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "usuario/auth/nueva-contrasena";
        }

        logger.info("Actualizando contraseña para usuario: {}", email);
        usuarioService.actualizarContrasena(email, contrasena);
        tokenService.eliminarToken(token);
        return "redirect:/login/contrasenaActualizada";
    }

    /**
     * Muestra una vista de confirmación cuando la contraseña se ha actualizado con éxito.
     *
     * @return la vista que informa al usuario de que su contraseña ha sido actualizada.
     */
    @GetMapping("/contrasenaActualizada")
    public String mostrarContrasenaActualizada() {
        return "usuario/auth/contrasena-actualizada";
    }
}
