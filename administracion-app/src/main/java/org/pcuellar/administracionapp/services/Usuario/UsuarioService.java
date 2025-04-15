package org.pcuellar.administracionapp.services.Usuario;

import org.pcuellar.administracionapp.dto.RegistroUsuarioDTO;
import org.pcuellar.administracionapp.dto.UsuarioDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UsuarioService {
    UsuarioDTO registrarUsuario(RegistroUsuarioDTO registroUsuarioDTO);
    UsuarioDTO editarUsuarioEmail(UUID id, String email);
    UsuarioDTO editarUsuarioNombre(UUID id, String nombre);
    boolean eliminarUsuario(UUID id);
    UsuarioDTO buscarUsuario(UUID id);
    List<UsuarioDTO> listarUsuarios();
    void reiniciarIntentos(UUID id);
}
