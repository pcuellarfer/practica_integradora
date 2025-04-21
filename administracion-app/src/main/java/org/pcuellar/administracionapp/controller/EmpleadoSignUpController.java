package org.pcuellar.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.pcuellar.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.pcuellar.administracionapp.services.Empleado.EmpleadoService;
import org.pcuellar.administracionapp.auxiliar.TipoUsuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador encargado del proceso de registro de un empleado.
 * Maneja las vistas y la lógica de negocio para el login y el registro de un empleado.
 */
@Controller
@RequestMapping("/empleado")
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
    public RegistroEmpleadoDTO getEmpleado(HttpSession session) {
        RegistroEmpleadoDTO registroEmpleadoDTO = (RegistroEmpleadoDTO) session.getAttribute("empleado");
        if (registroEmpleadoDTO == null) {
            registroEmpleadoDTO = new RegistroEmpleadoDTO();
        }
        return registroEmpleadoDTO;
    }

    /**
     * Método que reinicia la sesión y redirige al formulario de registro.
     *
     * @param session La sesión HTTP que se invalida.
     * @return La URL de redirección al formulario de registro de empleado.
     */
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate(); // Esto cierra la sesión
        return "redirect:/login/username"; // Redirige a la página de login
    }

    /**
     * Muestra el formulario de registro para empleados.
     * <p>
     * Si ya existe una sesión activa con un {@link RegistroEmpleadoDTO} válido y su tipo de usuario está definido,
     * redirige automáticamente al dashboard del empleado, evitando que el usuario acceda nuevamente al
     * formulario de registro.
     *
     * @param registroEmpleadoDTO el objeto del empleado almacenado en la sesión, inyectado mediante {@code @ModelAttribute}.
     * @return la vista del formulario de registro de empleado si no hay sesión activa, o redirección al dashboard en caso contrario.
     */
    @GetMapping("/signup")
    public String mostrarLogin(@ModelAttribute("empleado") RegistroEmpleadoDTO registroEmpleadoDTO) {
        if (registroEmpleadoDTO != null && registroEmpleadoDTO.getTipoUsuario() != null) {
            return "redirect:/empleado/main/empleado-dashboard";
        }
        return "empleado/auth/signUp-empleado-user";
    }


    /**
     * Método que guarda los datos básicos del empleado (nombre, email) después de la validación.
     * Si hay errores en los datos, redirige al formulario de registro.
     *
     * @param registroEmpleadoDTO Los datos del empleado a guardar.
     * @param nombre      El nombre del empleado.
     * @param email       El email del empleado.
     * @param result      Resultado de la validación de los datos del empleado.
     * @param session     La sesión HTTP para almacenar los datos.
     * @param model       El modelo para pasar datos a la vista.
     * @return La URL de redirección según el resultado de la validación.
     */
    @PostMapping("/signup")
    public String guardarDatosEmpleado(@ModelAttribute("empleado") RegistroEmpleadoDTO registroEmpleadoDTO,
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

        registroEmpleadoDTO.setNombre(nombre);
        registroEmpleadoDTO.setEmail(email);
        session.setAttribute("empleado", registroEmpleadoDTO);

        return "redirect:/empleado/signup-password";
    }

    /**
     * Método que muestra la vista para ingresar la contraseña del empleado.
     * Si el empleado no está registrado o tiene campos vacíos, redirige al formulario de registro.
     *
     * @param registroEmpleadoDTO El objeto empleado que contiene los datos ingresados.
     * @return La vista para ingresar la contraseña o redirección al registro si hay problemas.
     */
    @GetMapping("/signup-password")
    public String mostrarLoginContrasena(@ModelAttribute("empleado") RegistroEmpleadoDTO registroEmpleadoDTO) {
        if (registroEmpleadoDTO == null || registroEmpleadoDTO.getNombre().isBlank()) {
            return "redirect:/empleado/signup";
        }
        return "empleado/auth/signUp-empleado-passwd";
    }

    /**
     * Método que procesa la contraseña del empleado y lo registra en la base de datos.
     * Si la contraseña es válida, guarda al empleado en la base de datos y redirige al dashboard del empleado.
     * Si hay errores, muestra un mensaje de error.
     *
     * @param registroEmpleadoDTO   El objeto empleado con los datos registrados.
     * @param contrasena    La contraseña introducida por el usuario.
     * @param model         El modelo para pasar mensajes de error a la vista.
     * @return La URL de redirección al dashboard o al formulario de contraseña con error.
     */
    @PostMapping("/signup-password")
    public String procesarLoginContrasena(@ModelAttribute("empleado") RegistroEmpleadoDTO registroEmpleadoDTO,
                                          @RequestParam String contrasena,
                                          Model model) {

        if (registroEmpleadoDTO == null || registroEmpleadoDTO.getNombre().isBlank()) {
            return "redirect:/empleado/login";
        }

        if (contrasena == null || contrasena.isBlank()) {
            model.addAttribute("error", "La contraseña no puede estar vacía.");
            return "empleado/auth/signUp-empleado-passwd";
        }

        // Asignamos la contraseña al empleado
        registroEmpleadoDTO.setContrasena(contrasena);
        // Asignamos el tipo de usuario al empleado
        registroEmpleadoDTO.setTipoUsuario(TipoUsuario.EMPLEADO);
        // Guardamos el empleado en la base de datos a través del servicio
        empleadoService.registrarEmpleado(registroEmpleadoDTO);

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
    public String dashboard(HttpSession session, Model model) {
        RegistroEmpleadoDTO empleado = (RegistroEmpleadoDTO) session.getAttribute("empleado");
        model.addAttribute("nombre", empleado.getNombre());
        return "empleado/main/empleado-dashboard";
    }

}
