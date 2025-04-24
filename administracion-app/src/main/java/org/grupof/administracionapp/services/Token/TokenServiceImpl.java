package org.grupof.administracionapp.services.Token;

import org.grupof.administracionapp.dto.TokenRecuperacionDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementación de {@link TokenService} que gestiona tokens de recuperación en memoria utilizando un {@link ConcurrentHashMap}.
 * Esta implementación es útil para pruebas o entornos donde no se requiere persistencia a largo plazo.
 */
@Service
public class TokenServiceImpl implements TokenService {

    private final ConcurrentHashMap<String, String> tokens = new ConcurrentHashMap<>();

    /**
     * Guarda un token asociado a un correo electrónico en el almacenamiento en memoria.
     *
     * @param email el correo electrónico del usuario
     * @param token el token generado
     * @return actualmente devuelve {@code null}, se recomienda devolver un {@link TokenRecuperacionDTO} en futuras versiones
     */
    @Override
    public TokenRecuperacionDTO guardarToken(String email, String token) {
        tokens.put(token, email);
        return null;
    }

    /**
     * Valida si un token existe en el almacenamiento.
     *
     * @param token el token a validar
     * @return un {@link Optional} que contiene el token si es válido, o vacío si no existe
     */
    @Override
    public Optional<String> validarToken(String token) {
        return Optional.ofNullable(tokens.get(token));
    }

    /**
     * Obtiene el correo electrónico asociado a un token.
     *
     * @param token el token cuya información se desea recuperar
     * @return el correo electrónico asociado al token, o {@code null} si no existe
     */
    @Override
    public String obtenerEmailPorToken(String token) {
        return tokens.get(token);
    }

    /**
     * Elimina un token del almacenamiento.
     *
     * @param token el token que se desea eliminar
     */
    @Override
    public void eliminarToken(String token) {
        tokens.remove(token);
    }
}
