package com.example.gesvet.service;

import com.example.gesvet.models.Clientes;
import java.util.Optional;

public interface ClienteService {

    public Clientes save(Clientes clientes);

    public Optional<Clientes> get(Integer id);

    public void update(Clientes clientes);

}
