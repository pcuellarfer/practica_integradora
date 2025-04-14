package org.pcuellar.administracionapp.services;

import org.pcuellar.administracionapp.dto.UsuarioDTO;

import java.util.List;
import java.util.UUID;

public interface UsuarioService {
    UsuarioDTO registrarUsuario(UsuarioDTO dto);
    UsuarioDTO editarUsuarioEmail(UUID id, String email);
    UsuarioDTO editarUsuarioNombre(UUID id, String nombre);
    boolean eliminarUsuario(UUID id);
    UsuarioDTO buscarUsuario(UUID id);
    List<UsuarioDTO> listarUsuarios();
    void reiniciarIntentos(UUID id);
}
