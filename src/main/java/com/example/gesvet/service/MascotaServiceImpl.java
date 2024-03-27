package com.example.gesvet.service;

import com.example.gesvet.models.Mascota;
import com.example.gesvet.repository.MascotaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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

    @Override
    public List<Mascota> findByUsuarioId(Integer usuarioId) {
        return mascotaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Optional<Mascota> findByUsuarioIdAndMascotaId(Integer usuarioId, Integer mascotaId) {
        return mascotaRepository.findByUsuarioIdAndId(usuarioId, mascotaId);
    }

}
