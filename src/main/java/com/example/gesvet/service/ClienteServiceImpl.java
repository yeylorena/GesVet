/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.gesvet.service;

import com.example.gesvet.models.Clientes;
import com.example.gesvet.repository.ClienteRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author sofia
 */
@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    
    private ClienteRepository clienteRepository;
    
    @Override
    public Clientes save(Clientes clientes) {
        return clienteRepository.save(clientes);
    }

    @Override
    public Optional<Clientes> get(Integer id) {
        return clienteRepository.findById(id);
    }

    @Override
    public void update(Clientes clientes) {
        clienteRepository.save(clientes);
    }

   
    
}
