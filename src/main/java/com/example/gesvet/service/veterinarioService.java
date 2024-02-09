package com.example.gesvet.service;

import com.example.gesvet.models.Veterinario;
import java.util.List;
import java.util.Optional;

public interface veterinarioService {

    public Veterinario save(Veterinario veterinario);

    public Optional<Veterinario> get(Long id);

    public void update(Veterinario veterinario);

    public void delete(Long id);

    public List<Veterinario> findAll();

}
