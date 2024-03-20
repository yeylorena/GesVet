package com.example.gesvet.service;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.User;
import com.example.gesvet.repository.RecuperarContraseñausuRepository;
import com.example.gesvet.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    // Método para contar el número de usuarios con un rol específico
    @Autowired
    private RecuperarContraseñausuRepository recuperarContraseñausuRepository;

    public UserServiceImpl(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public User save(UserDto userDto) {
        User user = new User(userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()), userDto.getNombre(), userDto.getApellido(),
                userDto.getDireccion(), userDto.getTelefono(), userDto.getRole(), userDto.getAcercade(), userDto.getImagen());
        return userRepository.save(user);

    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void updateUser(UserDto userDto) {
        // Verificar si el ID no es nulo
        Integer userId = userDto.getId();
        if (userId == null) {
            throw new IllegalArgumentException("ID del usuario no puede ser nulo");
        }

        // Obtener el usuario existente desde la base de datos
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));

        // Actualizar los campos necesarios
        existingUser.setNombre(userDto.getNombre());
        existingUser.setApellido(userDto.getApellido());
        existingUser.setDireccion(userDto.getDireccion());
        existingUser.setTelefono(userDto.getTelefono());
        existingUser.setAcercade(userDto.getAcercade());
        existingUser.setImagen(userDto.getImagen());

        // Guardar el usuario actualizado en la base de datos
        userRepository.save(existingUser);
    }

    @Override
    public void eliminarUsuario(Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Desactivar el usuario en lugar de borrarlo
            user.setActivo(false);

            // Guardar el usuario actualizado en la base de datos
            userRepository.save(user);
        }
    }

    @Override
    public User findById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));
    }

    @Override
    public void cambiarContrasenaYEnviarCorreo(User user) {
        // Lógica para cambiar la contraseña

        // Enviar correo electrónico
        enviarCorreoElectronico(user);
    }

    private void enviarCorreoElectronico(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getUsername()); // Usar el campo de correo electrónico
        message.setSubject("Contraseña cambiada exitosamente");
        String contenidoMensaje = "Cordial saludo " + ",\n\n"
                + "Te informamos que la contraseña de tu cuenta en Gesvet ha sido cambiada con éxito.\n"
                + "Si realizaste esta acción, puedes ignorar este mensaje.\n\n"
                + "¡Gracias por confiar en Gesvet!\n\n"
                + "Atentamente,\n"
                + "El equipo de Gesvet";

        message.setText(contenidoMensaje);

        javaMailSender.send(message);
    }

    @Override
    public List<User> findByRoleAndActivo(String role, boolean activo) {
        return userRepository.findByRoleAndActivo(role, activo);
    }

    @Override
    public int countUsersByRole(String role) {
        return userRepository.countByRole(role);
    }

    @Override
    public Optional<User> get(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean usuarioDatosPersonalesCompletos(User user) {
    return StringUtils.isNotBlank(user.getNombre())
            && StringUtils.isNotBlank(user.getApellido())
            && StringUtils.isNotBlank(user.getDireccion())
            && StringUtils.isNotBlank(user.getTelefono())
            && StringUtils.isNotBlank(user.getAcercade())
            && StringUtils.isNotBlank(user.getImagen());
}

}
