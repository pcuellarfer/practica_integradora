package org.pcuellar.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.pcuellar.administracionapp.auxiliar.TipoUsuario;
import org.pcuellar.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.pcuellar.administracionapp.dto.Usuario.UsuarioDTO;
import org.pcuellar.administracionapp.services.Usuario.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador que gestiona el flujo de inicio de sesión de los usuarios,
 * incluyendo la introducción del nombre de usuario y la contraseña, y la redirección
 * a sus respectivos paneles según su rol.
 */
@Controller
@RequestMapping("/login")
public class InicioSesionController {

    private final UsuarioService usuarioService;

    /**
     * Constructor que inyecta el servicio encargado de la lógica relacionada con usuarios.
     *
     * @param usuarioService servicio de gestión de usuarios.
     */
    public InicioSesionController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene o crea un objeto UsuarioDTO para la sesión.
     *
     * @param session sesión HTTP actual.
     * @return objeto UsuarioDTO existente en sesión o uno nuevo si no existe.
     */
    @ModelAttribute("usuario")
    public UsuarioDTO getUsuario(HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");
        if (usuarioDTO == null) {
            //usuarioDTO = new RegistroEmpleadoDTO(); // Se instancia como Empleado por defecto
        }
        return usuarioDTO;
    }

    /**
     * Cierra la sesión actual e invalida los datos de sesión.
     *
     * @param session sesión HTTP actual.
     * @return redirección al formulario de nombre de usuario.
     */
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login/username";
    }

    /**
     * Muestra el formulario para introducir el nombre de usuario.
     * Si ya hay un usuario autenticado en sesión, redirige al dashboard correspondiente.
     *
     * @param usuario usuario almacenado en sesión.
     * @return vista del formulario de nombre de usuario o redirección al dashboard.
     */
    @GetMapping("/username")
    public String mostrarFormularioNombre(@ModelAttribute("usuario") UsuarioDTO usuario) {
        if (usuario != null && usuario.getTipoUsuario() != null) {
            if (usuario.getTipoUsuario() == TipoUsuario.EMPLEADO) {
                return "redirect:/empleado/dashboard";
            } else if (usuario.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/login/dashboard";
        }
        return "usuario/auth/login-nombre";
    }

    /**
     * Procesa el formulario de ingreso del nombre de usuario durante el login.
     * <p>
     * Este método valida que el nombre no esté vacío y que exista un usuario con ese nombre
     * en la base de datos. Si el usuario ya está autenticado (según su tipo), redirige directamente
     * al dashboard correspondiente. Si el nombre es válido y existe, guarda el objeto
     * {@link UsuarioDTO} en la sesión y redirige al formulario de ingreso de contraseña.
     * </p>
     *
     * @param usuarioDTO el usuario actual en sesión (si existe).
     * @param nombre el nombre ingresado por el usuario.
     * @param result resultado de la validación.
     * @param session la sesión HTTP actual donde se guarda el usuario autenticado.
     * @param model el modelo para pasar atributos a la vista en caso de error.
     * @return la vista siguiente en el proceso de login o redirección al dashboard si ya está autenticado.
     */
    @PostMapping("/username")
    public String procesarFormularioNombre(@ModelAttribute("usuario") UsuarioDTO usuarioDTO,
                                           @RequestParam String nombre,
                                           @Validated BindingResult result,
                                           HttpSession session, Model model) {

        if (usuarioDTO != null && usuarioDTO.getTipoUsuario() != null) {
            if (usuarioDTO.getTipoUsuario() == TipoUsuario.EMPLEADO) {
                return "redirect:/empleado/dashboard";
            } else if (usuarioDTO.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/login/dashboard";
        }

        if (result.hasErrors() || nombre == null || nombre.isBlank()) {
            model.addAttribute("error", "El nombre no puede estar vacío.");
            return "usuario/auth/login-nombre";
        }

        // Buscar usuario por nombre en la base de datos
        UsuarioDTO usuarioExistente = usuarioService.buscarPorNombre(nombre);

        if (usuarioExistente == null) {
            model.addAttribute("error", "No existe un usuario con ese nombre.");
            return "usuario/auth/login-nombre";
        }

        // Guardar en sesión y avanzar
        session.setAttribute("usuario", usuarioExistente);
        return "usuario/auth/login-contrasena";
    }


    /**
     * Muestra el formulario para introducir la contraseña del usuario.
     *
     * @param usuarioDTO usuario actual almacenado en sesión.
     * @return vista del formulario de contraseña o redirección al formulario de nombre si el nombre no está definido.
     */
    @GetMapping("/password")
    public String mostrarFormularioContrasena(@ModelAttribute("usuario") UsuarioDTO usuarioDTO) {
        if (usuarioDTO == null || usuarioDTO.getNombre().isBlank()) {
            return "redirect:/login/username";
        }

        return "usuario/auth/login-contrasena";
    }

    /**
     * Procesa la contraseña ingresada por el usuario e intenta autenticarlo.
     *
     * @param usuarioDTO usuario actual almacenado en sesión.
     * @param contrasena contraseña ingresada.
     * @param model      modelo para pasar atributos a la vista.
     * @return redirección al dashboard según el tipo de usuario, o vista con error si la autenticación falla.
     */
    @PostMapping("/password")
    public String procesarFormularioContrasena(@ModelAttribute("usuario") UsuarioDTO usuarioDTO,
                                               @RequestParam String contrasena,
                                               Model model) {
        if (usuarioDTO != null && usuarioDTO.getTipoUsuario() != null) {
            if (usuarioDTO.getTipoUsuario() == TipoUsuario.EMPLEADO) {
                return "redirect:/empleado/dashboard";
            } else if (usuarioDTO.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/login/dashboard";
        }

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

        if (usuarioDTO.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
            return "admin/main/admin-dashboard";
        }

        return "redirect:/login/dashboard";
    }

    /**
     * Muestra un dashboard genérico si el tipo de usuario no es específico o si se accede por defecto.
     *
     * @param session sesión HTTP actual.
     * @param model   modelo para pasar atributos a la vista.
     * @return vista del dashboard genérico del usuario.
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        model.addAttribute("nombre", usuario.getNombre());
        return "usuario/main/usuario-dashboard";
    }
}
