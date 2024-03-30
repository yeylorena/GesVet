package com.example.gesvet.controller;

import com.example.gesvet.models.RecuperarContrasenaTokenusu;
import com.example.gesvet.models.User;
import com.example.gesvet.models.respuesta;
import com.example.gesvet.service.RecuperarContrasenausuService;
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
import com.example.gesvet.repository.RecuperarContrasenausuRepository;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contrasena")
public class RecuperarContrasenausuControllerapi {

    @Autowired
    private UserService userService;

    @Autowired
    private RecuperarContrasenausuService recuperarContraseñausuService;

    @Autowired
    RecuperarContrasenausuRepository recuperarContraseñausuRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/password-request")
    public String passwordRequest() {

        return "password-request";
    }

    @PostMapping("/password-request")
    public ResponseEntity<Object> savePasswordRequest(@RequestBody Map<String, String> requestBody) {
    String username = requestBody.get("username"); 
        User user = userService.findByUsername(username);
        if (user == null) {

            var respuesta = new respuesta("error", "Este correo electrónico no está registrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }

        RecuperarContrasenaTokenusu recuperarContraseñaTokenusu = new RecuperarContrasenaTokenusu();
        recuperarContraseñaTokenusu.setExpireTime(recuperarContraseñausuService.expireTimeRange());
        recuperarContraseñaTokenusu.setToken(recuperarContraseñausuService.generateToken());
        recuperarContraseñaTokenusu.setUser(user);
        recuperarContraseñaTokenusu.setUsed(false);

        recuperarContraseñausuRepository.save(recuperarContraseñaTokenusu);

        String emailLink = "http://192.168.0.101:8080/reset-password?token=" + recuperarContraseñaTokenusu.getToken();

        try {
            recuperarContraseñausuService.sendEmail(user.getUsername(), "Enlace para restablecer contraseña", emailLink);
            var respuesta = new respuesta("Creado", "Se ha enviado un correo electrónico con instrucciones para restablecer la contraseña");
            return ResponseEntity.status(HttpStatus.OK).body(respuesta);
        } catch (UnsupportedEncodingException | MessagingException e) {
            var respuesta = new respuesta("error", "Error al enviar correo electrónico");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody Map<String, String> requestBody) {
        String token = requestBody.get("token");
        RecuperarContrasenaTokenusu recuperarContraseñaTokenusu = recuperarContraseñausuRepository.findByToken(token);
        if (recuperarContraseñaTokenusu == null) {
            var respuesta = new respuesta("error", "El token proporcionado no es válido");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }

        // Verificar si el token ha sido utilizado
        if (recuperarContraseñaTokenusu.isUsed()) {
            var respuesta = new respuesta("error", "El token ya ha sido utilizado");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }
        // Verificar si el token ha expirado utilizando el servicio
        if (recuperarContraseñausuService.isExpired(recuperarContraseñaTokenusu)) {
            var respuesta = new respuesta("error", "El token ha expirado");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }

        // Aquí puedes realizar cualquier lógica adicional según tus necesidades, como verificar si el token ha expirado, etc.
        var respuesta = new respuesta("Creado", "Token válido");
        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> saveResetPassword(@RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword, @RequestParam("token") String token) {

        // Verificar si la contraseña y la confirmación coinciden
        if (!password.equals(confirmPassword)) {
            var respuesta = new respuesta("error", "La contraseña y la confirmación no coinciden");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }
        RecuperarContrasenaTokenusu recuperarContraseñaTokenusu = recuperarContraseñausuRepository.findByToken(token);
        if (recuperarContraseñaTokenusu == null) {
            var respuesta = new respuesta("error", "El token proporcionado no es válido");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }

        User user = recuperarContraseñaTokenusu.getUser();
        user.setPassword(passwordEncoder.encode(password));
        recuperarContraseñaTokenusu.setUsed(true);
        userService.save(user);
        recuperarContraseñausuRepository.save(recuperarContraseñaTokenusu);
        userService.cambiarContrasenaYEnviarCorreo(user);
        var respuesta = new respuesta("Creado", "Contraseña restablecida exitosamente");
        // Aquí puedes devolver cualquier otra información que desees, como un mensaje de éxito, por ejemplo
        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
    }

}
