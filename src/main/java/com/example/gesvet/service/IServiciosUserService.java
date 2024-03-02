/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.gesvet.service;

import com.example.gesvet.models.ServiciosUser;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author nicol
 */
public interface IServiciosUserService {
    
     public ServiciosUser save(ServiciosUser serviciosuser);

    public Optional<ServiciosUser> get(Integer id);

    public void update(ServiciosUser serviciosuser);

    public void delete(Integer id);

    public List<ServiciosUser> findAll();
    
}
