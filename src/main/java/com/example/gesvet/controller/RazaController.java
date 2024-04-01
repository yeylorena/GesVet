package com.example.gesvet.controller;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.Especie;
import com.example.gesvet.models.Mascota;
import com.example.gesvet.models.Raza;
import com.example.gesvet.models.User;
import com.example.gesvet.service.EspecieService;
import com.example.gesvet.service.RazaService;
import com.example.gesvet.service.UserService;
import java.security.Principal;
import java.util.Optional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/razas")
public class RazaController {

    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RazaController.class);

    @Autowired
    private RazaService razaService;

    @Autowired
    private EspecieService especieService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String razasAndEspecies(Model model, Authentication authentication, Principal principal) {
        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario actual
        String username = authentication.getName();

        // Buscar al usuario por su nombre de usuario
        User user = userService.findByUsername(username);

        // Crear un objeto UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setDireccion(user.getDireccion());
        userDto.setTelefono(user.getTelefono());
        userDto.setRole(user.getRole());
        userDto.setAcercade(user.getAcercade());
        userDto.setImagen("/images/" + user.getImagen());
        model.addAttribute("userDto", userDto);

        model.addAttribute("razas", razaService.findAll());
        model.addAttribute("especies", especieService.findAll());

        return "mascotas/razaEspecie";
    }

    @GetMapping("/create")
    public String create(Model model, Authentication authentication, Principal principal) {
        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario actual
        String username = authentication.getName();

        // Buscar al usuario por su nombre de usuario
        User user = userService.findByUsername(username);

        // Crear un objeto UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setDireccion(user.getDireccion());
        userDto.setTelefono(user.getTelefono());
        userDto.setRole(user.getRole());
        userDto.setAcercade(user.getAcercade());
        userDto.setImagen("/images/" + user.getImagen());
        model.addAttribute("userDto", userDto);
        return "mascotas/create";
    }

    @GetMapping("/createEs")
    public String creates(Model model, Authentication authentication, Principal principal) {
        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario actual
        String username = authentication.getName();

        // Buscar al usuario por su nombre de usuario
        User user = userService.findByUsername(username);

        // Crear un objeto UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setDireccion(user.getDireccion());
        userDto.setTelefono(user.getTelefono());
        userDto.setRole(user.getRole());
        userDto.setAcercade(user.getAcercade());
        userDto.setImagen("/images/" + user.getImagen());
        model.addAttribute("userDto", userDto);
        return "mascotas/CreateEspecie";
    }

    // metodo para guardar la raza en la base de datos 
    @PostMapping("/save")
    public String save(Raza raza, Model model, Authentication authentication, Principal principal, RedirectAttributes redirectAttributes) {
        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario actual
        String username = authentication.getName();

        // Buscar al usuario por su nombre de usuario
        User user = userService.findByUsername(username);

        // Crear un objeto UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setDireccion(user.getDireccion());
        userDto.setTelefono(user.getTelefono());
        userDto.setRole(user.getRole());
        userDto.setAcercade(user.getAcercade());
        userDto.setImagen("/images/" + user.getImagen());
        model.addAttribute("userDto", userDto);
        LOGGER.info("Este es el objeto de la raza: {}", raza);
        Mascota m = new Mascota(1, "", "", "", "", "", "");
        if (razaService.existsByNombre(raza.getNombre())) {
            // Agregar un mensaje de flash para mostrar en la vista
            redirectAttributes.addFlashAttribute("razaexiste", "Ya existe una raza con el mismo nombre.");
            return "redirect:/razas/create";
        }
        razaService.save(raza);
        redirectAttributes.addFlashAttribute("success", true);

        return "redirect:/razas";
    }

    @PostMapping("/saveEspecie")
    public String saveEspecie(Especie especie, Model model, Authentication authentication, Principal principal, RedirectAttributes redirectAttributes) {
        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario actual
        String username = authentication.getName();

        // Buscar al usuario por su nombre de usuario
        User user = userService.findByUsername(username);

        // Crear un objeto UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setDireccion(user.getDireccion());
        userDto.setTelefono(user.getTelefono());
        userDto.setRole(user.getRole());
        userDto.setAcercade(user.getAcercade());
        userDto.setImagen("/images/" + user.getImagen());
        model.addAttribute("userDto", userDto);
        LOGGER.info("Este es el objeto de la especie: {}", especie);
        if (especieService.existsByNombre(especie.getNombre())) {
            // Agregar un mensaje de flash para mostrar en la vista
            redirectAttributes.addFlashAttribute("especieexiste", "Ya existe una especie con el mismo nombre.");
            return "redirect:/razas/createEs";
        }
        especieService.save(especie);
        redirectAttributes.addFlashAttribute("especiecreada", "Especie agregada con éxito");
        return "redirect:/razas";
    }

    // metodo para editar la raza 
    @GetMapping("/editarRaza/{id}")
    public String editar(@PathVariable Integer id, Model model, Authentication authentication, Principal principal) {
        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario actual
        String username = authentication.getName();

        // Buscar al usuario por su nombre de usuario
        User user = userService.findByUsername(username);

        // Crear un objeto UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setDireccion(user.getDireccion());
        userDto.setTelefono(user.getTelefono());
        userDto.setRole(user.getRole());
        userDto.setAcercade(user.getAcercade());
        userDto.setImagen("/images/" + user.getImagen());
        model.addAttribute("userDto", userDto);
        Raza raza = new Raza();
        Optional<Raza> optionalRaza = razaService.get(id);
        raza = optionalRaza.get();
        model.addAttribute("Raza", raza);
        return "mascotas/editarRaza";
    }

    @GetMapping("/editarEspecie/{id}")
    public String editarEs(@PathVariable Integer id, Model model, Authentication authentication, Principal principal) {
        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el nombre de usuario actual
        String username = authentication.getName();

        // Buscar al usuario por su nombre de usuario
        User user = userService.findByUsername(username);

        // Crear un objeto UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setDireccion(user.getDireccion());
        userDto.setTelefono(user.getTelefono());
        userDto.setRole(user.getRole());
        userDto.setAcercade(user.getAcercade());
        userDto.setImagen("/images/" + user.getImagen());
        model.addAttribute("userDto", userDto);
        Especie especie = new Especie();
        Optional<Especie> optionalEspecie = especieService.get(id);
        especie = optionalEspecie.get();
        model.addAttribute("especieEdit", especie);
        return "mascotas/editarEspecie";
    }
    // metodo para actualizar la edicción y guardar 

    @PostMapping("/update")
    public String update(Raza raza, RedirectAttributes redirectAttributes) {
        razaService.update(raza);
        redirectAttributes.addFlashAttribute("razaactualizada", "Raza actualizada con éxito");
        return "redirect:/razas";
    }

    @PostMapping("/updateEspecie")
    public String updates(Especie especie, RedirectAttributes redirectAttributes) {
        especieService.update(especie);
        redirectAttributes.addFlashAttribute("especieactualizada", "Especie actualizada con éxito");
        return "redirect:/razas";
    }

    // método para eliminar la raza 
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Raza r = razaService.get(id).orElse(null);
        r.setActivo(false);
        razaService.update(r);
        redirectAttributes.addFlashAttribute("razaEliminada", "Raza eliminada con éxito");
        return "redirect:/razas";
    }

    @GetMapping("/deleteEspecie/{id}")
    public String deletes(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Especie e = especieService.get(id).orElse(null);
        e.setActivo(false);
        especieService.update(e);
        redirectAttributes.addFlashAttribute("especieEliminada", "Especie eliminada con éxito");
        return "redirect:/razas";
    }

}
