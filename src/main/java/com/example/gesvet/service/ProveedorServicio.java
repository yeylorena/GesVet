package com.example.gesvet.service;

import com.example.gesvet.models.Proveedor;
import com.example.gesvet.repository.ProveedorRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service //Registrar en el contenedor de Spring
public class ProveedorServicio {

    @Autowired //Inyectar repositorio
    private ProveedorRepositorio proveedorRepositorio;

    //metodo para listar todos los proveedores
    public List<Proveedor> listAll() {
        return proveedorRepositorio.findAll();
    }

    //metodo para guardar un proveedor
    public void save(Proveedor proveedor) {
        proveedorRepositorio.save(proveedor);
    }

    //metodo para obtener un solo proveedor
    public Proveedor get(Long id) {
        return proveedorRepositorio.findById(id).get();
    }

    //metodo para eliminar un proveedor
    public void delete(Long id) {
        proveedorRepositorio.deleteById(id);
    }

}
