package com.example.gesvet.controller;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.User;
import com.example.gesvet.service.UserService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PerfilControllerAdmin {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/perfil_admin")
    public String mostrarPerfilvet(Model model,
            Authentication authentication, Principal principal) throws IOException {

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
        userDto.setImagen(user.getImagen());

        model.addAttribute("userDto", userDto);
        return "Perfil_admin";
    }

    @PostMapping("/perfil_admin/editaradmin")
    public String editarPerfiladmin(UserDto userDto, @RequestParam("file") MultipartFile imagen, RedirectAttributes redirectAttributes) {

        try {
            // Si se ha seleccionado una imagen
            if (!imagen.isEmpty()) {
                // Realizar la escritura del archivo
                byte[] bytesImg = imagen.getBytes();
                Path directorioImgenes = Paths.get("images//"); // Ajustar según necesidades
                String rutaAbsoluta = directorioImgenes.toFile().getAbsolutePath();
                Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + imagen.getOriginalFilename());
                Files.write(rutaCompleta, bytesImg);
                userDto.setImagen(imagen.getOriginalFilename());
            } else {
                // Mantener la imagen anterior
                User user = userService.findById(userDto.getId());
                userDto.setImagen(user.getImagen());
            }

            // Actualizar los datos del usuario en la base de datos
            userService.updateUser(userDto);

            redirectAttributes.addFlashAttribute("modificacionExitosa", true);
        } catch (IOException e) {
            // Agregar un mensaje para la alerta de error
            redirectAttributes.addFlashAttribute("errorModificacion", true);
            e.printStackTrace(); // Puedes manejar el error según tus necesidades
        } catch (ServiceException e) {
            // Agregar un mensaje para la alerta de error del servicio
            redirectAttributes.addFlashAttribute("errorModificacion", true);
            e.printStackTrace(); // Puedes manejar el error según tus necesidades
        }

        // Redirigir a la página de perfil o a donde desees después de la edición
        return "redirect:/perfil_admin";
    }

    @PostMapping("/perfil_admin/cambiar-contrasenaadmin")
    public String cambiarContrasenaadmin(@ModelAttribute("userDto") UserDto userDto, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Validar la contraseña actual antes de realizar el cambio
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);

            if (!passwordEncoder.matches(userDto.getCurrentPassword(), user.getPassword())) {
                // Contraseña actual no coincide, mostrar mensaje de error
                redirectAttributes.addFlashAttribute("errorContrasena", true);
                return "redirect:/perfil_admin";
            }

            // Validar que la nueva contraseña y la confirmación coincidan
            if (!userDto.getNewPassword().equals(userDto.getConfirmNewPassword())) {
                // Contraseñas no coinciden, mostrar mensaje de error
                redirectAttributes.addFlashAttribute("errorContrasenaConfirmacion", true);
                return "redirect:/perfil_admin";
            }

            // Actualizar la contraseña en la base de datos
            user.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
            userService.save(user);
            userService.cambiarContrasenaYEnviarCorreo(user);

            redirectAttributes.addFlashAttribute("cambioContrasenaExitoso", true);
        } catch (ServiceException e) {
            // Manejar excepciones según sea necesario
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorCambioContrasena", true);
        }

        return "redirect:/perfil_admin";
    }

    @PostMapping("/perfil_admin/eliminar-cuentaadmin")
    public String eliminarCuentaadmin(Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username);

            // Eliminar el usuario de la base de datos
            userService.eliminarUsuario(user.getId());
             userService.enviarEliminacionDeLaCuenta(user);

            // Realizar la desconexión (logout) si es necesario
            // Esto dependerá de cómo estés manejando la autenticación en tu aplicación
            // Redirigir a la página de inicio u otra página después de eliminar la cuenta
            return "redirect:/logout";
        } catch (Exception e) {
            // Manejar la excepción según sea necesario
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorEliminarCuenta", true);
            return "redirect:/perfil_admin";
        }
    }

}
