package com.example.gesvet.service;

import com.example.gesvet.models.Mascota;
import com.example.gesvet.repository.MascotaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de mascotas.
 */
@Service
public class MascotaServiceImpl implements MascotaService {

    @Autowired

    private MascotaRepository mascotaRepository;

    @Override
    public Mascota save(Mascota mascota) {
        // Asignar la mascota al usuario
        mascota.getUsuario().getMascotas().add(mascota);
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

    @Override
    public void delete(Integer id) {
        mascotaRepository.deleteById(id);
    }

    @Override
    public List<Mascota> findAll() {
        return mascotaRepository.findAll();
    }

    @Override
    public Mascota create(Mascota mascota) {
        return mascotaRepository.save(mascota);
    }

    @Override
    public Mascota findById(Integer id) {
        return mascotaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
    }
}
