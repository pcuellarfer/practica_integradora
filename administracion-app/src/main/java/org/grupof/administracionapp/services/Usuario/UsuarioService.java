package org.grupof.administracionapp.services.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Usuario;

import java.util.List;
import java.util.UUID;

public interface UsuarioService {
    UsuarioDTO registrarUsuario(UsuarioDTO registroUsuarioDTO);
    Usuario buscarPorId(UUID id);
    UsuarioDTO editarUsuarioEmail(UUID id, String email);
    UsuarioDTO editarUsuarioNombre(UUID id, String nombre);
    boolean eliminarUsuario(UUID id);
    UsuarioDTO buscarUsuario(UUID id);
    boolean existeNombre(String nombre);
    boolean existePorEmail(String email);
    boolean validarNombreContrasena(String nombre, String contrasena);
    List<UsuarioDTO> listarUsuarios();
    void reiniciarIntentos(UUID id);
    UsuarioDTO getUsuario();
    UsuarioDTO getUsuarioPorNombre(String susuario);
    void iniciarSesion(UsuarioDTO usuarioDTO);
    UsuarioDTO buscarPorEmail(String email);
    boolean buscarBloqueado(@NotBlank(message = "El email no puede estar vacío") @Email(message = "El email no tiene un formato válido") String email);
    void bloquearUsuario(@NotBlank(message = "El email no puede estar vacío") @Email(message = "El email no tiene un formato válido") String email, String demasiadosIntentosFallidos);
}
