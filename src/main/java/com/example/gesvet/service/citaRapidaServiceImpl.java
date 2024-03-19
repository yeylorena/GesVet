package com.example.gesvet.service;

import com.example.gesvet.models.User;
import com.example.gesvet.models.citaRapida;
import com.example.gesvet.repository.citaRapidaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class citaRapidaServiceImpl implements citaRapidaService {

    @Autowired //sirve para decir que estamos implementando a esta clase un objeto
    private citaRapidaRepository citaRapidaRepository;

    @Autowired //sirve para decir que estamos implementando a esta clase un objeto
    private UserService userService;

    @Override
    public citaRapida save(citaRapida citarapida) {
        citarapida.getUsuario().getCitasRapidas().add(citarapida);
        return citaRapidaRepository.save(citarapida);
    }

    @Override
    public Optional<citaRapida> get(Long id) {
        return citaRapidaRepository.findById(id);
    }

    @Override
    public void update(citaRapida citarapida) {
        citaRapidaRepository.save(citarapida); //utilizamos el mismo metodo save ya que si no se encuntra el objet lo crea y si ya esta crado lo actualiza
    }

    @Override
    public void delete(Long id) {
        citaRapidaRepository.deleteById(id);
    }

    @Override
    public List<citaRapida> findAll() {
        return citaRapidaRepository.findAll();
    }

    @Override
    public citaRapida findById(Long id) {
        return citaRapidaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("cita no encontrada con: " + id));

    }

    @Override
    public boolean isCitaDisponibleParaVeterinario(LocalDateTime inicio, Integer veterinarioId) {
        User veterinario = userService.findById(veterinarioId);

        return !citaRapidaRepository.existsByUsuarioAndInicio(veterinario, inicio);
    }

    @Override
    public List<citaRapida> findPendientesByUsuario(User user) {
        return citaRapidaRepository.findByUsuarioAndEstado(user, "pendiente");
    }


}
