package org.grupof.administracionapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Clase de configuración de seguridad para la aplicación.
 * Define los filtros de seguridad y el codificador de contraseñas utilizados por Spring Security.
 */
@Configuration
public class SecurityConfig {

    /**
     * Define el bean encargado de codificar contraseñas utilizando el algoritmo BCrypt.
     *
     * @return Instancia de {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura la cadena de filtros de seguridad para manejar las peticiones HTTP.
     * En este caso, permite el acceso libre a las rutas de login, dashboard, empleado y usuario.
     *
     * @param http Objeto {@link HttpSecurity} utilizado para configurar la seguridad HTTP.
     * @return {@link SecurityFilterChain} con la configuración aplicada.
     * @throws Exception en caso de error durante la configuración.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login/**", "/dashboard/**", "/empleado/**", "/usuario/**", "/nominas/**", "/api/nominas/**")
                        .permitAll()
                        .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable); //desactiva un sistema de protección que impide que formularios o scripts maliciosos hagan POST

        return http.build();
    }
}
