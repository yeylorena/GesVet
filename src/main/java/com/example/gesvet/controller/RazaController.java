package com.example.gesvet.controller;

import com.example.gesvet.models.Especie;
import com.example.gesvet.models.Mascota;
import com.example.gesvet.models.Raza;
import com.example.gesvet.service.EspecieService;
import com.example.gesvet.service.RazaService;
import java.util.Optional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/razas")
public class RazaController {

    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RazaController.class);

    @Autowired
    private RazaService razaService;

    @Autowired
    private EspecieService especieService;

    @GetMapping("")
    public String razasAndEspecies(Model model) {
        model.addAttribute("razas", razaService.findAll());
        model.addAttribute("especies", especieService.findAll());
        return "mascotas/razaEspecie";
    }

    @GetMapping("/create")
    public String create() {
        return "mascotas/create";
    }

    @GetMapping("/createEs")
    public String creates() {
        return "mascotas/CreateEspecie";
    }

    // metodo para guardar la raza en la base de datos 
    @PostMapping("/save")
    public String save(Raza raza) {
        LOGGER.info("Este es el objeto de la raza: {}", raza);
        Mascota m = new Mascota(1, "", "", "", "", "", "");
        raza.addMascota(m);
        razaService.save(raza);
        return "redirect:/razas";
    }

    @PostMapping("/saveEspecie")
    public String saveEspecie(Especie especie) {
        LOGGER.info("Este es el objeto de la especie: {}", especie);
        especieService.save(especie);
        return "redirect:/razas";
    }

    // metodo para editar la raza 
    @GetMapping("/editarRaza/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Raza raza = new Raza();
        Optional<Raza> optionalRaza = razaService.get(id);
        raza = optionalRaza.get();
        model.addAttribute("Raza", raza);
        return "mascotas/editarRaza";
    }

    @GetMapping("/editarEspecie/{id}")
    public String editarEs(@PathVariable Integer id, Model model) {
        Especie especie = new Especie();
        Optional<Especie> optionalEspecie = especieService.get(id);
        especie = optionalEspecie.get();
        model.addAttribute("especieEdit", especie);
        return "mascotas/editarEspecie";
    }
    // metodo para actualizar la edicción y guardar 

    @PostMapping("/update")
    public String update(Raza raza) {
        razaService.update(raza);
        return "redirect:/razas";
    }

    @PostMapping("/updateEspecie")
    public String updates(Especie especie) {
        especieService.update(especie);
        return "redirect:/razas";
    }

    // método para eliminar la raza 
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        razaService.delete(id);

        return "redirect:/razas";
    }

    @GetMapping("/deleteEspecie/{id}")
    public String deletes(@PathVariable Integer id) {
        especieService.delete(id);
        return "redirect:/razas";
    }

}
