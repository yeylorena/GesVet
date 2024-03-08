package com.example.gesvet.service;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.User;
import java.awt.Image;
import java.util.List;
import java.util.Optional;

public interface UserService {

    public Optional<User> get(Long id);

    User findByUsername(String username);

    User findById(Long userId);

    User save(UserDto userDto);

    User save(User user);

    void updateUser(UserDto userDto);

    void eliminarUsuario(Long userId);

    void cambiarContrasenaYEnviarCorreo(User user);

    List<User> findByRoleAndActivo(String role, boolean activo);

    public int countUsersByRole(String role);

}
