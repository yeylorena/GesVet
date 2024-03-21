package com.example.gesvet.controller.login;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.jwtUtil.JwtUtils;
import com.example.gesvet.models.User;
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
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ResponseEntity<String> updateUserDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody UserDto userDto) {
        String jwtToken = authorizationHeader.substring(7); // Eliminar "Bearer " del encabezado

        Claims claims = JwtUtils.extractClaims(jwtToken);

        if (claims != null) {
            String username = claims.getSubject();
            
            var user=userService.findByUsername(username);
            userDto.setId(user.getId());

            // Actualizar los detalles del usuario
            userService.updateUser(userDto);

            return ResponseEntity.status(HttpStatus.OK).body("Detalles del usuario actualizados con éxito");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado");
        }
    }

    @PostMapping("/cambiar-contrasena")
    public ResponseEntity<String> cambiarContrasena(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
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
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La contraseña actual no coincide");
                }

                // Validar que la nueva contraseña y la confirmación coincidan
                if (!userDto.getNewPassword().equals(userDto.getConfirmNewPassword())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La nueva contraseña y la confirmación no coinciden");
                }

                // Actualizar la contraseña en la base de datos
                user.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
                userService.save(user);

                return ResponseEntity.status(HttpStatus.OK).body("Contraseña cambiada con éxito");
            } catch (ServiceException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cambiar la contraseña");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado");
        }
    }

    @DeleteMapping("/eliminar-cuenta")
    public ResponseEntity<String> eliminarCuenta(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String jwtToken = authorizationHeader.substring(7); // Eliminar "Bearer " del encabezado

        Claims claims = JwtUtils.extractClaims(jwtToken);

        if (claims != null) {
            String username = claims.getSubject();

            try {
                // Obtener el usuario actual desde la base de datos
                User user = userService.findByUsername(username);

                // Eliminar el usuario de la base de datos
                userService.eliminarUsuario(user.getId());

                // Realizar la desconexión (logout) si es necesario
                // Esto dependerá de cómo estés manejando la autenticación en tu aplicación
                return ResponseEntity.status(HttpStatus.OK).body("Usuario eliminado con éxito");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el usuario");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado");
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
