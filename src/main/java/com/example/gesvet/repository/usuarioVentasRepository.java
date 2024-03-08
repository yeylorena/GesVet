package com.example.gesvet.repository;

import com.example.gesvet.models.UsuarioVentas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface usuarioVentasRepository extends JpaRepository<UsuarioVentas, Long> {

}
