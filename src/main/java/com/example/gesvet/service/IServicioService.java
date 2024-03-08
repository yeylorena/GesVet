package com.example.gesvet.service;

import com.example.gesvet.models.Servicios;
import java.util.List;
import java.util.Optional;

public interface IServicioService {

    public Servicios save(Servicios servicios);

    public Optional<Servicios> get(Integer id);

    public void update(Servicios servicios);

    public void delete(Integer id);

    public List<Servicios> findAll();

}
