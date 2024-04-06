package com.example.gesvet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController {

    @GetMapping("/error403")
    public String handle403() {
        return "error403";
    }
}
