package com.example.gesvet.repository;

import com.example.gesvet.models.Productos;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductoRepository extends JpaRepository<Productos, Integer> {
    
}
