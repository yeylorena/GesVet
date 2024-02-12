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

        String emailContent = "<p>Hola</p>"
                + "Haga clic en el enlace a continuación para restablecer la contraseña"
                + "<p><a href=\"" + emailLink + "\">Cambiar mi contraseña</a></p>"
                + "<br>"
                + "Ignorar este correo electrónico si no realizó la solicitud";
        helper.setText(emailContent, true);
        helper.setFrom("nicolas260805@gmail.com", "GesVet");
        helper.setSubject(subject);
        helper.setTo(to);
        javaMailSender.send(message);
    }

    public boolean isExpired(RecuperarContraseñaTokenusu recuperarContraseñaTokenusu) {
        return LocalDateTime.now().isAfter(recuperarContraseñaTokenusu.getExpireTime());
    }

    public String checkValidity(RecuperarContraseñaTokenusu recuperarContraseñaTokenusu, Model model) {

        if (recuperarContraseñaTokenusu == null) {
            model.addAttribute("error", "Invalid Token");
            return "error-page";
        } else if (recuperarContraseñaTokenusu.isUsed()) {
            model.addAttribute("error", "the token is already used");
            return "error-page";
        } else if (isExpired(recuperarContraseñaTokenusu)) {
            model.addAttribute("error", "the token is expired");
            return "error-page";
        } else {
            return "reset-password";
        }

    }

}
