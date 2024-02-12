/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.gesvet.service;

import com.example.gesvet.models.Clientes;
import java.util.Optional;

/**
 *
 * @author sofia
 */
public interface ClienteService {
      
    public Clientes save(Clientes clientes);
    public Optional<Clientes> get(Integer id);
    public void update(Clientes clientes);

}
