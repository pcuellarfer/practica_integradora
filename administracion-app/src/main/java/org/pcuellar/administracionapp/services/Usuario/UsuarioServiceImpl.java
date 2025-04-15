package org.pcuellar.administracionapp.services.Usuario;

import org.modelmapper.ModelMapper;
import org.pcuellar.administracionapp.dto.RegistroUsuarioDTO;
import org.pcuellar.administracionapp.dto.UsuarioDTO;
import org.pcuellar.administracionapp.entity.Usuario;
import org.pcuellar.administracionapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public UsuarioDTO registrarUsuario(RegistroUsuarioDTO registroUsuarioDTO) {

        //devuelve usuario DTO con mucho NULL
        Usuario usuario = modelMapper.map(registroUsuarioDTO, Usuario.class);
        //Usuario usuario = convertToEntity(registroUsuarioDTO);
        usuario = usuarioRepository.save(usuario);

        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    @Override
    public UsuarioDTO editarUsuarioEmail(UUID id, String email) {
        return null;
    }

    @Override
    public UsuarioDTO editarUsuarioNombre(UUID id, String nombre) {
        return null;
    }

    @Override
    public boolean eliminarUsuario(UUID id) {
        return false;
    }

    @Override
    public UsuarioDTO buscarUsuario(UUID id) {
        return null;
    }

    @Override
    public List<UsuarioDTO> listarUsuarios() {
        return List.of();
    }

    @Override
    public void reiniciarIntentos(UUID id) {

    }

    private Usuario convertToEntity (RegistroUsuarioDTO registroUsuarioDTO) {
        return modelMapper.map(registroUsuarioDTO, Usuario.class);
    }
    private UsuarioDTO convertToDTO (Usuario usuario) {
        return modelMapper.map(usuario, UsuarioDTO.class);
    }
}
