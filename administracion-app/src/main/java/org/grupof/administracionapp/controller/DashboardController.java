package org.grupof.administracionapp.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.grupof.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
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
import java.util.UUID;

/**
 * Controlador para gestionar las vistas del panel principal (dashboard)
 * tanto para usuarios como para empleados.
 */
@Controller
// @RequiredArgsConstructor
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    private final EmpleadoService empleadoService;
    private final CatalogoService catalogoService;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;
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
     * Muestra el dashboard correspondiente según si el usuario es solo usuario o también empleado.
     *
     * <p>Verifica la sesión del usuario y, si es válida, comprueba si el usuario tiene un empleado asociado.
     * En caso de no tenerlo, se muestra el dashboard general de usuario. Si lo tiene, se muestra el dashboard
     * específico para empleados, incluyendo información del usuario, el empleado y el contador de accesos.
     *
     * <p>También se recupera la cookie 'contador_sesiones' para mostrar el número de sesiones desde el navegador actual.
     *
     * @param session la sesión HTTP actual
     * @param modelo  el modelo para pasar atributos a la vista
     * @param request la solicitud HTTP, usada para recuperar cookies
     * @return el nombre de la vista del dashboard correspondiente
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model modelo, HttpServletRequest request) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");

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

        String contadorSesion = "0";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("contador_sesiones".equals(cookie.getName())) {
                    contadorSesion = cookie.getValue();
                    logger.debug("Cookie 'contador_sesiones' encontrada con valor: {}", contadorSesion);
                }
            }
        } else {
            logger.debug("No se encontraron cookies en la solicitud");
        }

        modelo.addAttribute("contadorSesiones", contadorSesiones);
        modelo.addAttribute("navegadorId", contadorSesion);
        modelo.addAttribute("usuario", usuarioDTO);
        modelo.addAttribute("empleado", empleadoDTO);

        return "empleado/main/empleado-dashboard";
    }


    /**
     * Muestra el submenú de etiquetado para empleados.
     * @param session sesión HTTP actual
     * @return vista del submenú de etiquetado
     */
    @GetMapping("dashboard/submenu-etiquetado")
    public String mostrarSubmenuSubordinados(HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario"); //casting

        if (usuarioDTO == null) {
            logger.warn("Intento de acceso al dashboard sin usuario en sesión /submenu-etiquetado");
            return "redirect:/login/username";
        }
        return "empleado/main/empleado-submenu-etiquetado";
    }

    /**
     * Muestra el submenú de productos para empleados.
     * @param session sesión HTTP actual
     * @return vista del submenú de productos
     */
    @GetMapping("dashboard/submenu-productos")
    public String mostrarSubmenuProductos(HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario"); //casting

        if (usuarioDTO == null) {
            logger.warn("Intento de acceso al dashboard sin usuario en sesión /submenu-productos");
            return "redirect:/login/username";
        }
        return "empleado/main/empleado-submenu-productos";
    }


    /**
     * Muestra el submenú de nóminas del empleado si hay un usuario en sesión.
     * Redirige al login si no hay sesión activa.
     *
     * @param session sesión HTTP actual
     * @return vista del submenú de nóminas o redirección al login
     */
    @GetMapping("dashboard/submenu-nominas")
    public String mostrarSubmenuNominas(HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioDTO == null) {
            logger.warn("Acceso denegado: no hay usuario en sesión (/submenu-nominas)");
            return "redirect:/login/username";
        }

        logger.info("Usuario {} accede al submenú de nóminas", usuarioDTO.getEmail());
        return "empleado/main/empleado-submenu-nominas";
    }

    /**
     * Muestra la vista de subida de catálogo, con mensajes de éxito o error si están presentes.
     * Redirige al login si no hay usuario en sesión.
     *
     * @param mensaje mensaje opcional para mostrar en la vista (puede ser null)
     * @param error mensaje de error opcional (puede ser null)
     * @param model modelo para pasar atributos a la vista
     * @param session sesión HTTP actual
     * @return vista para la subida de catálogo o redirección al login si no hay sesión activa
     */
    @GetMapping("dashboard/subida-catalogo")
    public String mostrarSubidaCatalogo(@RequestParam(value = "mensaje", required = false) String mensaje,
                                        @RequestParam(value = "error", required = false) String error,
                                        Model model,
                                        HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioDTO == null) {
            logger.warn("Intento de acceso al dashboard sin usuario en sesión (/subida-catalogo)");
            return "redirect:/login/username";
        }

        if (mensaje != null) {
            logger.info("Mostrando vista de catálogo con mensaje: {}", mensaje);
            model.addAttribute("mensaje", mensaje);
        }

        if (error != null) {
            logger.error("Error al procesar el catálogo: {}", error);
            model.addAttribute("error", error);
        }

        logger.info("Usuario {} accede al submenú de subida de catálogo", usuarioDTO.getEmail());
        return "empleado/main/catalogo";
    }


    /**
     * Muestra el catálogo de productos en la vista del empleado.
     * Carga todos los productos disponibles, los tipos de producto y las categorías.
     *
     * @param model el modelo de la vista que contiene los atributos a renderizar
     * @param session sesión HTTP actual
     * @return la ruta de la plantilla Thymeleaf que representa el catálogo
     */
    @GetMapping("/dashboard/mostrarCatalogo")
    public String mostrarCatalogo(Model model, HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario"); //casting

        if (usuarioDTO == null) {
            logger.warn("Intento de acceso al dashboard sin usuario en sesión /mostrarCatalogo");
            return "redirect:/login/username";
        }
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
     * @param session sesión HTTP actual
     * @return la plantilla Thymeleaf del catálogo con los productos filtrados
     */
    @PostMapping("/dashboard/mostrarCatalogo")
    public String filtrarCatalogo(@RequestParam String tipoProducto,
                                  @RequestParam String categoria,
                                  Model model, HttpSession session) {

        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario"); //casting

        if (usuarioDTO == null) {
            logger.warn("Intento de acceso al dashboard sin usuario en sesión /mostrarCatalogo POST");
            return "redirect:/login/username";
        }
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

    /**
     * Muestra los detalles de un producto específico basado en su ID.
     * Redirige al login si no hay usuario en sesión.
     *
     * @param id ID del producto que se va a mostrar
     * @param model modelo para pasar atributos a la vista
     * @param session sesión HTTP actual
     * @return vista con los detalles del producto o redirección al login si no hay sesión activa
     */
    @GetMapping("/dashboard/mostrarCatalogo/{id}")
    public String mostrarDetalleProducto(@PathVariable UUID id, Model model, HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioDTO == null) {
            logger.warn("Intento de acceso al dashboard sin usuario en sesión /mostrarCatalogo/{}", id);
            return "redirect:/login/username";
        }

        Producto producto = productoService.obtenerProductoPorId(id);

        if (producto == null) {
            logger.error("Producto con ID {} no encontrado", id);
            return "redirect:/dashboard";
        }

        // Añadir el producto al modelo
        model.addAttribute("producto", producto);
        model.addAttribute("productoId", id);
        logger.info("Usuario {} accede a los detalles del producto con ID {}", usuarioDTO.getEmail(), id);
        return "empleado/main/empleado-detalleProducto";
    }
}