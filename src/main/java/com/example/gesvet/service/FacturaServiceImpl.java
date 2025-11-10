package com.example.gesvet.service;

import com.example.gesvet.models.Factura;
import com.example.gesvet.models.User;
import com.example.gesvet.repository.IFacturaRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        int numero = 0;
        String numeroConcatenado = "";

        List<Factura> facturas = findAll();
        List<Integer> numeros = new ArrayList<>();

        facturas.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));

        if (facturas.isEmpty()) {
            numero = 1;
        } else {
            numero = numeros.stream().max(Integer::compare).get();
            numero++;
        }

        if (numero < 10) {
            numeroConcatenado = "000000000" + numero;
        } else if (numero < 100) {
            numeroConcatenado = "00000000" + numero;
        } else if (numero < 1000) {
            numeroConcatenado = "0000000" + numero;
        } else if (numero < 10000) {
            numeroConcatenado = "000000" + numero;
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
        // Rango de fechas para hoy
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        return facturaRepository.findDetallesFactura(startOfDay, endOfDay);
    }
}
