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
     * Método que recupera el objeto empleado de la sesión o crea uno nuevo si no existe.
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
     * Método que reinicia la sesión y redirige al formulario de registro.
     *
     * @param session La sesión HTTP que se invalida.
     * @return La URL de redirección al formulario de registro de empleado.
     */
    @GetMapping("/reinicio")
    public String reinicio(HttpSession session) {
        session.invalidate();
        return "redirect:/empleado/signup";
    }

    /**
     * Método que muestra la vista de registro del empleado.
     *
     * @return El nombre de la vista de registro de empleado.
     */
    @GetMapping("/signup")
    public String mostrarLogin() {
        return "empleado/auth/signUp-empleado-user";
    }

    /**
     * Método que guarda los datos básicos del empleado (nombre, email) después de la validación.
     * Si hay errores en los datos, redirige al formulario de registro.
     *
     * @param empleado Los datos del empleado a guardar.
     * @param nombre El nombre del empleado.
     * @param email El email del empleado.
     * @param result Resultado de la validación de los datos del empleado.
     * @param session La sesión HTTP para almacenar los datos.
     * @param model El modelo para pasar datos a la vista.
     * @return La URL de redirección según el resultado de la validación.
     */
    @PostMapping("/signup")
    public String guardarDatosEmpleado(@ModelAttribute("empleado") Empleado empleado,
                                       @RequestParam String nombre,
                                       @RequestParam String email,
                                       @Validated BindingResult result,
                                       HttpSession session, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("error", "Por favor, complete todos los campos requeridos.");
            return "empleado/auth/signUp-empleado-user";
        }

        if (nombre == null || nombre.isBlank()) {
            model.addAttribute("error", "El nombre no puede estar vacío.");
            return "empleado/auth/signUp-empleado-user";
        }

        if (email == null || email.isBlank()) {
            model.addAttribute("error", "El email no puede estar vacío.");
            return "empleado/auth/signUp-empleado-user";
        }

        // En lugar de agregar el empleado directamente a la sesión,
        // creamos un nuevo objeto cada vez
        session.setAttribute("empleado", new Empleado());

        return "redirect:/empleado/signup-password";
    }

    /**
     * Método que muestra la vista para ingresar la contraseña del empleado.
     * Si el empleado no está registrado o tiene campos vacíos, redirige al formulario de registro.
     *
     * @param empleado El objeto empleado que contiene los datos ingresados.
     * @param model El modelo para pasar datos a la vista.
     * @return La vista para ingresar la contraseña o redirección al registro si hay problemas.
     */
    @GetMapping("/signup-password")
    public String mostrarLoginContrasena(@ModelAttribute("empleado") Empleado empleado, Model model) {
        if (empleado == null || empleado.getNombre().isBlank()) {
            return "redirect:/empleado/signup";
        }
        return "empleado/auth/signUp-empleado-passwd";
    }

    /**
     * Método que procesa la contraseña del empleado y lo registra en la base de datos.
     * Si la contraseña es válida, guarda al empleado en la base de datos y redirige al dashboard del empleado.
     * Si hay errores, muestra un mensaje de error.
     *
     * @param empleado El objeto empleado con los datos registrados.
     * @param contrasena La contraseña introducida por el usuario.
     * @param model El modelo para pasar mensajes de error a la vista.
     * @param sessionStatus El estado de la sesión que se completa si todo es exitoso.
     * @return La URL de redirección al dashboard o al formulario de contraseña con error.
     */
    @PostMapping("/signup-password")
    public String procesarLoginContrasena(@ModelAttribute("empleado") Empleado empleado,
                                          @RequestParam String contrasena,
                                          Model model,
                                          SessionStatus sessionStatus) {

        if (empleado == null || empleado.getNombre().isBlank()) {
            return "redirect:/empleado/login";
        }

        if (contrasena == null || contrasena.isBlank()) {
            model.addAttribute("error", "La contraseña no puede estar vacía.");
            return "empleado/auth/signUp-empleado-passwd";
        }

        // Asignamos la contraseña al empleado
        empleado.setContrasena(contrasena);

        // Guardamos el empleado en la base de datos a través del servicio
        empleadoService.registrarEmpleado(empleado);

        // Limpia la sesión una vez el empleado esté registrado
        sessionStatus.setComplete();

        // Redirige al dashboard del empleado
        return "redirect:/empleado/dashboard";
    }

    /**
     * Método que muestra el dashboard del empleado después de un registro exitoso.
     *
     * @param model El modelo para pasar datos a la vista del dashboard.
     * @return El nombre de la vista del dashboard del empleado.
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "empleado/main/empleado-dashboard";
    }
}
