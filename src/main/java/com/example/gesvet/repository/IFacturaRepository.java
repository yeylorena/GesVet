package com.example.gesvet.repository;

import com.example.gesvet.models.Factura;
import com.example.gesvet.models.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IFacturaRepository extends JpaRepository<Factura, Integer> {

    List<Factura> findByUsuario(User usuario);

    @Query("SELECT f FROM Factura f WHERE f.usuario.role = :role")
    List<Factura> findByUserRole(@Param("role") String role);

}
