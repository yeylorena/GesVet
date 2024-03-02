package com.example.gesvet.repository;

import com.example.gesvet.models.Factura;
import com.example.gesvet.models.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFacturaRepository extends JpaRepository<Factura, Integer> {

    List<Factura>findByUsuario(User usuario);
}
