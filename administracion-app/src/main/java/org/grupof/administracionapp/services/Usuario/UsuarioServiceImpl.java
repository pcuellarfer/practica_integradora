package org.grupof.administracionapp.services.Usuario;

import org.modelmapper.ModelMapper;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Usuario;
import org.grupof.administracionapp.repository.UsuarioRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UsuarioDTO registrarUsuario(UsuarioDTO usuarioDTO) {

        //devuelve usuario DTO con mucho NULL
        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
        //Usuario usuario = convertToEntity(registroUsuarioDTO);
        usuario = usuarioRepository.save(usuario);

        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    @Override
    public Usuario buscarPorId(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
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
    public boolean existeNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre).isPresent();
    }

    @Override
    public boolean existePorEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }



    @Override
    public boolean validarNombreContrasena(String nombre, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNombre(nombre);

        return usuarioOpt
                .map(usuario -> usuario.getContrasena().equals(contrasena)) // comparación directa
                .orElse(false);
    }

    @Override
    public List<UsuarioDTO> listarUsuarios() {
        return List.of();
    }

    @Override
    public void reiniciarIntentos(UUID id) {

    }

    @Override
    public UsuarioDTO getUsuario() {
        return null;
    }

    @Override
    public UsuarioDTO getUsuarioPorNombre(String susuario) {
        return null;
    }

    @Override
    public void iniciarSesion(UsuarioDTO usuarioDTO) {

    }

    @Override
    public UsuarioDTO buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(usuario -> {
                    UsuarioDTO dto = new UsuarioDTO();
                    dto.setId(usuario.getId());
                    dto.setEmail(usuario.getEmail());
                    dto.setContrasena(usuario.getContrasena());
                    return dto;
                })
                .orElse(null);
    }

    @Override
    public boolean buscarBloqueado(String email) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        return usuarioOptional.map(Usuario::isEstadoBloqueado).orElse(false);
    }

    @Override
    public void bloquearUsuario(String email, String demasiadosIntentosFallidos) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setEstadoBloqueado(true);
            usuario.setMotivoBloqueo("Demasiados intentos fallidos");
            usuarioRepository.save(usuario);
        }
    }
}
