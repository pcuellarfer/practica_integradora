package org.grupof.administracionapp.services.banco;

import org.grupof.administracionapp.entity.registroEmpleado.Banco;
import org.grupof.administracionapp.repository.BancoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio para la gestión de entidades bancarias.
 * Proporciona métodos para obtener todos los bancos y para obtener un banco
 * por su identificador único.
 */
@Service
public class BancoServiceImpl implements BancoService {

    private final BancoRepository bancoRepository;

    /**
     * Constructor que inyecta el repositorio de bancos.
     *
     * @param bancoRepository el repositorio para acceder a la persistencia de bancos.
     */
    public BancoServiceImpl(BancoRepository bancoRepository) {
        this.bancoRepository = bancoRepository;
    }

    /**
     * Obtiene una lista con todos los bancos disponibles en la base de datos.
     *
     * @return una lista de objetos {@link Banco}.
     */
    @Override
    public List<Banco> getAllBancos() {
        return bancoRepository.findAll();
    }

    /**
     * Obtiene un banco a partir de su identificador único.
     *
     * @param id el UUID que identifica al banco.
     * @return el objeto {@link Banco} correspondiente, o {@code null} si no se encuentra.
     */
    @Override
    public Banco GetBancoById(UUID id) {
        return bancoRepository.findById(id).orElse(null);
    }
}

