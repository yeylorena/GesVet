
package com.example.gesvet.service;

import com.example.gesvet.models.Usuario;
import java.util.Optional;


public interface IUsuarioService {
    Optional<Usuario> findById(Integer id);
}
