package org.grupof.administracionapp.services.Empleado;


import org.grupof.administracionapp.dto.Empleado.*;
import org.grupof.administracionapp.entity.embeddable.CuentaCorriente;
import org.grupof.administracionapp.entity.embeddable.Direccion;
import org.grupof.administracionapp.entity.embeddable.TarjetaCredito;
import org.grupof.administracionapp.entity.registroEmpleado.*;
import org.grupof.administracionapp.repository.GeneroRepository;
import org.grupof.administracionapp.services.Departamento.DepartamentoService;
import org.grupof.administracionapp.services.Especialidades.EspecialidadesService;
import org.grupof.administracionapp.services.Genero.GeneroService;
import org.grupof.administracionapp.services.Pais.PaisService;
import org.grupof.administracionapp.services.TipoDocumento.TipoDocumentoService;
import org.grupof.administracionapp.services.banco.BancoService;
import org.modelmapper.ModelMapper;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.entity.Usuario;
import org.grupof.administracionapp.repository.EmpleadoRepository;
import org.grupof.administracionapp.repository.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementación del servicio {@link EmpleadoService} que gestiona
 * las operaciones CRUD relacionadas con los empleados.
 * Utiliza {@link EmpleadoRepository} y {@link UsuarioRepository} para interactuar con la base de datos.
 */
