package org.pcuellar.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.pcuellar.administracionapp.Model.Empleado;
import org.pcuellar.administracionapp.services.Empleado.EmpleadoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

/**
 * Controlador encargado del proceso de registro de un empleado.
 * Maneja las vistas y la lógica de negocio para el login y el registro de un empleado.
 */
@Controller
@RequestMapping("/empleado")
@SessionAttributes("empleado")
public class EmpleadoSignUpController {

    private final EmpleadoService empleadoService;

    /**
     * Constructor del controlador.
     *
     * @param empleadoService El servicio de empleado que se inyecta.
     */
    public EmpleadoSignUpController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    /**
     * Recupera el objeto empleado de la sesión. Si no existe, crea uno nuevo.
     *
     * @param session La sesión HTTP donde se almacena el objeto empleado.
     * @return El objeto Empleado de la sesión.
     */
    @ModelAttribute("empleado")
    public Empleado getEmpleado(HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleado");
        if (empleado == null) {
            empleado = new Empleado();
        }
        return empleado;
    }

    /**
     * Método que reinicia la sesión y redirige al login.
     *
     * @param session La sesión HTTP que se invalida.
     * @return La URL de redirección al login.
     */
    @GetMapping("/reinicio")
    public String reinicio(HttpSession session) {
        session.invalidate();
        return "redirect:/empleado/login";
    }

    /**
     * Método que muestra la vista de login del empleado.
     *
     * @return El nombre de la vista para el login del empleado.
     */
    @GetMapping("/login")
    public String mostrarLogin() {
        return "empleado/auth/signUp-empleado-user";
    }

    /**
     * Método que guarda los datos del empleado después de la validación.
     * Si hay errores en la validación, redirige al formulario de login.
     *
     * @param empleado Los datos del empleado a guardar.
     * @param result Resultado de la validación de los datos del empleado.
     * @param session La sesión HTTP para almacenar los datos.
     * @param model El modelo para pasar datos a la vista.
     * @return La URL de redirección según el resultado de la validación.
     */
    @PostMapping("/login")
    public String guardarDatosEmpleado(@ModelAttribute("empleado") Empleado empleado,
                                       @Validated BindingResult result,
                                       HttpSession session, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("error", "Por favor, complete todos los campos requeridos.");
            return "empleado/auth/signUp-empleado-user";
        }
        session.setAttribute("empleado", empleado);
        return "redirect:/empleado/login-password";
    }

    /**
     * Método que muestra la vista de login para la contraseña del empleado.
     * Si el empleado no está registrado o tiene campos vacíos, lo redirige al formulario de login.
     *
     * @param empleado El objeto Empleado que contiene los datos.
     * @param model El modelo para pasar datos a la vista.
     * @return La vista de la contraseña o redirección al login si hay problemas.
     */
    @GetMapping("/login-password")
    public String mostrarLoginContrasena(@ModelAttribute("empleado") Empleado empleado, Model model) {
        if (empleado == null || empleado.getNombre().isBlank()) {
            return "redirect:/empleado/login";
        }
        return "empleado/auth/signUp-empleado-passwd";
    }

    /**
     * Método que procesa la contraseña del empleado.
     * Si la contraseña es correcta, guarda al empleado en la base de datos
     * y redirige al dashboard del empleado. Si es incorrecta, muestra un error.
     *
     * @param empleado El objeto Empleado con los datos.
     * @param contrasena La contraseña introducida por el usuario.
     * @param model El modelo para pasar mensajes de error a la vista.
     * @param sessionStatus El estado de la sesión que se completa si todo es exitoso.
     * @return La URL de redirección al dashboard o al formulario de contraseña con error.
     */
    @PostMapping("/login-password")
    public String procesarLoginContrasena(@ModelAttribute("empleado") Empleado empleado,
                                          @RequestParam String contrasena,
                                          Model model,
                                          SessionStatus sessionStatus) {
        if (empleado == null || empleado.getNombre().isBlank()) {
            return "redirect:/empleado/login";
        }

        if (empleado.getContrasena().equals(contrasena)) {
            // Guardamos el empleado en la base de datos
            empleadoService.registrarEmpleado(empleado);

            // Limpiamos la sesión
            sessionStatus.setComplete();
            return "redirect:/empleado/dashboard";
        } else {
            model.addAttribute("error", "Contraseña incorrecta.");
            return "empleado/auth/signUp-empleado-passwd";
        }
    }

    /**
     * Método que muestra el dashboard del empleado después del login exitoso.
     *
     * @param model El modelo para pasar datos a la vista del dashboard.
     * @return El nombre de la vista del dashboard del empleado.
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "empleado/main/empleado-dashboard";
    }
}
