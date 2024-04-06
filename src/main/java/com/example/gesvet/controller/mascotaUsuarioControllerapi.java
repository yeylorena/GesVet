package com.example.gesvet.controller;

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
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
            var respuesta = new respuesta(
                    "error",
                    "Usuario no autorizado"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
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

            // Validar que los campos obligatorios no estén vacíos
            if (StringUtils.isEmpty(mascotaRequest.getNombre()) || StringUtils.isEmpty(mascotaRequest.getColor())
                    || StringUtils.isEmpty(mascotaRequest.getEdad()) || StringUtils.isEmpty(mascotaRequest.getTiempo())
                    || StringUtils.isEmpty(mascotaRequest.getGenero()) || StringUtils.isEmpty(mascotaRequest.getDetalles())
                    || StringUtils.isEmpty(mascotaRequest.getEspecie()) || StringUtils.isEmpty(mascotaRequest.getRaza())
                    || StringUtils.isEmpty(mascotaRequest.getImagen())) {
                var respuesta = new respuesta(
                        "error",
                        "Por favor, complete todos los campos obligatorios"
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
            }

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
                mascota.setDetalles(mascotaRequest.getDetalles());

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

    @GetMapping("/editarMascota/{id}")
    public ResponseEntity<Object> editMascota(@PathVariable Integer id, HttpServletRequest request) {
        String jwtToken = extractTokenFromRequest(request);

        // Validar el token
        Claims claims = JwtUtils.extractClaims(jwtToken);

        if (claims != null) {
            String username = claims.getSubject();
            User user = userService.findByUsername(username);

            // Buscar la mascota por su ID y el ID del usuario
            Optional<Mascota> optionalMascota = mascotaService.findByUsuarioIdAndMascotaId(user.getId(), id);
            if (optionalMascota.isPresent()) {
                Mascota mascota = optionalMascota.get();

                // Construir un objeto que contenga la información de la mascota y las listas de razas y especies
                Map<String, Object> response = new HashMap<>();
                response.put("mascota", mascota);

                // Devolver una respuesta exitosa con la información de la mascota
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                // Si no se encuentra la mascota, devolver una respuesta de error
                var respuesta = new respuesta(
                        "error",
                        "Mascota no encontrada"
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
            }
        } else {
            // Si el token no es válido, devolver una respuesta de error

            var respuesta = new respuesta(
                    "error",
                    "Usuario no autenticado"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateMascota(@RequestBody Mascota mascota, HttpServletRequest request) {
        // Obtener el token JWT de la solicitud
        String jwtToken = extractTokenFromRequest(request);

        // Validar el token JWT
        Claims claims = JwtUtils.extractClaims(jwtToken);
        if (claims != null) {
            String username = claims.getSubject();
            User user = userService.findByUsername(username);
            if (user != null) {

                // Validar que los campos obligatorios no estén vacíos
                if (StringUtils.isEmpty(mascota.getNombre()) || StringUtils.isEmpty(mascota.getColor())
                        || StringUtils.isEmpty(mascota.getEdad()) || StringUtils.isEmpty(mascota.getTiempo())
                        || StringUtils.isEmpty(mascota.getGenero()) || StringUtils.isEmpty(mascota.getDetalles())
                        || StringUtils.isEmpty(mascota.getEspecie()) || StringUtils.isEmpty(mascota.getRaza())
                        || StringUtils.isEmpty(mascota.getImagen())) {
                    var respuesta = new respuesta(
                            "error",
                            "Por favor, complete todos los campos obligatorios"
                    );
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
                }
                // Asignar el usuario a la mascota que se está actualizando
                mascota.setUsuario(user);

                try {
                    // Actualiza la mascota en la base de datos
                    mascotaService.update(mascota);

                    // Devuelve una respuesta exitosa
                    respuesta respuesta = new respuesta(
                            "Éxito",
                            "Mascota actualizada exitosamente"
                    );
                    return ResponseEntity.status(HttpStatus.OK).body(respuesta);
                } catch (Exception e) {
                    // Maneja cualquier excepción que pueda ocurrir al actualizar la mascota
                    e.printStackTrace();
                    // Devuelve una respuesta de error
                    respuesta respuesta = new respuesta(
                            "error",
                            "Error al actualizar la mascota"
                    );
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
                }
            } else {
                respuesta respuesta = new respuesta(
                        "error",
                        "Usuario no autenticado"
                );
                // Si el usuario no está autenticado, devuelve una respuesta de error
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
            }
        } else {
            // Si el token no es válido, devuelve una respuesta de error
            respuesta respuesta = new respuesta(
                    "error",
                    "Token JWT inválido"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        }
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Object> deleteMascota(@PathVariable Integer id, HttpServletRequest request) {
        // Obtener el token JWT de la solicitud
        String jwtToken = extractTokenFromRequest(request);

        // Validar el token JWT
        Claims claims = JwtUtils.extractClaims(jwtToken);
        if (claims != null) {
            try {
                // Buscar la mascota por su ID
                Optional<Mascota> optionalMascota = mascotaService.get(id);

                if (optionalMascota.isPresent()) {
                    Mascota mascota = optionalMascota.get();
                    // Desactivar la mascota estableciendo el estado activo en false
                    mascota.setActivo(false);
                    // Actualizar la mascota en la base de datos
                    mascotaService.update(mascota);
                    // Devolver una respuesta exitosa
                    respuesta respuesta = new respuesta(
                            "Creado",
                            "Mascota eliminada exitosamente"
                    );
                    return ResponseEntity.ok(respuesta);
                } else {
                    // Si no se encuentra la mascota, devolver una respuesta de error
                    var respuesta = new respuesta(
                            "error",
                            "Mascota no encontrada"
                    );
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
                }
            } catch (Exception e) {
                // Si ocurre alguna excepción, devolver una respuesta de error
                var respuesta = new respuesta(
                        "error",
                        "Error al procesar la solicitud"
                );
                // Si el usuario no está autenticado, devuelve una respuesta de error
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
            }
        } else {
            // Si el token no es válido, devuelve una respuesta de error
            respuesta respuesta = new respuesta(
                    "error",
                    "Usuario no autorizado"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        }
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
