package com.example.gesvet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
public class clientesController {

    @GetMapping("")
    public String show(Model model) {  //el objeto model lleva información desde el backend hacia la vista
        return "clientes/clientes";

    }

}
