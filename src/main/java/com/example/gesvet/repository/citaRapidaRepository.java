package com.example.gesvet.repository;

import com.example.gesvet.models.User;
import com.example.gesvet.models.citaRapida;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface citaRapidaRepository extends JpaRepository<citaRapida, Long> {

    public boolean existsByUsuarioAndInicio(User veterinario, LocalDateTime inicio);

    public boolean existsByMascotaIdAndInicioAndVeterinarioCitaNot(Integer idMascota, LocalDateTime inicio, String valueOf);

    public List<citaRapida> findByUsuarioAndEstado(User user, String pendiente);

}
