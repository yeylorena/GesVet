package com.example.gesvet.controller;

import com.example.gesvet.models.RecuperarContraseñaTokenusu;
import com.example.gesvet.models.User;
import com.example.gesvet.repository.RecuperarContraseñausuRepository;
import com.example.gesvet.service.RecuperarContraseñausuService;
import com.example.gesvet.service.UserService;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller

public class RecuperarContrasenausuController {

    @Autowired
    private UserService userService;

    @Autowired
    private RecuperarContraseñausuService recuperarContraseñausuService;

    @Autowired
    RecuperarContraseñausuRepository recuperarContraseñausuRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/password-request")
    public String passwordRequest() {

        return "password-request";
    }

    @PostMapping("/password-request")
    public String savePasswordRequest(@RequestParam("username") String username, Model model) {
        User user = userService.findByUsername(username);
        if (user == null) {
            model.addAttribute("error", "Este correo electrónico no está registrado");
            return "password-request";
        }

        RecuperarContraseñaTokenusu recuperarContraseñaTokenusu = new RecuperarContraseñaTokenusu();
        recuperarContraseñaTokenusu.setExpireTime(recuperarContraseñausuService.expireTimeRange());
        recuperarContraseñaTokenusu.setToken(recuperarContraseñausuService.generateToken());
        recuperarContraseñaTokenusu.setUser(user);
        recuperarContraseñaTokenusu.setUsed(false);

        recuperarContraseñausuRepository.save(recuperarContraseñaTokenusu);

        String emailLink = "http://localhost:8080/reset-password?token=" + recuperarContraseñaTokenusu.getToken();

        try {
            recuperarContraseñausuService.sendEmail(user.getUsername(), "Enlace para restablecer contraseña", emailLink);
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error al enviar correo electrónico");
            return "password-request";
        }

        return "redirect:/password-request?success";
    }

    @GetMapping("/reset-password")
    public String resetPassword(@Param(value = "token") String token, Model model, RedirectAttributes redirectAttributes, HttpSession session) {

        session.setAttribute("token", token);
        RecuperarContraseñaTokenusu recuperarContraseñaTokenusu = recuperarContraseñausuRepository.findByToken(token);
        return recuperarContraseñausuService.checkValidity(recuperarContraseñaTokenusu, model, redirectAttributes);

    }

    @PostMapping("/reset-password")
    public String saveResetPassword(HttpServletRequest request, HttpSession session, Model model) {
        String password = request.getParameter("password");
        String token = (String) session.getAttribute("token");

        RecuperarContraseñaTokenusu recuperarContraseñaTokenusu = recuperarContraseñausuRepository.findByToken(token);
        User user = recuperarContraseñaTokenusu.getUser();
        user.setPassword(passwordEncoder.encode(password));
        recuperarContraseñaTokenusu.setUsed(true);
        userService.save(user);
        recuperarContraseñausuRepository.save(recuperarContraseñaTokenusu);
        userService.cambiarContrasenaYEnviarCorreo(user);

        model.addAttribute("message", "Has restablecido exitosamente tu contraseña");

        return "reset-password";
    }

}
