package com.example.gesvet.service;

import com.example.gesvet.models.User;
import com.example.gesvet.models.agendarcitaAdmin;
import com.example.gesvet.repository.agendarcitaadminRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class agendarcitadminserviceImpl implements agendarcitaadminService {

    @Autowired //sirve para decir que estamos implementando a esta clase un objeto
    private agendarcitaadminRepository agendarcitaadminRepository;

    @Autowired //sirve para decir que estamos implementando a esta clase un objeto
    private UserService userService;

    @Override
    public agendarcitaAdmin save(agendarcitaAdmin agendarCita) {
        agendarCita.getUsuario().getAgendarCitas().add(agendarCita);
        return agendarcitaadminRepository.save(agendarCita);
    }

    @Override
    public Optional<agendarcitaAdmin> get(Integer id) {
        return agendarcitaadminRepository.findById(id);
    }

    @Override
    public void update(agendarcitaAdmin agendarCita) {
        agendarcitaadminRepository.save(agendarCita); //utilizamos el mismo metodo save ya que si no se encuntra el objet lo crea y si ya esta crado lo actualiza
    }

    @Override
    public void delete(Integer id) {
        agendarcitaadminRepository.deleteById(id);
    }

    @Override
    public List<agendarcitaAdmin> findAll() {
        return agendarcitaadminRepository.findAll();
    }

    @Override
    public agendarcitaAdmin findById(Integer id) {
        return agendarcitaadminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("cita no encontrada con: " + id));
    }

    @Override
    public boolean isCitaDisponibleParaVeterinario(LocalDateTime inicio, Integer veterinarioId) {
        User veterinario = userService.findById(veterinarioId);

        return !agendarcitaadminRepository.existsByUsuarioAndInicio(veterinario, inicio);
    }

}
