package com.example.gesvet.repository;

import com.example.gesvet.models.Mascota;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MascotaRepository extends JpaRepository<Mascota, Integer> {

    List<Mascota> findByUsuarioId(Integer usuarioId);

    // En el repositorio (MascotaRepository)
    Optional<Mascota> findByUsuarioIdAndId(Integer usuarioId, Integer mascotaId);

}
