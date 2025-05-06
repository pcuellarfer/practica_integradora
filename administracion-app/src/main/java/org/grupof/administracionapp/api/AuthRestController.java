package org.grupof.administracionapp.api;

import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthRestController {

    // Método POST para la autenticación de usuarios
    @PostMapping("/api/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody UsuarioDTO usuarioDTO) {
        // Validar las credenciales (esto es un ejemplo simple)
        if (usuarioDTO.getEmail().equals("davidsmh23@gmail.com") && usuarioDTO.getContrasena().equals("Contraseña@-")) {
            // Aquí podrías generar un JWT o devolver algún otro tipo de respuesta
            return ResponseEntity.ok("Autenticación exitosa");  // Respuesta de éxito
        } else {
            return ResponseEntity.status(401).body("Credenciales inválidas");  // Error 401 si las credenciales son incorrectas
        }
    }
}
