
package com.example.gesvet.service;

import com.example.gesvet.models.Productos;
import com.example.gesvet.repository.ProductoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoServiceImpl implements ProductoService{

    @Autowired
    private ProductoRepository productoRepository;
    
    @Override
    public Productos save(Productos productos) {
        return productoRepository.save(productos);
    }

    @Override
    public Optional<Productos> get(Integer id) {
        return productoRepository.findById(id);
    }

    @Override
    public void update(Productos productos) {
        productoRepository.save(productos);
    }

    @Override
    public void delete(Integer id) {
        productoRepository.deleteById(id);
    }

    @Override
    public List<Productos> findAll() {
        return productoRepository.findAll();
    }
    
    
    
}
