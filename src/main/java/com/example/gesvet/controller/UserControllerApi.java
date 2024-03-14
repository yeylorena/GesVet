package com.example.gesvet.controller;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.respuesta;
import com.example.gesvet.models.User;
import com.example.gesvet.service.CustomUserDetailsService;
import com.example.gesvet.service.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserControllerApi {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    Logger logger = Logger.getLogger(UserControllerApi.class.getName());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    private UserService userService;

    public UserControllerApi(UserService userService) {

        this.userService = userService;
    }

    @GetMapping("/homes")
    public String homes(Model model, Principal principal) {
        String username = "";

        if (principal != null) {
            // Obtener el nombre de administrador actual
            username = principal.getName();

            // Obtener los detalles del administrador actual
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            model.addAttribute("userdetail", userDetails);

            // Buscar al administrador por su nombre de usuario
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
            userDto.setImagen("/images/" + user.getImagen()); // Asegúrate de tener la ruta correcta

            model.addAttribute("userDto", userDto);
        }

        return "Inicio_admin";
    }

    @GetMapping("/login")
    public String login(Model model, UserDto userDto) {
        model.addAttribute("user", userDto);
        return "Login_usu";
    }

    /*
    @PostMapping("/login-page")
    public ResponseEntity<?> postLogin(@RequestBody User user) {
        String message = "Invalid Credential";

        try {
            var usernamePasswordAuthenticationToken
                    = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        String token = jwtUtil.createToken(user.getUsername());
        // Puedes incluir el token en el cuerpo de la respuesta
        return ResponseEntity.ok(Map.of("message", "Login exitoso", "token", token));
    }
     */
    @GetMapping("/register")
    public String register(Model model, UserDto userDto) {

        model.addAttribute("user", userDto);
        return "Registro_Usu";
    }

     @PostMapping("/register")
    public ResponseEntity<Object> registerSave(@RequestBody @Valid UserDto userDto, BindingResult bindingResult, Model model) {
        
        if (bindingResult.hasErrors()) {
            //return "Registro_Usu"; // Devolver al formulario de registro si hay errores de validación
            var respuesta = new respuesta(
                    "error",    
                    "verificar  el formulario"
            );

            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByUsername(userDto.getUsername());

        if (user != null) {
            //  model.addAttribute("userexist", user);
            //   return "Registro_Usu";
            var respuesta = new respuesta(
                    "error",
                    "el usuario ya existe formulario"
            );
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);

        }

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            // model.addAttribute("passwordMismatch", true);
            //   return "Registro_Usu";
            var respuesta = new respuesta(
                    "error",
                    "las contrasñas no coinciden"
            );
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }

        userService.save(userDto);
        // return "redirect:/register?success";
        var respuesta = new respuesta(
                "Creado",
                "Usuario creado"
        );
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
    /*
   @GetMapping("/details")
public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String token) {
    try {
        // Extraer el nombre de usuario del token
        String username = jwtUtil.verifyJws(token.replace("Bearer ", "")).getSubject();

        // Obtener detalles del usuario
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Construir un objeto que contenga los detalles relevantes del usuario
        Map<String, String> userDetailMap = Map.of(
                "username", userDetails.getUsername(),
                "nombre", ((CustomUserDetail) userDetails).getNombre(),
                "apellido", ((CustomUserDetail) userDetails).getApellido(),
                "direccion", ((CustomUserDetail) userDetails).getDireccion(),
                "telefono", ((CustomUserDetail) userDetails).getTelefono(),
                "acercade", ((CustomUserDetail) userDetails).getAcercade(),
                "imagen", ((CustomUserDetail) userDetails).getImagen()
        );

        return ResponseEntity.ok(userDetailMap);
    } catch (Exception e) {
        // Imprime el mensaje de la excepción para depurar
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener detalles del usuario");
    }
}

     */
}
