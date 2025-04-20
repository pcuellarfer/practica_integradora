package org.pcuellar.administracionapp.services.Usuario;

import org.pcuellar.administracionapp.dto.Usuario.UsuarioDTO;

import java.util.List;
import java.util.UUID;

public interface UsuarioService {
    UsuarioDTO registrarUsuario(UsuarioDTO registroUsuarioDTO);
    UsuarioDTO editarUsuarioEmail(UUID id, String email);
    UsuarioDTO editarUsuarioNombre(UUID id, String nombre);
    boolean eliminarUsuario(UUID id);
    UsuarioDTO buscarUsuario(UUID id);
    boolean existeNombre(String nombre);
    boolean validarNombreContrasena(String nombre, String contrasena);
    List<UsuarioDTO> listarUsuarios();
    void reiniciarIntentos(UUID id);

    UsuarioDTO getUsuario();

    UsuarioDTO getUsuarioPorNombre(String susuario);

    void iniciarSesion(UsuarioDTO usuarioDTO);
}
