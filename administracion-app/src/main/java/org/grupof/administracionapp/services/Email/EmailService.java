package org.grupof.administracionapp.services.Email;

/**
 * Interfaz que define las operaciones para el envío de correos electrónicos.
 * Proporciona métodos para enviar correos con enlaces personalizados.
 */
public interface EmailService {

    /**
     * Envía un correo electrónico con un enlace incluido en el cuerpo del mensaje.
     *
     * @param destinatario la dirección de correo electrónico del destinatario
     * @param asunto el asunto del correo electrónico
     * @param enlace el enlace que se debe incluir en el cuerpo del mensaje
     */
    void enviarCorreoConEnlace(String destinatario, String asunto, String enlace);
}

