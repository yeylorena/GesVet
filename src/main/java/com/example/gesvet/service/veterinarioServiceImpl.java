package com.example.gesvet.service;

import com.example.gesvet.models.Veterinario;
import com.example.gesvet.repository.veterinarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class veterinarioServiceImpl implements veterinarioService {

    @Autowired //sirve para decir que estamos implementando a esta clase un objeto
    private veterinarioRepository VeterinarioRepository;

    @Override
    public Veterinario save(Veterinario veterinario) {
        return VeterinarioRepository.save(veterinario);
    }

    @Override
    public Optional<Veterinario> get(Long id) {
        return VeterinarioRepository.findById(id);
    }

    @Override
    public void update(Veterinario veterinario) {
        VeterinarioRepository.save(veterinario); //utilizamos el mismo metodo save ya que si no se encuntra el objet lo crea y si ya esta crado lo actualiza
    }

    @Override
    public void delete(Long id) {
        VeterinarioRepository.deleteById(id);
    }

    @Override
    public List<Veterinario> findAll() {
        return (List<Veterinario>) VeterinarioRepository.findAll();  //esto lo cambie
    }

}
