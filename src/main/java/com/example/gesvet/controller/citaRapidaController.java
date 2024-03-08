package com.example.gesvet.controller;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.Especie;
import com.example.gesvet.models.Mascota;
import com.example.gesvet.models.User;
import com.example.gesvet.models.citaRapida;
import com.example.gesvet.repository.citaRapidaRepository;
import com.example.gesvet.service.EspecieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.gesvet.service.UserService;
import com.example.gesvet.service.citaRapidaService;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/citasRapidas")
public class citaRapidaController {

    @Autowired
    private EspecieService especieService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private citaRapidaService citarapidaservice;

    @Autowired
    private citaRapidaRepository citarapidarepository;

    @GetMapping("")
    public String clientes(Model model, Principal principal) {
        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener la lista de usuarios con rol "ADMIN" y activos
        List<User> usuarios = userService.findByRoleAndActivo("ADMIN", true);

        // Preparar los datos de los usuarios para mostrarlos en la vista
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
            userDto.setImagen(user.getImagen());
            // Obtener las mascotas asociadas a este usuario
            List<Mascota> mascotas = user.getMascotas();

            List<String> nombresMascotas = new ArrayList<>();
            for (Mascota mascota : mascotas) {
                nombresMascotas.add(mascota.getNombre()); // O cualquier otro atributo que desees mostrar
            }

            userDto.setMascotas(nombresMascotas);

            usuariosDto.add(userDto);

        }
        List<citaRapida> citas = citarapidaservice.findAll(); // Suponiendo que tienes un método para obtener todas las citas

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Formatear cada cita en la lista
        citas.forEach(cita -> cita.setFormattedFecha(cita.getInicio().format(formatter)));

        List<Especie> especies = especieService.getAllEspecies();
        model.addAttribute("listadoCitas", citarapidaservice.findAll()); //hago uso del objetio de la clase veterinarioService y hago referencia o llamo al metodo findAll, entonces se envia la variable denominada "listadoVeterinarios"y posteriormente se recibe en la vista
        model.addAttribute("especies", especies);
        model.addAttribute("usuarios", usuariosDto);

        return "citas/citasPresencial";
    }

    @PostMapping("/guardar")
    public String saveM(citaRapida citarapida, Model model, int especie, @RequestParam("usuario") Long idVeterinario, Authentication authentication, Principal principal, RedirectAttributes redirectAttributes) {
        // Obtener el usuario actual
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());

        // Obtener el veterinario seleccionado por su ID
        Optional<User> optionalVeterinario = userService.get(idVeterinario);
        if (optionalVeterinario.isPresent()) {
            User veterinario = optionalVeterinario.get();
            citarapida.setUsuario(user);
            citarapida.setVeterinarioCita(String.valueOf(veterinario.getId()));
            citarapida.setNombreVeterinario(veterinario.getNombre() + " " + veterinario.getApellido()); // Guardar el nombre del veterinario

            // Verificar la disponibilidad de la cita para el veterinario
            LocalDateTime inicio = citarapida.getInicio();
            boolean disponible = citarapidaservice.isCitaDisponibleParaVeterinario(inicio, idVeterinario);

            if (disponible) {
                // Guardar la cita si está disponible
                citarapidaservice.save(citarapida);
                return "redirect:/citasRapidas";
            } else {
                // Manejar el caso en el que la cita no está disponible para el veterinario
                // Puedes simplemente agregar el mensaje de error al modelo
                redirectAttributes.addFlashAttribute("error", "La cita no está disponible para el veterinario en el momento especificado.");
                return "redirect:/citasRapidas";
            }

        }
        return "redirect:/citasRapidas";
    }

    @GetMapping("/finalizar/{citaId}")
    public String finalizarCita(@PathVariable Long citaId) {
        Optional<citaRapida> optionalCita = citarapidaservice.get(citaId);
        if (optionalCita.isPresent()) {
            citaRapida cita = optionalCita.get();
            cita.setEstado("completado");
            citarapidaservice.update(cita); // Actualizar estado en la base de datos
            cita.setFin(LocalDateTime.now());

            // Formatear la fecha de finalización
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            cita.setFormattedFechaFin(cita.getFin().format(formatter));

            // Guardar los cambios en la cita (si es necesario)
            citarapidaservice.save(cita);
        }
        return "redirect:/citasRapidas";
    }

}
