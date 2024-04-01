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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        // Obtener todas las citas
        List<citaRapida> todasLasCitas = citarapidaservice.findAll();

        // Filtrar las citas pendientes
        List<citaRapida> citasPendientes = todasLasCitas.stream()
                .filter(cita -> "Pendiente".equals(cita.getEstado()))
                .sorted(Comparator.comparing(citaRapida::getInicio))
                .collect(Collectors.toList());

        // Filtrar las citas por estado "cancelado" y "completado"
        List<citaRapida> citasCanceladasYCompletadas = todasLasCitas.stream()
                .filter(cita -> "Cancelado".equals(cita.getEstado()) || "Completado".equals(cita.getEstado()))
                .sorted(Comparator.comparing(citaRapida::getInicio))
                .collect(Collectors.toList());

        // Formatear la fecha de cada cita en las listas
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        citasPendientes.forEach(cita -> cita.setFormattedFecha(cita.getInicio().format(formatter)));
        citasCanceladasYCompletadas.forEach(cita -> cita.setFormattedFecha(cita.getInicio().format(formatter)));

        // Agregar las listas al modelo
        model.addAttribute("citasPendientes", citasPendientes);
        model.addAttribute("citasCompletadas", citasCanceladasYCompletadas);
        model.addAttribute("userDto", userDto);

        List<Especie> especies = especieService.getAllEspecies();
        model.addAttribute("especies", especies);

        return "citas/citas_vet";
    }

    @GetMapping("/finalizar/{citaId}")
    public String finalizarCita(@PathVariable Long citaId, RedirectAttributes redirectAttributes) {
        Optional<citaRapida> optionalCita = citarapidaservice.get(citaId);
        if (optionalCita.isPresent()) {
            citaRapida cita = optionalCita.get();
            LocalDateTime ahora = LocalDateTime.now();

            // Verificar si la fecha y hora actual es igual o después de la fecha y hora de inicio de la cita
            if (!ahora.isBefore(cita.getInicio())) {
                cita.setEstado("Completado");
                cita.setFin(LocalDateTime.now());

                // Formatear la fecha de finalización
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                cita.setFormattedFechaFin(cita.getFin().format(formatter));

                // Actualizar la cita en la base de datos
                citarapidaservice.save(cita);
                redirectAttributes.addFlashAttribute("success", true);
            } else {
                redirectAttributes.addFlashAttribute("error", "No se puede finalizar la cita antes de su fecha de inicio.");
            }
        }
        return "redirect:/listado";
    }

    @GetMapping("/cancelar/{citaId}")
    public String cancelarCita(@PathVariable Long citaId, RedirectAttributes redirectAttributes) {
        Optional<citaRapida> optionalCita = citarapidaservice.get(citaId);
        if (optionalCita.isPresent()) {
            citaRapida cita = optionalCita.get();
            cita.setEstado("Cancelado");
            citarapidaservice.update(cita); // Actualizar estado en la base de datos
            cita.setFin(LocalDateTime.now());

            // Formatear la fecha de finalización
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            cita.setFormattedFechaFin(cita.getFin().format(formatter));

            // Guardar los cambios en la cita (si es necesario)
            citarapidaservice.save(cita);
            redirectAttributes.addFlashAttribute("cancelar", true);
        }
        return "redirect:/listado";
    }
}
