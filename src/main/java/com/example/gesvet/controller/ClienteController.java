package com.example.gesvet.controller;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.Mascota;
import com.example.gesvet.models.User;
import com.example.gesvet.service.UserService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String clientes(Model model, Principal principal) {
        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener la lista de usuarios con rol "USER" y activos
        List<User> usuarios = userService.findByRoleAndActivo("USER", true);
        // Obtener la lista de usuarios con rol "ADMIN" y activos

        // Preparar los datos de los usuarios con rol "USER" para mostrarlos en la vista
        List<UserDto> usuariosDto = new ArrayList<>();
        for (User user : usuarios) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setNombre(user.getNombre());
            userDto.setApellido(user.getApellido());
            userDto.setDireccion(user.getDireccion());
            userDto.setTelefono(user.getTelefono());
            userDto.setRole(user.getRole());
            userDto.setAcercade(user.getAcercade());

            // Obtener las mascotas asociadas a este usuario
            List<Mascota> mascotas = user.getMascotas();
            List<String> nombresMascotas = new ArrayList<>();
            for (Mascota mascota : mascotas) {
                nombresMascotas.add(mascota.getNombre()); // O cualquier otro atributo que desees mostrar
            }
            userDto.setMascotas(nombresMascotas);

            usuariosDto.add(userDto);
        }
        model.addAttribute("usuarios", usuariosDto);

        return "clientes/clientes";

    }

    @GetMapping("/mascotas")
    public String mostrarMascotas(@RequestParam Integer userId, Authentication authentication, Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener la lista de usuarios con rol "USER" y activos
        List<User> usuarios = userService.findByRoleAndActivo("USER", true);
        // Obtener la lista de usuarios con rol "ADMIN" y activos

        // Preparar los datos de los usuarios con rol "USER" para mostrarlos en la vista
        List<UserDto> usuariosDto = new ArrayList<>();
        for (User user : usuarios) {
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

            // Obtener las mascotas asociadas a este usuario
            List<Mascota> mascotas = user.getMascotas();
            List<String> nombresMascotas = new ArrayList<>();
            for (Mascota mascota : mascotas) {
                nombresMascotas.add(mascota.getNombre()); // O cualquier otro atributo que desees mostrar
            }
            userDto.setMascotas(nombresMascotas);

            usuariosDto.add(userDto);
        }
        model.addAttribute("usuarios", usuariosDto);
        User cliente = userService.findById(userId);
        List<Mascota> mascotas = cliente.getMascotas();

        model.addAttribute("cliente", cliente);
        model.addAttribute("mascotas", mascotas);

        return "clientes/consultar_mascotas";
    }

}
