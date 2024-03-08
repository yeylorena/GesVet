package com.example.gesvet.service;

import com.example.gesvet.models.UsuarioVentas;
import com.example.gesvet.repository.usuarioVentasRepository;

public class UsuarioVentasServiceImpl implements UsuarioVentasService {

    private usuarioVentasRepository usuarioVentasrepository;

    public UsuarioVentasServiceImpl(usuarioVentasRepository usuarioVentasrepository) {

        this.usuarioVentasrepository = usuarioVentasrepository;
    }

    @Override
    public UsuarioVentas save(UsuarioVentas usuarioventas) {
        return usuarioVentasrepository.save(usuarioventas);
    }

}
