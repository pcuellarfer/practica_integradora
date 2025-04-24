package org.grupof.administracionapp;

import org.grupof.administracionapp.entity.Usuario;
import org.grupof.administracionapp.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatosIniciales {

    private final PasswordEncoder passwordEncoder;

    public DatosIniciales(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner insertarDatos(UsuarioRepository usuarioRepository) {
        return args -> {
            Usuario usuarioInicial = new Usuario();
            usuarioInicial.setNombre("Juan");
            usuarioInicial.setContrasena(passwordEncoder.encode("Contraseña@-"));
            usuarioInicial.setEmail("juan@gmail.com");

            usuarioRepository.save(usuarioInicial);

        };
    }
}
