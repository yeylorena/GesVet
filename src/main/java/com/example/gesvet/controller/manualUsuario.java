package com.example.gesvet.controller;

import com.example.gesvet.service.UserService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/pdf")
public class manualUsuario {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @GetMapping("descargar")
    public String clientes(Model model, Principal principal) {
        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        return "Plantillas/Manual-Usuario";

    }

}
