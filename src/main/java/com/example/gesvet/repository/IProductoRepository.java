package com.example.gesvet.repository;

import com.example.gesvet.models.Categorias;
import com.example.gesvet.models.Productos;


import java.util.List;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductoRepository extends JpaRepository<Productos, Integer> {

    
    // PARA COLOCAR EL PRODUTO 
    List<Productos> findByCantidadLessThanEqualAndActivosIsTrue(int cantidad);
    
      @Query("SELECT p FROM Productos p WHERE p.cantidad <= :minimoStock ORDER BY p.cantidad ASC")
    List<Productos> findProductosConPocoStock(@Param("minimoStock") int minimoStock, Pageable pageable);
    

    List<Productos> findByCategoria(Categorias categoria);
}
