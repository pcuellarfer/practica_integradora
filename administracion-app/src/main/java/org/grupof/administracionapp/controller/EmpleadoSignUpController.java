package org.grupof.administracionapp.controller;

import org.grupof.administracionapp.dto.Empleado.Paso1PersonalDTO;
import org.grupof.administracionapp.dto.Empleado.Paso2ContactoDTO;
import org.grupof.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.grupof.administracionapp.services.Genero.GeneroService;
import org.grupof.administracionapp.services.Pais.PaisService;
import org.grupof.administracionapp.services.TipoDocumento.TipoDocumentoService;
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

    public EmpleadoSignUpController(PaisService paisService, GeneroService generoService, TipoDocumentoService tipoDocumentoService) {
        this.paisService = paisService;
        this.generoService = generoService;
        this.tipoDocumentoService = tipoDocumentoService;
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
            BindingResult errores,
            Model modelo) {

        if (errores.hasErrors()) {
            return "empleado/auth/FormDatosPersonales";
        }

        registroEmpleadoDTO().setPaso1PersonalDTO(paso1);

        //prueba
        System.err.println(paso1.getNombre());
        System.err.println(paso1.getApellido());
        System.err.println(paso1.getFoto());
        System.err.println(paso1.getGenero());
        System.err.println(paso1.getFechaNacimiento());
        System.err.println(paso1.getEdad());
        System.err.println(paso1.getPais());
        System.err.println(paso1.getComentarios());

        return "redirect:/registro/paso2";
    }

    @GetMapping("/paso2")
    public String mostrarPaso2(Model modelo) {
        modelo.addAttribute("paso2", new Paso2ContactoDTO());
        modelo.addAttribute("documentos", tipoDocumentoService.getAllTipoDocumento());
        return "empleado/auth/FormDatosContacto";
    }

    @PostMapping("/paso2")
    public String procesarPaso2(
            @ModelAttribute("paso2") Paso2ContactoDTO paso2,
            BindingResult errores,
            Model modelo) {

        if (errores.hasErrors()) {
            return "empleado/auth/FormDatosPersonales";
        }

        registroEmpleadoDTO().setPaso2ContactoDTO(paso2);

        return "redirect:/registro/paso2";
    }

}
