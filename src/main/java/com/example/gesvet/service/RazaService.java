/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.gesvet.service;

import com.example.gesvet.models.Raza;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author sofia
 */
public interface RazaService {
    
    public Raza save(Raza raza);
    public Optional<Raza> get(Integer id);
    public void update(Raza raza);
    public void delete(Integer id);
    public List<Raza> findAll();

    public List<Raza> getAllRazas();
}
