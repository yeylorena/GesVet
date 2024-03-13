package com.example.gesvet.service;

import com.example.gesvet.models.Categorias;
import java.util.List;
import java.util.Optional;

public interface ICategoriasService {

    Categorias save(Categorias categorias);

    Optional<Categorias> get(Integer id);

    void update(Categorias categorias);

    void delete(Integer id);

    List<Categorias> findAll();

    // Nuevo método para obtener categorías por tipo
     List<Categorias> findByTipoCategoria(String tipoCategoria);
      boolean existsByNombre(String nombre);
      
       Categorias findById(Integer id);
}


    

