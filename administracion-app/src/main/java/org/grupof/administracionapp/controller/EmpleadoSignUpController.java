package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.grupof.administracionapp.dto.Empleado.*;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.TipoTarjeta;
import org.grupof.administracionapp.entity.embeddable.CuentaCorriente;
import org.grupof.administracionapp.entity.embeddable.Direccion;
import org.grupof.administracionapp.entity.embeddable.TarjetaCredito;
import org.grupof.administracionapp.entity.registroEmpleado.*;
import org.grupof.administracionapp.services.Departamento.DepartamentoService;
import org.grupof.administracionapp.services.Empleado.EmpleadoService;
import org.grupof.administracionapp.services.Especialidades.EspecialidadesService;
import org.grupof.administracionapp.services.Genero.GeneroService;
import org.grupof.administracionapp.services.Pais.PaisService;
import org.grupof.administracionapp.services.TipoDocumento.TipoDocumentoService;
import org.grupof.administracionapp.services.TipoTarjetaService.TipoTarjetaService;
import org.grupof.administracionapp.services.TipoVia.TipoViaService;
import org.grupof.administracionapp.services.banco.BancoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controlador que gestiona el proceso de registro de un nuevo empleado
 * a través de un formulario dividido en varios pasos.
 * Cada paso recoge información distinta y la guarda en sesión
 * hasta su almacenamiento definitivo.
 * Usa los servicios correspondientes para poblar datos de selección y guardar el resultado final.
 */
@Controller
@RequestMapping("/registro")
@SessionAttributes({"registroEmpleado", "usuario"})
public class EmpleadoSignUpController {

    private static final Logger logger = LoggerFactory.getLogger(EmpleadoSignUpController.class);

    private final PaisService paisService;
    private final GeneroService generoService;
    private final TipoDocumentoService tipoDocumentoService;
    private final DepartamentoService departamentoService;
    private final BancoService bancoService;
    private final TipoTarjetaService tipoTarjetaService;
    private final EmpleadoService empleadoService;
    private final TipoViaService tipoViaService;
    private final EspecialidadesService especialidadesService;

    public EmpleadoSignUpController(PaisService paisService,
                                    GeneroService generoService,
                                    TipoDocumentoService tipoDocumentoService,
                                    DepartamentoService departamentoService,
                                    BancoService bancoService,
                                    TipoTarjetaService tipoTarjetaService,
                                    EmpleadoService empleadoService,
                                    TipoViaService tipoViaService,
                                    EspecialidadesService especialidadesService) {
        this.paisService = paisService;
        this.generoService = generoService;
        this.tipoDocumentoService = tipoDocumentoService;
        this.departamentoService = departamentoService;
        this.bancoService = bancoService;
        this.tipoTarjetaService = tipoTarjetaService;
        this.empleadoService = empleadoService;
        this.tipoViaService = tipoViaService;
        this.especialidadesService = especialidadesService;
    }

    /**
     * Inicializa un nuevo objeto {@link RegistroEmpleadoDTO} vacío para la sesión.
     *
     * @return una nueva instancia de RegistroEmpleadoDTO
     */
    @ModelAttribute("registroEmpleado")
    public RegistroEmpleadoDTO registroEmpleadoDTO() {
        return new RegistroEmpleadoDTO();
    }

    /**
     * Recupera el usuario en sesión si existe.
     *
     * @param usuario el usuario en sesión, puede ser null
     * @return el DTO del usuario
     */
    @ModelAttribute("usuario")
    public UsuarioDTO getUsuario(@SessionAttribute(value = "usuario", required = false) UsuarioDTO usuario) {
        return usuario;
    }

