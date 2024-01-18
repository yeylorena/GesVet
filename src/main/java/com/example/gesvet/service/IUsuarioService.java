
package com.example.gesvet.service;

import com.example.gesvet.models.Usuario;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface IUsuarioService {
    Optional<Usuario> findById(Integer id);
}
