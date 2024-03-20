package com.example.gesvet.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ayuda")
public class ayudaadminController {

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("")
    public String show(Model model, Principal principal) {  //el objeto model lleva información desde el backend hacia la vista
        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);
        return "ayuda/ayudaVet";

    }

}
