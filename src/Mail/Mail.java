/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mail;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Erick Vidal
 */
public class Mail {
     private Properties properties;
    private Session session;

    public Mail() throws IOException {
        this.properties = new Properties();
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        session = Session.getDefaultInstance(properties);
    }

    public void enviarEmail(String asunto, String mensaje, String correo) throws MessagingException {
        final String correoRemitente = "erickvidal328@gmail.com";
        final String password = "pcenowaxsmlysrhe";
        MimeMessage contenedor = new MimeMessage(session);
        contenedor.setFrom(new InternetAddress(correoRemitente));
        contenedor.addRecipient(Message.RecipientType.TO, new InternetAddress(correo));
        contenedor.setSubject(asunto);
        contenedor.setText(mensaje);
        Transport t = session.getTransport("smtp");
        t.connect(correoRemitente, password);
        t.sendMessage(contenedor, contenedor.getRecipients(Message.RecipientType.TO));
        t.close();
    }
}
