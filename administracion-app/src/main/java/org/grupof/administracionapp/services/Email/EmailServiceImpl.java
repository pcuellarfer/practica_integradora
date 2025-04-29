package org.grupof.administracionapp.services.Email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de envío de correos electrónicos.
 * Utiliza {@link JavaMailSender} para enviar correos con un enlace incluido.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    /**
     * Constructor que inicializa el servicio de correo con un {@link JavaMailSender}.
     *
     * @param mailSender el componente que gestiona el envío de correos electrónicos
     */
    public EmailServiceImpl(JavaMailSender mailSender) {
        super();
        this.mailSender = mailSender;
    }

    /**
     * Envía un correo electrónico con un enlace al destinatario especificado.
     *
     * @param destinatario la dirección de correo electrónico del destinatario
     * @param asunto el asunto del mensaje
     * @param enlace el enlace que se incluirá en el cuerpo del mensaje
     */
    @Override
    public void enviarCorreoConEnlace(String destinatario, String asunto, String enlace) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject(asunto);
        mensaje.setText("Haz clic en el siguiente enlace para continuar: " + enlace);
        mensaje.setFrom("tuemail@gmail.com");

        mailSender.send(mensaje);
    }
}
