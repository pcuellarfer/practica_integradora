package org.grupof.administracionapp.services.Usuario;

import jakarta.persistence.EntityNotFoundException;
import org.grupof.administracionapp.dto.Usuario.RegistroUsuarioDTO;
import org.modelmapper.ModelMapper;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Usuario;
import org.grupof.administracionapp.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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
     * @param registroUsuarioDTO objeto DTO con los datos del usuario a registrar
     * @return el usuario registrado como DTO
     */
    @Override
    public UsuarioDTO registrarUsuario(RegistroUsuarioDTO registroUsuarioDTO) {
        Usuario usuario = modelMapper.map(registroUsuarioDTO, Usuario.class);
        usuario = usuarioRepository.save(usuario);
        return modelMapper.map(usuario, UsuarioDTO.class);//devolver directamente UsuarioDTO
        //return modelMapper.map(usuario, RegistroUsuarioDTO.class); //para que devuelva un RegistroUsuarioDTO
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

    /**
     * Desbloquea al usuario con el email proporcionado.
     *
     * <p>Este método elimina el bloqueo del usuario, estableciendo el campo
     * {@code bloqueadoHasta} como {@code null} y el estado de bloqueo {@code false}.
     * Luego, guarda los cambios en la base de datos.
     *
     * @param email El email del usuario a desbloquear.
     * @throws RuntimeException Si no se encuentra un usuario con el email dado.
     */
    @Override
    public void desbloquearUsuario(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setBloqueadoHasta(null);
        usuario.setEstadoBloqueado(false);
        usuarioRepository.save(usuario);
    }

    /**
     * Actualiza el tiempo de desbloqueo del usuario con el email proporcionado.
     *
     * <p>Este método establece el campo {@code bloqueadoHasta} del usuario a 30 segundos
     * después de la fecha y hora actuales. Luego, guarda los cambios en la base de datos.
     *
     * @param email El email del usuario cuyo tiempo de desbloqueo se desea actualizar.
     * @param localDateTime El momento en que se establece el nuevo tiempo de desbloqueo.
     * @throws EntityNotFoundException Si no se encuentra un usuario con el email proporcionado.
     */
    @Override
    public void actualizarTiempoDesbloqueo(String email, LocalDateTime localDateTime) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con email: " + email));

        // Sumar 30 segundos desde ahora
        usuario.setBloqueadoHasta(LocalDateTime.now().plusSeconds(30));
        usuarioRepository.save(usuario);
    }

    /**
     * Busca un usuario en la base de datos por su email.
     *
     * <p>Este método intenta encontrar un usuario cuyo email coincida con el proporcionado.
     * Si el usuario existe, lo devuelve; de lo contrario, lanza una excepción {@link EntityNotFoundException}.
     *
     * @param email El email del usuario a buscar.
     * @return El objeto {@link Usuario} correspondiente al email proporcionado.
     * @throws EntityNotFoundException Si no se encuentra un usuario con el email proporcionado.
     */
    @Override
    public Usuario buscarPorEmailFecha(String email) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        if (usuarioOptional.isPresent()) {
            return usuarioOptional.get();
        } else {
            throw new EntityNotFoundException("Usuario no encontrado con email: " + email);
        }
    }

    /**
     * Actualiza el número de inicios de sesión del usuario.
     *
     * @param email El email del usuario.
     * @param nuevoContador El nuevo valor del contador de inicios.
     */
    @Override
    public void actualizarContadorInicios(String email, int nuevoContador) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con email: " + email));

        usuario.setContadorInicios(nuevoContador);
        usuarioRepository.save(usuario);
    }

    /**
     * Obtiene el número de inicios de sesión del usuario con el email proporcionado.
     *
     * @param email El email del usuario.
     * @return El número actual de inicios de sesión.
     * @throws EntityNotFoundException Si no se encuentra el usuario con ese email.
     */
    @Override
    public int getContadorInicios(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con email: " + email));

        return usuario.getContadorInicios();
    }

    @Override
    public void actualizarContadorPorNavegador(String email, String navegadorId) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con email: " + email));

        // Obtener mapa o lista de contadores por navegador
        Map<String, Integer> contadores = usuario.getContadoresPorNavegador();
        if (contadores == null) {
            contadores = new HashMap<>();
        }

        // Actualizar contador de este navegador
        int actual = contadores.getOrDefault(navegadorId, 0);
        contadores.put(navegadorId, actual + 1);

        usuario.setContadoresPorNavegador(contadores);
        usuarioRepository.save(usuario);
    }
}
