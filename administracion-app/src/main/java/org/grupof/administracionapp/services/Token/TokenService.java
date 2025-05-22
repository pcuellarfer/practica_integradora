package org.grupof.administracionapp.services.Token;

import org.grupof.administracionapp.dto.TokenRecuperacionDTO;
import java.util.Optional;

/**
 * Servicio para la gestión de tokens de recuperación de contraseña.
 * Permite guardar, validar, obtener y eliminar tokens asociados a un correo electrónico.
 */
public interface TokenService {

    /**
     * Guarda un token de recuperación asociado a un correo electrónico.
     *
     * @param email el correo electrónico del usuario
     * @param token el token generado
     * @return un objeto {@link TokenRecuperacionDTO} que representa el token guardado
     */
    TokenRecuperacionDTO guardarToken(String email, String token);

    /**
     * Valida si un token existe y es válido.
     *
     * @param token el token a validar
     * @return un {@link Optional} que contiene el token si es válido, o vacío si no lo es
     */
    Optional<String> validarToken(String token);

    /**
     * Obtiene el correo electrónico asociado a un token.
     *
     * @param token el token a consultar
     * @return el correo electrónico asociado al token
     */
    String obtenerEmailPorToken(String token);

    /**
     * Elimina un token del sistema.
     *
     * @param token el token que se desea eliminar
     */
    void eliminarToken(String token);
}
