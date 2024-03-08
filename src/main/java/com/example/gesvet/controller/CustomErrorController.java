package com.example.gesvet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController {

    @GetMapping("/error403")
    public String handle403() {
        // Puedes realizar algún procesamiento adicional si es necesario
        return "error403"; // Nombre de la página HTML personalizada
    }
}
