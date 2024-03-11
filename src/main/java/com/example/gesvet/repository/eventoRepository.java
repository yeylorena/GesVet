package com.example.gesvet.repository;

import com.example.gesvet.models.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface eventoRepository extends JpaRepository<Evento, Integer> {

}
