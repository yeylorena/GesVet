package com.example.gesvet.service;

import com.example.gesvet.models.User;
import com.example.gesvet.models.citaRapida;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface citaRapidaService {

    public citaRapida save(citaRapida citarapida);

    public Optional<citaRapida> get(Long id);

    public void update(citaRapida citarapida);

    public void delete(Long id);

    public List<citaRapida> findAll();

    citaRapida findById(Long id);

    public boolean isCitaDisponibleParaVeterinario(LocalDateTime inicio, Integer veterinarioId);

    public List<citaRapida> findPendientesByUsuario(User user);

}
