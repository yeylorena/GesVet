package com.example.gesvet.service;

import com.example.gesvet.models.Factura;
import com.example.gesvet.models.User;
import com.example.gesvet.repository.IFacturaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacturaServiceImpl implements IFacturaService {

    @Autowired
    private IFacturaRepository facturaRepository;

    @Override
    public Factura save(Factura factura) {
        return facturaRepository.save(factura);
    }

    @Override
    public List<Factura> findAll() {
        return facturaRepository.findAll();
    }

    public String generarNumFactura() {

        //Se crean dos variables. Uno para int y otro para string
        int numero = 0;
        String numeroConcatenado = "";

        //Se crea lista para almacenar facturas
        List<Factura> facturas = findAll();

        //Se crea lista para almacenar números
        List<Integer> numeros = new ArrayList<Integer>();

        //Método para recorrer lista factura y adicionar el numero de cada una en la lista numeros, haciendo el debido parseo
        facturas.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));

        if (facturas.isEmpty()) {
            numero = 1;
        } else {
            //Método para extraer el número mayor de factura y almacenarlo en la lista números
            numero = numeros.stream().max(Integer::compare).get();

            //El último número se incrementa el valor
            numero++;
        }

        if (numero < 10) { //0000001000
            numeroConcatenado = "000000000" + String.valueOf(numero);
        } else if (numero < 100) {
            numeroConcatenado = "00000000" + String.valueOf(numero);
        } else if (numero < 1000) {
            numeroConcatenado = "0000000" + String.valueOf(numero);
        } else if (numero < 10000) {
            numeroConcatenado = "0000000" + String.valueOf(numero);
        }

        return numeroConcatenado;
    }

    @Override
    public List<Factura> findByUsuario(User usuario) {
        return facturaRepository.findByUsuario(usuario);
    }

    @Override
    public Optional<Factura> findById(Integer id) {
        return facturaRepository.findById(id);
    }

    @Override
    public List<Factura> findByUser_Role(String role) {
        return facturaRepository.findByUserRole(role);
    }

    @Override
    public void update(Factura factura) {
        facturaRepository.save(factura);
    }

    @Override
    public void delete(Factura factura) {
        facturaRepository.delete(factura);
    }

    @Override
    public List<Object[]> obtenerTopProductosMasVendidos() {
        return facturaRepository.findTopProductosMasVendidos();
    }

    @Override
    public List<Factura> findByUsuarioAndEstadoPago(User usuario, String estadoPago) {
        // Lógica para buscar facturas por usuario y estado de pago
        // Puedes utilizar el repositorio de facturas o realizar una lógica personalizada aquí
        // Por ejemplo, si estás utilizando Spring Data JPA:
        return facturaRepository.findByUsuarioAndEstadoPago(usuario, estadoPago);
    }

    @Override
    public List<Object[]> findDetallesCompras() {
        return facturaRepository.findDetallesCompras();
    }

    @Override
    public List<Object[]> findDetallesVentas() {
        return facturaRepository.findDetallesVentas();
    }
    
    @Override
    public List<Object[]> findDetallesFactura() {
        return facturaRepository.findDetallesFactura();
    }
}
