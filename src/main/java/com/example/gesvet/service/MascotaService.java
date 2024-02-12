/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.gesvet.service;

import com.example.gesvet.models.Mascota;
import java.util.Optional;

/**
 *
 * @author sofia
 */
public interface MascotaService {
      public Mascota save(Mascota mascota);
    public Optional<Mascota> get(Integer id);
    public void update(Mascota Mascota);
}
