package com.example.gesvet.controller;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.Especie;
import com.example.gesvet.models.User;
import com.example.gesvet.models.citaRapida;
import com.example.gesvet.service.EspecieService;
import com.example.gesvet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.gesvet.service.citaRapidaService;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class citas {

    @Autowired
    private citaRapidaService citarapidaservice;

    @Autowired
    private EspecieService especieService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @GetMapping("/listado")
    public String listarCitas(Model model, Authentication authentication, Principal principal) {
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
        List<citaRapida> citas = citarapidaservice.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Formatear cada cita en la lista
        citas.forEach(cita -> cita.setFormattedFecha(cita.getInicio().format(formatter)));

        model.addAttribute("listadoCitas", citas);
        model.addAttribute("userDto", userDto);

        List<Especie> especies = especieService.getAllEspecies();
        model.addAttribute("especies", especies);

        return "citas/citas_vet";
    }

}
