package com.example.gesvet.repository;

import com.example.gesvet.models.User;
import com.example.gesvet.models.agendarcitaAdmin;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface agendarcitaadminRepository extends JpaRepository<agendarcitaAdmin, Integer> {

    public boolean existsByUsuarioAndInicio(User veterinario, LocalDateTime inicio);

}
