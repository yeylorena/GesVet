package com.example.gesvet.service;

import com.example.gesvet.models.RecuperarContraseñaTokenusu;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class RecuperarContraseñausuService {

    @Autowired
    JavaMailSender javaMailSender;

    private final int MINUTES = 10;

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public LocalDateTime expireTimeRange() {
        return LocalDateTime.now().plusMinutes(MINUTES);
    }

    public void sendEmail(String to, String subject, String emailLink) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String emailContent = "<p style=\"color: black;\">Hola,</p>"
                + "<p style=\"color: black;\">Recibimos una solicitud para restablecer la contraseña de tu cuenta. Si solicitaste este cambio, haz clic en el siguiente enlace para continuar con el proceso:</p>"
                + "<p><a href=\"" + emailLink + "\">Cambiar mi contraseña</a></p>" + "<p style=\"color: black;\">Por motivos de seguridad, este enlace es de un solo uso y solo será válido durante los próximos 10 minutos. Después de ese tiempo, deberás solicitar un nuevo enlace.</p>"
                + "<p style=\"color: black;\">Si no solicitaste este cambio, puedes ignorar este correo electrónico.</p>"
                + "<p style=\"color: black;\">Gracias,</p>"
                + "El equipo de GesVet.";

        helper.setText(emailContent, true);
        helper.setFrom("nicolas260805@gmail.com", "Atención y soporte GesVet");
        helper.setSubject(subject);
        helper.setTo(to);
        javaMailSender.send(message);
    }

    public boolean isExpired(RecuperarContraseñaTokenusu recuperarContraseñaTokenusu) {
        return LocalDateTime.now().isAfter(recuperarContraseñaTokenusu.getExpireTime());
    }

    public String checkValidity(RecuperarContraseñaTokenusu recuperarContraseñaTokenusu, Model model, RedirectAttributes redirectAttributes) {
        if (recuperarContraseñaTokenusu == null) {
            redirectAttributes.addFlashAttribute("Invalid_Token", true);
            return "password-request";
        } else if (recuperarContraseñaTokenusu.isUsed()) {
            redirectAttributes.addFlashAttribute("token_used", true);
            return "password-request";
        } else if (isExpired(recuperarContraseñaTokenusu)) {
            redirectAttributes.addFlashAttribute("token_expired", true);
            return "password-request";
        } else {
            return "reset-password";
        }
    }

}
