/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.gesvet.controller;

import com.example.gesvet.models.Clientes;
import com.example.gesvet.repository.ClienteRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author sofia
 */
@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @GetMapping("")
    public String clientes(Model model) {
        List<Clientes> clientes = clienteRepository.findAll();
        int size = clientes.size();
        System.out.println(size);

        model.addAttribute("clientes", clientes);
        return "clientes/Gestion_Clientes";

    }
    @Autowired
    private ClienteRepository clienteRepository;


}
