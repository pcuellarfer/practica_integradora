package org.grupof.administracionapp.services.Pais;

import org.grupof.administracionapp.entity.registroEmpleado.Pais;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Interfaz que define los servicios para gestionar información de países.
 */
public interface PaisService {

     /**
      * Obtiene una lista con todos los países disponibles en el sistema.
      *
      * @return una lista de objetos {@link Pais} que representa todos los países.
      */
     List<Pais> getAllPaises();

     /**
      * Recupera un país por su identificador único.
      *
      * @param id el UUID que identifica al país.
      * @return el objeto {@link Pais} correspondiente al identificador proporcionado.
      * @throws NoSuchElementException si no se encuentra un país con el ID dado.
      */
     Pais getPaisById(UUID id);

     /**
      * Obtiene el nombre de un país a partir de su identificador.
      *
      * @param id el UUID que identifica al país.
      * @return el nombre del país como una cadena de texto.
      * @throws NoSuchElementException si no se encuentra un país con el ID dado.
      */
     String obtenerNombrePais(UUID id);
}