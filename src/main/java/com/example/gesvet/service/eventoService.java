package com.example.gesvet.service;

import com.example.gesvet.models.Evento;
import java.util.List;
import java.util.Optional;

public interface eventoService {

    public Evento save(Evento evento);

    public Optional<Evento> get(Integer id);

    public void update(Evento evento);

    public void delete(Integer id);

    public List<Evento> findAll();

    Evento findById(Integer id);
}
