package com.example.gesvet.service;

import com.example.gesvet.models.Tipocategoria;
import com.example.gesvet.repository.ITipocategoriaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipocategoriaServiceImpl implements ITipocategoriaservice {

    @Autowired
    ITipocategoriaRepository tipocategoriarepository;

    @Override
    public Tipocategoria save(Tipocategoria tipocategoria) {

        return tipocategoriarepository.save(tipocategoria);
    }

    @Override
    public Optional<Tipocategoria> get(Integer id) {

        return tipocategoriarepository.findById(id);
    }

    @Override
    public void update(Tipocategoria tipocategoria) {
        tipocategoriarepository.save(tipocategoria);

    }

    @Override
    public void delete(Integer id) {
        tipocategoriarepository.deleteById(id);

    }

    @Override
    public List<Tipocategoria> findAll() {
        return tipocategoriarepository.findAll();

    }

}
