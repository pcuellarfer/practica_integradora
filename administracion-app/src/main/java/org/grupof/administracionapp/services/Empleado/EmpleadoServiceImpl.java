package org.grupof.administracionapp.services.Empleado;


import org.grupof.administracionapp.dto.Empleado.*;
import org.grupof.administracionapp.dto.nominas.NombreApellidoEmpleadoDTO;
import org.grupof.administracionapp.entity.embeddable.CuentaCorriente;
import org.grupof.administracionapp.entity.embeddable.Direccion;
import org.grupof.administracionapp.entity.embeddable.TarjetaCredito;
import org.grupof.administracionapp.entity.registroEmpleado.*;
import org.grupof.administracionapp.services.Departamento.DepartamentoService;
import org.grupof.administracionapp.services.Especialidades.EspecialidadesService;
import org.grupof.administracionapp.services.Genero.GeneroService;
import org.grupof.administracionapp.services.Pais.PaisService;
import org.grupof.administracionapp.services.TipoDocumento.TipoDocumentoService;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.entity.Usuario;
import org.grupof.administracionapp.repository.EmpleadoRepository;
import org.grupof.administracionapp.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación del servicio {@link EmpleadoService} que gestiona
 * las operaciones CRUD relacionadas con los empleados.
 * Utiliza {@link EmpleadoRepository} y {@link UsuarioRepository} para interactuar con la base de datos.
 */
