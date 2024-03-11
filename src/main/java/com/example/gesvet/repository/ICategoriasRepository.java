/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.gesvet.repository;

import com.example.gesvet.models.Categorias;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoriasRepository extends JpaRepository<Categorias, Integer> {
      List<Categorias> findByTipocategoriaNombre(String tipoCategoriaNombre);
      boolean existsByNombre(String nombre);
}
