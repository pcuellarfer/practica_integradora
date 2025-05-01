package org.grupof.administracionapp.controller;

import jakarta.validation.Valid;
import org.grupof.administracionapp.dto.Empleado.*;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.TipoTarjeta;
import org.grupof.administracionapp.entity.embeddable.CuentaCorriente;
import org.grupof.administracionapp.entity.embeddable.Direccion;
import org.grupof.administracionapp.entity.embeddable.TarjetaCredito;
import org.grupof.administracionapp.entity.registroEmpleado.*;
import org.grupof.administracionapp.repository.BancoRepository;
import org.grupof.administracionapp.services.Departamento.DepartamentoService;
import org.grupof.administracionapp.services.Empleado.EmpleadoService;
import org.grupof.administracionapp.services.Especialidades.EspecialidadesService;
import org.grupof.administracionapp.services.Genero.GeneroService;
import org.grupof.administracionapp.services.Pais.PaisService;
import org.grupof.administracionapp.services.TipoDocumento.TipoDocumentoService;
import org.grupof.administracionapp.services.TipoTarjetaService.TipoTarjetaService;
import org.grupof.administracionapp.services.TipoVia.TipoViaService;
import org.grupof.administracionapp.services.banco.BancoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/registro")
@SessionAttributes({"registroEmpleado", "usuario"})
public class EmpleadoSignUpController {

    private final PaisService paisService;
    private final GeneroService generoService;
    private final TipoDocumentoService tipoDocumentoService;
    private final DepartamentoService departamentoService;
    private final BancoRepository bancoRepository;
    private final BancoService bancoService;
    private final TipoTarjetaService tipoTarjetaService;
    private final EmpleadoService empleadoService;
    private final TipoViaService tipoViaService;
    private final EspecialidadesService especialidadesService;

    public EmpleadoSignUpController(PaisService paisService,
                                    GeneroService generoService,
                                    TipoDocumentoService tipoDocumentoService,
                                    DepartamentoService departamentoService,
                                    BancoRepository bancoRepository,
                                    BancoService bancoService,
                                    TipoTarjetaService tipoTarjetaService, EmpleadoService empleadoService, TipoViaService tipoViaService, EspecialidadesService especialidadesService) {
        this.paisService = paisService;
        this.generoService = generoService;
        this.tipoDocumentoService = tipoDocumentoService;
        this.departamentoService = departamentoService;
        this.bancoRepository = bancoRepository;
        this.bancoService = bancoService;
        this.tipoTarjetaService = tipoTarjetaService;
        this.empleadoService = empleadoService;
        this.tipoViaService = tipoViaService;
        this.especialidadesService = especialidadesService;
    }

    //añadir un nuevo RegistroEmpleadoDTO vacio a la sesion
    @ModelAttribute("registroEmpleado")
    public RegistroEmpleadoDTO registroEmpleadoDTO() {
        return new RegistroEmpleadoDTO();
    }

    @ModelAttribute("usuario")
    public UsuarioDTO getUsuario(@SessionAttribute(value = "usuario", required = false) UsuarioDTO usuario) {
        return usuario;
    }

    // Paso 1
    @GetMapping("/empleado")
    public String mostrarPaso1(Model modelo,
                               @SessionAttribute(value = "usuario", required = false) UsuarioDTO usuario,
                               RedirectAttributes redirectAttributes) {

        if (usuario == null) {
            //se usa redirectAttributes y addflashAttribute porque con model.addatribute no se guarda entre redirecciones
            redirectAttributes.addFlashAttribute("error", "Estabas intentando registrar un empleado sin usuario. Te hemos redirigido para que registres un usuario.");
            return "redirect:/registro/usuario";
        }

        modelo.addAttribute("paso1", new Paso1PersonalDTO());
        modelo.addAttribute("paises", paisService.getAllPaises());
        modelo.addAttribute("generos", generoService.getAllGeneros());

        return "empleado/auth/FormDatosPersonales";
    }

    @PostMapping("/paso1")
    public String procesarPaso2(
            @ModelAttribute("paso1") @Valid Paso1PersonalDTO paso1,
            BindingResult errores,
            @ModelAttribute("registroEmpleado") RegistroEmpleadoDTO registroEmpleado,
            Model modelo,
            @SessionAttribute(value = "usuario", required = false) UsuarioDTO usuarioDTO) {

        if (errores.hasErrors()) {
            modelo.addAttribute("paises", paisService.getAllPaises());
            modelo.addAttribute("generos", generoService.getAllGeneros());
            return "empleado/auth/FormDatosPersonales";
        }

        registroEmpleado.setPaso1PersonalDTO(paso1);

        return "redirect:/registro/paso2";
    }

    // Paso 2
    @GetMapping("/paso2")
    public String mostrarPaso2(Model modelo,
                               @SessionAttribute(value = "usuario", required = false) UsuarioDTO usuario,
                               RedirectAttributes redirectAttributes) {

        if (usuario == null) {
            //se usa redirectAttributes y addflashAttribute porque con model.addatribute no se guarda entre redirecciones
            redirectAttributes.addFlashAttribute("error", "Estabas intentando registrar un empleado sin usuario. Te hemos redirigido para que registres un usuario.");
            return "redirect:/registro/usuario";
        }

        Paso2ContactoDTO paso2 = new Paso2ContactoDTO();
        paso2.setDireccion(new Direccion());

        modelo.addAttribute("paso2", paso2);
        modelo.addAttribute("tiposVias", tipoViaService.getAllTipoVia());
        modelo.addAttribute("documentos", tipoDocumentoService.getAllTipoDocumento());

        return "empleado/auth/FormDatosContacto";
    }