@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    private static final Logger logger = LoggerFactory.getLogger(EmpleadoServiceImpl.class);

    private final EmpleadoRepository empleadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final GeneroService generoService;
    private final PaisService paisService;
    private final TipoDocumentoService tipoDocumentoService;
    private final DepartamentoService departamentoService;
    private final EspecialidadesService especialidadesService;


    @Autowired
    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository,
                               UsuarioRepository usuarioRepository,
                               GeneroService generoService,
                               PaisService paisService,
                               TipoDocumentoService tipoDocumentoService,
                               DepartamentoService departamentoService,
                               EspecialidadesService especialidadesService) {
        this.empleadoRepository = empleadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.generoService = generoService;
        this.paisService = paisService;
        this.tipoDocumentoService = tipoDocumentoService;
        this.departamentoService = departamentoService;
        this.especialidadesService = especialidadesService;

    }

    /**
     * Guarda un nuevo empleado en la base de datos.
     *
     * @param empleado el objeto {@link Empleado} a guardar.
     */
    @Override
    public void guardarEmpleado(Empleado empleado) {
        empleadoRepository.save(empleado);
        logger.info("Empleado guardado en base de datos: {}", empleado.getId());
    }

    /**
     * Actualiza los datos de un empleado existente, incluyendo la posible actualización de la foto.
     *
     * @param usuarioId el UUID del usuario/empleado a actualizar.
     * @param dto el DTO {@link RegistroEmpleadoDTO} con los datos a actualizar.
     * @param foto archivo {@link MultipartFile} con la nueva foto (puede ser vacío).
     * @param uploadDir ruta del directorio donde se guarda la foto.
     * @throws IOException si ocurre un error al guardar la foto.
     */
    @Override
    public void actualizarDatosEmpleado(UUID usuarioId,
                                        RegistroEmpleadoDTO dto,
                                        MultipartFile foto,
                                        String uploadDir) throws IOException {
        logger.info("Actualizando datos del empleado con usuarioId: {}", usuarioId);

        Empleado empleado = empleadoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));

        if (!foto.isEmpty()) {
            logger.info("Se detectó subida de nueva foto para empleado: {}", empleado.getId());

            String tipo = foto.getContentType();
            if (!"image/png".equals(tipo) && !"image/gif".equals(tipo)) {
                logger.warn("Tipo de imagen no permitido: {}", tipo);
                throw new IllegalArgumentException("Solo se permiten imágenes PNG o GIF.");
            }

            if (foto.getSize() > 10 * 1024 * 1024) {
                logger.warn("Imagen demasiado grande ({} bytes) para empleado: {}", foto.getSize(), empleado.getId());
                throw new IllegalArgumentException("La imagen debe pesar menos de 10 MB.");
            }

            dto.setFotoBytes(foto.getBytes());
            dto.setFotoTipo(foto.getContentType());

            String extension = tipo.equals("image/png") ? ".png" : ".gif";
            String nombreArchivo = empleado.getId() + extension;

            File directorioBase = new File(uploadDir);
            String rutaAbsoluta = directorioBase.getAbsolutePath();
            Path ruta = Paths.get(rutaAbsoluta, nombreArchivo);

            Files.createDirectories(ruta.getParent());
            Files.write(ruta, dto.getFotoBytes());

            String urlFoto = "/uploads/empleados/" + nombreArchivo;
            empleado.setFotoUrl(urlFoto);
            logger.info("Foto actualizada y guardada en: {}", urlFoto);
        }

        Paso1PersonalDTO personal = dto.getPaso1PersonalDTO();
        empleado.setNombre(personal.getNombre());
        empleado.setApellido(personal.getApellido());
        empleado.setGenero(generoService.getGeneroById(personal.getGenero()));
        empleado.setFechaNacimiento(personal.getFechaNacimiento());
        empleado.setEdad(personal.getEdad());
        empleado.setPais(paisService.getPaisById(personal.getPais()));
        empleado.setComentarios(personal.getComentarios());

        empleadoRepository.save(empleado);
        logger.info("Datos del empleado {} actualizados correctamente.", empleado.getId());
    }

    /**
     * Obtiene el detalle completo de un empleado a partir del ID de usuario.
     *
     * @param usuarioId UUID del usuario/empleado.
     * @return un {@link EmpleadoDetalleDTO} con los detalles completos del empleado.
     */
    @Override
    public EmpleadoDetalleDTO obtenerDetalleEmpleado(UUID usuarioId) {
        logger.info("Obteniendo detalles del empleado con usuarioId: {}", usuarioId);

        Empleado empleadoEntidad = empleadoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));

        RegistroEmpleadoDTO dto = this.buscarEmpleadoPorUsuarioId(usuarioId);

        EmpleadoDetalleDTO detalle = new EmpleadoDetalleDTO();
        detalle.setEmpleado(dto);

        UUID idGenero = dto.getPaso1PersonalDTO().getGenero();
        detalle.setNombreGenero(generoService.obtenerNombreGenero(idGenero));

        UUID idPais = dto.getPaso1PersonalDTO().getPais();
        detalle.setNombrePais(paisService.obtenerNombrePais(idPais));

        UUID idDepartamento = dto.getPaso3ProfesionalDTO().getDepartamento();
        detalle.setNombreDepartamento(departamentoService.obtenerNombreDepartamento(idDepartamento));

        String rutaFoto = empleadoEntidad.getFotoUrl();
        if (rutaFoto != null && !rutaFoto.isBlank()) {
            String nombreArchivo = Paths.get(rutaFoto).getFileName().toString();
            detalle.setNombreFoto(nombreArchivo);
        }

        Empleado jefe = empleadoEntidad.getJefe();
        if (jefe != null) {
            String nombreJefe = jefe.getNombre() + " " + jefe.getApellido();
            detalle.setNombreJefe(nombreJefe);
        }

        logger.info("Detalle del empleado con usuarioId {} obtenido correctamente", usuarioId);
        return detalle;
    }

    /**
     * Obtiene los datos para editar el registro de un empleado.
     *
     * @param usuarioId UUID del empleado.
     * @return un {@link RegistroEmpleadoDTO} con los datos para edición.
     */
    @Override
    public RegistroEmpleadoDTO obtenerRegistroEmpleadoParaEdicion(UUID usuarioId) {
        logger.info("Obteniendo datos para edición del empleado con usuarioId: {}", usuarioId);
        return this.buscarEmpleadoPorUsuarioId(usuarioId);
    }

    /**
     * Busca empleados filtrando por nombre, género, departamentos y rango de fechas de contratación.
     *
     * @param nombre nombre o parte del nombre para filtrar (puede ser null o vacío).
     * @param generoId UUID del género para filtrar (puede ser null).
     * @param departamentoIds lista de UUIDs de departamentos para filtrar (puede ser null o vacía).
     * @param fechaInicio fecha mínima de contratación (puede ser null).
     * @param fechaFin fecha máxima de contratación (puede ser null).
     * @return lista de empleados que cumplen los filtros.
     */
    @Override
    public List<Empleado> buscarEmpleados(String nombre, UUID generoId, List<UUID> departamentoIds,
                                          LocalDate fechaInicio, LocalDate fechaFin) {
        logger.info("Buscando empleados con filtros - nombre: {}, generoId: {}, departamentos: {}, fechaInicio: {}, fechaFin: {}",
                nombre, generoId, departamentoIds, fechaInicio, fechaFin);

        List<Empleado> empleados = empleadoRepository.findAll();

        List<Empleado> filtrados = empleados.stream()
                .filter(e -> nombre == null || nombre.isBlank() ||
                        e.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .filter(e -> generoId == null ||
                        (e.getGenero() != null && e.getGenero().getId().equals(generoId)))
                .filter(e -> departamentoIds == null || departamentoIds.isEmpty() ||
                        (e.getDepartamento() != null && departamentoIds.contains(e.getDepartamento().getId())))
                .filter(e -> fechaInicio == null ||
                        (e.getFechaContratacion() != null && !e.getFechaContratacion().isBefore(fechaInicio)))
                .filter(e -> fechaFin == null ||
                        (e.getFechaContratacion() != null && !e.getFechaContratacion().isAfter(fechaFin)))
                .toList();

        logger.info("Se encontraron {} empleados que cumplen los criterios.", filtrados.size());
        return filtrados;
    }



    /**
     * Registra un nuevo empleado en el sistema a partir de los datos proporcionados en un objeto
     * {@link RegistroEmpleadoDTO} y lo asocia con un usuario existente identificado por {@link UsuarioDTO}.
     *
     * <p>El proceso de registro se divide en los siguientes pasos:
     * <ul>
     *   <li><strong>Paso 1:</strong> Datos personales como nombre, apellido, fecha de nacimiento, edad, género, país, etc.</li>
     *   <li><strong>Paso 2:</strong> Datos de contacto incluyendo documento de identidad, teléfono y dirección completa.</li>
     *   <li><strong>Paso 3:</strong> Información profesional como departamento y especialidades asignadas.</li>
     *   <li><strong>Paso 4:</strong> Información económica como salario, comisión, cuenta corriente y tarjeta de crédito.</li>
     * </ul>
     *
     * <p>Este método obtiene los datos auxiliares (género, país, tipo de documento, departamento, especialidades, etc.)
     * desde sus respectivos servicios, crea una instancia de {@link Empleado} con todos los datos y la guarda en la base de datos.
     *
     * @param registroEmpleadoDTO Objeto que contiene todos los datos del empleado estructurados por secciones (pasos).
     * @param usuarioDTO Objeto que representa al usuario al que se asociará el nuevo empleado.
     * @throws RuntimeException si el usuario indicado no existe en la base de datos.
     */
    @Override
    public void registrarEmpleado(RegistroEmpleadoDTO registroEmpleadoDTO, UsuarioDTO usuarioDTO) {
        logger.info("Iniciando registro de empleado con ID de usuario: {}", usuarioDTO.getId());

        Usuario usuario = usuarioRepository.findById(usuarioDTO.getId())
                .orElseThrow(() -> {
                    logger.error("Usuario no encontrado con ID: {}", usuarioDTO.getId());
                    return new RuntimeException("Usuario no encontrado");
                });

        Empleado empleado = new Empleado();

        // Paso 1 - Datos personales
        Paso1PersonalDTO personalDTO = registroEmpleadoDTO.getPaso1PersonalDTO();
        logger.info("Registrando datos personales para el empleado: {}", personalDTO.getNombre());

        UUID empleadoId = registroEmpleadoDTO.getEmpleadoId();
        empleado.setId(empleadoId);

        empleado.setNombre(personalDTO.getNombre());
        empleado.setApellido(personalDTO.getApellido());

        String fotoUrl = registroEmpleadoDTO.getFotoUrl();
        empleado.setFotoUrl(fotoUrl);

        empleado.setFechaNacimiento(personalDTO.getFechaNacimiento());
        empleado.setEdad(personalDTO.getEdad());
        empleado.setComentarios(personalDTO.getComentarios());

        UUID generoId = personalDTO.getGenero();
        Genero genero = generoService.getGeneroById(generoId);
        empleado.setGenero(genero);

        UUID paisId = personalDTO.getPais();
        Pais pais = paisService.getPaisById(paisId);
        empleado.setPais(pais);

        // Paso 2 - Datos de contacto
        Paso2ContactoDTO contactoDTO = registroEmpleadoDTO.getPaso2ContactoDTO();
        logger.info("Registrando datos de contacto para el empleado: {}", contactoDTO.getDocumento());

        UUID tipoDocumentoId = contactoDTO.getTipoDocumento();
        TipoDocumento tipoDocumento = tipoDocumentoService.getTipoDocumentoById(tipoDocumentoId);

        empleado.setTipoDocumento(tipoDocumento);
        empleado.setDocumento(contactoDTO.getDocumento());
        empleado.setPrefijoTelefono(contactoDTO.getPrefijoTelefono());
        empleado.setTelefono(contactoDTO.getTelefono());

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

        // Paso 3 - Datos profesionales
        Paso3ProfesionalDTO profDTO = registroEmpleadoDTO.getPaso3ProfesionalDTO();
        logger.info("Registrando datos profesionales para el empleado en el departamento ID: {}", profDTO.getDepartamento());

        UUID departamentoId = profDTO.getDepartamento();
        Departamento departamento = departamentoService.getDepartamentoById(departamentoId);
        empleado.setDepartamento(departamento);

        Set<UUID> especialidadIds = profDTO.getEspecialidades();
        Set<Especialidad> especialidades = especialidadIds.stream()
                .map(especialidadesService::getEspecialidadById)
                .collect(Collectors.toSet());

        empleado.setEspecialidades(especialidades);

        // Paso 4 - Datos económicos
        Paso4EconomicosDTO economicosDTO = registroEmpleadoDTO.getPaso4EconomicosDTO();
        logger.info("Registrando datos económicos para el empleado: {}", economicosDTO.getCuentaCorriente().getNumCuenta());

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
        logger.info("Empleado registrado correctamente con ID: {}", empleado.getId());
    }

    /**
     * Edita un empleado existente usando los datos proporcionados en el DTO.
     *
     * <p>Si el empleado con el ID dado existe, se actualizan sus datos y se guardan
     * en la base de datos. Luego, se devuelve un DTO con la información actualizada.
     * Si no se encuentra el empleado, se devuelve {@code null}.
     *
     * @param id ID del empleado a editar.
     * @param dto Datos nuevos del empleado.
     * @return DTO con los datos actualizados, o {@code null} si no se encuentra el empleado.
     */
    @Override
    public RegistroEmpleadoDTO editarEmpleado(UUID id, RegistroEmpleadoDTO dto) {
        logger.info("Iniciando edición de empleado con ID: {}", id);

        Optional<Empleado> opt = empleadoRepository.findById(id);
        if (opt.isEmpty()) {
            logger.warn("Empleado con ID: {} no encontrado", id);
            return null;
        }

        Empleado empleado = opt.get();
        logger.info("Empleado encontrado con ID: {}. Actualizando datos...", id);

        // Usar BeanUtils para copiar propiedades
        BeanUtils.copyProperties(dto, empleado);
        Empleado actualizado = empleadoRepository.save(empleado);

        logger.info("Empleado con ID: {} actualizado exitosamente", id);

        RegistroEmpleadoDTO resultado = new RegistroEmpleadoDTO();
        BeanUtils.copyProperties(actualizado, resultado);

        logger.info("Resultado de la edición de empleado con ID: {} preparado", id);
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
        logger.info("Iniciando eliminación de empleado con ID: {}", id);

        // Verificar si el empleado existe
        if (!empleadoRepository.existsById(id)) {
            logger.warn("Empleado con ID: {} no encontrado para eliminar", id);
            return false;
        }

        // Eliminar el empleado
        empleadoRepository.deleteById(id);
        logger.info("Empleado con ID: {} eliminado exitosamente", id);
        return true;
    }

    /**
     * Busca un empleado por su identificador.
     *
     * @param id Identificador del empleado.
     * @return DTO del empleado encontrado, o null si no existe.
     */
    @Override
    public RegistroEmpleadoDTO buscarRegistroEmpleado(UUID id) {
        logger.info("Buscando empleado con ID: {}", id);
        return empleadoRepository.findById(id)
                .map(emp -> {
                    logger.info("Empleado con ID: {} encontrado", id);
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

    @Override //una lista con el id, nombre y apellido de todos los empleados
    public List<NombreApellidoEmpleadoDTO> obtenerNombreYApellidoEmpleados() {
        return empleadoRepository.findAll().stream()
                .map(emp -> new NombreApellidoEmpleadoDTO(emp.getId(), emp.getNombre(), emp.getApellido()))
                .toList();
    }

    /**
     * Busca un empleado en el sistema a partir del identificador de usuario (UUID)
     * y construye un objeto {@link RegistroEmpleadoDTO} que contiene los datos
     * personales, de contacto, profesionales y económicos del empleado.
     *
     * <p>El DTO resultante se compone de cuatro secciones:
     * <ul>
     *   <li>{@link Paso1PersonalDTO}: Datos personales como nombre, apellido, edad, género, país, etc.</li>
     *   <li>{@link Paso2ContactoDTO}: Información de contacto, documento y dirección completa.</li>
     *   <li>{@link Paso3ProfesionalDTO}: Departamento y especialidades asignadas.</li>
     *   <li>{@link Paso4EconomicosDTO}: Información económica, cuenta corriente y tarjeta de crédito.</li>
     * </ul>
     *
     * @param usuarioId El UUID que identifica al usuario vinculado al empleado.
     * @return Un objeto {@link RegistroEmpleadoDTO} con toda la información del empleado
     *         si se encuentra uno asociado al usuario, o {@code null} si no se encuentra.
     */
    @Override
    public RegistroEmpleadoDTO buscarEmpleadoPorUsuarioId(UUID usuarioId) {
        logger.info("Buscando empleado con usuarioId: {}", usuarioId);

        Optional<Empleado> empleadoOpt = empleadoRepository.findByUsuarioId(usuarioId);

        if (empleadoOpt.isPresent()) {
            logger.info("Empleado encontrado con usuarioId: {}", usuarioId);

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

            logger.info("Empleado con usuarioId: {} encontrado y mapeado correctamente", usuarioId);
            return dto;
        }

        logger.warn("No se encontró empleado con usuarioId: {}", usuarioId);
        return null;
    }

    @Override
    public Optional<Empleado> obtenerEmpleadoPorUsuarioId(UUID usuarioId) {
        return empleadoRepository.findByUsuarioId(usuarioId);
    }

    //evitar jefes circulares

    public Set<UUID> obtenerIdsSubordinadosRecursivos(Empleado jefe) {
        Set<UUID> ids = new HashSet<>(); //crea un set vacío de IDs

        // función recursiva integrada directamente
        for (Empleado subordinado : jefe.getSubordinados()) { //recorre cada subordinado del jefe
            if (ids.add(subordinado.getId())) { //si el ID no estaba, lo añade
                ids.addAll(obtenerIdsSubordinadosRecursivos(subordinado)); //añade también los subordinados de ese subordinado
            }
        }

        return ids; // devuelve el set con todos los IDs únicos
    }

    public Set<UUID> obtenerIdsJefesRecursivos(Empleado empleado) {
        Set<UUID> ids = new HashSet<>(); //crea un set vacio para UUIDs
        Empleado actual = empleado.getJefe(); //mete al jefe del empleado en una variable
        while (actual != null) { //mientras siga habiendo jefe,es decir, no hemos llegado al jefe jefazo
            ids.add(actual.getId()); //mete al set el id del jefe 1
            actual = actual.getJefe();//mete a actual su jefe, es decir, el jefe del jefe
        }
        return ids;
    }

    @Override
    public List<Empleado> buscarTodosMenosConJerarquia(UUID jefeId){
        Empleado jefe = empleadoRepository.findById(jefeId) //mete el empleado en una variable
                .orElseThrow(() -> new IllegalArgumentException("Jefe no encontrado"));//si no esta, excepcion

        //mete todos los ids que no deberian estar
        Set<UUID> idsProhibidos = obtenerIdsSubordinadosRecursivos(jefe); // los de abajo
        idsProhibidos.add(jefe.getId()); // él mismo
        idsProhibidos.addAll(obtenerIdsJefesRecursivos(jefe)); // los de arriba

        List<Empleado> todos = empleadoRepository.findAll(); //mete en una lista todos los empleados
        todos.removeIf(emp -> idsProhibidos.contains(emp.getId())); //elimina los prohibidos

        return todos; //alfinal devuelve una lista de empleados sin: el mismo, sus subordinados y sus jefes
    }
    /////////


    @Override
    public List<Empleado> buscarPorIds(List<UUID> ids) {
        return empleadoRepository.findAllById(ids);
    }

    @Override
    public void guardarTodos(List<Empleado> empleados) {
        empleadoRepository.saveAll(empleados);
    }

    @Override
    public List<Empleado> buscarPorJefe(Empleado jefe) {
        return empleadoRepository.findByJefe(jefe);
    }

    public Optional<Empleado> buscarPorId(UUID id) {
        return empleadoRepository.findById(id);
    }

    /**
     * Bloquea un empleado en la base de datos estableciendo su estado de usuario como bloqueado.
     * <p>
     * Este método busca al empleado en la base de datos por su ID. Si el empleado existe,
     * se recupera su usuario asociado y se marca como bloqueado. Si no se encuentra al
     * empleado o el usuario asociado, se lanza una excepción.
     *
     * @param empleadoId el identificador único del empleado que se desea bloquear
     * @throws RuntimeException si el empleado no se encuentra en la base de datos
     *                          o si el empleado no tiene un usuario asociado
     */
    @Override
    public void bloquearEmpleado(UUID empleadoId, String motivoBloqueo) {
        logger.info("Intentando bloquear al empleado con ID: {}", empleadoId);

        Optional<Empleado> empleadoOpt = empleadoRepository.findById(empleadoId);
        LocalDateTime fechaActual = LocalDateTime.now(ZoneId.of("Europe/Madrid"));

        if (empleadoOpt.isPresent()) {
            Empleado empleado = empleadoOpt.get();
            Usuario usuario = empleado.getUsuario();

            if (usuario != null) {
                logger.info("Empleado con ID: {} encontrado. Bloqueando usuario asociado...", empleadoId);
                usuario.setEstadoBloqueado(true);
                usuario.setBloqueadoHasta(fechaActual.plusSeconds(30)); // Bloqueo por 30 segundos
                usuario.setMotivoBloqueo(motivoBloqueo);
                usuarioRepository.save(usuario);
                logger.info("Usuario con ID: {} bloqueado correctamente", usuario.getId());
            } else {
                logger.error("Bloqueo: El empleado con ID: {} no tiene un usuario asociado", empleadoId);
                throw new RuntimeException("El empleado no tiene un usuario asociado");
            }
        } else {
            logger.error("Excepcion bloqueo: Empleado con ID: {} no encontrado", empleadoId);
            throw new RuntimeException("Empleado no encontrado");
        }
    }

    /**
     * Desbloquea un empleado en la base de datos estableciendo su estado de usuario como no bloqueado.
     * <p>
     * Este método busca al empleado en la base de datos por su ID. Si el empleado existe,
     * se recupera su usuario asociado y se marca como no bloqueado. Si no se encuentra al
     * empleado o el usuario asociado, se lanza una excepción.
     *
     * @param empleadoId el identificador único del empleado que se desea desbloquear
     * @throws RuntimeException si el empleado no se encuentra en la base de datos
     *                          o si el empleado no tiene un usuario asociado
     */
    @Override
    public void desbloquearEmpleado(UUID empleadoId) {
        logger.info("Intentando desbloquear al empleado con ID: {}", empleadoId);

        Optional<Empleado> empleadoOpt = empleadoRepository.findById(empleadoId);

        if (empleadoOpt.isPresent()) {
            Empleado empleado = empleadoOpt.get();
            Usuario usuario = empleado.getUsuario();

            if (usuario != null) {
                logger.info("Empleado con ID: {} encontrado. Desbloqueando usuario asociado...", empleadoId);
                usuario.setEstadoBloqueado(false);
                usuarioRepository.save(usuario);
                logger.info("Usuario con ID: {} desbloqueado correctamente", usuario.getId());
            } else {
                logger.error("Desbloqueo: El empleado con ID: {} no tiene un usuario asociado", empleadoId);
                throw new RuntimeException("El empleado no tiene un usuario asociado");
            }
        } else {
            logger.error("Excepcion desbloqueo: Empleado con ID: {} no encontrado", empleadoId);
            throw new RuntimeException("Empleado no encontrado");
        }
    }

    /**
     * Recupera todos los empleados ordenados por apellido y, en caso de apellidos iguales,
     * por nombre en orden ascendente.
     *
     * @return una lista de empleados ordenados alfabéticamente por apellido y nombre.
     */
    @Override
    public List<Empleado> getEmpleadosOrdenados() {
        logger.info("Obteniendo lista de empleados ordenada por apellido y nombre...");
        List<Empleado> empleadosOrdenados = empleadoRepository.findAll(
                Sort.by("apellido").ascending().and(Sort.by("nombre").ascending()));
        logger.debug("Se han recuperado {} empleados.", empleadosOrdenados.size());
        return empleadosOrdenados;
    }

    /**
     * Verifica si el usuario asociado al empleado con el ID proporcionado está bloqueado.
     *
     * @param empleadoId el identificador único del empleado.
     * @return {@code true} si el usuario está bloqueado, {@code false} si no lo está.
     * @throws RuntimeException si no se encuentra el empleado o si no tiene un usuario asociado.
     */
    @Override
    public boolean obtenerEstadoEmpleado(UUID empleadoId) {
        logger.info("Consultando el estado de bloqueo del empleado con ID: {}", empleadoId);

        Optional<Empleado> empleadoOpt = empleadoRepository.findById(empleadoId);

        if (empleadoOpt.isPresent()) {
            Empleado empleado = empleadoOpt.get();
            Usuario usuario = empleado.getUsuario();

            if (usuario != null) {
                boolean estado = usuario.isEstadoBloqueado();
                logger.info("El estado de bloqueo del usuario con ID {} es: {}", usuario.getId(), estado);
                return estado;
            } else {
                logger.error("El empleado con ID {} no tiene un usuario asociado", empleadoId);
                throw new RuntimeException("El empleado no tiene un usuario asociado");
            }
        } else {
            logger.error("Empleado no encontrado con ID: {}", empleadoId);
            throw new RuntimeException("Empleado no encontrado");
        }
    }

}
