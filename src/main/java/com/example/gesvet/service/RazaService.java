package com.example.gesvet.service;

import com.example.gesvet.models.Raza;
import java.util.List;
import java.util.Optional;

public interface RazaService {

    public Raza save(Raza raza);

    public Optional<Raza> get(Integer id);

    public void update(Raza raza);

    public void delete(Integer id);

    public List<Raza> findAll();

    public List<Raza> getAllRazas();
}
