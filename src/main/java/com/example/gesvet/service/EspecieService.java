package com.example.gesvet.service;

import com.example.gesvet.models.Especie;
import java.util.List;
import java.util.Optional;

public interface EspecieService {

    public Especie save(Especie especie);

    public Optional<Especie> get(Integer id);

    public void update(Especie especie);

    public void delete(Integer id);

    public List<Especie> findAll();

    public List<Especie> getAllEspecies();

    Especie getById(Integer id);

    public boolean existsByNombre(String nombre);

    public List<Especie> getActiveEspecies();
}
