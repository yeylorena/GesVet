package com.example.gesvet.repository;

import com.example.gesvet.models.Tipocategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITipocategoriaRepository extends JpaRepository<Tipocategoria, Integer>{
     boolean existsByNombre(String nombre);

}
