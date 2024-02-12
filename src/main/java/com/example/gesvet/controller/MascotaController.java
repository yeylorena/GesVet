/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.gesvet.controller;

import com.example.gesvet.models.Mascota;
import com.example.gesvet.repository.MascotaRepository;
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
@RequestMapping("/masotas")
public class MascotaController {
    @GetMapping("")
    public String mascotas(Model model) {
        List<Mascota> mascota = MascotaRepository.findAll();
        int size = mascota.size();
        System.out.println(size);

        model.addAttribute("mascota", mascota);
        return "clientes/Gestion_Clientes";

    }
    @Autowired
    private MascotaRepository MascotaRepository;

}
