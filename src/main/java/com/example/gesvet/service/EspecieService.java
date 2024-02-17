/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.gesvet.service;

import com.example.gesvet.models.Especie;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author sofia
 */
public interface EspecieService {
    public Especie save(Especie especie);
    public Optional<Especie> get(Integer id);
    public void update(Especie especie);
    public void delete(Integer id);
    public List<Especie> findAll();

    public List<Especie> getAllEspecies();
}
