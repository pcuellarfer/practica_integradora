package org.grupof.administracionapp;

import org.grupof.administracionapp.entity.Usuario;
import org.grupof.administracionapp.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Clase de configuración que se encarga de insertar datos iniciales en la base de datos al inicio de la aplicación.
 * Utiliza un {@link CommandLineRunner} para ejecutar el código cuando la aplicación se inicia.
 */
@Configuration
public class DatosIniciales {

    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor que recibe un {@link PasswordEncoder} para codificar las contraseñas de los usuarios.
     *
     * @param passwordEncoder el codificador de contraseñas que se utilizará para almacenar las contraseñas de manera segura.
     */
    public DatosIniciales(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Método que crea un {@link CommandLineRunner} para insertar un usuario inicial en la base de datos.
     * Este usuario tiene un nombre, un email y una contraseña codificada.
     *
     * @param usuarioRepository el repositorio de usuarios donde se guardará el usuario inicial.
     * @return el {@link CommandLineRunner} que se ejecutará al iniciar la aplicación.
     */
    @Bean
    CommandLineRunner insertarDatos(UsuarioRepository usuarioRepository) {
        return args -> {
            Usuario usuarioInicial = new Usuario();
            usuarioInicial.setNombre("Juan");
            usuarioInicial.setContrasena(passwordEncoder.encode("Contraseña@-"));
            usuarioInicial.setEmail("davidsmh23@gmail.com");

            usuarioRepository.save(usuarioInicial);
        };
    }
}
