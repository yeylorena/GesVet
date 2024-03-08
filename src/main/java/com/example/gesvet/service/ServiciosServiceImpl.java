package com.example.gesvet.service;

import com.example.gesvet.models.Servicios;
import com.example.gesvet.repository.IServicioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiciosServiceImpl implements IServicioService {

    @Autowired
    private IServicioRepository servicioRepository;

    @Override
    public Servicios save(Servicios servicios) {
        return servicioRepository.save(servicios);
    }

    @Override
    public Optional<Servicios> get(Integer id) {
        return servicioRepository.findById(id);
    }

    @Override
    public void update(Servicios servicios) {
        servicioRepository.save(servicios);
    }

    @Override
    public void delete(Integer id) {
        servicioRepository.deleteById(id);
    }

    @Override
    public List<Servicios> findAll() {
        return servicioRepository.findAll();
    }

}
