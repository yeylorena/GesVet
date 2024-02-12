package com.example.gesvet.service;

import com.example.gesvet.models.Mascota;
import com.example.gesvet.repository.MascotaRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementación del servicio de mascotas.
 */
public class MascotaServiceImpl implements MascotaService {

    @Autowired
    
    private MascotaRepository mascotaRepository;
    
    @Override
    public Mascota save(Mascota mascota) {
        return mascotaRepository.save(mascota);
    }

    @Override
    public Optional<Mascota> get(Integer id) {
        return mascotaRepository.findById(id);
    }

    @Override
    public void update(Mascota mascota) {
        mascotaRepository.save(mascota);
    }
}
