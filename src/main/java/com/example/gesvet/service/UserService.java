package com.example.gesvet.service;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User findByUsername(String username);

    User findById(Integer userId);

    User save(UserDto userDto);

    User save(User user);

    void updateUser(UserDto userDto);

    void eliminarUsuario(Integer userId);

    void cambiarContrasenaYEnviarCorreo(User user);

    List<User> findByRoleAndActivo(String role, boolean activo);

    public int countUsersByRole(String role);

    public Optional<User> get(Integer id);
    
     public boolean usuarioDatosPersonalesCompletos(User user);
     
      void enviarEliminacionDeLaCuenta(User user);

}
