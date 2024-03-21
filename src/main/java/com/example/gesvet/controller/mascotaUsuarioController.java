package com.example.gesvet.controller;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.Especie;
import com.example.gesvet.models.Mascota;
import com.example.gesvet.models.Raza;
import com.example.gesvet.models.User;
import com.example.gesvet.service.EspecieService;
import com.example.gesvet.service.MascotaService;
import com.example.gesvet.service.RazaService;
import com.example.gesvet.service.UserService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mascotasUsuarios")
public class mascotaUsuarioController {

    private static final Logger LOGGER = LoggerFactory.getLogger(mascotaUsuarioController.class);

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private RazaService razaService;

    @Autowired
    private EspecieService especieService;
    @Autowired
    private UserDetailsService userDetailsService;

    // id usuarios 
    @Autowired
    private UserService userService;

    @GetMapping("")
    public String mascotas(Model model, Authentication authentication, Principal principal) {

        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());  // Aquí estás obteniendo el ID del usuario
        userDto.setUsername(user.getUsername());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setDireccion(user.getDireccion());
        userDto.setTelefono(user.getTelefono());
        userDto.setRole(user.getRole());
        userDto.setAcercade(user.getAcercade());
        userDto.setImagen("/images/" + user.getImagen());

        model.addAttribute("userDto", userDto);

        // Obtener todas las razas y especies disponibles
        List<Raza> razas = razaService.getAllRazas();
        model.addAttribute("razas", razas);

        List<Especie> especies = especieService.getAllEspecies();
        model.addAttribute("especies", especies);

        // Obtener las mascotas del usuario actual solamente
        List<Mascota> mascotas = user.getMascotas(); // Aquí obtenemos las mascotas asociadas al usuario actual
        model.addAttribute("mascotas", mascotas);

        //obtener la información de las razas 
        return "mascotasUsuario/misMascotas";
    }

    @GetMapping("/crearMascota")
    public String crearM(Model model, Authentication authentication, Principal principal) {

        // Nuevo objeto Mascota para el formulario
        Mascota mascota = new Mascota();
        model.addAttribute("mascota", mascota);

        return "mascotasUsuario/misMascotas";
    }

    @PostMapping("/saveM")
    public String saveM(Mascota mascota, Model model, @RequestParam("file") MultipartFile imagen, int especie, Authentication authentication, Principal principal, RedirectAttributes redirectAttributes) {

        // Obtener el usuario actual
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());

        //traer id de usuario
        mascota.setUsuario(user);
        try {
            var objEspecie = especieService.get(especie);
            mascota.setEspecie(objEspecie.get());
            // Manejar el archivo de imagen
            if (!imagen.isEmpty()) {
                // Realizar la escritura del archivo
                byte[] bytesImg = imagen.getBytes();
                Path directorioImgenes = Paths.get("images//"); // Ajustar según necesidades
                String rutaAbsoluta = directorioImgenes.toFile().getAbsolutePath();
                Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + imagen.getOriginalFilename());
                Files.write(rutaCompleta, bytesImg);
                mascota.setImagen(imagen.getOriginalFilename());
            }

            // Guardar la mascota
            mascotaService.save(mascota);
            redirectAttributes.addFlashAttribute("success", true);

            // Obtener todas las mascotas nuevamente
            List<Mascota> mascotasActualizadas = mascotaService.findAll();
            model.addAttribute("mascotas", mascotasActualizadas);

            // Redirigir a la página de "Mis mascotas"
            return "redirect:/mascotasUsuarios";
        } catch (IOException e) {
            // Manejar cualquier excepción de E/S (Input/Output) que pueda ocurrir al guardar la imagen
            e.printStackTrace();

            return "errorPage"; // Reemplaza con la página de error adecuada
        }

    }

    // metodo para editar la mascota 
    @GetMapping("/editarMascota/{id}")
    public String edit(@PathVariable Integer id, Model model, Authentication authentication, Principal principal) {   //PathVariable esta anotacion mapea el id o la variable que viene en la url y pasarla a la variable que esta contigua a la anotacion pathVaribale
        // Obtener los detalles del usuario actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());  // Aquí estás obteniendo el ID del usuario
        userDto.setUsername(user.getUsername());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setDireccion(user.getDireccion());
        userDto.setTelefono(user.getTelefono());
        userDto.setRole(user.getRole());
        userDto.setAcercade(user.getAcercade());
        userDto.setImagen("/images/" + user.getImagen());

        model.addAttribute("userDto", userDto);
        Mascota mascota = new Mascota();
        Optional<Mascota> optionalMascota = mascotaService.get(id);
        mascota = optionalMascota.get();  //trae el veterinario que hemos mandado a buscar

        LOGGER.info("Mascota buscada: {}", mascota);
        model.addAttribute("mascotasEditar", mascota); //Al objeto model llamamos el metodo addAttribute y le declaramos una variable llamada "veterinariosEditar" que la lleve a la vista y se le pasa el valor de lo que tiene el objeto de la clase Veterinario la cual denominamos "veterinario"
        User usuarioMascota = mascota.getUsuario();
        model.addAttribute("usuarioMascota", usuarioMascota);

        // Obtener todas las razas y especies disponibles
        List<Raza> razas = razaService.getAllRazas();
        model.addAttribute("razas", razas);

        List<Especie> especies = especieService.getAllEspecies();
        model.addAttribute("especies", especies);

        //Luego nos envía a la vista todo el objeto buscado
        return "mascotasUsuario/editarMascota";
    }

    @PostMapping("/update")
    public String update(Mascota mascota, @RequestParam(value = "file", required = false) MultipartFile imagen) {
        if (imagen != null && !imagen.isEmpty()) {
            try {
                // Procesa la nueva imagen si se ha seleccionado
                byte[] bytesImg = imagen.getBytes();
                Path directorioImagenes = Paths.get("images/");
                String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
                Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + imagen.getOriginalFilename());
                Files.write(rutaCompleta, bytesImg);
                mascota.setImagen(imagen.getOriginalFilename());
            } catch (IOException e) {
                // Maneja cualquier excepción de E/S que pueda ocurrir al guardar la imagen
                e.printStackTrace();
                return "errorPage";
            }
        } else {
            // Si no se selecciona una nueva imagen, mantén la imagen actual
            Mascota mascotaActual = mascotaService.get(mascota.getId()).orElse(null);
            if (mascotaActual != null) {
                mascota.setImagen(mascotaActual.getImagen());
            }
        }

        // Actualiza la mascota en la base de datos
        mascotaService.update(mascota);

        // Redirige al usuario a la página principal de mascotas
        return "redirect:/mascotasUsuarios";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        mascotaService.delete(id);
        return "redirect:/mascotasUsuarios";
    }

}
