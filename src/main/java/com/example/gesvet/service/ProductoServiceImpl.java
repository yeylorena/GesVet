
package com.example.gesvet.service;

import com.example.gesvet.models.Productos;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.gesvet.repository.IProductoRepository;

@Service
public class ProductoServiceImpl implements IProductoService{

    @Autowired
    private IProductoRepository productoRepository;
    
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
