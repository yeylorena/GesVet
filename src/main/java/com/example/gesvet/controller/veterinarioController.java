package com.example.gesvet.controller;

import com.example.gesvet.models.Veterinario;
import com.example.gesvet.service.veterinarioService;
import java.util.Optional;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/veterinarios")
public class veterinarioController {

    private final Logger LOGGER = LoggerFactory.getLogger(veterinarioController.class);

    @Autowired //el mismo contenerdor de spring intancea el objeto
    private veterinarioService VeterinarioService;  // esta es la interfaz definida en la que se encuentra todos los metodos crud

    @GetMapping("")
    public String show(Model model) {  //el objeto model lleva información desde el backend hacia la vista
        model.addAttribute("listadoVeterinarios", VeterinarioService.findAll()); //hago uso del objetio de la clase veterinarioService y hago referencia o llamo al metodo findAll, entonces se envia la variable denominada "listadoVeterinarios"y posteriormente se recibe en la vista

        return "veterinarios/gestionveterinaria";

    }

    @PostMapping("/save")
    public String save(Veterinario veterinario, Model model) {
        LOGGER.info("este es el objeto de la vista veterinarios{}", veterinario);
        VeterinarioService.save(veterinario);
        model.addAttribute("registroExitoso", true);
        return "redirect:/veterinarios";  //es una petición directamente a nuestro controlador de veterinarios

    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {   //PathVariable esta anotacion mapea el id o la variable que viene en la url y pasarla a la variable que esta contigua a la anotacion pathVaribale
        Veterinario veterinario = new Veterinario();
        Optional<Veterinario> optionalVeterinario = VeterinarioService.get(id);
        veterinario = optionalVeterinario.get();  //trae el veterinario que hemos mandado a buscar

        LOGGER.info("Veterinario buscado: {}", veterinario);
        model.addAttribute("veterinariosEditar", veterinario); //Al objeto model llamamos el metodo addAttribute y le declaramos una variable llamada "veterinariosEditar" que la lleve a la vista y se le pasa el valor de lo que tiene el objeto de la clase Veterinario la cual denominamos "veterinario"

        //Luego nos envía a la vista todo el objeto buscado
        return "veterinarios/editar";
    }

    @PostMapping("/update")
    public String update(Veterinario veterinario) {  //recibe como parametro un objeto de tipo Veterinario
        VeterinarioService.update(veterinario);
        return "redirect:/veterinarios";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        VeterinarioService.delete(id);
        return "redirect:/veterinarios";
    }

}
