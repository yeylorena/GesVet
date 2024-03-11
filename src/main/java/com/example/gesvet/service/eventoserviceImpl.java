package com.example.gesvet.service;

import com.example.gesvet.models.Evento;
import com.example.gesvet.repository.eventoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class eventoserviceImpl implements eventoService {

    @Autowired
    private eventoRepository eventoRepository;

    @Override
    public Evento save(Evento evento) {
        return eventoRepository.save(evento);
    }

    @Override
    public Optional<Evento> get(Integer id) {
        return eventoRepository.findById(id);
    }

    @Override
    public void update(Evento evento) {
        eventoRepository.save(evento);
    }

    @Override
    public void delete(Integer id) {
        eventoRepository.deleteById(id);
    }

    @Override
    public List<Evento> findAll() {
        return eventoRepository.findAll();
    }

    @Override
    public Evento findById(Integer id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + id));

    }

}
