package org.grupof.administracionapp.controller;

import org.grupof.administracionapp.dto.nominas.LineaNominaDTO;
import org.grupof.administracionapp.dto.nominas.NominaDTO;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.services.Empleado.EmpleadoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequestMapping("/nominas")
@Controller
public class nominaController {

    private final EmpleadoService empleadoService;

    public nominaController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping("/seleccionar-empleado")
    public String seleccionarEmpleado(Model model) {
        List<Empleado> empleados = empleadoService.getEmpleadosOrdenados();
        model.addAttribute("empleados", empleados);
        return "empleado/main/seleccionar-empleado";
    }

    @GetMapping("/alta")
    public String mostrarFormularioAlta(@RequestParam UUID empleadoId, Model model) {

        Empleado empleado = empleadoService.buscarPorId(empleadoId)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));

        NominaDTO dto = new NominaDTO();
        dto.setEmpleadoId(empleadoId);
        dto.setFechaInicio(LocalDate.now().withDayOfMonth(1));
        dto.setFechaFin(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()));

        LineaNominaDTO lineaInicial = new LineaNominaDTO();
        lineaInicial.setConcepto("Sueldo base");
        dto.setLineasNomina(Set.of(lineaInicial));

        model.addAttribute("empleado", empleado);
        model.addAttribute("altaNominaDTO", dto);
        return "empleado/main/formulario-alta";
    }

}
