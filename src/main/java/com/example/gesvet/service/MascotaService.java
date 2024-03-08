package com.example.gesvet.service;

import com.example.gesvet.models.Mascota;
import java.util.List;
import java.util.Optional;

public interface MascotaService {

    public Mascota save(Mascota mascota);

    public Optional<Mascota> get(Integer id);

    public void update(Mascota Mascota);

    public void delete(Integer id);

    public List<Mascota> findAll();

    Mascota findById(Integer id);

    Mascota create(Mascota mascota);
}
