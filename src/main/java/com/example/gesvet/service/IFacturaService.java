package com.example.gesvet.service;

import com.example.gesvet.models.Factura;
import java.util.List;

public interface IFacturaService {

    List<Factura> findAll();

    Factura save(Factura factura);

    String generarNumFactura();

}
