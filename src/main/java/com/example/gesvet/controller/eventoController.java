package com.example.gesvet.controller;

import com.example.gesvet.service.eventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/eventos")
public class eventoController {

    @Autowired
    private eventoService eventoService;

}
