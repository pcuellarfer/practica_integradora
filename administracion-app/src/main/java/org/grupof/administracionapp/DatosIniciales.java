package org.grupof.administracionapp;

import org.grupof.administracionapp.entity.TipoTarjeta;
import org.grupof.administracionapp.entity.Usuario;
import org.grupof.administracionapp.entity.embeddable.TarjetaCredito;
import org.grupof.administracionapp.entity.registroEmpleado.*;
import org.grupof.administracionapp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Clase de configuración que se encarga de insertar datos iniciales en la base de datos al inicio de la aplicación.
 * Utiliza un {@link CommandLineRunner} para ejecutar el código cuando la aplicación se inicia.
 */
@Configuration
public class DatosIniciales {

    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor que recibe un {@link PasswordEncoder} para codificar las contraseñas de los usuarios.
     *
     * @param passwordEncoder el codificador de contraseñas que se utilizará para almacenar las contraseñas de manera segura.
     */
    public DatosIniciales(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Método que crea un {@link CommandLineRunner} para insertar un usuario inicial en la base de datos.
     * Este usuario tiene un nombre, un email y una contraseña codificada.
     *
     * @param usuarioRepository el repositorio de usuarios donde se guardará el usuario inicial.
     * @return el {@link CommandLineRunner} que se ejecutará al iniciar la aplicación.
     */
    @Bean
    CommandLineRunner insertarDatos(UsuarioRepository usuarioRepository,
                                    PaisRepository paisRepository,
                                    GeneroRepository generoRepository,
                                    TipoDocumentoRepository tipoDocumentoRepository,
                                    DepartamentoRepository departamentoRepository,
                                    TipoViaRepository tipoViaRepository, EspecialidadRepository especialidadRepository, BancoRepository bancoRepository, TipoTarjetaRepository tipoTarjetaRepository) {
        return args -> {
            Usuario usuarioInicial = new Usuario();
            usuarioInicial.setNombre("Juan");
            usuarioInicial.setContrasena(passwordEncoder.encode("Contraseña@-"));
            usuarioInicial.setEmail("davidsmh23@gmail.com");

            usuarioRepository.save(usuarioInicial);


            ///// PAISES //////

            Pais espana = new Pais("España", "+34");
            Pais francia = new Pais("Francia", "+33");
            Pais belgica = new Pais("Belgica", "+32");
            paisRepository.save(espana);
            paisRepository.save(francia);
            paisRepository.save(belgica);

            ///// GENEROS //////

            Genero femenino = new Genero("Femenino");
            Genero masculino = new Genero("Masculino");
            Genero noBinarie = new Genero("No Binarie");
            Genero paco = new Genero("Paco");

            generoRepository.save(femenino);
            generoRepository.save(masculino);
            generoRepository.save(noBinarie);
            generoRepository.save(paco);

            /// TIPOS DE DOCUMENTOS ///

            TipoDocumento DNI = new TipoDocumento("DNI");
            TipoDocumento NIE = new TipoDocumento("NIE");
            TipoDocumento pasaporte = new TipoDocumento("PASAPORTE");

            tipoDocumentoRepository.save(DNI);
            tipoDocumentoRepository.save(NIE);
            tipoDocumentoRepository.save(pasaporte);

            /// DEPARTAMENTOS ///

            Departamento informatica = new Departamento("Informatica");
            Departamento RRHH = new Departamento("Universidad");
            Departamento marketing = new Departamento("Marketing");

            departamentoRepository.save(informatica);
            departamentoRepository.save(RRHH);
            departamentoRepository.save(marketing);

            ///  TIPOS VIA ///

            TipoVia calle = new TipoVia("Calle");
            TipoVia avenida = new TipoVia("Avenida");
            TipoVia paseo = new TipoVia("Paseo");

            tipoViaRepository.save(calle);
            tipoViaRepository.save(avenida);
            tipoViaRepository.save(paseo);

            ///  ESPECIALIDADES ///

            Especialidad creativo = new Especialidad("Creativo");
            Especialidad trabajoEquipo = new Especialidad("Trabajo en equipo");
            Especialidad rapido = new Especialidad("Rapido");

            especialidadRepository.save(creativo);
            especialidadRepository.save(trabajoEquipo);
            especialidadRepository.save(rapido);

            /// BANCOS ///

            Banco bbva = new Banco("BBVA");
            Banco caixa = new Banco("Caixa");
            Banco sabadell = new Banco("Sabadell");

            bancoRepository.save(bbva);
            bancoRepository.save(caixa);
            bancoRepository.save(sabadell);

            ///  TIPO TARJETA ///

            TipoTarjeta debito = new TipoTarjeta("Debito");
            TipoTarjeta credito = new TipoTarjeta("Credito");
            TipoTarjeta prepago = new TipoTarjeta("Prepago");

            tipoTarjetaRepository.save(debito);
            tipoTarjetaRepository.save(credito);
            tipoTarjetaRepository.save(prepago);

        };
    }
}
