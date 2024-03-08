package com.example.gesvet.service;

import com.example.gesvet.models.Raza;
import com.example.gesvet.repository.RazaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RazaServiceImpl implements RazaService {

    @Autowired
    private RazaRepository razaRepository;

    @Override
    public Raza save(Raza raza) {
        return razaRepository.save(raza);
    }

    @Override
    public Optional<Raza> get(Integer id) {
        return razaRepository.findById(id);
    }

    @Override
    public void update(Raza raza) {
        razaRepository.save(raza);
    }

    @Override
    public void delete(Integer id) {
        razaRepository.deleteById(id);
    }

    @Override
    public List<Raza> findAll() {
        return razaRepository.findAll();
    }

    // metodo para que cargue solo el nombre de la raza
    public List<String> getAllRazaNames() {
        List<Raza> razas = razaRepository.findAll();
        List<String> nombresRazas = new ArrayList<>();
        for (Raza raza : razas) {
            nombresRazas.add(raza.getNombre());
        }
        return nombresRazas;
    }

    @Override
    public List<Raza> getAllRazas() {
        return razaRepository.findAll();
    }
}