@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    private final ModelMapper modelMapper = new ModelMapper();
    private final EmpleadoRepository empleadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final GeneroService generoService;
    private final PaisService paisService;
    private final TipoDocumentoService tipoDocumentoService;
    private final DepartamentoService departamentoService;
    private final EspecialidadesService especialidadesService;
    private final BancoService bancoService;

    @Autowired
    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository,
                               UsuarioRepository usuarioRepository,
                               GeneroService generoService,
                               PaisService paisService,
                               TipoDocumentoService tipoDocumentoService,
                               DepartamentoService departamentoService,
                               EspecialidadesService especialidadesService, BancoService bancoService) {
        this.empleadoRepository = empleadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.generoService = generoService;
        this.paisService = paisService;
        this.tipoDocumentoService = tipoDocumentoService;
        this.departamentoService = departamentoService;
        this.especialidadesService = especialidadesService;
        this.bancoService = bancoService;
    }

    /**
     * Registra un nuevo empleado en el sistema y lo asocia a un usuario existente.
     *
     * @param registroEmpleadoDTO Objeto que contiene los datos del nuevo empleado.
     * @param usuarioDTO Objeto del usuario con el que se asociará el empleado.
     */
    @Override
    public void registrarEmpleado(RegistroEmpleadoDTO registroEmpleadoDTO, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioDTO.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Empleado empleado = new Empleado();

        //he tenido que usar sets en ved de model mapper porque no sabia mapear objetos DTO
        //paso 1 - personales
        Paso1PersonalDTO personalDTO = registroEmpleadoDTO.getPaso1PersonalDTO();

        empleado.setNombre(personalDTO.getNombre());
        empleado.setApellido(personalDTO.getApellido());
        empleado.setFechaNacimiento(personalDTO.getFechaNacimiento());
        empleado.setEdad(personalDTO.getEdad());
        empleado.setComentarios(personalDTO.getComentarios());

        UUID generoId = personalDTO.getGenero();
        Genero genero = generoService.getGeneroById(generoId);
        empleado.setGenero(genero);

        UUID paisId = personalDTO.getPais();
        Pais pais = paisService.getPaisById(paisId);
        empleado.setPais(pais);

        //paso 2 - contacto
        Paso2ContactoDTO contactoDTO = registroEmpleadoDTO.getPaso2ContactoDTO();

        UUID tipoDocumentoId = contactoDTO.getTipoDocumento();
        TipoDocumento tipoDocumento = tipoDocumentoService.getTipoDocumentoById(tipoDocumentoId);

        empleado.setTipoDocumento(tipoDocumento);
        empleado.setDocumento(contactoDTO.getDocumento());
        empleado.setPrefijoTelefono(contactoDTO.getPrefijoTelefono());
        empleado.setTelefono(contactoDTO.getTelefono());

        //dirección
        Direccion direccionDTO = contactoDTO.getDireccion();
        Direccion direccion = new Direccion();
        direccion.setTipoVia(direccionDTO.getTipoVia());
        direccion.setNombreDireccion(direccionDTO.getNombreDireccion());
        direccion.setNumeroDireccion(direccionDTO.getNumeroDireccion());
        direccion.setPortal(direccionDTO.getPortal());
        direccion.setPlanta(direccionDTO.getPlanta());
        direccion.setPuerta(direccionDTO.getPuerta());
        direccion.setLocalidad(direccionDTO.getLocalidad());
        direccion.setRegion(direccionDTO.getRegion());
        direccion.setCodigoPostal(direccionDTO.getCodigoPostal());

        empleado.setDireccion(direccion);

        //paso 3 - profesionales
        Paso3ProfesionalDTO profDTO = registroEmpleadoDTO.getPaso3ProfesionalDTO();

        UUID departamentoId = profDTO.getDepartamento();
        Departamento departamento = departamentoService.getDepartamentoById(departamentoId);
        empleado.setDepartamento(departamento);


        Set<UUID> especialidadIds = profDTO.getEspecialidades();
        //transforma todas las entradas id en objetos especialidad con el metodo del servicio
        Set<Especialidad> especialidades = especialidadIds.stream()
                .map(especialidadesService::getEspecialidadById)
                .collect(Collectors.toSet());

        empleado.setEspecialidades(especialidades);

        //paso 4 - economicos

        Paso4EconomicosDTO economicosDTO = registroEmpleadoDTO.getPaso4EconomicosDTO();

        UUID bancoId = economicosDTO.getCuentaCorriente().getBanco();
        CuentaCorriente cuentaCorriente = new CuentaCorriente();
        cuentaCorriente.setBanco(bancoId);
        cuentaCorriente.setNumCuenta(economicosDTO.getCuentaCorriente().getNumCuenta());

        empleado.setCuentaCorriente(cuentaCorriente);

        empleado.setSalario(economicosDTO.getSalario());
        empleado.setComision(economicosDTO.getComision());

        TarjetaCredito tarjetaCredito = new TarjetaCredito();
        tarjetaCredito.setTipoTarjeta(economicosDTO.getTarjetaCredito().getTipoTarjeta());
        tarjetaCredito.setNumTarjeta(economicosDTO.getTarjetaCredito().getNumTarjeta());
        tarjetaCredito.setMesCaducidad(economicosDTO.getTarjetaCredito().getMesCaducidad());
        tarjetaCredito.setAnoCaducidad(economicosDTO.getTarjetaCredito().getAnoCaducidad());
        tarjetaCredito.setCvc(economicosDTO.getTarjetaCredito().getCvc());

        empleado.setTarjetaCredito(tarjetaCredito);

        empleado.setUsuario(usuario);

        empleadoRepository.save(empleado);
    }

    /**
     * Edita los datos de un empleado existente.
     *
     * @param id Identificador del empleado a editar.
     * @param dto Objeto con los nuevos datos del empleado.
     * @return DTO del empleado actualizado, o null si no se encontró.
     */
    @Override
    public RegistroEmpleadoDTO editarEmpleado(UUID id, RegistroEmpleadoDTO dto) {
        Optional<Empleado> opt = empleadoRepository.findById(id);
        if (opt.isEmpty()) return null;

        Empleado empleado = opt.get();
        BeanUtils.copyProperties(dto, empleado);
        Empleado actualizado = empleadoRepository.save(empleado);

        RegistroEmpleadoDTO resultado = new RegistroEmpleadoDTO();
        BeanUtils.copyProperties(actualizado, resultado);
        return resultado;
    }

    /**
     * Elimina lógicamente un empleado del sistema.
     *
     * @param id Identificador del empleado a eliminar.
     * @return true si el empleado fue eliminado, false si no existía.
     */
    @Override
    public boolean eliminarEmpleado(UUID id) {
        if (!empleadoRepository.existsById(id)) return false;
        empleadoRepository.deleteById(id);
        return true;
    }

    /**
     * Busca un empleado por su identificador.
     *
     * @param id Identificador del empleado.
     * @return DTO del empleado encontrado, o null si no existe.
     */
    @Override
    public RegistroEmpleadoDTO buscarEmpleado(UUID id) {
        return empleadoRepository.findById(id)
                .map(emp -> {
                    RegistroEmpleadoDTO dto = new RegistroEmpleadoDTO();
                    BeanUtils.copyProperties(emp, dto);
                    return dto;
                }).orElse(null);
    }

    /**
     * Obtiene una lista de todos los empleados registrados en el sistema.
     *
     * @return Lista de DTOs representando a los empleados.
     */
    @Override
    public List<RegistroEmpleadoDTO> listarEmpleados() {
        return empleadoRepository.findAll().stream()
                .map(emp -> {
                    RegistroEmpleadoDTO dto = new RegistroEmpleadoDTO();
                    BeanUtils.copyProperties(emp, dto);
                    return dto;
                }).collect(Collectors.toList());
    }

    /**
     * Busca un empleado a partir del identificador del usuario al que está asociado.
     *
     * @param usuarioId Identificador del usuario asociado al empleado.
     * @return DTO del empleado correspondiente, o null si no existe.
     */
    @Override
    public RegistroEmpleadoDTO buscarEmpleadoPorUsuarioId(UUID usuarioId) {
        Optional<Empleado> empleadoOpt = empleadoRepository.findByUsuarioId(usuarioId);

        if (empleadoOpt.isPresent()) {
            Empleado empleado = empleadoOpt.get();
            RegistroEmpleadoDTO dto = new RegistroEmpleadoDTO();

            // Paso 1
            Paso1PersonalDTO paso1 = new Paso1PersonalDTO();
            paso1.setNombre(empleado.getNombre());
            paso1.setApellido(empleado.getApellido());
            paso1.setFechaNacimiento(empleado.getFechaNacimiento());
            paso1.setEdad(empleado.getEdad());
            paso1.setComentarios(empleado.getComentarios());
            paso1.setGenero(empleado.getGenero().getId());
            paso1.setPais(empleado.getPais().getId());
            dto.setPaso1PersonalDTO(paso1);

            // Paso 2
            Paso2ContactoDTO paso2 = new Paso2ContactoDTO();
            paso2.setTipoDocumento(empleado.getTipoDocumento().getId());
            paso2.setDocumento(empleado.getDocumento());
            paso2.setPrefijoTelefono(empleado.getPrefijoTelefono());
            paso2.setTelefono(empleado.getTelefono());

            Direccion direccion = empleado.getDireccion();
            Direccion direccionDTO = new Direccion();
            direccionDTO.setTipoVia(direccion.getTipoVia());
            direccionDTO.setNombreDireccion(direccion.getNombreDireccion());
            direccionDTO.setNumeroDireccion(direccion.getNumeroDireccion());
            direccionDTO.setPortal(direccion.getPortal());
            direccionDTO.setPlanta(direccion.getPlanta());
            direccionDTO.setPuerta(direccion.getPuerta());
            direccionDTO.setLocalidad(direccion.getLocalidad());
            direccionDTO.setRegion(direccion.getRegion());
            direccionDTO.setCodigoPostal(direccion.getCodigoPostal());

            paso2.setDireccion(direccionDTO);
            dto.setPaso2ContactoDTO(paso2);

            // Paso 3
            Paso3ProfesionalDTO paso3 = new Paso3ProfesionalDTO();
            paso3.setDepartamento(empleado.getDepartamento().getId());

            Set<UUID> especialidadIds = empleado.getEspecialidades().stream()
                    .map(Especialidad::getId)
                    .collect(Collectors.toSet());
            paso3.setEspecialidades(especialidadIds);

            dto.setPaso3ProfesionalDTO(paso3);

            // Paso 4
            Paso4EconomicosDTO paso4 = new Paso4EconomicosDTO();
            paso4.setSalario(empleado.getSalario());
            paso4.setComision(empleado.getComision());

            CuentaCorriente cc = new CuentaCorriente();
            cc.setBanco(empleado.getCuentaCorriente().getBanco());
            cc.setNumCuenta(empleado.getCuentaCorriente().getNumCuenta());
            paso4.setCuentaCorriente(cc);

            TarjetaCredito tc = new TarjetaCredito();
            tc.setTipoTarjeta(empleado.getTarjetaCredito().getTipoTarjeta());
            tc.setNumTarjeta(empleado.getTarjetaCredito().getNumTarjeta());
            tc.setMesCaducidad(empleado.getTarjetaCredito().getMesCaducidad());
            tc.setAnoCaducidad(empleado.getTarjetaCredito().getAnoCaducidad());
            tc.setCvc(empleado.getTarjetaCredito().getCvc());
            paso4.setTarjetaCredito(tc);

            dto.setPaso4EconomicosDTO(paso4);

            return dto;
        }

        return null;
    }

    @Override
    public List<Empleado> getEmpleadosOrdenados() {
        return empleadoRepository.findAll(Sort.by("apellido").ascending().and(Sort.by("nombre").ascending()));
    }
}
