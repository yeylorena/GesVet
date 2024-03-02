/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.gesvet.service;

import com.example.gesvet.models.MetodoPago;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author nicol
 */
public interface IMetodoPagoService {
    
     MetodoPago save(MetodoPago metodopago);

    Optional<MetodoPago> get(Integer id);

    void update(MetodoPago metodopago);

    void delete(Integer id);

    List<MetodoPago> findAll();
    
}
