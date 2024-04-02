package com.example.gesvet.controller.login;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.jwtUtil.JwtUtils;
import com.example.gesvet.models.User;
import com.example.gesvet.models.respuesta;
import com.example.gesvet.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/secret")
public class SecretController {

    @Autowired
    PasswordEncoder passwordEncoder;
    private UserService userService;

    public SecretController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<String> getSecret() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UUID.randomUUID().toString());
    }

    @GetMapping("/user-details")
    public ResponseEntity<UserDto> getUserDetails(HttpServletRequest request) {
        String jwtToken = extractTokenFromRequest(request);

        // Validar el token
        Claims claims = JwtUtils.extractClaims(jwtToken);

        if (claims != null) {
            String username = claims.getSubject();

            // Obtener todos los detalles del usuario desde la base de datos o el servicio correspondiente
            User user = userService.findByUsername(username);

            // Construir un objeto UserDto con todos los detalles del usuario
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

            // Devolver los detalles del usuario en la respuesta
            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/user-details")
    public ResponseEntity<Object> updateUserDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody UserDto userDto) {
        String jwtToken = authorizationHeader.substring(7); // Eliminar "Bearer " del encabezado

        Claims claims = JwtUtils.extractClaims(jwtToken);
        
        if (claims != null) {
            String username = claims.getSubject();

            var user = userService.findByUsername(username);
            userDto.setId(user.getId());

            // Actualizar los detalles del usuario
            userService.updateUser(userDto);

            var respuesta = new respuesta(
                    "Creado",
                    "Modificación exitosa"
            );

            return ResponseEntity.status(HttpStatus.OK).body(respuesta);
        } else {
            var respuesta = new respuesta(
                    "error",
                    "Usuario no autorizado"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        }
    }

    @PostMapping("/cambiar-contrasena")
    public ResponseEntity<Object> cambiarContrasena(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody UserDto userDto) {
        String jwtToken = authorizationHeader.substring(7); // Eliminar "Bearer " del encabezado

        Claims claims = JwtUtils.extractClaims(jwtToken);

        if (claims != null) {
            String username = claims.getSubject();

            try {
                // Obtener el usuario actual desde la base de datos
                User user = userService.findByUsername(username);

                // Validar la contraseña actual antes de realizar el cambio
                if (!passwordEncoder.matches(userDto.getCurrentPassword(), user.getPassword())) {
                    var respuesta = new respuesta(
                            "error",
                            "La contraseña actual no coincide"
                    );
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
                }

                // Validar que la nueva contraseña y la confirmación coincidan
                if (!userDto.getNewPassword().equals(userDto.getConfirmNewPassword())) {

                    var respuesta = new respuesta(
                            "error",
                            "La nueva contraseña y la confirmación no coinciden"
                    );
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
                }

                // Actualizar la contraseña en la base de datos
                user.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
                userService.cambiarContrasenaYEnviarCorreo(user);
                userService.save(user);

                var respuesta = new respuesta(
                        "Creado",
                        "Contraseña cambiada con éxito"
                );

                return ResponseEntity.status(HttpStatus.OK).body(respuesta);
            } catch (ServiceException e) {

                var respuesta = new respuesta(
                        "error",
                        "Error al cambiar la contraseña"
                );
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
            }
        } else {

            var respuesta = new respuesta(
                    "error",
                    "No autorizado"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        }
    }

    @PostMapping("/eliminar-cuenta")
    public ResponseEntity<Object> eliminarCuenta(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String jwtToken = authorizationHeader.substring(7);

        // Validar el token
        Claims claims = JwtUtils.extractClaims(jwtToken);

        if (claims != null) {
            String username = claims.getSubject();

            try {
                // Obtener el usuario actual desde la base de datos
                User user = userService.findByUsername(username);

                // Eliminar el usuario de la base de datos
                userService.eliminarUsuario(user.getId());
                userService.enviarEliminacionDeLaCuenta(user);

                // Realizar la desconexión (logout) si es necesario
                // Esto dependerá de cómo estés manejando la autenticación en tu aplicación
                // Devolver una respuesta de éxito
                var respuesta = new respuesta(
                        "Creado",
                        "Cuenta desactivada con éxito"
                );

                return ResponseEntity.status(HttpStatus.OK).body(respuesta);
            } catch (Exception e) {
                // Manejar la excepción según sea necesario
                e.printStackTrace();

                // Devolver una respuesta de error
                var respuesta = new respuesta(
                        "error",
                        "Error al desactivar la cuenta"
                );
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
            }
        } else {
            // Token inválido o no proporcionado, devolver una respuesta de no autorizado
            var respuesta = new respuesta(
                    "error",
                    "No autorizado"
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
