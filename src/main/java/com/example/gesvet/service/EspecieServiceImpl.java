package com.example.gesvet.service;

import com.example.gesvet.models.Especie;
import com.example.gesvet.repository.EspecieRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EspecieServiceImpl implements EspecieService {

    @Autowired
    private EspecieRepository especieRepository;

    @Override
    public Especie save(Especie especie) {
        return especieRepository.save(especie);
    }

    @Override
    public Optional<Especie> get(Integer id) {
        return especieRepository.findById(id);
    }

    @Override
    public void update(Especie especie) {
        especieRepository.save(especie);
    }

    @Override
    public void delete(Integer id) {
        especieRepository.deleteById(id);
    }

    @Override
    public List<Especie> findAll() {
        return especieRepository.findAll();
    }

    @Override
    public List<Especie> getAllEspecies() {
        return especieRepository.findAll();
    }

}
