package com.example.gesvet.service;

import com.example.gesvet.models.agendarcitaAdmin;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface agendarcitaadminService {

    public agendarcitaAdmin save(agendarcitaAdmin agendarCita);

    public Optional<agendarcitaAdmin> get(Integer id);

    public void update(agendarcitaAdmin agendarCita);

    public void delete(Integer id);

    public List<agendarcitaAdmin> findAll();

    agendarcitaAdmin findById(Integer id);

    public boolean isCitaDisponibleParaVeterinario(LocalDateTime inicio, Integer veterinarioId);

}
