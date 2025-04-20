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

/**
 * Controlador que gestiona el registro de nuevos usuarios del tipo USUARIO.
 */
@Controller
@RequestMapping("/usuario")
public class  UsuarioSignUpController {

    private final UsuarioService usuarioService;

    /**
     * Constructor que inyecta el servicio de usuario.
     *
     * @param usuarioService el servicio encargado de la lógica relacionada con usuarios.
     */
    public UsuarioSignUpController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Devuelve un objeto {@link UsuarioDTO} almacenado en sesión o uno nuevo si no existe.
     *
     * @param session la sesión HTTP actual.
     * @return una instancia de {@link UsuarioDTO}, normalmente de tipo {@link EmpleadoDTO}.
     */
    @ModelAttribute("usuario")
    public UsuarioDTO getUsuario(HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");
        if (usuarioDTO == null) {
            usuarioDTO = new EmpleadoDTO();
        }
        return usuarioDTO;
    }

    /**
     * Cierra la sesión del usuario actual.
     *
     * @param session la sesión HTTP.
     * @return redirige a la vista de login.
     */
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login/username";
    }

    /**
     * Muestra el formulario de registro para nuevos usuarios.
     *
     * @param usuarioDTO el usuario actual almacenado en sesión.
     * @return redirige al dashboard si ya está autenticado o muestra el formulario de registro.
     */
    @GetMapping("/signup")
    public String mostrarLogin(@ModelAttribute("empleado") UsuarioDTO usuarioDTO) {
        if (usuarioDTO != null && usuarioDTO.getTipoUsuario() == TipoUsuario.USUARIO) {
            return "redirect:/usuario/main/usuario-dashboard";
        }
        return "usuario/auth/signUp-usuario";
    }

    /**
     * Procesa el formulario de registro de un nuevo usuario.
     *
     * @param usuarioDTO el usuario en sesión.
     * @param nombre el nombre introducido por el usuario.
     * @param email el email introducido por el usuario.
     * @param contrasena la contraseña introducida por el usuario.
     * @param result el resultado de la validación.
     * @param model el modelo para pasar atributos a la vista.
     * @return redirige al dashboard si el registro fue exitoso o vuelve al formulario con errores.
     */
    @PostMapping("/signup")
    public String registrarUsuario(@ModelAttribute("usuario") UsuarioDTO usuarioDTO,
                                   @RequestParam String nombre,
                                   @RequestParam String email,
                                   @RequestParam String contrasena,
                                   @Validated BindingResult result,
                                   HttpSession session,
                                   Model model) {

        if (usuarioDTO != null && usuarioDTO.getTipoUsuario() == TipoUsuario.USUARIO) {
            return "redirect:/usuario/main/usuario-dashboard";
        }

        if (result.hasErrors()) {
            model.addAttribute("error", "Por favor, complete todos los campos requeridos.");
            return "usuario/auth/signUp-usuario";
        }

        if (nombre == null || nombre.isBlank()) {
            model.addAttribute("error", "El nombre no puede estar vacío.");
            return "usuario/auth/signUp-usuario";
        }

        if (email == null || email.isBlank()) {
            model.addAttribute("error", "El email no puede estar vacío.");
            return "usuario/auth/signUp-usuario";
        }

        if (contrasena == null || contrasena.isBlank()) {
            model.addAttribute("error", "La contraseña no puede estar vacía.");
            return "usuario/auth/signUp-usuario";
        }

        assert usuarioDTO != null;
        usuarioDTO.setNombre(nombre);
        usuarioDTO.setEmail(email);
        usuarioDTO.setContrasena(contrasena);
        usuarioDTO.setTipoUsuario(TipoUsuario.USUARIO);
        session.setAttribute("usuario", usuarioDTO);
        usuarioService.registrarUsuario(usuarioDTO);

        return "redirect:/usuario/dashboard";
    }

    /**
     * Muestra el dashboard principal del usuario autenticado.
     *
     * @param session la sesión HTTP actual.
     * @param model el modelo para pasar atributos a la vista.
     * @return vista del dashboard del usuario.
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");
        model.addAttribute("nombre", usuarioDTO.getNombre());
        return "usuario/main/usuario-dashboard";
    }
}
