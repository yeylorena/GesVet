package com.example.gesvet.controller;

import com.example.gesvet.models.Especie;
import com.example.gesvet.models.citaRapida;
import com.example.gesvet.service.EspecieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.gesvet.service.citaRapidaService;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class citas {

    @Autowired
    private citaRapidaService citarapidaservice;

    @Autowired
    private EspecieService especieService;

    @GetMapping("/listado")
    public String listarCitas(Model model) {
        List<citaRapida> citas = citarapidaservice.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Formatear cada cita en la lista
        citas.forEach(cita -> cita.setFormattedFecha(cita.getInicio().format(formatter)));

        model.addAttribute("listadoCitas", citas);

        List<Especie> especies = especieService.getAllEspecies();
        model.addAttribute("especies", especies);

        return "citas/citas_vet";
    }

}
