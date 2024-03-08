package com.example.gesvet.repository;

import com.example.gesvet.models.Servicios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IServicioRepository extends JpaRepository<Servicios, Integer> {

}
