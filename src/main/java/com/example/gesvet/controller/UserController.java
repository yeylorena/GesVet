package com.example.gesvet.controller;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.User;
import com.example.gesvet.service.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserDetailsService userDetailsService;

    private UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        String username = "";

        if (principal != null) {
            // Obtener el nombre de usuario actual
            username = principal.getName();

            // Obtener los detalles del usuario actual
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            model.addAttribute("userdetail", userDetails);

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
            userDto.setImagen("/images/" + user.getImagen()); // Asegúrate de tener la ruta correcta

            model.addAttribute("userDto", userDto);
        }

        return "Inicio_usu";
    }

    @GetMapping("/homes")
    public String homes(Model model, Principal principal) {
        String username = "";

        if (principal != null) {
            // Obtener el nombre de administrador actual
            username = principal.getName();

            // Obtener los detalles del administrador actual
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            model.addAttribute("userdetail", userDetails);

            // Buscar al administrador por su nombre de usuario
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
            userDto.setImagen("/images/" + user.getImagen()); // Asegúrate de tener la ruta correcta

            model.addAttribute("userDto", userDto);
        }

        return "Inicio_admin";
    }

    @GetMapping("/login")
    public String login(Model model, UserDto userDto) {
        model.addAttribute("user", userDto);
        return "Login_usu";
    }

    @GetMapping("/register")
    public String register(Model model, UserDto userDto) {

        model.addAttribute("user", userDto);
        return "Registro_Usu";
    }

    @PostMapping("/register")
    public String registerSave(@Valid @ModelAttribute("user") UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "Registro_Usu"; // Devolver al formulario de registro si hay errores de validación
        }

        User user = userService.findByUsername(userDto.getUsername());

        if (user != null) {
            model.addAttribute("userexist", user);
            return "Registro_Usu";
        }

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            model.addAttribute("passwordMismatch", true);
            return "Registro_Usu";
        }

        userService.save(userDto);
        return "redirect:/register?success";
    }

}
