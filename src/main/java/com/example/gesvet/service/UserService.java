package com.example.gesvet.service;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.User;
import java.awt.Image;
import java.util.List;

public interface UserService {

    User findByUsername(String username);

    User findById(Long userId);

    User save(UserDto userDto);

    User save(User user);

    void updateUser(UserDto userDto);

    void eliminarUsuario(Long userId);

}
