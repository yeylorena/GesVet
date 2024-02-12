package com.example.gesvet.repository;

import com.example.gesvet.models.DetalleFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDetalleFactRepository extends JpaRepository<DetalleFactura, Integer> {

}
