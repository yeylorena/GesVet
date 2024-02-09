/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.gesvet.service;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.User;
import com.example.gesvet.repository.RecuperarContraseñausuRepository;
import com.example.gesvet.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    private UserRepository userRepository;
    
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
        Long userId = userDto.getId();
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
public void eliminarUsuario(Long userId) {
    Optional<User> optionalUser = userRepository.findById(userId);

    if (optionalUser.isPresent()) {
        User user = optionalUser.get();

        // Eliminar manualmente los registros relacionados
        recuperarContraseñausuRepository.eliminarTokensPorUsuario(user);

        // Eliminar el usuario
        userRepository.deleteById(userId);
    }
}


     @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));
    }

}
