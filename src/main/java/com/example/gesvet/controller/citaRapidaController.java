package com.example.gesvet.controller;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.Especie;
import com.example.gesvet.models.Evento;
import com.example.gesvet.models.Mascota;
import com.example.gesvet.models.ServiciosUser;
import com.example.gesvet.models.User;
import com.example.gesvet.models.citaRapida;
import com.example.gesvet.repository.citaRapidaRepository;
import com.example.gesvet.service.EspecieService;
import com.example.gesvet.service.IServiciosUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.gesvet.service.UserService;
import com.example.gesvet.service.citaRapidaService;
import com.example.gesvet.service.eventoService;
import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private IServiciosUserService serviciouserservice;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private citaRapidaService citarapidaservice;

    @Autowired
    private citaRapidaRepository citarapidarepository;

    @Autowired
    private eventoService eventoService;

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

        List<Especie> especies = especieService.getAllEspecies();
        model.addAttribute("especies", especies);

        List<ServiciosUser> servicios = serviciouserservice.findAll();
        model.addAttribute("servicios", servicios);

        model.addAttribute("usuarios", usuariosDto);
        List<citaRapida> citas = citarapidaservice.findAll();

        model.addAttribute("citas", citas);

        // Agregar las citas agendadas al modelo
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Formatear cada cita en la lista
        citas.forEach(cita -> cita.setFormattedFecha(cita.getInicio().format(formatter)));

        return "citas/citasPresencial";
    }

    @PostMapping("/guardar")
    public String saveM(citaRapida citarapida, Model model, @RequestParam("inicio") String inicioStr, @RequestParam("servicio") Integer idServicio, int especie, @RequestParam("usuario") Integer idVeterinario, Authentication authentication, Principal principal, RedirectAttributes redirectAttributes) {
        // Obtener la fecha y hora de inicio de la cita
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime inicio = LocalDateTime.parse(inicioStr, formatter);
        // Obtener el usuario actual
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());

        ServiciosUser servicio = serviciouserservice.findById(idServicio);
        // Verificar si se encontró el servicio
        if (servicio != null) {
            // Obtener el nombre del servicio
            String nombreServicio = servicio.getNombre(); // Suponiendo que el nombre del servicio se encuentra en el atributo 'nombre' de la entidad Servicio
            // Asignar el nombre del servicio al atributo 'nombreCita' de la citaRapida
            citarapida.setNombreCita(nombreServicio);
        }
        // Obtener el veterinario seleccionado por su ID
        Optional<User> optionalVeterinario = userService.get(idVeterinario);
        if (!optionalVeterinario.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "El veterinario seleccionado no es válido.");
            return "redirect:/citasRapidas";
        }
        User veterinario = optionalVeterinario.get();

        // 1. Validación de fecha y hora futura
        LocalDateTime ahora = LocalDateTime.now();
        if (citarapida.getInicio().isBefore(ahora)) {
            redirectAttributes.addFlashAttribute("error", "No puedes seleccionar fechas pasadas.");
            return "redirect:/citasRapidas";
        }
        // Validar si el día actual es domingo
        if (citarapida.getInicio().getDayOfWeek() == DayOfWeek.SUNDAY) {
            redirectAttributes.addFlashAttribute("error", "Lo sentimos, no se pueden programar citas los domingos.");
            return "redirect:/citasRapidas";
        }
        // Validación de horario de atención (por ejemplo, no después de las 6 PM ni antes de las 9 AM)
        if (citarapida.getInicio().getHour() >= 18 || citarapida.getInicio().getHour() < 9) {
            redirectAttributes.addFlashAttribute("error", "Las citas no pueden ser programadas después de las 6 PM ni antes de las 9 AM.");
            return "redirect:/citasRapidas";
        }
        // Validar que la cita no sea para más de un año en el futuro
        LocalDateTime fechaMaxima = LocalDateTime.now().plusYears(1);
        if (citarapida.getInicio().isAfter(fechaMaxima)) {
            redirectAttributes.addFlashAttribute("error", "No se pueden agendar citas para más de un año.");
            return "redirect:/citasRapidas";
        }

        // Verificar la disponibilidad de la cita para el veterinario
        boolean disponible = citarapidaservice.isCitaDisponibleParaVeterinario(inicio, idVeterinario);

        if (disponible) {
            citarapida.setUsuario(user);
            citarapida.setVeterinarioCita(String.valueOf(veterinario.getId()));
            citarapida.setNombreVeterinario(veterinario.getNombre() + " " + veterinario.getApellido());

            // Guardar la cita
            citarapidaservice.save(citarapida);
            redirectAttributes.addFlashAttribute("success", true);

            return "redirect:/citasRapidas";
        } else {
            // Manejar el caso en el que la cita no está disponible para el veterinario
            // Puedes simplemente agregar el mensaje de error al modelo
            redirectAttributes.addFlashAttribute("error", "La cita no está disponible para el veterinario en la fecha especificada.");
            return "redirect:/citasRapidas";
        }
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

    @GetMapping("/eventos")
    @ResponseBody
    public List<Evento> getEventos() {
        List<Evento> eventos = eventoService.findAll(); // Suponiendo que tienes un método para obtener todos los eventos
        return eventos;
    }

    // Métodos para manejar los eventos
    @PostMapping("/evento/crear")
    public String crearEvento(@ModelAttribute("evento") Evento evento) {
        eventoService.save(evento);
        return "redirect:/citasRapidas";
    }

    @PostMapping("/evento/eliminar/{id}")
    public String eliminarEvento(@PathVariable Integer id) {
        eventoService.delete(id);
        return "redirect:/citasRapidas";
    }

}
