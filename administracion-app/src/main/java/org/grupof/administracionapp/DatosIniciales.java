package org.grupof.administracionapp;

import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.entity.TipoTarjeta;
import org.grupof.administracionapp.entity.Usuario;
import org.grupof.administracionapp.entity.embeddable.CuentaCorriente;
import org.grupof.administracionapp.entity.embeddable.Direccion;
import org.grupof.administracionapp.entity.embeddable.TarjetaCredito;
import org.grupof.administracionapp.entity.registroEmpleado.*;
import org.grupof.administracionapp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.UUID;

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
                                    EmpleadoRepository empleadoRepository,
                                    PaisRepository paisRepository,
                                    GeneroRepository generoRepository,
                                    TipoDocumentoRepository tipoDocumentoRepository,
                                    DepartamentoRepository departamentoRepository,
                                    TipoViaRepository tipoViaRepository,
                                    EspecialidadRepository especialidadRepository,
                                    BancoRepository bancoRepository,
                                    TipoTarjetaRepository tipoTarjetaRepository) {
        return _ -> {
            Usuario usuarioInicial = new Usuario();
            usuarioInicial.setNombre("Juan");
            usuarioInicial.setContrasena(passwordEncoder.encode("Contraseña@-"));
            usuarioInicial.setEmail("davidsmh23@gmail.com");

            Usuario paco = new Usuario();
            paco.setNombre("Paco");
            paco.setContrasena(passwordEncoder.encode("Aaaaaaaa-1"));
            paco.setEmail("Paco@paco.paco");


            usuarioRepository.save(usuarioInicial);
            usuarioRepository.save(paco);


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

            generoRepository.save(femenino);
            generoRepository.save(masculino);
            generoRepository.save(noBinarie);

            // TIPOS DE DOCUMENTOS ///

            TipoDocumento DNI = new TipoDocumento("DNI");
            TipoDocumento NIE = new TipoDocumento("NIE");
            TipoDocumento pasaporte = new TipoDocumento("PASAPORTE");

            tipoDocumentoRepository.save(DNI);
            tipoDocumentoRepository.save(NIE);
            tipoDocumentoRepository.save(pasaporte);

            // DEPARTAMENTOS ///

            Departamento informatica = new Departamento("Informatica");
            Departamento RRHH = new Departamento("Universidad");
            Departamento marketing = new Departamento("Marketing");

            departamentoRepository.save(informatica);
            departamentoRepository.save(RRHH);
            departamentoRepository.save(marketing);

            //  TIPOS VIA ///

            TipoVia calle = new TipoVia("Calle");
            TipoVia avenida = new TipoVia("Avenida");
            TipoVia paseo = new TipoVia("Paseo");

            tipoViaRepository.save(calle);
            tipoViaRepository.save(avenida);
            tipoViaRepository.save(paseo);

            //  ESPECIALIDADES ///

            Especialidad creativo = new Especialidad("Creativo");
            Especialidad trabajoEquipo = new Especialidad("Trabajo en equipo");
            Especialidad rapido = new Especialidad("Rapido");

            especialidadRepository.save(creativo);
            especialidadRepository.save(trabajoEquipo);
            especialidadRepository.save(rapido);

            // BANCOS ///

            Banco bbva = new Banco("BBVA");
            Banco caixa = new Banco("Caixa");
            Banco sabadell = new Banco("Sabadell");

            bancoRepository.save(bbva);
            bancoRepository.save(caixa);
            bancoRepository.save(sabadell);

            //  TIPO TARJETA ///

            TipoTarjeta visa = new TipoTarjeta("Visa");
            TipoTarjeta masterCard = new TipoTarjeta("Master Card");
            TipoTarjeta americanExpress = new TipoTarjeta("American express");

            tipoTarjetaRepository.save(visa);
            tipoTarjetaRepository.save(masterCard);
            tipoTarjetaRepository.save(americanExpress);

            // DPARTAMENTO ///

            Departamento departamento = new Departamento("Departamento");
            departamentoRepository.save(departamento);


            // EMPLEADO ///

            //usando el usuario inicial
            Empleado empleado = new Empleado();
            empleado.setNombre("Juan");
            empleado.setUsuario(usuarioInicial);
            empleado.setGenero(masculino);
            empleado.setPais(espana);
            empleado.setTipoDocumento(DNI);
            empleado.setApellido("Lopez");
            empleado.setFechaNacimiento(LocalDate.parse("1990-01-01"));

            Direccion direccion = new Direccion();
            direccion.setTipoVia(UUID.randomUUID());
            empleado.setDireccion(direccion);

            empleado.setDepartamento(departamento);

            CuentaCorriente cuentaCorriente = new CuentaCorriente();
            cuentaCorriente.setBanco(UUID.randomUUID());
            empleado.setCuentaCorriente(cuentaCorriente);

            TarjetaCredito tarjetaCredito = new TarjetaCredito();
            tarjetaCredito.setTipoTarjeta(UUID.randomUUID());
            empleado.setTarjetaCredito(tarjetaCredito);

            //para paco
            Empleado pacoE = new Empleado();
            pacoE.setNombre("Paco");
            pacoE.setUsuario(paco);
            pacoE.setGenero(masculino);
            pacoE.setPais(espana);
            pacoE.setTipoDocumento(DNI);
            pacoE.setApellido("Paquito");
            pacoE.setFechaNacimiento(LocalDate.parse("1990-01-02"));

            Direccion direccion1 = new Direccion();
            direccion1.setTipoVia(UUID.randomUUID());
            pacoE.setDireccion(direccion1);

            pacoE.setDepartamento(departamento);

            CuentaCorriente cuentaCorriente1 = new CuentaCorriente();
            cuentaCorriente1.setBanco(UUID.randomUUID());
            pacoE.setCuentaCorriente(cuentaCorriente1);

            TarjetaCredito tarjetaCredito1 = new TarjetaCredito();
            tarjetaCredito1.setTipoTarjeta(UUID.randomUUID());
            pacoE.setTarjetaCredito(tarjetaCredito1);

            empleadoRepository.save(empleado);

            // Usuario secundario
            Usuario usuarioSecundario = new Usuario();
            usuarioSecundario.setNombre("Maria");
            usuarioSecundario.setContrasena(passwordEncoder.encode("ClaveSegura123"));
            usuarioSecundario.setEmail("maria@example.com");
            usuarioRepository.save(usuarioSecundario);

            // Empleado asociado al usuario secundario
            Empleado empleado2 = new Empleado();
            empleado2.setNombre("Maria");
            empleado2.setApellido("Gomez");
            empleado2.setUsuario(usuarioSecundario);
            empleado2.setGenero(femenino);
            empleado2.setPais(francia);
            empleado2.setTipoDocumento(pasaporte);
            empleado2.setFechaNacimiento(LocalDate.parse("1985-06-15"));

            Direccion direccion2 = new Direccion();
            direccion2.setTipoVia(avenida.getId()); // usa ID válido ya guardado
            empleado2.setDireccion(direccion2);

            empleado2.setDepartamento(marketing); // Usa un departamento existente

            CuentaCorriente cuenta2 = new CuentaCorriente();
            cuenta2.setBanco(caixa.getId()); // usa ID válido ya guardado
            empleado2.setCuentaCorriente(cuenta2);

            TarjetaCredito tarjeta2 = new TarjetaCredito();
            tarjeta2.setTipoTarjeta(visa.getId()); // usa ID válido ya guardado
            empleado2.setTarjetaCredito(tarjeta2);

            empleadoRepository.save(empleado2);

            empleadoRepository.save(pacoE);
        };
    }
}
