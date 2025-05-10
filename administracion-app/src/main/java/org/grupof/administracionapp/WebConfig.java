package org.grupof.administracionapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * Configuración personalizada de Spring MVC para exponer recursos estáticos, como imágenes subidas.
 * <p>
 * Esta clase permite mapear una URL específica (por ejemplo, /imagenes/**) a un directorio en el sistema de archivos,
 * de manera que los archivos ubicados allí puedan ser servidos públicamente desde el navegador.
 * <p>
 * Por ejemplo, una imagen almacenada en el directorio configurado como `app.upload.dir`
 * podrá ser accedida desde la ruta `/imagenes/nombreArchivo.ext`.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Directorio donde se almacenan los archivos subidos, configurado desde application.properties
     * mediante la propiedad `app.upload.dir`.
     */
    @Value("${app.upload.dir}")
    private String uploadDir;

    /**
     * Añade un manejador de recursos para permitir el acceso a los archivos estáticos desde el navegador.
     * <p>
     * La ruta virtual `/imagenes/**` se enlaza con el directorio físico especificado por `uploadDir`.
     *
     * @param registry el registro de manejadores de recursos.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String pathAbsoluto = Paths.get(uploadDir).toAbsolutePath().toUri().toString();

        registry
                .addResourceHandler("/imagenes/**")
                .addResourceLocations(pathAbsoluto);
    }
}
