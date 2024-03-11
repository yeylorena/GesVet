package com.example.gesvet.service;

import com.example.gesvet.models.UsuarioVentas;
import com.example.gesvet.repository.usuarioVentasRepository;
import org.springframework.stereotype.Service;



@Service

public class UsuarioVentasServiceImpl implements UsuarioVentasService {

    private usuarioVentasRepository usuarioVentasrepository;

    public UsuarioVentasServiceImpl(usuarioVentasRepository usuarioVentasrepository) {

        this.usuarioVentasrepository = usuarioVentasrepository;
    }

    @Override
    public UsuarioVentas save(UsuarioVentas usuarioventas) {
        return usuarioVentasrepository.save(usuarioventas);
    }

     @Override
    public void delete(UsuarioVentas usuarioventas) {
        // Lógica para eliminar el usuario de la base de datos
        // Por ejemplo, puedes usar el repositorio de UsuarioVentas si estás usando Spring Data JPA
        usuarioVentasrepository.delete(usuarioventas);
    }


}
