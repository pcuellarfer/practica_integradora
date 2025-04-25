package org.grupof.administracionapp.services.Usuario;

import org.modelmapper.ModelMapper;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Usuario;
import org.grupof.administracionapp.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementación del servicio de usuario que gestiona la lógica de negocio
 * relacionada con las operaciones sobre los usuarios.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor que recibe el repositorio de usuarios para acceder a los datos.
     *
     * @param usuarioRepository repositorio de usuario
     */
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param usuarioDTO objeto DTO con los datos del usuario a registrar
     * @return el usuario registrado como DTO
     */
    @Override
    public UsuarioDTO registrarUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
        usuario = usuarioRepository.save(usuario);
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    /**
     * Busca un usuario por su ID.
     *
     * @param id UUID del usuario
     * @return entidad Usuario
     */
    @Override
    public Usuario buscarPorId(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    /**
     * Edita el correo electrónico de un usuario. (No implementado)
     *
     * @param id UUID del usuario
     * @param email nuevo email
     * @return UsuarioDTO actualizado (actualmente retorna null)
     */
    @Override
    public UsuarioDTO editarUsuarioEmail(UUID id, String email) {
        return null;
    }

    /**
     * Edita el nombre de un usuario. (No implementado)
     *
     * @param id UUID del usuario
     * @param nombre nuevo nombre
     * @return UsuarioDTO actualizado (actualmente retorna null)
     */
    @Override
    public UsuarioDTO editarUsuarioNombre(UUID id, String nombre) {
        return null;
    }

    /**
     * Elimina un usuario. (No implementado)
     *
     * @param id UUID del usuario
     * @return true si se eliminó, false si no (actualmente retorna false)
     */
    @Override
    public boolean eliminarUsuario(UUID id) {
        return false;
    }

    /**
     * Busca un usuario y lo retorna como DTO. (No implementado)
     *
     * @param id UUID del usuario
     * @return UsuarioDTO (actualmente retorna null)
     */
    @Override
    public UsuarioDTO buscarUsuario(UUID id) {
        return null;
    }

    /**
     * Verifica si existe un usuario con el nombre dado.
     *
     * @param nombre nombre del usuario
     * @return true si existe, false si no
     */
    @Override
    public boolean existeNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre).isPresent();
    }

    /**
     * Verifica si existe un usuario con el email dado.
     *
     * @param email correo electrónico
     * @return true si existe, false si no
     */
    @Override
    public boolean existePorEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    /**
     * Valida si el nombre de usuario y la contraseña son correctos.
     *
     * @param nombre nombre del usuario
     * @param contrasena contraseña del usuario
     * @return true si las credenciales son válidas, false si no
     */
    @Override
    public boolean validarNombreContrasena(String nombre, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNombre(nombre);
        return usuarioOpt
                .map(usuario -> usuario.getContrasena().equals(contrasena))
                .orElse(false);
    }

    /**
     * Lista todos los usuarios registrados. (Actualmente retorna lista vacía)
     *
     * @return lista de usuarios en formato DTO
     */
    @Override
    public List<UsuarioDTO> listarUsuarios() {
        return List.of();
    }

    /**
     * Reinicia los intentos de inicio de sesión fallidos de un usuario. (No implementado)
     *
     * @param id UUID del usuario
     */
    @Override
    public void reiniciarIntentos(UUID id) {}

    /**
     * Retorna el usuario actual. (No implementado)
     *
     * @return UsuarioDTO (actualmente null)
     */
    @Override
    public UsuarioDTO getUsuario() {
        return null;
    }

    /**
     * Retorna un usuario por su nombre. (No implementado)
     *
     * @param susuario nombre del usuario
     * @return UsuarioDTO (actualmente null)
     */
    @Override
    public UsuarioDTO getUsuarioPorNombre(String susuario) {
        return null;
    }

    /**
     * Inicia sesión con un usuario. (No implementado)
     *
     * @param usuarioDTO DTO del usuario
     */
    @Override
    public void iniciarSesion(UsuarioDTO usuarioDTO) {}

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email email del usuario
     * @return UsuarioDTO si lo encuentra, null si no
     */
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

    /**
     * Verifica si el usuario está bloqueado por email.
     *
     * @param email correo electrónico
     * @return true si está bloqueado, false si no
     */
    @Override
    public boolean buscarBloqueado(String email) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        return usuarioOptional.map(Usuario::isEstadoBloqueado).orElse(false);
    }

    /**
     * Bloquea a un usuario y guarda el motivo.
     *
     * @param email email del usuario
     * @param demasiadosIntentosFallidos motivo del bloqueo
     */
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

    /**
     * Actualiza la contraseña de un usuario codificándola antes de guardarla.
     * Busca al usuario por su email, y si existe, codifica la nueva contraseña utilizando
     * el {@link org.springframework.security.crypto.password.PasswordEncoder} y la guarda
     * en el repositorio.
     *
     * @param email el email del usuario cuya contraseña se desea actualizar.
     * @param nuevaContrasena la nueva contraseña en texto plano.
     */
    @Override
    public void actualizarContrasena(String email, String nuevaContrasena) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            String contrasenaCodificada = passwordEncoder.encode(nuevaContrasena);
            usuario.setContrasena(contrasenaCodificada);
            usuarioRepository.save(usuario);
        }
    }

}
