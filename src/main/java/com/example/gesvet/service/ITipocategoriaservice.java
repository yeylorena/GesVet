package com.example.gesvet.service;

import com.example.gesvet.models.Tipocategoria;
import java.util.List;
import java.util.Optional;

public interface ITipocategoriaservice {

    Tipocategoria save(Tipocategoria tipocategoria);

    Optional<Tipocategoria> get(Integer id);

    void update(Tipocategoria tipocategoria);

    void delete(Integer id);

    List<Tipocategoria> findAll();

    boolean existsByNombre(String nombre);
    

}
