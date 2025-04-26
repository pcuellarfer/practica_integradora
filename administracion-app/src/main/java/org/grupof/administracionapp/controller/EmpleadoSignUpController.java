package org.grupof.administracionapp.controller;

import org.grupof.administracionapp.dto.Empleado.*;
import org.grupof.administracionapp.entity.embeddable.CuentaCorriente;
import org.grupof.administracionapp.entity.embeddable.Direccion;
import org.grupof.administracionapp.entity.embeddable.TarjetaCredito;
import org.grupof.administracionapp.repository.BancoRepository;
import org.grupof.administracionapp.services.Departamento.DepartamentoService;
import org.grupof.administracionapp.services.Genero.GeneroService;
import org.grupof.administracionapp.services.Pais.PaisService;
import org.grupof.administracionapp.services.TipoDocumento.TipoDocumentoService;
import org.grupof.administracionapp.services.TipoTarjetaService.TipoTarjetaService;
import org.grupof.administracionapp.services.banco.BancoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registro")
@SessionAttributes("registroEmpleado")
public class EmpleadoSignUpController {

    private final PaisService paisService;
    private final GeneroService generoService;
    private final TipoDocumentoService tipoDocumentoService;
    private final DepartamentoService departamentoService;
    private final BancoRepository bancoRepository;
    private final BancoService bancoService;
    private final TipoTarjetaService tipoTarjetaService;

    public EmpleadoSignUpController(PaisService paisService,
                                    GeneroService generoService,
                                    TipoDocumentoService tipoDocumentoService,
                                    DepartamentoService departamentoService, BancoRepository bancoRepository, BancoService bancoService, TipoTarjetaService tipoTarjetaService) {
        this.paisService = paisService;
        this.generoService = generoService;
        this.tipoDocumentoService = tipoDocumentoService;
        this.departamentoService = departamentoService;
        this.bancoRepository = bancoRepository;
        this.bancoService = bancoService;
        this.tipoTarjetaService = tipoTarjetaService;
    }

    //añadir un nuevo RegistroEmpleadoDTO vacio a la sesion
    @ModelAttribute("registroEmpleado")
    public RegistroEmpleadoDTO registroEmpleadoDTO() {
        return new RegistroEmpleadoDTO();
    }

    @GetMapping("/empleado")
    public String mostrarPaso1(Model modelo) {
        modelo.addAttribute("paso1", new Paso1PersonalDTO());
        modelo.addAttribute("paises", paisService.getAllPaises());
        modelo.addAttribute("generos", generoService.getAllGeneros());

        return "empleado/auth/FormDatosPersonales";
    }

    @PostMapping("/paso1")
    public String procesarPaso2(
            @ModelAttribute("paso1") Paso1PersonalDTO paso1,
            @ModelAttribute("registroEmpleado") RegistroEmpleadoDTO registroEmpleado,
            BindingResult errores,
            Model modelo) {

        if (errores.hasErrors()) {
            return "empleado/auth/FormDatosPersonales";
        }

        registroEmpleadoDTO().setPaso1PersonalDTO(paso1);

        return "redirect:/registro/paso2";
    }

    @GetMapping("/paso2")
    public String mostrarPaso2(Model modelo) {
        Paso2ContactoDTO paso2 = new Paso2ContactoDTO();
        paso2.setDireccion(new Direccion());
        modelo.addAttribute("paso2", paso2);
        modelo.addAttribute("documentos", tipoDocumentoService.getAllTipoDocumento());
        return "empleado/auth/FormDatosContacto";
    }

    @PostMapping("/paso2")
    public String procesarPaso2(
            @ModelAttribute("paso2") Paso2ContactoDTO paso2,
            @ModelAttribute("registroEmpleado") RegistroEmpleadoDTO registroEmpleado,
            BindingResult errores,
            Model modelo) {

        if (errores.hasErrors()) {
            return "empleado/auth/FormDatosContacto";
        }

        registroEmpleadoDTO().setPaso2ContactoDTO(paso2);

        return "redirect:/registro/paso3";
    }

    @GetMapping("/paso3")
    public String mostrarPaso3(Model modelo) {
        modelo.addAttribute("paso3", new Paso3ProfesionalDTO());
        modelo.addAttribute("departamentos", departamentoService.getAllDepartamentos());
        return "empleado/auth/FormDatosProfesionales";
    }

    @PostMapping("/paso3")
    public String procesarPaso3(
            @ModelAttribute("paso3") Paso3ProfesionalDTO paso3,
            @ModelAttribute("registroEmpleado") RegistroEmpleadoDTO registroEmpleado,
            BindingResult errores,
            Model modelo) {

        if (errores.hasErrors()) {
            return "empleado/auth/FormDatosProfesionales";
        }

        registroEmpleadoDTO().setPaso3ProfesionalDTO(paso3);

        return "redirect:/registro/paso4";
    }

    @GetMapping("/paso4")
    public String mostrarPaso4(Model modelo) {
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
            @ModelAttribute("paso4") Paso4EconomicosDTO paso4,
            @ModelAttribute("registroEmpleado") RegistroEmpleadoDTO registroEmpleado,
            BindingResult errores,
            Model modelo) {

        if (errores.hasErrors()) {
            return "empleado/auth/FormDatosEconomicos";
        }

        registroEmpleadoDTO().setPaso4EconomicosDTO(paso4);

        return "redirect:/registro/paso5";
    }
}
