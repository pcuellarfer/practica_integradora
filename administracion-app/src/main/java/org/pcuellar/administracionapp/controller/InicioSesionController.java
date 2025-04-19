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
 * Controlador que gestiona el flujo de inicio de sesión de usuarios.
 */
@Controller
@RequestMapping("/login")
@SessionAttributes("susuario")
public class InicioSesionController {

    private final UsuarioService usuarioService;

    /**
     * Constructor que inyecta el servicio de usuarios.
     *
     * @param usuarioService servicio encargado de la lógica relacionada con usuarios.
     */
    public InicioSesionController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Proporciona un objeto UsuarioDTO para almacenar en sesión si no existe ya uno.
     *
     * @param session la sesión HTTP actual.
     * @return un nuevo EmpleadoDTO si no existe usuario en sesión.
     */
    @ModelAttribute("susuario")
    public UsuarioDTO getUsuario(HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("susuario");
        if (usuarioDTO == null) {
            usuarioDTO = new EmpleadoDTO();
        }
        return usuarioDTO;
    }

    /**
     * Cierra la sesión actual e invalida la sesión HTTP.
     *
     * @param session la sesión HTTP actual.
     * @return redirige al formulario de nombre de usuario.
     */
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login/username";
    }

    /**
     * Muestra el formulario para introducir el nombre de usuario.
     *
     * @param susuario el usuario actual almacenado en sesión.
     * @return vista del formulario de nombre de usuario.
     */
    @GetMapping("/username")
    public String mostrarFormularioNombre(@ModelAttribute("susuario") UsuarioDTO susuario) {
        if (susuario != null && susuario.getTipoUsuario() != null) {
            // Redirigimos según el tipo de usuario
            if (susuario.getTipoUsuario() == TipoUsuario.EMPLEADO) {
                return "redirect:/empleado/dashboard";
            } else if (susuario.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/login/dashboard";
        }

        return "usuario/auth/login-nombre";
    }

    /**
     * Procesa el formulario de nombre de usuario.
     *
     * @param usuarioDTO el usuario actual de sesión.
     * @param nombre     el nombre ingresado por el usuario.
     * @param result     resultado de la validación.
     * @param session    la sesión HTTP.
     * @param model      el modelo para pasar atributos a la vista.
     * @return la vista de contraseña si el nombre es válido, o vuelve al formulario si no lo es.
     */
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

    /**
     * Muestra el formulario para introducir la contraseña.
     *
     * @param usuarioDTO el usuario actual almacenado en sesión.
     * @return la vista del formulario de contraseña, o redirige al formulario de nombre si el nombre no está definido.
     */
    @GetMapping("/password")
    public String mostrarFormularioContrasena(@ModelAttribute("susuario") UsuarioDTO usuarioDTO) {
        if (usuarioDTO == null || usuarioDTO.getNombre().isBlank()) {
            return "redirect:/login/username";
        }
        return "usuario/auth/login-contrasena";
    }

    /**
     * Procesa el formulario de contraseña e intenta autenticar al usuario.
     *
     * @param usuarioDTO el usuario actual almacenado en sesión.
     * @param contrasena la contraseña ingresada.
     * @param model      el modelo para pasar atributos a la vista.
     * @return redirige al dashboard correspondiente según el tipo de usuario, o vuelve al formulario si hay error.
     */
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

        // Otros tipos de usuario redirigen aquí por defecto.
        return "redirect:/login/dashboard";
    }

    /**
     * Muestra el dashboard genérico del usuario autenticado.
     *
     * @param session la sesión HTTP actual.
     * @param model   el modelo para pasar atributos a la vista.
     * @return vista del dashboard del usuario.
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        UsuarioDTO susuario = (UsuarioDTO) session.getAttribute("susuario");
        model.addAttribute("nombre", susuario.getNombre());
        return "usuario/main/usuario-dashboard";
    }
}
