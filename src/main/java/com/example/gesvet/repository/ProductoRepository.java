
package com.example.gesvet.repository;

import com.example.gesvet.models.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Productos, Integer>{
    
}