    @PostMapping("/paso2")
    public String procesarPaso2(
            @ModelAttribute("paso2") @Valid Paso2ContactoDTO paso2,
            BindingResult errores,
            @ModelAttribute("registroEmpleado") RegistroEmpleadoDTO registroEmpleado,
            Model modelo,
            @SessionAttribute(value = "usuario", required = false) UsuarioDTO usuario) {

        if (errores.hasErrors()) {
            modelo.addAttribute("tiposVias", tipoViaService.getAllTipoVia());
            modelo.addAttribute("documentos", tipoDocumentoService.getAllTipoDocumento());
            return "empleado/auth/FormDatosContacto";
        }

        registroEmpleado.setPaso2ContactoDTO(paso2);

        return "redirect:/registro/paso3";
    }

    // Paso 3
    @GetMapping("/paso3")
    public String mostrarPaso3(Model modelo,
                               @SessionAttribute(value = "usuario", required = false) UsuarioDTO usuario,
                               RedirectAttributes redirectAttributes) {

        if (usuario == null) {
            //se usa redirectAttributes y addflashAttribute porque con model.addatribute no se guarda entre redirecciones
            redirectAttributes.addFlashAttribute("error", "Estabas intentando registrar un empleado sin usuario. Te hemos redirigido para que registres un usuario.");
            return "redirect:/registro/usuario";
        }

        modelo.addAttribute("paso3", new Paso3ProfesionalDTO());
        modelo.addAttribute("departamentos", departamentoService.getAllDepartamentos());
        modelo.addAttribute("especialidades", especialidadesService.getAllEspecialidades());

        return "empleado/auth/FormDatosProfesionales";
    }

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
            return "empleado/auth/FormDatosProfesionales";
        }

        registroEmpleado.setPaso3ProfesionalDTO(paso3);

        return "redirect:/registro/paso4";
    }

    // Paso 4
    @GetMapping("/paso4")
    public String mostrarPaso4(Model modelo,
                               @SessionAttribute(value = "usuario", required = false) UsuarioDTO usuario,
                               RedirectAttributes redirectAttributes) {

        if (usuario == null) {
            //se usa redirectAttributes y addflashAttribute porque con model.addatribute no se guarda entre redirecciones
            redirectAttributes.addFlashAttribute("error", "Estabas intentando registrar un empleado sin usuario. Te hemos redirigido para que registres un usuario.");
            return "redirect:/registro/usuario";
        }

        Paso4EconomicosDTO paso4 = new Paso4EconomicosDTO();
        paso4.setCuentaCorriente(new CuentaCorriente());
        paso4.setTarjetaCredito(new TarjetaCredito());

        modelo.addAttribute("paso4", paso4);
        modelo.addAttribute("bancos", bancoService.getAllBancos());
        modelo.addAttribute("tiposTarjeta", tipoTarjetaService.getAllTiposTarjetas());

        return "empleado/auth/FormDatosEconomicos";
    }

    @PostMapping("/paso4")
    public String procesarPaso4(
            @ModelAttribute("paso4") @Valid Paso4EconomicosDTO paso4,
            BindingResult errores,
            @ModelAttribute("registroEmpleado") RegistroEmpleadoDTO registroEmpleado,
            Model modelo,
            @SessionAttribute(value = "usuario", required = false) UsuarioDTO usuario) {

        if (errores.hasErrors()) {
            System.err.println(errores.toString());
            modelo.addAttribute("bancos", bancoService.getAllBancos());
            modelo.addAttribute("tiposTarjeta", tipoTarjetaService.getAllTiposTarjetas());
            return "empleado/auth/FormDatosEconomicos";
        }

        registroEmpleado.setPaso4EconomicosDTO(paso4);

        return "redirect:/registro/paso5";
    }

    // Paso 5
    @GetMapping("/paso5")
    public String mostrarPaso5(@ModelAttribute("registroEmpleado") RegistroEmpleadoDTO registroEmpleado,
                               Model modelo,
                               @SessionAttribute(value = "usuario", required = false) UsuarioDTO usuario,
                               RedirectAttributes redirectAttributes) {

        if (usuario == null) {
            //se usa redirectAttributes y addflashAttribute porque con model.addatribute no se guarda entre redirecciones
            redirectAttributes.addFlashAttribute("error", "Estabas intentando registrar un empleado sin usuario. Te hemos redirigido para que registres un usuario.");
            return "redirect:/registro/usuario";
        }

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

    @PostMapping("/paso5")
    public String procesarPaso5(
            @ModelAttribute("registroEmpleado") RegistroEmpleadoDTO registroEmpleadoDTO,
            @SessionAttribute(value = "usuario", required = false) UsuarioDTO usuarioDTO,
            SessionStatus sesion) {

        if (usuarioDTO == null) {
            return "redirect:/registro/usuario";
        }

        empleadoService.registrarEmpleado(registroEmpleadoDTO, usuarioDTO);

        return "redirect:/dashboard/dashboard";
    }
}

