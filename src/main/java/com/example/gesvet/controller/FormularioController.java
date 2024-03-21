package com.example.gesvet.controller;

import com.example.gesvet.models.FormularioPrincipal;
import com.example.gesvet.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FormularioController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/enviar-formulario")
    public String enviarFormulario(@ModelAttribute("formulario") FormularioPrincipal formulario, Model model, RedirectAttributes redirectAttributes) {
        // Lógica para procesar los campos del formulario

        // Construir el cuerpo del correo con los datos del formulario
        String cuerpoCorreo = "¡Hola!\n\n"
                + "Soy " + formulario.getNombre() + " " + formulario.getApellido() + ".\n"
                + "Quisiera compartir la siguiente información contigo:\n\n"
                + "Nombre: " + formulario.getNombre() + "\n"
                + "Apellidos: " + formulario.getApellido() + "\n"
                + "Teléfono: " + formulario.getTelefono() + "\n"
                + "Email: " + formulario.getEmail() + "\n"
                + "Dirección: " + formulario.getDireccion() + "\n"
                + "¿Cómo supo de nosotros?: " + formulario.getComosupodenosostros() + "\n\n"
                + "Mensaje: " + formulario.getMensaje() + "\n\n"
                + "¡Espero que tengas un excelente día!";

        // Enviar correo electrónico
        emailService.enviarCorreo("soportegesvet@gmail.com", "Nuevo formulario enviado", cuerpoCorreo);
        redirectAttributes.addFlashAttribute("FormularioExitoso", true);
        // Puedes redirigir a una página de éxito o a donde sea necesario
        return "redirect:/principal";
    }
}
