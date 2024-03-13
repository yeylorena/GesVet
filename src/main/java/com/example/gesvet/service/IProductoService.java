package com.example.gesvet.service;

import com.example.gesvet.models.Categorias;
import com.example.gesvet.models.Productos;
import java.util.List;
import java.util.Optional;

public interface IProductoService {

    public Productos save(Productos productos);

    public Optional<Productos> get(Integer id);

    public void update(Productos productos);

    public void delete(Integer id);

    public List<Productos> findAll();

    
    Productos findById(Integer id);
    
    List<Productos> getTopProductosConPocoStock(int limite, int minimoStock);
    
 List<Productos> findByCategoria(Categorias categoria);
}
