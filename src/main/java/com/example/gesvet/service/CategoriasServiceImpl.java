package com.example.gesvet.service;

import com.example.gesvet.models.Categorias;
import com.example.gesvet.repository.ICategoriasRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriasServiceImpl implements ICategoriasService {

    @Autowired
    private ICategoriasRepository categoriasRepository;

    @Override
    public Categorias save(Categorias categorias) {
        return categoriasRepository.save(categorias);
    }

    @Override
    public Optional<Categorias> get(Integer id) {
        return categoriasRepository.findById(id);
    }

    @Override
    public void update(Categorias categorias) {
        categoriasRepository.save(categorias);
    }

    @Override
    public void delete(Integer id) {
        categoriasRepository.deleteById(id);
    }

    @Override
    public List<Categorias> findAll() {
        return categoriasRepository.findAll();
    }

    @Override
    public List<Categorias> findByTipoCategoria(String tipoCategoria) {
        return categoriasRepository.findByTipocategoriaNombre(tipoCategoria);
    }

    
  
 @Override
    public boolean existsByNombre(String nombre) {
        // Implementa la lógica para verificar si existe una categoría con el mismo nombre
        // Puedes utilizar el método correspondiente de tu repositorio categoriasRepository
        // Devuelve true si existe, false si no
        return categoriasRepository.existsByNombre(nombre);
    }




    @Override
    public Categorias findById(Integer id) {
      return categoriasRepository.findById(id).orElse(null);}
}
