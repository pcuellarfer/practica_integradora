package org.grupof.administracionapp.controller;

import jakarta.servlet.http.HttpSession;
import org.grupof.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.entity.producto.Producto;
import org.grupof.administracionapp.services.CatalogoService;
import org.grupof.administracionapp.services.Categoria.CategoriaService;
import org.grupof.administracionapp.services.Empleado.EmpleadoService;
import org.grupof.administracionapp.services.Producto.ProductoService;
import org.grupof.administracionapp.services.Usuario.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para gestionar las vistas del panel principal (dashboard)
 * tanto para usuarios como para empleados.
 */
@Controller
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    private final EmpleadoService empleadoService;
    private final CatalogoService catalogoService;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    /**
     * Constructor para inyectar los servicios necesarios en el controlador del dashboard.
     *
     * @param empleadoService servicio para operaciones relacionadas con empleados
     * @param catalogoService servicio para gestionar el catálogo de productos
     * @param usuarioService servicio para gestionar la información de usuarios
     * @param productoService servicio para manejar lógica relacionada con productos
     * @param categoriaService servicio para acceder y gestionar las categorías de productos
     */
    public DashboardController(EmpleadoService empleadoService,
                               CatalogoService catalogoService,
                               UsuarioService usuarioService,
                               ProductoService productoService,
                               CategoriaService categoriaService) {
        this.empleadoService = empleadoService;
        this.catalogoService = catalogoService;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }


    /**
     * Muestra la vista del dashboard principal dependiendo del tipo de usuario.
     * Si el usuario tiene un empleado asociado, se redirige al dashboard de empleado,
     * en caso contrario, se muestra el dashboard genérico de usuario.
     *
     * @param session sesión HTTP actual
     * @param modelo  modelo de atributos para la vista
     * @return vista correspondiente al dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model modelo) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario"); //casting

        if (usuarioDTO == null) {
            logger.warn("Intento de acceso al dashboard sin usuario en sesión");
            return "redirect:/login/username";
        }

        logger.info("Accediendo al dashboard con usuario ID: {}", usuarioDTO.getId());

        RegistroEmpleadoDTO empleadoDTO = empleadoService.buscarEmpleadoPorUsuarioId(usuarioDTO.getId());

        if (empleadoDTO == null) {
            logger.info("Usuario ID {} no tiene empleado asociado. Mostrando dashboard de usuario.", usuarioDTO.getId());
            modelo.addAttribute("usuario", usuarioDTO);
            return "usuario/main/usuario-dashboard";
        }

        logger.info("Usuario ID {} es también empleado. Mostrando dashboard de empleado.", usuarioDTO.getId());

        UsuarioDTO usuarioBBDD = usuarioService.buscarPorEmail(usuarioDTO.getEmail());
        int contadorSesiones = usuarioService.getContadorInicios(usuarioBBDD.getEmail());

        modelo.addAttribute("contadorSesiones", contadorSesiones);
        modelo.addAttribute("usuario", usuarioDTO);
        modelo.addAttribute("empleado", empleadoDTO);

        return "empleado/main/empleado-dashboard";
    }

    /**
     * Muestra el submenú de etiquetado para empleados.
     *
     * @return vista del submenú de etiquetado
     */
    @GetMapping("dashboard/submenu-etiquetado")
    public String mostrarSubmenuSubordinados() {
        return "empleado/main/empleado-submenu-etiquetado";
    }

    /**
     * Muestra el submenú de productos para empleados.
     *
     * @return vista del submenú de productos
     */
    @GetMapping("dashboard/submenu-productos")
    public String mostrarSubmenuProductos() {
        return "empleado/main/empleado-submenu-productos";
    }


    @GetMapping("dashboard/submenu-nominas")
    public String mostrarSubmenuNominas() {
        return "empleado/main/empleado-submenu-nominas";
    }

    /**
     * Controlador para mostrar la vista del catálogo tras la subida de un archivo.
     * Si se proporciona un mensaje como parámetro en la URL, este se añade al modelo
     * para que pueda mostrarse en la vista.
     *
     * @param mensaje Mensaje opcional que se mostrará en la vista, como confirmación o advertencia.
     * @param model Modelo para pasar datos a la vista.
     * @return Nombre de la vista que representa la página del catálogo.
     */
    @GetMapping("dashboard/subida-catalogo")
    public String mostrarSubidaCatalogo(@RequestParam(value = "mensaje", required = false) String mensaje,
                                        @RequestParam(value = "error", required = false) String error,
                                        Model model) {
        Logger logger = LoggerFactory.getLogger(getClass());

        if (mensaje != null) {
            logger.info("Mostrando vista de catálogo con mensaje: {}", mensaje);
            model.addAttribute("mensaje", mensaje);
        }

        if (error != null) {
            logger.error("Error al procesar el catálogo: {}", error);
            model.addAttribute("error", error);
        }

        return "empleado/main/catalogo";
    }

    /**
     * Muestra el catálogo de productos en la vista del empleado.
     * Carga todos los productos disponibles, los tipos de producto y las categorías.
     *
     * @param model el modelo de la vista que contiene los atributos a renderizar
     * @return la ruta de la plantilla Thymeleaf que representa el catálogo
     */
    @GetMapping("/dashboard/mostrarCatalogo")
    public String mostrarCatalogo(Model model) {
        logger.info("Accediendo a /dashboard/mostrarCatalogo");

        List<Producto> productos = catalogoService.obtenerTodosLosProductos();
        logger.debug("Número de productos cargados: {}", productos.size());

        model.addAttribute("productos", productos);
        model.addAttribute("tipoProducto", productoService.mostrarTiposProducto());
        model.addAttribute("categorias", categoriaService.obtenerTodasLasCategorias());

        return "empleado/main/empleado-mostrarCatalogo";
    }


    /**
     * Procesa el formulario de filtrado del catálogo de productos.
     * Filtra los productos según el tipo de producto y la categoría seleccionados.
     *
     * @param tipoProducto el tipo de producto seleccionado (puede estar vacío)
     * @param categoria la categoría seleccionada (puede estar vacía)
     * @param model el modelo que se pasa a la vista
     * @return la plantilla Thymeleaf del catálogo con los productos filtrados
     */
    @PostMapping("/dashboard/mostrarCatalogo")
    public String filtrarCatalogo(@RequestParam String tipoProducto,
                                  @RequestParam String categoria,
                                  Model model) {

        logger.info("Filtrando catálogo con tipoProducto='{}' y categoria='{}'", tipoProducto, categoria);

        List<Producto> productos = productoService.filtrarCatalogo(tipoProducto, categoria);
        logger.debug("Número de productos encontrados tras el filtro: {}", productos.size());

        model.addAttribute("productos", productos);
        model.addAttribute("tipoProducto", productoService.mostrarTiposProducto());
        model.addAttribute("categorias", categoriaService.obtenerTodasLasCategorias());
        model.addAttribute("selectedTipoProducto", tipoProducto);
        model.addAttribute("selectedCategoria", categoria);

        return "empleado/main/empleado-mostrarCatalogo";
    }
}