package com.example.gesvet.controller;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.Especie;
import com.example.gesvet.models.Mascota;
import com.example.gesvet.models.ServiciosUser;
import com.example.gesvet.models.User;
import com.example.gesvet.models.citaRapida;
import com.example.gesvet.repository.citaRapidaRepository;
import com.example.gesvet.service.EspecieService;
import com.example.gesvet.service.IServiciosUserService;
import com.example.gesvet.service.MascotaService;
import com.example.gesvet.service.UserService;
import com.example.gesvet.service.citaRapidaService;
import com.example.gesvet.service.eventoService;
import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class agedarcitaUserController {

    @Autowired
    private EspecieService especieService;

    @Autowired
    private IServiciosUserService serviciouserservice;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private citaRapidaService citarapidaservice;

    @Autowired
    private citaRapidaRepository citarapidarepository;

    @Autowired
    private eventoService eventoService;

    @GetMapping("citas")
    public String show(Model model, Authentication authentication, Principal principal) {  //el objeto model lleva información desde el backend hacia la vista
        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Obtener la lista de usuarios con rol "ADMIN" y activos
        List<User> usuarios = userService.findByRoleAndActivo("ADMIN", true);

        // datos de los ADMIN o sea de los veterinarios para ser mostrados en el form select
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

            usuariosDto.add(userDto);
        }

        User user = userService.findByUsername(userDetails.getUsername());
        List<Mascota> mascotas = user.getMascotas();
        model.addAttribute("mascotas", mascotas);

        // Obtener las citas pendientes del usuario
        List<citaRapida> citasPendientes = citarapidaservice.findPendientesByUsuario(user);
        model.addAttribute("citasPendientes", citasPendientes);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (citaRapida cita : citasPendientes) {
            cita.setFormattedFecha(cita.getInicio().format(formatter));
        }
        List<citaRapida> citas = citarapidaservice.findAll();
        model.addAttribute("citas", citas);

        // Obtener los servicios
        List<ServiciosUser> servicios = serviciouserservice.findAll();
        model.addAttribute("servicios", servicios);

        model.addAttribute("usuarios", usuariosDto);
        return "citas/citas_usu";

    }

    @PostMapping("/agendar")
    public String saveM(citaRapida citarapida, Model model, @RequestParam("servicio") Integer idServicio,
            @RequestParam("mascota") Integer idMascota, @RequestParam("usuario") Integer idVeterinario,
            @RequestParam("inicio") String inicioStr, Authentication authentication, Principal principa, RedirectAttributes redirectAttributes) {
        // Obtener la fecha y hora de inicio de la cita
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime inicio = LocalDateTime.parse(inicioStr, formatter);

        // Obtener el usuario actual
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());

        // Obtener el nombre del cliente
        String nombreCliente = user.getNombre(); // Suponiendo que el nombre del cliente se encuentra en el atributo 'nombre' de la entidad User

        // Asignar el nombre del cliente al atributo 'nombreDueño' de la citaRapida
        citarapida.setNombreDueño(nombreCliente);

        // Obtener el servicio seleccionado por su ID
        ServiciosUser servicio = serviciouserservice.findById(idServicio);
        // Verificar si se encontró el servicio
        if (servicio == null) {
            redirectAttributes.addFlashAttribute("error", "El servicio seleccionado no es válido.");
            return "redirect:/citas";
        }

        // Obtener la mascota seleccionada por su ID
        Mascota mascota = mascotaService.findById(idMascota);
        // Verificar si se encontró la mascota
        if (mascota == null) {
            redirectAttributes.addFlashAttribute("error", "La mascota seleccionada no es válida.");
            return "redirect:/citas";
        }

        // Obtener la especie de la mascota
        Especie especie = mascota.getEspecie();
        if (especie == null) {
            redirectAttributes.addFlashAttribute("error", "La especie de la mascota no está especificada.");
            return "redirect:/citas";
        }

        // Obtener el veterinario seleccionado por su ID
        Optional<User> optionalVeterinario = userService.get(idVeterinario);
        if (!optionalVeterinario.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "El veterinario seleccionado no es válido.");
            return "redirect:/citas";
        }
        User veterinario = optionalVeterinario.get();

        // 1. Validación de fecha y hora futura
        LocalDateTime ahora = LocalDateTime.now();
        if (citarapida.getInicio().isBefore(ahora)) {
            redirectAttributes.addFlashAttribute("error", "No puedes seleccionar fechas pasadas.");
            return "redirect:/citas";
        }

        // Validar si el día actual es domingo
        if (citarapida.getInicio().getDayOfWeek() == DayOfWeek.SUNDAY) {
            redirectAttributes.addFlashAttribute("error", "Lo sentimos, no se pueden programar citas los domingos.");
            return "redirect:/citas";
        }

        // Validación de horario de atención (por ejemplo, no después de las 6 PM ni antes de las 9 AM)
        if (citarapida.getInicio().getHour() >= 18 || citarapida.getInicio().getHour() < 9) {
            redirectAttributes.addFlashAttribute("error", "Las citas no pueden ser programadas después de las 6 PM ni antes de las 9 AM.");
            return "redirect:/citas";
        }

        // Validar que la cita no sea para más de un año en el futuro
        LocalDateTime fechaMaxima = LocalDateTime.now().plusYears(1);
        if (citarapida.getInicio().isAfter(fechaMaxima)) {
            redirectAttributes.addFlashAttribute("error", "No se pueden agendar citas para más de un año.");
            return "redirect:/citas";
        }

        // Validar que no haya una cita para la misma mascota en la misma fecha pero con otro veterinario
        if (citarapidarepository.existsByMascotaIdAndInicioAndVeterinarioCitaNot(idMascota, inicio, String.valueOf(idVeterinario))) {
            redirectAttributes.addFlashAttribute("error", "La mascota ya tiene una cita programada para esa fecha con otro veterinario.");
            return "redirect:/citas";
        }

        // Verificar la disponibilidad de la cita para el veterinario
        boolean disponible = citarapidaservice.isCitaDisponibleParaVeterinario(inicio, idVeterinario);

        if (disponible) {
            // Asignar los detalles restantes de la cita
            citarapida.setNombreCita(servicio.getNombre());
            citarapida.setNombreMascota(mascota.getNombre());
            citarapida.setEspecie(especie);
            citarapida.setUsuario(user);
            citarapida.setVeterinarioCita(String.valueOf(veterinario.getId()));
            citarapida.setNombreVeterinario(veterinario.getNombre() + " " + veterinario.getApellido());

            // Guardar la cita
            citarapidaservice.save(citarapida);
            redirectAttributes.addFlashAttribute("success", true);

            return "redirect:/citas";
        } else {
            // Manejar el caso en el que la cita no está disponible para el veterinario
            // Puedes simplemente agregar el mensaje de error al modelo
            redirectAttributes.addFlashAttribute("error", "La cita no está disponible para el veterinario en la fecha especificada.");
            return "redirect:/citas";
        }

    }

}