    /**
     * Muestra el formulario del Paso 1: Datos personales.
     *
     * @param modelo             el modelo para la vista
     * @param usuario            el usuario en sesión
     * @param redirectAttributes atributos para redirección en caso de error
     * @param session            la sesión HTTP actual
     * @return la vista correspondiente o redirección
     */
    @GetMapping("/empleado")
    public String mostrarPaso1(Model modelo,
                               @SessionAttribute(value = "usuario", required = false) UsuarioDTO usuario,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {

        Boolean autenticado = (Boolean) session.getAttribute("autenticado");
        if (usuario == null || autenticado == null || !autenticado) {
            logger.warn("Intento de registro de empleado sin usuario en sesión.");
            //se usa redirectAttributes y addflashAttribute porque con model.addatribute no se guarda entre redirecciones
            redirectAttributes.addFlashAttribute("error",
                    "Estabas intentando registrar un empleado sin usuario. Te hemos redirigido para que registres un usuario.");
            return "redirect:/registro/usuario";
        }

        RegistroEmpleadoDTO registroEmpleado = (RegistroEmpleadoDTO) session.getAttribute("registroEmpleado");
        if (registroEmpleado == null) {
            registroEmpleado = new RegistroEmpleadoDTO();
            registroEmpleado.setPaso1PersonalDTO(new Paso1PersonalDTO());
            session.setAttribute("registroEmpleado", registroEmpleado); // opcional si quieres garantizar que ya esté en sesión
        }

        modelo.addAttribute("paso1", registroEmpleado.getPaso1PersonalDTO());

        logger.info("Mostrando formulario de datos personales para usuario ID: {}", usuario.getId());
        modelo.addAttribute("paso1", new Paso1PersonalDTO());
        modelo.addAttribute("paises", paisService.getAllPaises());
        modelo.addAttribute("generos", generoService.getAllGeneros());

        return "empleado/auth/FormDatosPersonales";
    }

    /**
     * Procesa los datos personales ingresados en el paso 1 del formulario de registro de empleado.
     *
     * @param paso1 DTO con los datos personales del empleado validados.
     * @param errores Contiene errores de validación del formulario.
     * @param registroEmpleado Objeto del empleado en registro en proceso.
     * @param modelo Modelo para pasar atributos a la vista.
     * @param usuarioDTO Usuario actualmente en sesión.
     * @param foto Imagen de perfil enviada en el formulario.
     * @return Redirección al siguiente paso si es exitoso, o vuelve a la vista actual si hay errores.
     */
    @PostMapping("/paso1")
    public String procesarPaso2(
            @ModelAttribute("paso1") @Valid Paso1PersonalDTO paso1,
            BindingResult errores,
            @ModelAttribute("registroEmpleado") RegistroEmpleadoDTO registroEmpleado,
            Model modelo,
            @SessionAttribute(value = "usuario", required = false) UsuarioDTO usuarioDTO,
            @RequestParam("foto") MultipartFile foto,
            HttpSession session) {

        logger.info("Procesando paso 1 del formulario para usuario ID: {}",
                usuarioDTO != null ? usuarioDTO.getId() : "desconocido");

        //esta validacion de foto solo sale si todas las demas validaciones del campo 1 estan correctas
        if (errores.hasErrors()) {
            logger.warn("Errores de validación en formulario de datos personales para usuario ID: {}",
                    usuarioDTO != null ? usuarioDTO.getId() : "desconocido");
            modelo.addAttribute("paises", paisService.getAllPaises());
            modelo.addAttribute("generos", generoService.getAllGeneros());
            return "empleado/auth/FormDatosPersonales";
        }

        if (foto == null || foto.isEmpty()) {
            logger.warn("No se subió una imagen para usuario ID: {}",
                    usuarioDTO != null ? usuarioDTO.getId() : "desconocido");
            modelo.addAttribute("errorFoto", "Debe subir una foto.");
            modelo.addAttribute("paises", paisService.getAllPaises());
            modelo.addAttribute("generos", generoService.getAllGeneros());
            return "empleado/auth/FormDatosPersonales";
        }

        String tipo = foto.getContentType();
        if (!"image/png".equals(tipo) && !"image/gif".equals(tipo)) {
            logger.warn("Imagen con tipo no permitido: {} para usuario ID: {}", tipo,
                    usuarioDTO != null ? usuarioDTO.getId() : "desconocido");
            modelo.addAttribute("errorFoto", "Solo se permiten imágenes PNG o GIF.");
            modelo.addAttribute("paises", paisService.getAllPaises());
            modelo.addAttribute("generos", generoService.getAllGeneros());
            return "empleado/auth/FormDatosPersonales";
        }

        if (foto.getSize() > 10 * 1024 * 1024) {
            logger.warn("Imagen demasiado grande ({} bytes) para usuario ID: {}", foto.getSize(),
                    usuarioDTO != null ? usuarioDTO.getId() : "desconocido");
            modelo.addAttribute("errorFoto", "La imagen debe pesar menos de 10 MB.");
            modelo.addAttribute("paises", paisService.getAllPaises());
            modelo.addAttribute("generos", generoService.getAllGeneros());
            return "empleado/auth/FormDatosPersonales";
        }

        try {
            registroEmpleado.setFotoBytes(foto.getBytes());
            registroEmpleado.setFotoTipo(tipo);
            logger.info("Imagen subida correctamente para usuario ID: {}",
                    usuarioDTO != null ? usuarioDTO.getId() : "desconocido");
        } catch (IOException e) {
            logger.error("Error al leer la imagen para usuario ID: {}",
                    usuarioDTO != null ? usuarioDTO.getId() : "desconocido", e);
            errores.rejectValue("foto", "foto.error", "Error al procesar la imagen.");
            return "empleado/auth/FormDatosPersonales";
        }

        UUID empleadoId = UUID.randomUUID();
        registroEmpleado.setEmpleadoId(empleadoId);
        registroEmpleado.setPaso1PersonalDTO(paso1);
        session.setAttribute("registroEmpleado", registroEmpleado);
        logger.info("Paso 1 completado exitosamente para nuevo empleado ID: {}", empleadoId);

        return "redirect:/registro/paso2";
    }

    /**
     * Muestra el formulario del segundo paso del registro de un empleado, correspondiente
     * a los datos de contacto. Este método verifica que haya un usuario en la sesión.
     * Si no lo hay, redirige al formulario de registro de usuario con un mensaje de error.
     *
     * @param modelo             el modelo de Spring para pasar atributos a la vista
     * @param usuario            el objeto {@link UsuarioDTO} obtenido de la sesión, puede ser null si no está presente
     * @param redirectAttributes objeto para añadir atributos flash en caso de redirección
     * @return la vista del formulario de contacto o redirección al registro de usuario si no hay sesión válida
     */
    @GetMapping("/paso2")
    public String mostrarPaso2(Model modelo, @SessionAttribute(value = "usuario", required = false) UsuarioDTO usuario,
                               RedirectAttributes redirectAttributes, HttpSession session) {

        Boolean autenticado = (Boolean) session.getAttribute("autenticado");

        if (usuario == null || autenticado == null || !autenticado) {
            logger.warn("Intento de acceso a paso 2 sin usuario en sesión.");
            redirectAttributes.addFlashAttribute("error", "Estabas intentando registrar un empleado sin usuario.");
            return "redirect:/registro/usuario";
        }

        logger.info("Mostrando formulario de contacto para usuario ID: {}", usuario.getId());
        Paso2ContactoDTO paso2 = new Paso2ContactoDTO();
        paso2.setDireccion(new Direccion());
        modelo.addAttribute("paso2", paso2);
        modelo.addAttribute("paises", paisService.getAllPaises());
        modelo.addAttribute("tiposVias", tipoViaService.getAllTipoVia());
        modelo.addAttribute("documentos", tipoDocumentoService.getAllTipoDocumento());
        return "empleado/auth/FormDatosContacto";
    }

    /**
     * Procesa los datos de contacto enviados en el segundo paso del registro de un empleado.
     * Si el formulario contiene errores de validación, se vuelve a mostrar el mismo formulario.
     * Si los datos son válidos, se almacenan en el objeto de registro y se redirige al paso 3.
     *
     * @param paso2            el DTO que contiene los datos del formulario de contacto
     * @param registroEmpleado el DTO que almacena los datos del registro del empleado en múltiples pasos
     * @param errores          objeto que contiene errores de validación del formulario
     * @param usuario          el objeto {@link UsuarioDTO} obtenido de la sesión, puede ser null si no está presente
     * @return redirección al paso 3 del registro si no hay errores, o el formulario de contacto nuevamente si hay errores
     */
    @PostMapping("/paso2")
    public String procesarPaso2(
            @ModelAttribute("paso2") @Valid Paso2ContactoDTO paso2,
            BindingResult errores,
            @ModelAttribute("registroEmpleado") RegistroEmpleadoDTO registroEmpleado,
            Model modelo,
            @SessionAttribute(value = "usuario", required = false) UsuarioDTO usuario) {

        if (errores.hasErrors()) {
            modelo.addAttribute("paises", paisService.getAllPaises());
            modelo.addAttribute("tiposVias", tipoViaService.getAllTipoVia());
            modelo.addAttribute("documentos", tipoDocumentoService.getAllTipoDocumento());
            logger.warn("Errores en formulario de contacto para usuario ID: {}", usuario.getId());
            return "empleado/auth/FormDatosContacto";
        }

        logger.info("Datos de contacto registrados para usuario ID: {}", usuario.getId());
        registroEmpleado.setPaso2ContactoDTO(paso2);
        return "redirect:/registro/paso3";
    }

    /**
     * Muestra el formulario del tercer paso del registro de un empleado, correspondiente
     * a los datos profesionales. Verifica que exista un usuario en la sesión antes de mostrar el formulario.
     * Si no hay un usuario válido, redirige al formulario de registro de usuario con un mensaje de error.
     *
     * @param modelo             el modelo de Spring utilizado para pasar atributos a la vista
     * @param usuario            el objeto {@link UsuarioDTO} obtenido de la sesión, puede ser null si no está presente
     * @param redirectAttributes objeto para añadir atributos flash en caso de redirección
     * @param session            la sesión HTTP actual
     * @return la vista del formulario de datos profesionales o una redirección al registro de usuario
     */
    @GetMapping("/paso3")
    public String mostrarPaso3(Model modelo, @SessionAttribute(value = "usuario", required = false) UsuarioDTO usuario,
                               RedirectAttributes redirectAttributes,  HttpSession session) {
        Boolean autenticado = (Boolean) session.getAttribute("autenticado");

        if (usuario == null || autenticado == null || !autenticado) {
            logger.warn("Intento de acceso a paso 2 sin usuario en sesión. /paso3");
            redirectAttributes.addFlashAttribute("error", "Estabas intentando registrar un empleado sin usuario.");
            return "redirect:/registro/usuario";
        }

        logger.info("Mostrando formulario profesional para usuario ID: {}", usuario.getId());
        modelo.addAttribute("paso3", new Paso3ProfesionalDTO());
        modelo.addAttribute("departamentos", departamentoService.getAllDepartamentos());
        modelo.addAttribute("especialidades", especialidadesService.getAllEspecialidades());
        return "empleado/auth/FormDatosProfesionales";
    }

    /**
     * Procesa los datos profesionales enviados en el tercer paso del registro de un empleado.
     * Si hay errores de validación, se vuelve a mostrar el formulario con los errores.
     * Si los datos son válidos, se almacenan en el objeto de registro y se redirige al paso 4.
     *
     * @param paso3            el DTO que contiene los datos profesionales ingresados por el usuario
     * @param registroEmpleado el DTO que acumula los datos del registro del empleado en distintos pasos
     * @param errores          objeto que contiene los errores de validación del formulario
     * @param usuario          el objeto {@link UsuarioDTO} obtenido de la sesión, puede ser null si no está presente
     * @return redirección al paso 4 del registro si no hay errores, o el formulario profesional nuevamente si hay errores
     */
    @PostMapping("/paso3")
    public String procesarPaso3(
            @ModelAttribute("paso3") @Valid Paso3ProfesionalDTO paso3,
            BindingResult errores,
            @ModelAttribute("registroEmpleado") RegistroEmpleadoDTO registroEmpleado,
            Model modelo,
            @SessionAttribute(value = "usuario", required = false) UsuarioDTO usuario) {

        if (errores.hasErrors()) {
            modelo.addAttribute("departamentos", departamentoService.getAllDepartamentos());
            modelo.addAttribute("especialidades", especialidadesService.getAllEspecialidades());
            logger.warn("Errores en formulario profesional para usuario ID: {}", usuario.getId());
            return "empleado/auth/FormDatosProfesionales";
        }

        logger.info("Datos profesionales registrados para usuario ID: {}", usuario.getId());
        registroEmpleado.setPaso3ProfesionalDTO(paso3);
        return "redirect:/registro/paso4";
    }

    /**
     * Muestra el formulario del cuarto paso del registro de un empleado, correspondiente
     * a los datos económicos. Verifica que exista un usuario en la sesión antes de mostrar el formulario.
     * Si no hay un usuario válido, redirige al formulario de registro de usuario con un mensaje de error.
     *
     * @param modelo             el modelo de Spring utilizado para pasar atributos a la vista
     * @param usuario            el objeto {@link UsuarioDTO} obtenido de la sesión, puede ser null si no está presente
     * @param redirectAttributes objeto para añadir atributos flash en caso de redirección
     * @param session            la sesión HTTP actual
     * @return la vista del formulario de datos económicos o una redirección al registro de usuario
     */
    @GetMapping("/paso4")
    public String mostrarPaso4(Model modelo, @SessionAttribute(value = "usuario", required = false) UsuarioDTO usuario,
                               RedirectAttributes redirectAttributes, HttpSession session) {
        Boolean autenticado = (Boolean) session.getAttribute("autenticado");

        if (usuario == null || autenticado == null || !autenticado) {
            logger.warn("Intento de acceso a paso 2 sin usuario en sesión. /paso4");
            redirectAttributes.addFlashAttribute("error", "Estabas intentando registrar un empleado sin usuario.");
            return "redirect:/registro/usuario";
        }

        logger.info("Mostrando formulario económico para usuario ID: {}", usuario.getId());
        Paso4EconomicosDTO paso4 = new Paso4EconomicosDTO();
        paso4.setCuentaCorriente(new CuentaCorriente());
        paso4.setTarjetaCredito(new TarjetaCredito());
        modelo.addAttribute("paso4", paso4);
        modelo.addAttribute("bancos", bancoService.getAllBancos());
        modelo.addAttribute("tiposTarjeta", tipoTarjetaService.getAllTiposTarjetas());
        return "empleado/auth/FormDatosEconomicos";
    }

    /**
     * Procesa los datos económicos enviados en el cuarto paso del registro de un empleado.
     * Si hay errores de validación, se vuelve a mostrar el formulario con los errores.
     * Si los datos son válidos, se almacenan en el objeto de registro y se redirige al paso 5.
     *
     * @param paso4            el DTO que contiene los datos económicos ingresados por el usuario
     * @param registroEmpleado el DTO que acumula los datos del registro del empleado en distintos pasos
     * @param errores          objeto que contiene los errores de validación del formulario
     * @param usuario          el objeto {@link UsuarioDTO} obtenido de la sesión, puede ser null si no está presente
     * @return redirección al paso 5 del registro si no hay errores, o el formulario económico nuevamente si hay errores
     */
    @PostMapping("/paso4")
    public String procesarPaso4(
            @ModelAttribute("paso4") @Valid Paso4EconomicosDTO paso4,
            BindingResult errores,
            @ModelAttribute("registroEmpleado") RegistroEmpleadoDTO registroEmpleado,
            Model modelo,
            @SessionAttribute(value = "usuario", required = false) UsuarioDTO usuario) {

        if (errores.hasErrors()) {
            System.err.println(errores);
            modelo.addAttribute("bancos", bancoService.getAllBancos());
            modelo.addAttribute("tiposTarjeta", tipoTarjetaService.getAllTiposTarjetas());
            logger.warn("Errores en formulario económico para usuario ID: {}", usuario.getId());
            return "empleado/auth/FormDatosEconomicos";
        }

        logger.info("Datos económicos registrados para usuario ID: {}", usuario.getId());
        registroEmpleado.setPaso4EconomicosDTO(paso4);
        return "redirect:/registro/paso5";
    }

    /**
     * Muestra un resumen de todos los datos ingresados durante el proceso de registro del empleado.
     * Verifica que exista un usuario en la sesión antes de mostrar el resumen. Si no hay un usuario válido,
     * redirige al formulario de registro de usuario con un mensaje de error.
     * Este método reúne los datos de cada paso del registro (personal, contacto, profesional, económico)
     * y los prepara para ser mostrados en la vista de resumen.
     *
     * @param registroEmpleado   el DTO que acumula los datos del registro del empleado en distintos pasos
     * @param modelo             el modelo de Spring utilizado para pasar atributos a la vista
     * @param usuario            el objeto {@link UsuarioDTO} obtenido de la sesión, puede ser null si no está presente
     * @param redirectAttributes objeto para añadir atributos flash en caso de redirección
     * @param session            la sesión HTTP actual
     * @return la vista del resumen del registro del empleado o una redirección al registro de usuario si no hay sesión válida
     */
    @GetMapping("/paso5")
    public String mostrarPaso5(@ModelAttribute("registroEmpleado") RegistroEmpleadoDTO registroEmpleado,
                               Model modelo,
                               @SessionAttribute(value = "usuario", required = false) UsuarioDTO usuario,
                               RedirectAttributes redirectAttributes, HttpSession session) {

        Boolean autenticado = (Boolean) session.getAttribute("autenticado");

        if (usuario == null || autenticado == null || !autenticado) {
            logger.warn("Intento de acceso a paso 2 sin usuario en sesión.");
            redirectAttributes.addFlashAttribute("error", "Estabas intentando registrar un empleado sin usuario.");
            return "redirect:/registro/usuario";
        }

        logger.info("Mostrando resumen de registro para usuario ID: {}", usuario.getId());

        //paso 1 - personales
        UUID generoId = registroEmpleado.getPaso1PersonalDTO().getGenero();
        UUID paisId = registroEmpleado.getPaso1PersonalDTO().getPais();

        Genero genero = generoService.getGeneroById(generoId);
        Pais pais = paisService.getPaisById(paisId);

        //paso 2 - contacto

        UUID tipoDocumentoId = registroEmpleado.getPaso2ContactoDTO().getTipoDocumento();
        TipoDocumento tipoDocumento = tipoDocumentoService.getTipoDocumentoById(tipoDocumentoId);

        UUID tipoViaId = registroEmpleado.getPaso2ContactoDTO().getDireccion().getTipoVia();
        TipoVia tipoVia = tipoViaService.getTipoViaById(tipoViaId);

        //paso 3 - profesionales
        UUID departamentoId = registroEmpleado.getPaso3ProfesionalDTO().getDepartamento();
        Departamento departamento = departamentoService.getDepartamentoById(departamentoId);

        Set<UUID> especialidadIds = registroEmpleado.getPaso3ProfesionalDTO().getEspecialidades();
        Set<Especialidad> especialidades = especialidadIds.stream()
                .map(especialidadesService::getEspecialidadById)
                .collect(Collectors.toSet());

        //paso 4 - economicos
        UUID bancoId = registroEmpleado.getPaso4EconomicosDTO().getCuentaCorriente().getBanco();
        Banco banco = bancoService.GetBancoById(bancoId);

        UUID tipoTarjetaId = registroEmpleado.getPaso4EconomicosDTO().getTarjetaCredito().getTipoTarjeta();
        TipoTarjeta tipoTarjeta = tipoTarjetaService.getTipoTarjetaById(tipoTarjetaId);

        //añadir al modelo los campor para vista resumne
        modelo.addAttribute("genero", genero);
        modelo.addAttribute("pais", pais);
        modelo.addAttribute("tipoDocumento", tipoDocumento);
        modelo.addAttribute("tipoVia", tipoVia);
        modelo.addAttribute("departamento", departamento);
        modelo.addAttribute("especialidades", especialidades);
        modelo.addAttribute("banco", banco);
        modelo.addAttribute("tipoTarjeta", tipoTarjeta);
        modelo.addAttribute("paso5", registroEmpleado);
        return "empleado/auth/Resumen";
    }


    //meter lo que hay en e l aplication properties en una variable
    @Value("${app.upload.dir}")
    private String uploadDir;


    /**
     * Procesa el envío final del registro de un empleado. Este método verifica que exista un usuario en la sesión
     * antes de procesar el registro. Si no hay un usuario válido, redirige al formulario de registro de usuario.
     * Si el registro es válido, guarda los datos del empleado y redirige al dashboard.
     *
     * @param registroEmpleadoDTO el DTO que contiene todos los datos del registro del empleado recopilados a lo largo de los pasos
     * @param usuarioDTO          el objeto {@link UsuarioDTO} obtenido de la sesión, puede ser null si no está presente
     * @return una redirección al dashboard si el registro es exitoso, o al formulario de registro de usuario si no hay sesión válida
     */
    @PostMapping("/paso5")
    public String procesarPaso5(@ModelAttribute("registroEmpleado") RegistroEmpleadoDTO registroEmpleadoDTO,
                                @SessionAttribute(value = "usuario", required = false) UsuarioDTO usuarioDTO) {

        if (usuarioDTO == null) {
            logger.warn("Intento de envío final de registro sin usuario.");
            return "redirect:/registro/usuario";
        }

        byte[] fotoBytes = registroEmpleadoDTO.getFotoBytes();
        String tipo = registroEmpleadoDTO.getFotoTipo();//guardar el tipo de la foto en una variable

        if (fotoBytes != null && fotoBytes.length > 0 && tipo != null) {
            try {
                String extension = tipo.equals("image/png") ? ".png" : ".gif"; //guardar en variable la extension de la imagen
                String nombreArchivo = registroEmpleadoDTO.getEmpleadoId() + extension;  //juntar el uuid y la extension para formar el nombre del archivoi

                File directorioBase = new File(uploadDir); //crear un objeto file que representa donde se guardan los archivos
                String rutaAbsoluta = directorioBase.getAbsolutePath(); //pillar la ruta absolita de la variable directorioBase
                Path ruta = Paths.get(rutaAbsoluta, nombreArchivo); //crear la ruta buena de rutaAbsoluta+nombre del archivo

                Files.createDirectories(ruta.getParent()); //crea el directorio si no existe(esta tambien en datosIniciales, habra que ver con cual quedarse)
                Files.write(ruta, fotoBytes); //guarda foto en ruta

                registroEmpleadoDTO.setFotoUrl("/uploads/empleados/" + nombreArchivo);//meter al dto la url de su foto

            } catch (IOException e) {
                logger.error("Error al guardar la foto del empleado", e);
                return "redirect:/registro/paso5";
            }
        }

        logger.info("Registro finalizado para usuario ID: {}", usuarioDTO.getId());
        empleadoService.registrarEmpleado(registroEmpleadoDTO, usuarioDTO);
        return "redirect:/dashboard";
    }
}

