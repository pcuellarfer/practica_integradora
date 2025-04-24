package org.grupof.administracionapp.services.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Usuario;

import java.util.List;
import java.util.UUID;

/**
 * Interfaz para definir los servicios relacionados con la gestión de usuarios.
 */
public interface UsuarioService {

    /**
     * Registra un nuevo usuario a partir de un DTO.
     *
     * @param registroUsuarioDTO datos del usuario a registrar
     * @return el DTO del usuario registrado
     */
    UsuarioDTO registrarUsuario(UsuarioDTO registroUsuarioDTO);

    /**
     * Busca un usuario por su ID.
     *
     * @param id identificador del usuario
     * @return la entidad {@link Usuario} si se encuentra
     */
    Usuario buscarPorId(UUID id);

    /**
     * Edita el email de un usuario existente.
     *
     * @param id identificador del usuario
     * @param email nuevo email
     * @return el DTO del usuario actualizado
     */
    UsuarioDTO editarUsuarioEmail(UUID id, String email);

    /**
     * Edita el nombre de un usuario existente.
     *
     * @param id identificador del usuario
     * @param nombre nuevo nombre
     * @return el DTO del usuario actualizado
     */
    UsuarioDTO editarUsuarioNombre(UUID id, String nombre);

    /**
     * Elimina un usuario.
     *
     * @param id identificador del usuario
     * @return true si se eliminó correctamente, false en caso contrario
     */
    boolean eliminarUsuario(UUID id);

    /**
     * Busca un usuario por su ID y lo devuelve como DTO.
     *
     * @param id identificador del usuario
     * @return DTO del usuario encontrado
     */
    UsuarioDTO buscarUsuario(UUID id);

    /**
     * Verifica si un nombre de usuario ya existe.
     *
     * @param nombre nombre de usuario
     * @return true si ya existe, false si no
     */
    boolean existeNombre(String nombre);

    /**
     * Verifica si un email ya está registrado.
     *
     * @param email dirección de correo electrónico
     * @return true si ya existe, false si no
     */
    boolean existePorEmail(String email);

    /**
     * Valida que el nombre de usuario y la contraseña coincidan con un registro existente.
     *
     * @param nombre nombre de usuario
     * @param contrasena contraseña
     * @return true si las credenciales son válidas, false si no
     */
    boolean validarNombreContrasena(String nombre, String contrasena);

    /**
     * Lista todos los usuarios registrados.
     *
     * @return lista de {@link UsuarioDTO}
     */
    List<UsuarioDTO> listarUsuarios();

    /**
     * Reinicia el contador de intentos fallidos de inicio de sesión de un usuario.
     *
     * @param id identificador del usuario
     */
    void reiniciarIntentos(UUID id);

    /**
     * Obtiene el usuario autenticado actualmente.
     *
     * @return DTO del usuario actual
     */
    UsuarioDTO getUsuario();

    /**
     * Obtiene un usuario por su nombre.
     *
     * @param susuario nombre de usuario
     * @return DTO del usuario encontrado
     */
    UsuarioDTO getUsuarioPorNombre(String susuario);

    /**
     * Inicia sesión para un usuario.
     *
     * @param usuarioDTO DTO del usuario que desea iniciar sesión
     */
    void iniciarSesion(UsuarioDTO usuarioDTO);

    /**
     * Busca un usuario por su dirección de correo electrónico.
     *
     * @param email dirección de correo
     * @return DTO del usuario si existe, o null si no se encuentra
     */
    UsuarioDTO buscarPorEmail(String email);

    /**
     * Verifica si un usuario está bloqueado por fallos repetidos de inicio de sesión.
     *
     * @param email dirección de correo
     * @return true si está bloqueado, false si no
     */
    boolean buscarBloqueado(@NotBlank(message = "El email no puede estar vacío") @Email(message = "El email no tiene un formato válido") String email);

    /**
     * Bloquea a un usuario por múltiples intentos fallidos de inicio de sesión.
     *
     * @param email dirección de correo
     * @param demasiadosIntentosFallidos motivo del bloqueo
     */
    void bloquearUsuario(@NotBlank(message = "El email no puede estar vacío") @Email(message = "El email no tiene un formato válido") String email, String demasiadosIntentosFallidos);

    /**
     * Actualiza la contraseña de un usuario.
     *
     * @param s identificador o email del usuario
     * @param nuevaContrasena nueva contraseña a establecer
     */
    void actualizarContrasena(String s, String nuevaContrasena);
}
