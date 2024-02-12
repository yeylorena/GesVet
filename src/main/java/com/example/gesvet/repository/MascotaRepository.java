/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.gesvet.repository;

import com.example.gesvet.models.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author sofia
 */
public interface MascotaRepository extends JpaRepository<Mascota, Integer> {
    
}
