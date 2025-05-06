package org.grupof.administracionapp.api;

import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import jakarta.servlet.http.HttpSession;

@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final RestTemplate restTemplate;

    public AuthController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Endpoint que realiza la llamada a la API
    @GetMapping("/username")
    public String mostrarFormularioNombre(@ModelAttribute("usuario") UsuarioDTO usuario,
                                          HttpSession session) {
        if (session.getAttribute("usuario") != null && session.getAttribute("contraseña") != null) {
            logger.info("Sesión activa detectada. Redirigiendo al dashboard.");
            return "redirect:/dashboard/dashboard"; // O la URL de la página que quieras redirigir
        }

        // Realizar la llamada a la API para verificar al usuario
        String apiUrl = "http://api/authenticate";  // Aquí va la URL de tu API REST
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, usuario, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info("Usuario autenticado correctamente.");
            // Guardar la información necesaria en la sesión
            session.setAttribute("usuario", usuario.getEmail());
            session.setAttribute("contraseña", usuario.getContrasena());
            return "redirect:/dashboard/dashboard"; // Redirigir al dashboard después de autenticarse
        } else {
            logger.error("Error de autenticación. Redirigiendo al formulario de login.");
            return "usuario/auth/login-nombre"; // Redirigir al formulario de login si hay error
        }
    }
}
