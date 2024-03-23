package com.example.gesvet.controller;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.jwtUtil.JwtUtils;
import com.example.gesvet.models.Especie;
import com.example.gesvet.models.Mascota;
import com.example.gesvet.models.Raza;
import com.example.gesvet.models.User;
import com.example.gesvet.models.respuesta;
import com.example.gesvet.service.EspecieService;
import com.example.gesvet.service.MascotaService;
import com.example.gesvet.service.RazaService;
import com.example.gesvet.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/api/mascota")
public class mascotaUsuarioControllerapi {

    private static final Logger LOGGER = LoggerFactory.getLogger(mascotaUsuarioControllerapi.class);

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

    @GetMapping("/detailsmascota")
    public ResponseEntity<Object> getMascotas(HttpServletRequest request) {
        String jwtToken = extractTokenFromRequest(request);

        // Validar el token
        Claims claims = JwtUtils.extractClaims(jwtToken);

        if (claims != null) {
            String username = claims.getSubject();

            // Obtener todas las razas y especies disponibles
            List<Raza> razas = razaService.getAllRazas();
            List<Especie> especies = especieService.getAllEspecies();

            // Obtener las mascotas del usuario actual solamente
            List<Mascota> mascotas = userService.findByUsername(username).getMascotas();

            // Construir un objeto que contenga toda la información necesaria para la respuesta
            Map<String, Object> response = new HashMap<>();

            response.put("razas", razas);
            response.put("especies", especies);
            response.put("mascotas", mascotas);

            // Devolver una respuesta exitosa con la información
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/mascota/save")
    public ResponseEntity<Object> saveMascota(@RequestBody Mascota mascotaRequest, HttpServletRequest request) {
        String jwtToken = extractTokenFromRequest(request);

        // Validar el token
        Claims claims = JwtUtils.extractClaims(jwtToken);

        if (claims != null) {
            String username = claims.getSubject();

            // Obtener el usuario a partir del nombre de usuario en el token (subject)
            User user = userService.findByUsername(username);

            try {
                // Crear una nueva instancia de Mascota con los datos recibidos
                Mascota mascota = new Mascota();
                mascota.setUsuario(user);
                mascota.setNombre(mascotaRequest.getNombre());
                mascota.setImagen(mascotaRequest.getImagen());
                mascota.setColor(mascotaRequest.getColor());
                mascota.setEdad(mascotaRequest.getEdad());
                mascota.setTiempo(mascotaRequest.getTiempo());
                mascota.setGenero(mascotaRequest.getGenero());

                Especie especieRequest = mascotaRequest.getEspecie();
                if (especieRequest != null && especieRequest.getId() != null) {
                    int especieId = especieRequest.getId();
                    Especie especie = especieService.getById(especieId);
                    mascota.setEspecie(especie);
                } else {
                    // Manejar el caso en que la especie en la solicitud es nula o no tiene ID
                }

                Raza razaRequest = mascotaRequest.getRaza();
                if (razaRequest != null && razaRequest.getId() != null) {
                    int razaId = razaRequest.getId();
                    Raza raza = razaService.getById(razaId);
                    mascota.setRaza(raza);
                } else {
                    // Manejar el caso en que la raza en la solicitud es nula o no tiene ID
                }

                // Guardar la mascota
                mascotaService.save(mascota);

                // Crear una respuesta exitosa con un mensaje personalizado
                var respuesta = new respuesta("Creado", "Mascota guardada correctamente");
                return ResponseEntity.status(HttpStatus.OK).body(respuesta);
            } catch (Exception e) {
                // Manejar cualquier excepción que pueda ocurrir al guardar la mascota
                e.printStackTrace();
                // Devolver una respuesta de error si ocurre una excepción
                var respuestaError = new respuesta("error", "Error al guardar la mascota");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
            }
        } else {
            // Devolver una respuesta de error si el token no es válido
            var respuestaNoAutorizado = new respuesta("error", "Usuario no autorizado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuestaNoAutorizado);
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

    // Método auxiliar para extraer el token del encabezado de la solicitud
    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
