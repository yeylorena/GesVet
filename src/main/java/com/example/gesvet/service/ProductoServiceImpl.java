package com.example.gesvet.service;

import com.example.gesvet.models.Categorias;
import com.example.gesvet.models.Productos;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.gesvet.repository.IProductoRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;

@Service
public class ProductoServiceImpl implements IProductoService {

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
      @Override
    public Productos findById(Integer id) {
        // Implementación para buscar un producto por ID en la base de datos
        // Retorna el objeto Productos encontrado
        return productoRepository.findById(id).orElse(null);
    }
    @Scheduled(fixedDelay = 60000) // Ejecutar con un retraso mínimo de 1 minuto (en milisegundos)
public void verificarYActualizarEstadoProductos() {
    List<Productos> productosSinStock = productoRepository.findByCantidadLessThanEqualAndActivosIsTrue(0);

    for (Productos producto : productosSinStock) {
        producto.setActivo(false); // Cambiar el estado a inactivo
    }

    productoRepository.saveAll(productosSinStock);
}

 @Override
    public List<Productos> getTopProductosConPocoStock(int limite, int minimoStock) {
        Pageable pageRequest = PageRequest.of(0, limite);
        return productoRepository.findProductosConPocoStock(minimoStock, pageRequest);
    }
    
     @Override
public List<Productos> findByCategoria(Categorias categoria) {
    return productoRepository.findByCategoria(categoria);
}
}
