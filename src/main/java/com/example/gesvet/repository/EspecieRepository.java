package com.example.gesvet.repository;

import com.example.gesvet.models.Especie;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecieRepository extends JpaRepository<Especie, Integer> {

    public boolean existsByNombre(String nombre);

    List<Especie> findByActivo(boolean activo);

}
