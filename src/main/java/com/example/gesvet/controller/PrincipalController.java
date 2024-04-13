package com.example.gesvet.controller;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.User;
import com.example.gesvet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PrincipalController {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserService userService;

    @GetMapping("/principal")
    public String Principal(Model model, Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            // Si el usuario es un administrador, redirige a la ruta /homes
            model.addAttribute("perfilUrl", "/perfil_admin");
            model.addAttribute("ayudaUrl", "/ayuda");
            model.addAttribute("redirectUrl", "/homes");
        } else if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("USER"))) {
            // Si el usuario es un usuario regular, redirige a la ruta /home
            model.addAttribute("perfilUrl", "/perfil");
            model.addAttribute("redirectUrl", "/home");
            model.addAttribute("ayudaUrl", "/ayudausuario");
        } else {
            // Si el usuario no está autenticado, muestra los botones predeterminados
            model.addAttribute("isAdmin", false);
            return "index";
        }

        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener el objeto UserDto del usuario autenticado
        String username = authentication.getName();
        User user = userService.findByUsername(username);
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

        model.addAttribute("isAdmin", true);
        return "index";
    }
}
